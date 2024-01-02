package com.microsservicos.userapi.integration;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsservicos.dto.UserOutputDto;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = {
    "spring.flyway.locations=classpath:db/migration,classpath:db/migration-test",
    "spring.flyway.clean-disabled=false",
    "spring.flyway.schemas=users",
    "spring.datasource.url = jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
    "spring.datasource.driverClassName = org.h2.Driver",
    "spring.datasource.username = sa",
    "spring.datasource.password = password",
    "spring.datasource.hikari.maximumPoolSize=2",
    "spring.jpa.database-platform = org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.default_schema = users",
    "server.port = -1",
    "logging.level.org.flywaydb=DEBUG"
})
@ActiveProfiles("test")
@DirtiesContext
@AutoConfigureMockMvc
public class UserApiIntegrationTest {

  private static final UserOutputDto USER_1 = new UserOutputDto("João Silva",
      "12345678901",
      "Rua das Flores, 100",
      "joao.silva@example.com",
      "11987654321",
      LocalDateTime.of(2023, 12, 31, 23, 59, 59),
      "139cdb41-5fab-4283-8d07-a8b42960af48");

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private EntityManager entityManager;

  @Test
  @DisplayName("Find all users successfully")
  public void testFindAllSuccessEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/user").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(USER_1.obfuscate()))));
  }

  @Test
  @DisplayName("Get user by ID successfully")
  public void testGetUserByIdSuccessEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(USER_1.obfuscate())));
  }

  @Test
  @DisplayName("Get user by ID - User not found")
  public void testGetUserByIdUserNotFoundEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/user/100")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Get user by CPF successfully")
  public void testGetUserByCpfSuccessEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/user/cpf/12345678901?key=139cdb41-5fab-4283-8d07-a8b42960af48")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(USER_1.obfuscate())));
  }

  @Test
  @DisplayName("Get user by CPF - No key provided")
  public void testGetUserByCpfNoKeyEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/user/cpf/12345678901")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Get user by CPF - Invalid CPF length")
  public void testGetUserByCpfInvalidLengthCpfEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/user/cpf/1234567890?key=139cdb41-5fab-4283-8d07-a8b42960af48")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Get user by CPF - Invalid CPF and valid key")
  public void testGetUserByCpfInvalidCpfAndValidKeyEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/user/cpf/12345678900?key=139cdb41-5fab-4283-8d07-a8b42960af48")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Get user by CPF - Valid CPF and invalid key")
  public void testGetUserByCpfValidCpfAndInvalidKeyEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders
        .get("/user/cpf/12345678901?key=239cdb41-5fab-4283-8d07-a8b42960af48")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Get user by like name - Success with all")
  public void testGetUserByLikeNameSuccessWithAllEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/user/search?name=%").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(USER_1.obfuscate()))));
  }

  @Test
  @DisplayName("Get user by like name - Success")
  public void testGetUserByLikeNameSuccessEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/user/search?name=Jo%").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of(USER_1.obfuscate()))));
  }

  @Test
  @DisplayName("Get user by like name - Success - Empty result")
  public void testGetUserByLikeNameSuccessEmptyEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/user/search?name=JAVA").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(List.of())));
  }

  @Test
  @DisplayName("Get user by like name - No parameter name")
  public void testGetUserByLikeNameNoParamNameEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/user/search").contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Delete user by ID - Not found")
  public void testDeleteUserByIdNotFoundEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/user/100")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

  }

  @Test
  @Transactional
  @DisplayName("Delete user by ID - Success")
  public void testDeleteUserByIdSuccessEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/user/1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNoContent());
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("Create user with repeated CPF")
  public void testCreateUserWithRepetedCpfEndpoint() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(USER_1)))
        .andExpect(MockMvcResultMatchers.status().isConflict())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with invalid length CPF")
  public void testCreateUserWithInvalidLengthCpfEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("address", "Rua abc");
    body.put("email", "marcelaemail.com");
    body.put("phone", "1234-3454");
    body.put("cpf", "123");
    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user - With no Email Pattern")
  public void testCreateUserWithNoEmailPatternEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", "marcelaemail.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @Transactional
  @DisplayName("Create user - Success")
  public void testCreateUserSuccessEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", "marcela@email.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

    entityManager.flush();
    entityManager.clear();
  }

  @Test
  @DisplayName("Create user with null name")
  public void testCreateUserWithNameNullEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", null);
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", "marcela@email.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with blank name")
  public void testCreateUserWithNameBlankEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "");
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", "marcela@email.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with null cpf")
  public void testCreateUserWithCpfNullEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", null);
    body.put("address", "Rua abc");
    body.put("email", "marcela@email.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with blank cpf")
  public void testCreateUserWithCpfBlankEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "");
    body.put("address", "Rua abc");
    body.put("email", "marcela@email.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with null address")
  public void testCreateUserWithAddressNullEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", null);
    body.put("email", "marcela@email.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with blank address")
  public void testCreateUserWithAddressBlankEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", "");
    body.put("email", "marcela@email.com");
    body.put("phone", "1234-3454");

    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with null email")
  public void testCreateUserWithEmailNullEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", null);
    body.put("phone", "1234-3454");
    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with blank email")
  public void testCreateUserWithEmailBlankEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", "");
    body.put("phone", "1234-3454");
    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with null phone")
  public void testCreateUserWithPhoneNullEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", "marcela@email.com");
    body.put("phone", null);
    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }

  @Test
  @DisplayName("Create user with blank phone")
  public void testCreateUserWithPhoneBlankEndpoint() throws Exception {
    Map<String, Object> body = new HashMap<>();
    body.put("name", "marcela");
    body.put("cpf", "12345678910");
    body.put("address", "Rua abc");
    body.put("email", "marcela@email.com");
    body.put("phone", "");
    mockMvc.perform(MockMvcRequestBuilders.post("/user")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(body)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
  }
}
