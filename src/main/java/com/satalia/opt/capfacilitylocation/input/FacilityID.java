package com.satalia.opt.capfacilitylocation.input;

public class FacilityID {

  /** unique identifier for facilities */
  private final String id;

  public FacilityID(final int id) {
    this.id = Integer.toString(id);
  }

  @Override
  public boolean equals(Object o) {
    if(o instanceof  FacilityID) {
      return id.equals(((FacilityID) o).id);
    }
    return false;
  }

}
