/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jcg.secureappwithssl.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author omozegieaziegbe
 */
@RestController
@RequestMapping("/api")
public class ProductController {

  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/product")
  public String product() {
    // Construct a JSON string (for simplicity, using a hardcoded string)
    String jsonString = "{\"name\": \"Real World Java EE Patterns\"}";
    return jsonString;
  }

  @GetMapping("/testssl")
  public String remoteRestCall() {
    return productService.remoteRestCall();
  }
}
