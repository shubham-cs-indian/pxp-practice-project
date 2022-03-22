package com.cs.core.runtime.interactor.model.imprt.revision;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IRevisionCreationModel extends IModel{

  public List<Long> getBaseEntityIIDs();
  public void setBaseEntityIIDs(List<Long> baseEntityIIDs);
}
