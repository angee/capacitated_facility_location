package com.satalia.opt.capfacilitylocation.input;

import java.util.List;

public class ProblemInput {

  private final List<Facility> facilities;
  private final List<Client> clients;

  ProblemInput(final List<Facility> facilities, final List<Client> clients) {
    this.facilities = facilities;
    this.clients = clients;
  }

}
