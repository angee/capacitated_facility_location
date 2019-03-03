package com.satalia.opt.capfacilitylocation.input;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads the problem input from a benchmark file. Currently Beasley's benchmark format is supported
 * (see http://www.di.unipi.it/optimize/Data/mexch/BeasleyData.zip or
 * http://or-brescia.unibs.it/instances/instances_sscflp)
 *
 * @author Andrea Rendl-Pitrey
 */
public class BenchmarkReader {

  // final containers for creating the ProblemSetup
  private List<Facility> facilities;
  private List<Client> clients;

  // helper variables to collect data while reading the file
  private int numClients;
  private int numFacilities;
  private ArrayList<Cost> facilityBuildingCost;
  private ArrayList<Double> facilityCapacity;

  /** reader class to read the benchmark file */
  private Reader reader;

  private class Reader {
    private int lineNumber = 0;
    private BufferedReader reader;

    Reader(final String filename) throws FileNotFoundException {
      FileReader fileReader = new FileReader(filename);
      reader = new BufferedReader(fileReader);
    }

    String readNextLine() throws IOException {
      String line = reader.readLine();
      lineNumber++;
      return line;
    }

    int getLineNumber() {
      return lineNumber;
    }
  }

  public BenchmarkReader(final String filename) throws FileNotFoundException {
    this.reader = new Reader(filename);
  }

  public ProblemInput readBeasleyBenchmark() throws IOException {
    extractNumberOfFacilitiesAndClients(); // first line contains "numFacilities numClients"
    this.facilities = new ArrayList<>(numFacilities);
    this.clients = new ArrayList<>(numClients);
    this.facilityBuildingCost = new ArrayList<>(numFacilities);
    this.facilityCapacity = new ArrayList<>(numFacilities);

    readFacilityInfo();
    readClientInfo();
    readClientCosts();

    return new ProblemInput(facilities, clients);
  }

  private void extractNumberOfFacilitiesAndClients() throws IOException {
    String line = reader.readNextLine();
    String[] words = line.split(" ");
    if (words.length != 2) {
      throw new RuntimeException(
          "Invalid benchmark file. Expecting 2 words in line 1 instead of: " + line);
    }
    numFacilities = Integer.parseInt(words[0]);
    numClients = Integer.parseInt(words[1]);
  }

  private void readFacilityInfo() throws IOException {
    int facilityCount = 1;
    while (facilityCount <= this.numFacilities) {
      String line = reader.readNextLine();
      if (line == null) {
        throw new RuntimeException(
            "Expecting facility info for facility in line "
                + reader.getLineNumber()
                + " instead of:"
                + facilityCount);
      }
      String[] words = line.split(" "); // Format of line: "capacity cost"
      if (words.length != 2) {
        throw new RuntimeException(
            "Invalid benchmark file. Expecting 2 words in line "
                + reader.getLineNumber()
                + " instead of: "
                + line);
      }
      double capacity = Double.parseDouble(words[0]); // read capacity
      double facilityCost = Double.parseDouble(words[1]); // read cost
      this.facilityCapacity.add(capacity);
      this.facilityBuildingCost.add(new Cost(facilityCost));
      facilityCount++;
    }
  }

  private void readClientInfo() throws IOException {
    // read the demand info
    String line = reader.readNextLine();
    String[] words = line.split(" ");
    if (words.length != this.numClients) {
      throw new RuntimeException(
          "Reading client demand. Expecting "
              + this.numClients
              + " words in in line "
              + reader.getLineNumber()
              + " instead of: "
              + line);
    }
    Arrays.stream(words)
        .forEach(
            word -> {
              double demand = Double.parseDouble(word);
              clients.add(new Client(demand, "client-" + (clients.size() + 1)));
            });
  }

  private void readClientCosts() throws IOException {
    int facilityCount = 0;
    while (facilityCount < this.numFacilities) {
      String line = reader.readNextLine();
      if (line == null) {
        throw new RuntimeException(
            "Expecting demand cost for serving clients in line "
                + reader.getLineNumber()
                + ", but line is empty.");
      }
      Map<Client, Cost> costToMeetDemand = new HashMap<>(this.numFacilities);
      String[] words = line.split(" "); // Format of line: "cost_c1  cost_c2 ... cost_cn"
      if (words.length != this.numClients) {
        throw new RuntimeException(
            "Expecting "
                + this.numClients
                + " words in line "
                + reader.getLineNumber()
                + " instead of "
                + words.length);
      }
      for (int clientCnt = 0; clientCnt < numClients; clientCnt++) {
        final Client client = this.clients.get(clientCnt);
        costToMeetDemand.put(client, new Cost(Double.parseDouble(words[clientCnt])));
      }
      facilities.add(
          new Facility(
              facilityCapacity.get(facilityCount),
              facilityBuildingCost.get(facilityCount),
              costToMeetDemand,
              "facility-" + facilityCount));
      facilityCount++;
    }
  }
}
