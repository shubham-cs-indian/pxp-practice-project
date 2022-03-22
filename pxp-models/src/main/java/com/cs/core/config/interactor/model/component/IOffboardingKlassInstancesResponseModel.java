package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IOffboardingKlassInstancesResponseModel extends IModel {
  
  public static final String KLASS_INSTANCES = "klassInstances";
  
  public List<IKlassInstance> getKlassInstances();
  
  public void setKlassInstances(List<IKlassInstance> klassInstances);
}
