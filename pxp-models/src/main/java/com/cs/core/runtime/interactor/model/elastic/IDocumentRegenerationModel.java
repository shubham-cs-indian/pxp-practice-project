package com.cs.core.runtime.interactor.model.elastic;

import java.util.Set;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDocumentRegenerationModel extends IModel{
  
  public Set<Long> getBaseEntityIIDs();
  public void setBaseEntityIIDs(Set<Long> baseEntityIIDs);
  
  public Set<String> getClassifierCodes();
  public void setClassifierCodes(Set<String> classifierCodes);
  
  public Set<String> getPropertyCodes();
  public void setPropertyCodes(Set<String> propertyCodes);
}
