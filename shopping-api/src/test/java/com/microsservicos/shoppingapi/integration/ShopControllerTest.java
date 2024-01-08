package com.microsservicos.shoppingapi.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsservicos.dto.CategoryDto;
import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ItemInputDto;
import com.microsservicos.dto.ProductDto;
import com.microsservicos.dto.ShopOutputDto;
import com.microsservicos.dto.ShopInputDto;
import com.microsservicos.dto.UserOutputDto;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = {
    "spring.flyway.locations=classpath:db/migration,classpath:db/migration-test",
    "spring.flyway.clean-disabled=false",
    "spring.flyway.schemas=shopping",
    "spring.datasource.url = jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
    "spring.datasource.driverClassName = org.h2.Driver",
    "spring.datasource.username = sa",
    "spring.datasource.password = password",
    "spring.datasource.hikari.maximumPoolSize=2",
    "spring.jpa.database-platform = org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.default_schema = shopping",
    "server.port = -1",
    "logging.level.org.flywaydb=DEBUG"
})
@ActiveProfiles("test")
@DirtiesContext
@AutoConfigureMockMvc
public class ShopControllerTest {
  private static final String KEY = "139cdb41-5fab-4283-8d07-a8b42960af48";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EntityManager entityManager;

  @Mock
  @SuppressWarnings("rawtypes")
  private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

  @Mock
  @SuppressWarnings("rawtypes")
  private RestClient.RequestHeadersSpec requestHeadersSpec;

  @Mock
  private RestClient.ResponseSpec responseSpec;

  @MockBean
  @Qualifier("userClient")
  private RestClient userClient;

  @MockBean
  @Qualifier("productClient")
  private RestClient productClient;

  private static ItemDto ITEM1;
  private static ItemDto ITEM2;
  private static ShopOutputDto SHOP1;
  private static ShopOutputDto SHOP2;

  private static UserOutputDto user = new UserOutputDto("user test", "12345678910", "street B", "test@email.com",
      "900000000", LocalDateTime.now(), KEY);

  private static ItemInputDto itemBody = new ItemInputDto("video-game", 2);
  private static ShopInputDto body = new ShopInputDto(user.cpf(), List.of(itemBody));

  @BeforeAll
  private static void setup() throws ParseException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    ITEM1 = new ItemDto("video-game", 2001.0f, 1);
    ITEM2 = new ItemDto("video-game", 20.0f, 2);
    SHOP1 = new ShopOutputDto("12345678910", 2001.0, LocalDateTime.parse("2023-12-31 01:51:36.789", formatter),
        List.of(ITEM1));
    SHOP2 = new ShopOutputDto("12345678910", 20.0, LocalDateTime.parse("2020-12-31 01:51:36.789", formatter),
        List.of(ITEM2));
  }

  @BeforeEach
  private void setupEach() {
    Mockito.when(userClient.get()).thenReturn(requestHeadersUriSpec);
    Mockito.when(productClient.get()).thenReturn(requestHeadersUriSpec);
    Mockito.when(requestHeadersUriSpec.uri(Mockito.anyString())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.header(Mockito.any(),Mockito.any())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.accept(Mockito.any())).thenReturn(requestHeadersSpec);
    Mockito.when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
  }

  @Test
  public void getAllSuccess() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content()
            .json(objectMapper.writeValueAsString(List.of(SHOP1.obfuscate(), SHOP2.obfuscate()))));
  }

  @Test
  public void getByIdSuccess() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(SHOP1.obfuscate())));
  }

  @Test
  public void getByIdIdNotFound() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/-1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByIdIdInvalidId() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/a")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByUserSuccess() throws Exception {

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/12345678910")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1, SHOP2))));
  }

  @Test
  public void getByUserInvalidKey() throws Exception {

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "not found", HttpStatus.NOT_FOUND, null, null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/12345678910")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByUserNotFoundUserIdentifier() throws Exception {

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "not found", HttpStatus.NOT_FOUND, null, null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/12345678911")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByUserWithUserIdentifierLengthInvalid() throws Exception {

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "unprocessable entity", HttpStatus.UNPROCESSABLE_ENTITY, null, null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/1234567891")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByUserNoArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByUserNoKey() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/12345678910")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void shopByDateSuccess() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByDate")
        .param("after", "2023-12-31")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1.obfuscate()))));
  }

  @Test
  public void shopByDateSuccessAllData() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByDate")
        .param("after", "2020-12-31")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content()
            .json(objectMapper.writeValueAsString(List.of(SHOP1.obfuscate(), SHOP2.obfuscate()))));
  }

  @Test
  public void shopByDateSuccessNonData() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByDate")
        .param("after", "2024-01-03")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of())));
  }

  @Test
  public void shopByDateNoArg() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByDate")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserNotFound() throws Exception {

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "not found", HttpStatus.NOT_FOUND, null, null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserInvalidUserIdentifier() throws Exception {

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenThrow(new RestClientResponseException(
        "unprocessable entity", HttpStatus.UNPROCESSABLE_ENTITY, null, null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @Transactional
  public void createShopUserSuccess() throws Exception {

    ItemInputDto itemBody = new ItemInputDto("video-game", 2);
    ShopInputDto body = new ShopInputDto(user.cpf(), List.of(itemBody));

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenReturn(user);
    Mockito.when(responseSpec.body(ProductDto.class)).thenReturn(new ProductDto(itemBody.productIdentifier(), "Test Product", "Description",
            2000.0f, new CategoryDto(1L, "test")));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    entityManager.flush();
    entityManager.clear();
  }

  @Test
  public void createShopUserItemIsEmpty() throws Exception {

    ShopOutputDto body = new ShopOutputDto(user.cpf(), 100.0, LocalDateTime.now(), List.of());

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserUserIdentifierNull() throws Exception {

    ItemDto itemBody = new ItemDto("video-game", 20.0f, 2);
    ShopOutputDto body = new ShopOutputDto(null, 100.0, LocalDateTime.now(), List.of(itemBody));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserTotalNull() throws Exception {

    ItemDto itemBody = new ItemDto("video-game", 20.0f, 2);
    ShopOutputDto body = new ShopOutputDto(user.cpf(), null, LocalDateTime.now(), List.of(itemBody));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserDateNull() throws Exception {

    ItemDto itemBody = new ItemDto("video-game", 20.0f, 2);
    ShopOutputDto body = new ShopOutputDto(user.cpf(), 100.0, null, List.of(itemBody));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserItensNull() throws Exception {

    ShopOutputDto body = new ShopOutputDto(user.cpf(), 100.0, LocalDateTime.now(), null);

    Mockito.when(responseSpec.body(UserOutputDto.class)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserNoKey() throws Exception {

    ItemDto itemBody = new ItemDto("video-game", 20.0f, 2);
    ShopOutputDto body = new ShopOutputDto(user.cpf(), 100.0, LocalDateTime.now(), List.of(itemBody));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }
}
