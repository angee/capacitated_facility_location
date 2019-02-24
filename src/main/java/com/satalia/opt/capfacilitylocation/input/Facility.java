package com.satalia.opt.capfacilitylocation.input;

/**
 * Represents a facility in the capacitated facility location problem
 * @author Andrea Rendl-Pitrey
 */
public class Facility {

  /** the capacity of the facility (how much demand it can match) */
  private final double capacity;
  /** the cost to build the facility */
  private final Cost buildingCost;
  /** the unique name/identifier for the facility */
  private final String id;

  public Facility(final double capacity, final Cost buildingCost, final String id) {
    if (capacity < 0) {
      throw new RuntimeException("Capacity must be larger or equal to zero instead of " + capacity);
    }
    this.capacity = capacity;
    if (!buildingCost.isGreaterOrEqualZero()) {
      throw new RuntimeException(
          "Cost for setting up the facility must be larger than zero instead of: " + buildingCost);
    }
    this.buildingCost = buildingCost;
    this.id = id;
  }

  public double getCapacity() {
    return capacity;
  }

  public Cost getBuildingCost() {
    return buildingCost;
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Facility) {
      return id.equals(((Facility) o).id);
    }
    return false;
  }
}
