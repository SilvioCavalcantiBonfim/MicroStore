package com.microsservicos.productapi.integration;

import java.util.List;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsservicos.dto.product.CategoryDto;
import com.microsservicos.dto.product.ProductDto;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@SpringBootTest(properties = {
        "spring.flyway.locations=classpath:db/migration,classpath:db/migration-test",
        "spring.flyway.clean-disabled=false",
        "spring.flyway.schemas=products",
        "spring.datasource.url = jdbc:h2:mem:testdb;DB_CLOSE_ON_EXIT=FALSE;MODE=PostgreSQL",
        "spring.datasource.driverClassName = org.h2.Driver",
        "spring.datasource.username = sa",
        "spring.datasource.password = password",
        "spring.datasource.hikari.maximumPoolSize=2",
        "spring.jpa.database-platform = org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.default_schema = products",
        "server.port = -1",
        "logging.level.org.flywaydb=DEBUG"
})
@ActiveProfiles("test")
@DirtiesContext
@AutoConfigureMockMvc
public class ProductApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    private static final CategoryDto CATEGORY_1 = new CategoryDto(1L, "Eletrônico");
    private static final CategoryDto CATEGORY_2 = new CategoryDto(3L, "Brinquedos");

    private static final ProductDto PRODUCT_1 = new ProductDto("ABC123", "Smartphone Modelo X",
            "Um smartphone avançado com ótimas funcionalidades.", 799.99f, CATEGORY_1);
    private static final ProductDto PRODUCT_2 = new ProductDto("GHI789", "Boneca de Pelúcia",
            "Uma boneca fofa e adorável para presentear crianças.", 29.99f, CATEGORY_2);

    @Test
    public void getAllSuccess() throws JsonProcessingException, Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(List.of(PRODUCT_1))));
    }

    @Test
    public void getByCategoryIdSuccess() throws JsonProcessingException, Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/category/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(List.of(PRODUCT_1))));
    }

    @Test
    public void getByCategoryIdExceptionNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/category/-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getByCategoryIdExceptionIllegalArgument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/category/a").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void findByIdSuccess() throws JsonProcessingException, Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/ABC123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(PRODUCT_1)));
    }

    @Test
    public void findByIdExceptionNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/-1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void findByIdExceptionNoArgument() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/product/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    public void deleteSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/ABC123").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        entityManager.flush();
        entityManager.clear();
        
    }

    @Test
    public void deleteExceptionNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/XYZ000").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @Transactional
    public void CreateSuccess() throws JsonProcessingException, Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(PRODUCT_2)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content()
                        .json(objectMapper.writeValueAsString(PRODUCT_2)));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void CreateExceptionCategoryNotFound() throws JsonProcessingException, Exception {

        ProductDto body = new ProductDto("GHI789", "Boneca de Pelúcia",
                "Uma boneca fofa e adorável para presentear crianças.", 29.99f, new CategoryDto(-1L, "category"));

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void CreateExceptionWithIdNull() throws JsonProcessingException, Exception {

        ProductDto body = new ProductDto(null, "Boneca de Pelúcia",
                "Uma boneca fofa e adorável para presentear crianças.", 29.99f, CATEGORY_2);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createProductWithNullName() throws JsonProcessingException, Exception {
        ProductDto body = new ProductDto("GHI789", null,
                "Uma boneca fofa e adorável para presentear crianças.", 29.99f, CATEGORY_2);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createProductWithNullDescription() throws JsonProcessingException, Exception {
        ProductDto body = new ProductDto("GHI789", "Boneca de Pelúcia",
                null, 29.99f, CATEGORY_2);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createProductWithNullPrice() throws JsonProcessingException, Exception {
        ProductDto body = new ProductDto("GHI789", "Boneca de Pelúcia",
                "Uma boneca fofa e adorável para presentear crianças.", null, CATEGORY_2);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void createProductWithNullCategoryId() throws JsonProcessingException, Exception {
        ProductDto body = new ProductDto("GHI789", "Boneca de Pelúcia",
                "Uma boneca fofa e adorável para presentear crianças.", 29.99f, null);

        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

}
