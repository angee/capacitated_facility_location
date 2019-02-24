package com.satalia.opt.capfacilitylocation.input;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import org.junit.Test;
import org.mockito.Mockito;

public class ProblemInputTest {

  @Test
  public void getFacilities() {
    Facility facility = Mockito.mock(Facility.class);
    Client client = Mockito.mock(Client.class);

    ProblemInput problemInput =
        new ProblemInput(Collections.singletonList(facility), Collections.singletonList(client));

    assertEquals(Collections.singletonList(facility), problemInput.getFacilities());
  }

  @Test
  public void getClients() {
    Facility facility = Mockito.mock(Facility.class);
    Client client = Mockito.mock(Client.class);

    ProblemInput problemInput =
        new ProblemInput(Collections.singletonList(facility), Collections.singletonList(client));

    assertEquals(Collections.singletonList(client), problemInput.getClients());
  }
}
