package com.satalia.opt.capfacilitylocation.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a facility in the capacitated facility location problem
 *
 * @author Andrea Rendl-Pitrey
 */
public class Facility {

  /** the capacity of the facility (how much demand it can match) */
  private final double capacity;
  /** the cost to build the facility */
  private final Cost buildingCost;
  /** the cost to meet the demand of each client (incl. production and transportation costs) */
  private final Map<Client, Cost> costToMeetDemand;
  /** the unique name/identifier for the facility */
  private final String id;

  public Facility(
      final double capacity,
      final Cost buildingCost,
      final Map<Client, Cost> costToMeetDemand,
      final String id) {
    if (capacity < 0) {
      throw new RuntimeException("Capacity must be larger or equal to zero instead of " + capacity);
    }
    this.capacity = capacity;
    if (!buildingCost.isGreaterOrEqualZero()) {
      throw new RuntimeException(
          "Cost for setting up the facility must be larger than zero instead of: " + buildingCost);
    }
    this.buildingCost = buildingCost;
    this.costToMeetDemand = new HashMap<>(costToMeetDemand); // create a copy
    this.id = id;
  }

  public double getCapacity() {
    return capacity;
  }

  public Cost getBuildingCost() {
    return buildingCost;
  }

  /**
   * Returns the cost it would take if the client's demand was met by the given Facility
   *
   * @param client
   * @return the cost it would take if the client's demand was met by the given Facility
   */
  public Cost getCostToMeetDemand(final Client client) {
    return costToMeetDemand.get(client);
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Facility) {
      return id.equals(((Facility) o).id);
    }
    return false;
  }

  @Override
  public String toString() {
    return id;
  }
}
