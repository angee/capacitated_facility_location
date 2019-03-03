package com.satalia.opt.capfacilitylocation;

import com.satalia.opt.capfacilitylocation.input.BenchmarkReader;
import com.satalia.opt.capfacilitylocation.input.ProblemInput;
import com.satalia.opt.capfacilitylocation.solving.CbcSolver;
import com.satalia.opt.capfacilitylocation.solving.Solution;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {

    String inputFile;
    if (args.length != 1) { // TODO: implement proper CLI
      LOG.warn(
          "Expecting 1 program argument: the path to instance to solve. Solving Beasley cap62 instance.");
      inputFile = "src/main/resources/beasley/cap62"; // run the Beasley cap62 instance by default
    } else {
      inputFile = args[0];
    }

    try {
      BenchmarkReader reader = new BenchmarkReader(inputFile);
      ProblemInput problemInput = reader.readBeasleyBenchmark();
      CbcSolver solver = new CbcSolver(problemInput);
      Solution solution = solver.solve();
      System.out.println(solution);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
