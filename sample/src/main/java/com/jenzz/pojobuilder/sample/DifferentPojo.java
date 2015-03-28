package com.jenzz.pojobuilder.sample;

import com.jenzz.pojobuilder.api.Builder;
import java.util.Collection;

/**
 * Created by jenzz on 17/03/15.
 */
@Builder
class DifferentPojo {

  String aString;
  Long aLong;
  StaticNestedPojo staticNestedPojo;

  @Builder
  static class StaticNestedPojo {

    String aString;
    Collection<StaticNestedPojo> aCollection;
  }
}