package com.satalia.opt.capfacilitylocation.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ClientTest {

  @Test
  public void getDemand() {
    double demand = 1000;
    String id = "client-1";

    Client client = new Client(demand, id);

    assertEquals(demand, client.getDemand(), 0);
  }

}
