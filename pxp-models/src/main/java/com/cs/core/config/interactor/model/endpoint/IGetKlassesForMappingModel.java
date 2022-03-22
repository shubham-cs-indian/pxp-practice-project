package com.cs.core.config.interactor.model.endpoint;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetKlassesForMappingModel extends IModel {
  
  public Map<String, Object> getKlasses();
  
  public void setKlasses(Map<String, Object> klasses);
}
