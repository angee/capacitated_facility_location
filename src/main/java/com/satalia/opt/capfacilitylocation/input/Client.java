package com.satalia.opt.capfacilitylocation.input;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a client in the capacitated facility problem.
 *
 * @author Andrea Rendl-Pitrey
 */
public class Client {

  /** the demand of the client */
  private final double demand;
  /** the cost to meet the demand in case the demand is fulfilled by a particular Facility */
  private final Map<Facility, Cost> costToMeetDemand;
  /** the id/name of the string */
  private String id;

  public Client(final double demand, final Map<Facility, Cost> costToMeetDemand, final String id) {
    this.demand = demand;
    this.costToMeetDemand = new HashMap<>(costToMeetDemand); // create a copy
    this.id = id;
  }

  /**
   * Returns the cost it would take if the client's demand was met by the given Facility
   * @param facility
   * @return  the cost it would take if the client's demand was met by the given Facility
   */
  public Cost getCostToMeetDemand(final Facility facility) {
    return costToMeetDemand.get(facility);
  }

}
