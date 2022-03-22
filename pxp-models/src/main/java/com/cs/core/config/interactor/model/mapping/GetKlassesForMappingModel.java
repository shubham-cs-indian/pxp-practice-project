package com.cs.core.config.interactor.model.mapping;

import java.util.HashMap;
import java.util.Map;

public class GetKlassesForMappingModel implements IGetKlassesForMappingModel {
  
  private static final long serialVersionUID = 1L;
  Map<String, Object>       klasses          = new HashMap<>();
  
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
