package com.cs.core.runtime.interactor.model.eventinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IEventInstanceComprisingModel extends IModel {
  
  public static final String MODEL_TO_RETURN      = "modelToReturn";
  public static final String ORACLE_MODEL_TO_SAVE = "oracleModelToSave";
  
  public IEventInstanceModel getModelToReturn();
  
  public void setModelToReturn(IEventInstanceModel modelToReturn);
  
  public IEventInstanceModel getOracleModelToSave();
  
  public void setOracleModelToSave(IEventInstanceModel oracleModelToSave);
}
