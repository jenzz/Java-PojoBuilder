package com.jenzz.pojobuilder.sample;

import android.app.Activity;
import java.util.Collections;

import static com.jenzz.pojobuilder.sample.DifferentPojoBuilder.differentPojo;
import static com.jenzz.pojobuilder.sample.SamplePojoBuilder.samplePojo;
import static com.jenzz.pojobuilder.sample.StaticNestedPojoBuilder.staticNestedPojo;

public class MainActivity extends Activity {

  static {
    samplePojo()
        .aString("aString")
        .aInt(10)
        .aSet(Collections.<Integer>emptySet())
        .differentPojo(
            differentPojo()
                .aString("aString")
                .aLong(10L)
                .staticNestedPojo(
                    staticNestedPojo()
                        .aString("aString")
                        .aCollection(Collections.<DifferentPojo.StaticNestedPojo>emptyList()))
        )
        .build();
  }
}
