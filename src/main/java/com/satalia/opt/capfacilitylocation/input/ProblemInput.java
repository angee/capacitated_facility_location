package com.satalia.opt.capfacilitylocation.input;

import java.util.List;
import java.util.stream.IntStream;

public class ProblemInput {

  private final List<Facility> facilities;
  private final List<Client> clients;

  ProblemInput(final List<Facility> facilities, final List<Client> clients) {
    this.facilities = facilities;
    this.clients = clients;
  }

  List<Facility> getFacilities() {
    return this.facilities;
  }

  List<Client> getClients() {
    return this.clients;
  }

  public int getNumFacilities() {
    return this.facilities.size();
  }

  public int getNumClients() {
    return this.clients.size();
  }

  public IntStream clients() {
    return IntStream.range(0, clients.size());
  }

  public IntStream facilities() {
    return IntStream.range(0, facilities.size());
  }

  public Client getClient(int client) {
    if(client < 0 || client >= clients.size()) {
      throw new RuntimeException("Cannot find client, invalid client number: " + client);
    }
    return clients.get(client);
  }

  public Facility getFacility(int facility) {
    if(facility < 0 || facility >= facilities.size()) {
      throw new RuntimeException("Cannot find facility, invalid facility number: " + facility);
    }
    return facilities.get(facility);
  }

  /**
   * returns the cost for when the given facility serves the given client. Throws an exception
   * if the facility or client numbers are out of bounds.
   * @param facility
   * @param client
   * @return the cost for when the given facility serves the given client
   */
  public double getCostToMeetDemand(int facility, int client) {
    Facility f = getFacility(facility);
    Client c = getClient(client);
    return f.getCostToMeetDemand(c).getCost();
  }

  public double getBuildingCost(int facility) {
    return getFacility(facility).getBuildingCost().getCost();
  }
}
