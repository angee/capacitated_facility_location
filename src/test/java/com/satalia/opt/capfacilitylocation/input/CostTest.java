package com.satalia.opt.capfacilitylocation.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CostTest {

  @Test
  public void getCost() {
    double expectedCost = 1000;
    Cost cost = new Cost(expectedCost);

    assertEquals(expectedCost, cost.getCost(), 0);
  }

  @Test
  public void isGreaterThanZeroIsTrue() {
    double expectedCost = 0;
    Cost cost = new Cost(expectedCost);

    assertTrue(cost.isGreaterOrEqualZero());
  }

  @Test
  public void isGreaterThanZeroIsFalse() {
    double expectedCost = -1;
    Cost cost = new Cost(expectedCost);

    assertFalse(cost.isGreaterOrEqualZero());
  }
}
