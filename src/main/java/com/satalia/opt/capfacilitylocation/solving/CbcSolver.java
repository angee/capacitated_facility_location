package com.satalia.opt.capfacilitylocation.solving;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import com.satalia.opt.capfacilitylocation.input.ProblemInput;
import java.util.ArrayList;
import java.util.List;

/**
 * Mixed integer programming solver for the capacitated facility location problem.
 *
 * <h2>MIP model:</h2>
 *
 * <ul>
 *   <li>x_f binary: 1 if facility f is opened, 0 otherwise
 *   <li>y_f_c binary: 1 if demand is met by facility f for client c, 0 otherwise
 * </ul>
 *
 * <h2>Constraints:</h2>
 *
 * <ul>
 *   <li>forall clients c: sum (facilities f) y_f_c = 1 <br>
 *       all clients' demand is met
 *   <li>forall facilities f: sum (clients c) (demand_c * y_f_c) <= capacity_f * x_f <br>
 *       do not exceed f's capacity
 * </ul>
 *
 * <h2>Objective:</h2>
 *
 * <p>MINIMISE sum (facilities f) (x_j * cost_f) + sum (clients c, facilities f) (y_f_c *
 * costToMeetDemand_f_c)
 */
public class CbcSolver {

  static {
    System.loadLibrary("jniortools");
  }

  private final ProblemInput input;

  private MPSolver solver;
  /** x: 0-1 variable that is 1 if facility i is opened */
  private List<MPVariable> isFacilityOpened;
  /**
   * y: 0-1 variable for each facility, for each client, that is 1 if demand met by the facility for
   * the client
   */
  private List<List<MPVariable>> isDemandMet;

  public CbcSolver(ProblemInput problemInput) {
    this.input = problemInput;
    this.solver =
        new MPSolver(
            "capacitated_facility_location",
            MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
    createVariables();
    createConstraints();
    createObjective();
  }

  private void createVariables() {
    // x_f       binary:  1 if facility f is opened, 0 otherwise
    isFacilityOpened = new ArrayList<>(input.getNumFacilities());
    for (int facility = 0; facility < input.getNumFacilities(); facility++) {
      isFacilityOpened.add(solver.makeIntVar(0, 1, "x_" + (facility + 1)));
      // TODO: makeBoolVar instead?
    }
    // y_f_c     binary: 1 if demand is met by facility f for client c, 0 otherwise
    isDemandMet = new ArrayList<>(input.getNumFacilities());
    for (int facility = 0; facility < input.getNumFacilities(); facility++) {
      ArrayList<MPVariable> demandMetForClients = new ArrayList<>(input.getNumClients());
      for (int client = 0; client < input.getNumClients(); client++) {
        demandMetForClients.add(solver.makeIntVar(0.0, 1.0, "y_f" + facility + "_c" + client));
      }
      isDemandMet.add(demandMetForClients);
    }
  }

  private void createConstraints() {
    // forall clients c:
    //     sum (facilities f) y_f_c = 1     all clients' demand is met
    for (int client = 0; client < input.getNumClients(); client++) {
      MPConstraint demandConstraint = solver.makeConstraint(1, 1, "demandConstraint-c" + client);
      for (int facility = 0; facility < input.getNumFacilities(); facility++) {
        demandConstraint.setCoefficient(isDemandMet.get(facility).get(client), 1.0);
      }
    }
    // forall facilities f:
    //     sum (clients c) (demand_c * y_f_c)  - capacity_f * x_f  <= 0      do not exceed f's
    // capacity
    for (int facility = 0; facility < input.getNumFacilities(); facility++) {
      MPConstraint capacityConstraint =
          solver.makeConstraint(0, MPSolver.infinity(), "capacityConstraint-f" + facility);
      // sum (clients c) (demand_c * y_f_c)
      for (int client = 0; client < input.getNumClients(); client++) {
        capacityConstraint.setCoefficient(
            isDemandMet.get(facility).get(client), input.getClients().get(client).getDemand());
      }
      // capacity_f * x_f
      capacityConstraint.setCoefficient(
          isFacilityOpened.get(facility), input.getFacilities().get(facility).getCapacity() * -1);
    }
  }

  private void createObjective() {
    // MINIMISE
    //      sum (facilities f) (x_j * cost_f)
    //    + sum (clients c, facilities f) (y_f_c * costToMeetDemand_f_c)
    MPObjective objective = solver.objective();
    for (int facility = 0; facility < input.getNumFacilities(); facility++) {
      objective.setCoefficient(
          isFacilityOpened.get(facility),
          input.getFacilities().get(facility).getBuildingCost().getCost());
    }
    for (int client = 0; client < input.getNumClients(); client++) {
      for (int facility = 0; facility < input.getNumFacilities(); facility++) {
        objective.setCoefficient(
            isDemandMet.get(facility).get(client),
            input
                .getFacilities()
                .get(facility)
                .getCostToMeetDemand(input.getClients().get(client))
                .getCost());
      }
    }
    objective.minimization();
  }
}
