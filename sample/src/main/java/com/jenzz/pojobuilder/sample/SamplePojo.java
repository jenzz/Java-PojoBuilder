package com.jenzz.pojobuilder.sample;

import com.jenzz.pojobuilder.api.Builder;
import com.jenzz.pojobuilder.api.Ignore;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
class SamplePojo {

  // primitives
  byte aByte;
  short aShort;
  int aInt;
  long aLong;
  float aFloat;
  double aDouble;
  boolean aBoolean;
  char aChar;
  char[] aCharArray;

  // object types
  String aString;
  List<Byte> aList;
  Map<Short, Long> aMap;
  Set<Integer> aSet;
  DifferentPojo differentPojo;

  // ignored field
  @Ignore String ignoredString;
}