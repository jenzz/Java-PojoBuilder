package com.jenzz.pojobuilder.sample.example1;

import com.jenzz.pojobuilder.api.Builder;
import java.util.Collection;

/**
 * Created by jenzz on 17/03/15.
 */
@Builder
public class DifferentPojo {

  String aString;
  Long aLong;
  StaticNestedPojo staticNestedPojo;

  @Builder
  public static class StaticNestedPojo {

    String aString;
    Collection<StaticNestedPojo> aCollection;
  }
}