package com.bajdas.restshop.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Client {

  @Id
  @GeneratedValue
  private Long id;
  private String name;
}
