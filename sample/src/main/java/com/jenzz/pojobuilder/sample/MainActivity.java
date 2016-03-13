package com.jenzz.pojobuilder.sample;

import android.app.Activity;
import com.jenzz.pojobuilder.sample.example1.DifferentPojo;
import java.util.Collections;

import static com.jenzz.pojobuilder.sample.example1.DifferentPojoBuilder.differentPojo;
import static com.jenzz.pojobuilder.sample.example1.SamplePojoBuilder.samplePojo;
import static com.jenzz.pojobuilder.sample.example1.StaticNestedPojoBuilder.staticNestedPojo;
import static com.jenzz.pojobuilder.sample.example2.AddressBuilder.address;
import static com.jenzz.pojobuilder.sample.example2.UserBuilder.user;

public class MainActivity extends Activity {

  static {
    // example 1
    samplePojo(10, 10L)
        .aString("aString")
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

      // example 2
      user()
          .name("Bob The Builder")
          .age(25)
          .address(
              address()
                  .street("10 Hight Street")
                  .postcode("WC2")
                  .city("London"))
          .build();
  }
}
