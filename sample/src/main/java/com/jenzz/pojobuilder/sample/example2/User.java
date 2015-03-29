package com.jenzz.pojobuilder.sample.example2;

import com.jenzz.pojobuilder.api.Builder;

@Builder
public class User {

  String name;
  int age;

  Address address;
}
