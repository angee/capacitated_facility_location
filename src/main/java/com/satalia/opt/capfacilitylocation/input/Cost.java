package com.satalia.opt.capfacilitylocation.input;

/**
 * Represents cost in whatever currency.
 *
 * @author Andrea Rendl-Pitrey
 */
public class Cost {

  private final double cost;

  Cost(final double cost) {
    this.cost = cost;
  }

  double getCost() {
    return this.cost;
  }

  /**
   * Returns true if the cost is larger than zero (positive), false otherwise.
   * @return true if the cost is larger than zero and false otherwise.
   */
  boolean isGreaterOrEqualZero() {
    return this.cost >= 0;
  }

}
