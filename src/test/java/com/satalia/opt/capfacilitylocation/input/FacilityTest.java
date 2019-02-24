package com.satalia.opt.capfacilitylocation.input;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mockito.Mockito;

public class FacilityTest {

  @Test
  public void getCapacity() {
    FacilityID id = Mockito.mock(FacilityID.class);
    double capacity = 1000;
    double buildingCost = 8000;
    Facility facility = new Facility(capacity, buildingCost, id);

    double returnedCapacity = facility.getCapacity();

    assertEquals(capacity, returnedCapacity, 0);
  }

  @Test
  public void getBuildingCost() {
    FacilityID id = Mockito.mock(FacilityID.class);
    double capacity = 1000;
    double buildingCost = 8000;
    Facility facility = new Facility(capacity, buildingCost, id);

    double returnedBuildingCost = facility.getBuildingCost();

    assertEquals(buildingCost, returnedBuildingCost, 0);
  }

  @Test(expected = RuntimeException.class)
  public void createFacilityThrowsException_whenCapacityIsInvalid() {
    FacilityID id = Mockito.mock(FacilityID.class);
    double capacity = -1;
    double buildingCost = 8000;

    new Facility(capacity, buildingCost, id);
  }

  @Test(expected = RuntimeException.class)
  public void createFacilityThrowsException_whenBuildingCostIsInvalid() {
    FacilityID id = Mockito.mock(FacilityID.class);
    double capacity = 1000;
    double buildingCost = -1;

    new Facility(capacity, buildingCost, id);
  }
}
