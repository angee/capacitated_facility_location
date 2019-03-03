package com.satalia.opt.capfacilitylocation.solving.benders;

import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;
import com.satalia.opt.capfacilitylocation.input.ProblemInput;
import com.satalia.opt.capfacilitylocation.solving.FullProblem;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The subproblem in the Benders decomposition approach. It takes the solutions of the x_f variables
 * (isFacilityOpened) as input.
 *
 * <h2>MIP model:</h2>
 *
 * <h3>Variables</h3>
 *
 * <ul>
 * <li>y_f_c binary: 1 if demand is met by facility f for client c, 0 otherwise
 * </ul>
 *
 * <h3>Constraints:</h3>
 *
 * <ul>
 * <li>forall clients c: sum (facilities f) y_f_c = 1 <br>
 * all clients' demand is met
 * <li>forall facilities f: sum (clients c) (demand_c * y_f_c) <= capacity_f * x_f <br>
 * do not exceed f's capacity
 * </ul>
 *
 * where x_f are constant (the master variables)
 *
 * <h2>Objective:</h2>
 *
 * <p>MINIMISE sum (facilities f) (x_j * cost_f) + sum (clients c, facilities f) (y_f_c *
 * costToMeetDemand_f_c)
 *
 * @author Andrea Rendl-Pitrey
 */
public class SubProblem {

  static {
    System.loadLibrary("jniortools");
  }

  private static final Logger LOG = LoggerFactory.getLogger(FullProblem.class);

  /**
   * the problem specification/input
   */
  private final ProblemInput input;
  /**
   * the MIP solver object
   */
  private MPSolver solver;

  /**
   * is 1 if facility i is opened
   */
  private final List<Integer> isFacilityOpened;
  /**
   * y: 0-1 variable for each facility, for each client, that is 1 if demand met by the facility for
   * the client
   */
  private List<List<MPVariable>> isDemandMet;

  SubProblem(
      final ProblemInput problemInput, final List<Integer> isFacilityOpened, final int iteration) {
    this.input = problemInput;
    this.isFacilityOpened = isFacilityOpened;
    this.solver =
        new MPSolver(
            "capacitated_facility_location-SUBPROBLEM" + iteration,
            MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
    createVariables();
    createConstraints();
    createObjective();
    LOG.info(
        "Created model with "
            + solver.numVariables()
            + " variables and "
            + solver.numConstraints()
            + " constraints.");
    LOG.debug("LP model: \n" + solver.exportModelAsLpFormat(true));
  }

  public List<Cut> solve() {
    List<Cut> cuts = new ArrayList<>();
    // TODO: solver.solve();
    // TODO: derive cuts and return them
    return cuts;
  }

  private void createVariables() {
    // y_f_c     binary: 1 if demand is met by facility f for client c, 0 otherwise
    isDemandMet = new ArrayList<>(input.getNumFacilities());
    input
        .facilities()
        .forEach(
            facility -> {
              ArrayList<MPVariable> demandMetForClients = new ArrayList<>(input.getNumClients());
              input
                  .clients()
                  .forEach(
                      client ->
                          demandMetForClients.add(
                              solver.makeIntVar(0.0, 1.0, "y_f" + facility + "_c" + client)));
              isDemandMet.add(demandMetForClients);
            });
  }

  private void createConstraints() {
    // forall clients c:
    //     sum (facilities f) y_f_c = 1     all clients' demand is met
    input
        .clients()
        .forEach(
            client -> {
              MPConstraint demandConstraint =
                  solver.makeConstraint(1, 1, "demandConstraint-c" + client);
              input
                  .facilities()
                  .forEach(
                      facility ->
                          demandConstraint.setCoefficient(
                              isDemandMet.get(facility).get(client), 1.0));
            });

    // do not exceed the facilities' capacity  (right hand side of equation is constant)
    // forall facilities f:
    //     sum (clients c) (demand_c * y_f_c)   <=  x_f * capacity_f
    //     sum (clients c) (demand_c * isDemandMet_f_c)   <=  isFacilityOpened_f * capacity_f
    input
        .facilities()
        .forEach(
            facility -> {
              MPConstraint capacityConstraint =
                  solver.makeConstraint(
                      -MPSolver.infinity(), // set rhs of equation to: capacity_f * x_f
                      isFacilityOpened.get(facility) * input.getFacility(facility).getCapacity(),
                      "capacityConstraint-f" + facility);
              input // sum (clients c) (demand_c * y_f_c)
                  .clients()
                  .forEach(
                      client ->
                          capacityConstraint.setCoefficient(
                              isDemandMet.get(facility).get(client),
                              input.getClient(client).getDemand()));
            });
  }

  private void createObjective() {
    // MINIMISE
    //      sum (facilities f) (x_j * cost_f)
    //    + sum (clients c, facilities f) (y_f_c * costToMeetDemand_f_c)
    MPObjective objective = solver.objective();
    double costOfFacilities =
        input
            .facilities()
            .mapToDouble(
                facility -> // sum (facilities f) (x_j * cost_f)
                    isFacilityOpened.get(facility) * input.getBuildingCost(facility))
            .sum();
    objective.setOffset(costOfFacilities);
    input
        .clients()
        .forEach(
            client ->
                input
                    .facilities()
                    .forEach(
                        facility -> // sum (clients c, facilities f) (y_f_c * costToMeetDemand_f_c)
                            objective.setCoefficient(
                                isDemandMet.get(facility).get(client),
                                input.getCostToMeetDemand(facility, client))));

    objective.minimization();
  }
}
