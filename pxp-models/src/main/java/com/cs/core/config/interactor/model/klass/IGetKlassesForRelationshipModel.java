package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetKlassesForRelationshipModel extends IModel {
  
  public Map<String, Object> getKlasses();
  
  public void setKlasses(Map<String, Object> klasses);
}
