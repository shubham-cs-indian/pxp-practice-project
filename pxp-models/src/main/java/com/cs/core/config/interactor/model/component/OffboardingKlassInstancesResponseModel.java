package com.cs.core.config.interactor.model.component;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;

import java.util.List;

public class OffboardingKlassInstancesResponseModel
    implements IOffboardingKlassInstancesResponseModel {
  
  private static final long      serialVersionUID = 1L;
  
  protected List<IKlassInstance> klassInstances;
  
  @Override
  public List<IKlassInstance> getKlassInstances()
  {
    return this.klassInstances;
  }
  
  @Override
  public void setKlassInstances(List<IKlassInstance> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
}
