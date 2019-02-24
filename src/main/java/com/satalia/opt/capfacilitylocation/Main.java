package com.satalia.opt.capfacilitylocation;

import com.satalia.opt.capfacilitylocation.input.BenchmarkReader;
import com.satalia.opt.capfacilitylocation.input.ProblemInput;
import java.io.IOException;

public class Main {

  public static void main(String[] args) {

    if(args.length != 1) {
      throw new RuntimeException("Expecting 1 argument: the benchmark input file.");
    } // TODO: implement proper CLI


    try {
      BenchmarkReader reader = new BenchmarkReader(args[0]);
      ProblemInput problemInput = reader.readBeasleyBenchmark();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
