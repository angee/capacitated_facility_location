package com.satalia.opt.capfacilitylocation.solving.benders;

import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPSolver.ResultStatus;
import com.google.ortools.linearsolver.MPVariable;
import com.satalia.opt.capfacilitylocation.input.ProblemInput;
import com.satalia.opt.capfacilitylocation.solving.FullProblem;
import com.satalia.opt.capfacilitylocation.solving.Solution;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Master problem for Benders decomposition, where the master variables are the binary variables
 * deciding which facilities to open.
 *
 * <h2>MIP model:</h2>
 *
 * <ul>
 * <li>x_f binary: 1 if facility f is opened, 0 otherwise
 * <li>approximation: numerical variable >= 0, approximating cost of subproblem
 * </ul>
 *
 * <h2>Constraints:</h2>
 *
 * none, except cuts that are derived from the subproblem
 *
 * <h2>Objective:</h2>
 *
 * <p>MINIMISE sum (facilities f) (x_j * cost_f) + approximation
 *
 * @author Andrea Rendl-Pitrey
 */
public class MasterProblem {

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
   * x: 0-1 variable that is 1 if facility i is opened
   */
  private List<MPVariable> isFacilityOpened;

  /**
   * approximation for the objective value of the subproblem
   */
  private MPVariable approximationObjectiveSubProblem;

  public MasterProblem(final ProblemInput problemInput) {
    this.input = problemInput;
    this.solver =
        new MPSolver(
            "capacitated_facility_location-MASTER",
            MPSolver.OptimizationProblemType.CBC_MIXED_INTEGER_PROGRAMMING);
    createVariables();
    // there are no constraints in the master problem
    createObjective();
    LOG.info(
        "Created model with "
            + solver.numVariables()
            + " variables and "
            + solver.numConstraints()
            + " constraints.");
    LOG.debug("LP model: \n" + solver.exportModelAsLpFormat(true));
  }

  public Solution solve() {
    ResultStatus resultStatus = solver.solve();
    int iterations = 0;
    while (iterations < 100) { // TODO: add stopping criterion for Benders
      if (resultStatus == ResultStatus.OPTIMAL || resultStatus == ResultStatus.FEASIBLE) {
        SubProblem subproblem = new SubProblem(input, extractSolutionValues(), iterations++);
        // TODO: cuts = subproblem.solve()
        // TODO: solver.addConstraints(cuts);
        // TODO: resultStatus = solver.solve();
      } else {
        throw new RuntimeException(
            "Could not solve problem. Solver result status: " + resultStatus);
      }
    }
    return null; // TODO: extract solution
  }

  private void createVariables() {
    // x_f       binary:  1 if facility f is opened, 0 otherwise
    isFacilityOpened = new ArrayList<>(input.getNumFacilities());
    input
        .facilities()
        .forEach(facility -> isFacilityOpened.add(solver.makeIntVar(0, 1, "x_" + facility)));

    approximationObjectiveSubProblem =
        solver.makeNumVar(0.0, Double.POSITIVE_INFINITY, "approximation-subproblem");
  }

  private void createObjective() {
    // MINIMISE
    //      sum (facilities f) (x_j * cost_f)
    //    + approximation of the cost of the subproblem
    MPObjective objective = solver.objective();
    input
        .facilities()
        .forEach(
            facility -> // sum (facilities f) (x_j * cost_f)
                objective.setCoefficient(
                    isFacilityOpened.get(facility), input.getBuildingCost(facility)));

    // + an approximation of the subproblem's cost
    objective.setCoefficient(approximationObjectiveSubProblem, 1.0);

    objective.minimization();
  }

  private List<Integer> extractSolutionValues() {
    List<Integer> solutionValues = new ArrayList<>(this.isFacilityOpened.size());
    this.isFacilityOpened
        .forEach(var -> solutionValues.add(var.solutionValue() == 0.0 ? 0 : 1));
    return solutionValues;
  }
}
