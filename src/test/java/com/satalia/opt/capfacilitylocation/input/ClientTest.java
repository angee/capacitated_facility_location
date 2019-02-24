package com.satalia.opt.capfacilitylocation.input;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import org.junit.Test;
import org.mockito.Mockito;

public class ClientTest {

  @Test
  public void getCostForMeetingDemand() {
    Facility facilityA = Mockito.mock(Facility.class);
    Facility facilityB = Mockito.mock(Facility.class);
    Cost costA = Mockito.mock(Cost.class);
    Cost costB = Mockito.mock(Cost.class);
    HashMap<Facility,Cost> costToMeetDemand = new HashMap<>();
    costToMeetDemand.put(facilityA, costA);
    costToMeetDemand.put(facilityB, costB);
    double demand = 1000;
    String id = "client-1";

    Client client = new Client(demand, costToMeetDemand, id);

    assertEquals(costA, client.getCostToMeetDemand(facilityA));
    assertEquals(costB, client.getCostToMeetDemand(facilityB));
  }

}
