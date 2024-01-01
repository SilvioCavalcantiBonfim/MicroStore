package com.microsservicos.shoppingapi.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsservicos.dto.ItemDto;
import com.microsservicos.dto.ItemInputDto;
import com.microsservicos.dto.ShopDto;
import com.microsservicos.dto.ShopInputDto;
import com.microsservicos.dto.UserOutputDto;
import com.microsservicos.dto.product.CategoryDto;
import com.microsservicos.dto.product.ProductDto;

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

  @MockBean
  private RestTemplate restTemplate;

  private static ItemDto ITEM1;
  private static ItemDto ITEM2;
  private static ShopDto SHOP1;
  private static ShopDto SHOP2;

  private static UserOutputDto user = new UserOutputDto("user test", "12345678910", "street B", "test@email.com",
      "900000000", LocalDateTime.now(), KEY);

  private static ItemInputDto itemBody = new ItemInputDto("video-game", 2);
  private static ShopInputDto body = new ShopInputDto(user.cpf(), List.of(itemBody));
    
  @BeforeAll
  private static void setup() throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    ITEM1 = new ItemDto("video-game", 2001.0f, 1);
    ITEM2 = new ItemDto("video-game", 20.0f, 2);
    SHOP1 = new ShopDto("12345678910", 2001.0, sdf.parse("2023-12-31 01:51:36.789"), List.of(ITEM1));
    SHOP2 = new ShopDto("12345678910", 20.0, sdf.parse("2020-12-31 01:51:36.789"), List.of(ITEM2));
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

    when(restTemplate.getForEntity(anyString(), any())).thenReturn(ResponseEntity.ok(user));

    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/12345678910")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1, SHOP2))));
  }

  @Test
  public void getByUserInvalidKey() throws Exception {

    when(restTemplate.getForEntity(anyString(), any()))
        .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/12345678910")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByUserNotFoundUserIdentifier() throws Exception {

    when(restTemplate.getForEntity(anyString(), any()))
        .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByUser/12345678911")
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void getByUserWithUserIdentifierLengthInvalid() throws Exception {

    when(restTemplate.getForEntity(anyString(), any())).thenThrow(
        HttpClientErrorException.create(HttpStatus.UNPROCESSABLE_ENTITY, "unprocessable entity", null, null, null));

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
        .param("after", "31-12-2023")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(SHOP1.obfuscate()))));
  }

  @Test
  public void shopByDateSuccessAllData() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByDate")
        .param("after", "31-12-2020")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content()
            .json(objectMapper.writeValueAsString(List.of(SHOP1.obfuscate(), SHOP2.obfuscate()))));
  }

  @Test
  public void shopByDateSuccessNonData() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/shopping/shopByDate")
        .param("after", "31-12-2028")
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

    when(restTemplate.getForEntity(anyString(), any()))
        .thenThrow(HttpClientErrorException.create(HttpStatus.NOT_FOUND, "not found", null, null, null));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserInvalidUserIdentifier() throws Exception {

    when(restTemplate.getForEntity(anyString(), any())).thenThrow(
        HttpClientErrorException.create(HttpStatus.UNPROCESSABLE_ENTITY, "unprocessable entity", null, null, null));

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

    when(restTemplate
        .getForEntity(eq("http://localhost:8080/user/cpf/12345678910?key=139cdb41-5fab-4283-8d07-a8b42960af48"), any()))
        .thenReturn(ResponseEntity.ok(user));
    when(restTemplate.getForEntity(eq(String.format("http://localhost:8081/product/%s", itemBody.productIdentifier())),
        any()))
        .thenReturn(ResponseEntity.ok(new ProductDto(itemBody.productIdentifier(), "Test Product", "Description",
            2000.0f, new CategoryDto(1L, "test"))));

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

    ShopDto body = new ShopDto(user.cpf(), 100.0, new Date(), List.of());

    when(restTemplate
        .getForEntity(eq("http://localhost:8080/user/cpf/12345678910?key=139cdb41-5fab-4283-8d07-a8b42960af48"), any()))
        .thenReturn(ResponseEntity.ok(user));

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
    ShopDto body = new ShopDto(null, 100.0, new Date(), List.of(itemBody));

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
    ShopDto body = new ShopDto(user.cpf(), null, new Date(), List.of(itemBody));

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
    ShopDto body = new ShopDto(user.cpf(), 100.0, null, List.of(itemBody));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .header("key", KEY)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  public void createShopUserItensNull() throws Exception {

    ShopDto body = new ShopDto(user.cpf(), 100.0, new Date(), null);

    when(restTemplate
        .getForEntity(eq("http://localhost:8080/user/cpf/12345678910?key=139cdb41-5fab-4283-8d07-a8b42960af48"), any()))
        .thenReturn(ResponseEntity.ok(user));

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
    ShopDto body = new ShopDto(user.cpf(), 100.0, new Date(), List.of(itemBody));

    mockMvc.perform(MockMvcRequestBuilders.post("/shopping")
        .content(objectMapper.writeValueAsString(body))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }
}
