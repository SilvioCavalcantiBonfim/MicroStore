package com.microsservicos.shoppingapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
  
  @Bean
  public RestClient userClient(@Value("${USER_API_URL:http://localhost:8080}") String url){
    return RestClient.create(url);
  }
  
  @Bean
  public RestClient productClient(@Value("${PRODUCT_API_URL:http://localhost:8081}") String url){
    return RestClient.create(url);
  }

}
