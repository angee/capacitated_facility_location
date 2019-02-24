package com.satalia.opt.capfacilitylocation.input;

/**
 * Represents a client in the capacitated facility problem.
 *
 * @author Andrea Rendl-Pitrey
 */
public class Client {

  /** the demand of the client */
  private final double demand;

  /** the id/name of the string */
  private String id;

  public Client(final double demand, final String id) {
    this.demand = demand;
    this.id = id;
  }

  public double getDemand() {
    return this.demand;
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof Client) {
      return id.equals(((Client) o).id);
    }
    return false;
  }

  @Override
  public String toString() {
    return id;
  }
}
