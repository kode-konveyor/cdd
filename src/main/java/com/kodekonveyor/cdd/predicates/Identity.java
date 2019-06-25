package com.kodekonveyor.cdd.predicates;

import com.kodekonveyor.cdd.EqualityPredicate;

public class Identity implements EqualityPredicate {

  @Override
  public boolean equals(Object first, Object second) {
    return first.equals(second);
  }

}
