package com.cs.core.config.interactor.model.klass;

import java.util.HashMap;
import java.util.Map;

public class GetKlassesForRelationshipModel implements IGetKlassesForRelationshipModel {
  
  Map<String, Object> klasses = new HashMap<>();
  
  @Override
  public Map<String, Object> getKlasses()
  {
    return klasses;
  }
  
  @Override
  public void setKlasses(Map<String, Object> klasses)
  {
    this.klasses = klasses;
  }
}
