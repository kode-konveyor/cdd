package com.kodekonveyor.cdd.predicates;

import com.kodekonveyor.cdd.EqualityPredicate;

public class SameClass implements EqualityPredicate {

  @Override
  public boolean equals(Object first, Object second) {
    return first.getClass().equals(second.getClass());
  }

}
