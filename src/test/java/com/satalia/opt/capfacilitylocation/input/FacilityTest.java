package com.satalia.opt.capfacilitylocation.input;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

public class FacilityTest {

  @Test
  public void getCapacity() {
    String id = "1";
    double capacity = 1000;
    Cost buildingCost = Mockito.mock(Cost.class);
    when(buildingCost.isGreaterOrEqualZero()).thenReturn(true);
    Facility facility = new Facility(capacity, buildingCost, id);

    double returnedCapacity = facility.getCapacity();

    assertEquals(capacity, returnedCapacity, 0);
  }

  @Test
  public void getBuildingCost() {
    String id = "1";
    double capacity = 1000;
    double expectedCost = 8000;
    Cost buildingCost = new Cost(expectedCost);
    Facility facility = new Facility(capacity, buildingCost, id);

    Cost returnedBuildingCost = facility.getBuildingCost();

    assertEquals(expectedCost, returnedBuildingCost.getCost(), 0);
  }

  @Test(expected = RuntimeException.class)
  public void createFacilityThrowsException_whenCapacityIsSmallerThanZero() {
    String id = "1";
    double capacity = -1;
    Cost buildingCost = Mockito.mock(Cost.class);
    when(buildingCost.isGreaterOrEqualZero()).thenReturn(true);

    new Facility(capacity, buildingCost, id);
  }

  @Test(expected = RuntimeException.class)
  public void createFacilityThrowsException_whenBuildingCostIsSmallerThanZero() {
    String id = "1";
    double capacity = 1000;
    Cost buildingCost = Mockito.mock(Cost.class);
    when(buildingCost.isGreaterOrEqualZero()).thenReturn(false);

    new Facility(capacity, buildingCost, id);
  }
}
