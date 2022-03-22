package com.cs.core.runtime.interactor.model.eventinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class EventInstanceComprisingModel implements IEventInstanceComprisingModel {
  
  private static final long     serialVersionUID = 1L;
  
  protected IEventInstanceModel modelToReturn;
  protected IEventInstanceModel oracleModelToSave;
  
  @Override
  public IEventInstanceModel getModelToReturn()
  {
    return modelToReturn;
  }
  
  @JsonDeserialize(as = EventInstanceModel.class)
  @Override
  public void setModelToReturn(IEventInstanceModel modelToReturn)
  {
    this.modelToReturn = modelToReturn;
  }
  
  @Override
  public IEventInstanceModel getOracleModelToSave()
  {
    return oracleModelToSave;
  }
  
  @JsonDeserialize(as = EventInstanceModel.class)
  @Override
  public void setOracleModelToSave(IEventInstanceModel oracleModelToSave)
  {
    this.oracleModelToSave = oracleModelToSave;
  }
}
