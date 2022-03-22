package com.cs.core.config.interactor.model.processdetails;

import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetAllInstanceIdByProcessIdsModel extends ProcessModel
    implements IGetAllInstanceIdByProcessIdsModel {
  
  private static final long        serialVersionUID = 1L;
  protected IIdsListParameterModel instanceIds;
  
  @JsonDeserialize(as = IdsListParameterModel.class)
  @Override
  public IIdsListParameterModel getInstanceIds()
  {
    return instanceIds;
  }
  
  @JsonDeserialize(as = IdsListParameterModel.class)
  @Override
  public void setInstanceIds(IIdsListParameterModel instanceIds)
  {
    this.instanceIds = instanceIds;
  }
}
