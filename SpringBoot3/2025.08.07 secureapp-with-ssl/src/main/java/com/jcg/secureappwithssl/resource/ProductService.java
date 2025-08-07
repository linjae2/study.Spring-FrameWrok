/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jcg.secureappwithssl.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author omozegieaziegbe
 */
@Service
public class ProductService {
  private final RestTemplate restTemplate;

  @Autowired
  public ProductService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public String remoteRestCall() {
    return this.restTemplate.getForObject("https://jsonplaceholder.typicode.com/todos", String.class);
  }
}
