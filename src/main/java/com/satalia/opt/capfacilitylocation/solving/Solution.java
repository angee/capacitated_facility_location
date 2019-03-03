package com.satalia.opt.capfacilitylocation.solving;

import com.satalia.opt.capfacilitylocation.input.Client;
import com.satalia.opt.capfacilitylocation.input.Facility;
import java.util.List;
import java.util.Map;

/**
 * Stores the solution of the uncapacitated facility location problem
 *
 * @author Andrea Rendl-Pitrey
 */
public class Solution {

  /** the set of facilities that will be opened */
  private final List<Facility> openedFacilities;
  /** mapping each client to the facility it will be served with */
  private final Map<Client, Facility> facilityServingClient;
  /** the objective value */
  private final double objectiveValue;

  public Solution(
      final List<Facility> openedFacilities, final Map<Client, Facility> facilityServingClient) {
    this.openedFacilities = openedFacilities;
    this.facilityServingClient = facilityServingClient;
    this.objectiveValue = calculateObjectiveValue();
  }

  private double calculateObjectiveValue() {
    double objective =
        openedFacilities.stream()
            .mapToDouble(facility -> facility.getBuildingCost().getCost())
            .sum();
    objective +=
        facilityServingClient.entrySet().stream()
            .mapToDouble(e -> e.getValue().getCostToMeetDemand(e.getKey()).getCost())
            .sum();
    return objective;
  }

  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("SOLUTION\nOpened Facilities:\n");
    openedFacilities.forEach(
        f -> {
          sb.append(f);
          sb.append("\n");
        });
    sb.append("\nClients served by:\n");
    facilityServingClient
        .keySet()
        .forEach(
            client -> {
              sb.append(client);
              sb.append(" --> ");
              sb.append(facilityServingClient.get(client));
              sb.append("\n");
            });
    sb.append("Objective: ");
    sb.append(objectiveValue);
    sb.append("\n");
    return sb.toString();
  }
}
