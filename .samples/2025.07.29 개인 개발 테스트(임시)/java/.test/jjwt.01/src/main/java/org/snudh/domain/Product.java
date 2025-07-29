package org.snudh.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Product {

  @Id
  @GeneratedValue(generator = "prod-generator")
  @GenericGenerator(
      name = "prod-generator",
      parameters = @Parameter(name = "prefix", value = "prod"),
      strategy = "org.snudh.domain.generator.MyGenerator")
  private String prodId;

  public String getProdId() {
    return prodId;
  }

  public void setProdId(String prodId) {
    this.prodId = prodId;
  }

}
