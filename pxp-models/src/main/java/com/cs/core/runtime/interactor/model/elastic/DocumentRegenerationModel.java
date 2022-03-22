package com.cs.core.runtime.interactor.model.elastic;

import java.util.HashSet;
import java.util.Set;

public class DocumentRegenerationModel implements IDocumentRegenerationModel {
  
  protected Set<Long>   baseEntityIIDs  = new HashSet<>();
  protected Set<String> classifierCodes = new HashSet<>();
  protected Set<String> propertyCodes = new HashSet<>();

  
  @Override
  public Set<Long> getBaseEntityIIDs()
  {
    return baseEntityIIDs;
  }
  
  @Override
  public void setBaseEntityIIDs(Set<Long> baseEntityIIDs)
  {
    this.baseEntityIIDs = baseEntityIIDs;
  }
  
  @Override
  public Set<String> getClassifierCodes()
  {
    return classifierCodes;
  }
  
  @Override
  public void setClassifierCodes(Set<String> classifierCodes)
  {
    this.classifierCodes = classifierCodes;
  }
  
  @Override
  public Set<String> getPropertyCodes()
  {
    return propertyCodes;
  }
  
  @Override
  public void setPropertyCodes(Set<String> propertyCodes)
  {
    this.propertyCodes = propertyCodes;
  }
  
}
