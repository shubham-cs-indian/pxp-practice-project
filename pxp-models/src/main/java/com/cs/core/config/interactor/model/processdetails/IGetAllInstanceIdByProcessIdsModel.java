package com.cs.core.config.interactor.model.processdetails;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

public interface IGetAllInstanceIdByProcessIdsModel extends IProcessModel {
  
  public static final String INSTANCE_IDS = "instanceIds";
  
  public IIdsListParameterModel getInstanceIds();
  
  public void setInstanceIds(IIdsListParameterModel instanceIds);
}
