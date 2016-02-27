Java - POJO Builder [![Build Status](https://travis-ci.org/jenzz/Java-PojoBuilder.svg?branch=master)](https://travis-ci.org/jenzz/Java-PojoBuilder)
===================
* A Java code generator for the builder pattern using annotation processing

Usage
-----
Just annotate your POJOs with the `@Builder` annotation, like so:

```java
@Builder
public class User {
  String name;
  int age;
  Address address;
}
```

```java
@Builder
public class Address {
  String street;
  String postcode;
  String city;
}
```

When you compile your project, a new Builder class will be generated for each annotated POJO, like so:


```java
public final class UserBuilder {

  private String name;
  private int age;
  private Address address;

  public static UserBuilder user() {
    return new UserBuilder();
  }

  public static UserBuilder user(User from) {
    UserBuilder builder = new UserBuilder();
    builder.name = from.name;
    builder.age = from.age;
    builder.address = from.address;
    return builder;
  }

  public UserBuilder name(String name) {
    this.name = name;
    return this;
  }

  public UserBuilder age(int age) {
    this.age = age;
    return this;
  }

  public UserBuilder address(Address address) {
    this.address = address;
    return this;
  }

  public UserBuilder address(AddressBuilder addressBuilder) {
    this.address = addressBuilder.build();
    return this;
  }

  public User build() {
    User user = new User();
    user.name = name;
    user.age = age;
    user.address = address;
    return user;
  }
}
```

Using static imports, you can then create those objects as simple as:

```java
User user = user()
  .name("Bob The Builder")
  .age(25)
  .address(
      address()
      .street("10 Hight Street")
      .postcode("WC2")
      .city("London"))
  .build();
```

Fields annotated with `@Ignore` will be ignored.

Example
-------
Check out the [sample project](https://github.com/jenzz/Java-PojoBuilder/tree/master/sample) for an example implementation.

The sample project is currently an Android module, but it should work with a pure Java project just as well.
If somebody knows how to setup an annotation processor for a Java module using Gradle, please let me know.
The [apt plugin](https://bitbucket.org/hvisser/android-apt) currently supports only Android modules.

Download
--------

Grab it via Gradle:

```groovy
compile 'com.jenzz.pojobuilder:api:1.0'
apt 'com.jenzz.pojobuilder:processor:1.0'
```

You only need to include the **api** module (contains just the annotations) as a real dependency.

The annotation **processor** module can be a compile time only dependency. To do that I highly recommend using Hugo Visser's great [apt plugin](https://bitbucket.org/hvisser/android-apt).

License
-------
This project is licensed under the [MIT License](https://raw.githubusercontent.com/jenzz/Java-PojoBuilder/master/LICENSE).