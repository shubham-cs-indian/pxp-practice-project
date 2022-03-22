package com.cs.core.runtime.interactor.model.imprt.revision;

import java.util.List;

public class RevisionCreationModel implements IRevisionCreationModel {
  
  private List<Long> baseEntityIIDs;

  public RevisionCreationModel(List<Long> baseEntityIIDs)
  {
    this.baseEntityIIDs = baseEntityIIDs;
  }

  public List<Long> getBaseEntityIIDs()
  {
    return baseEntityIIDs;
  }

  public void setBaseEntityIIDs(List<Long> baseEntityIIDs)
  {
    this.baseEntityIIDs = baseEntityIIDs;
  }
}
