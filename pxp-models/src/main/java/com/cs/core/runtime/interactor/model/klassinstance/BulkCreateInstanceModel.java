package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BulkCreateInstanceModel implements IBulkCreateInstanceModel{

  List<ICreateInstanceModel> creationList;

  @Override
  public List<ICreateInstanceModel> getCreationList()
  {
    return creationList;
  }

  @JsonDeserialize(contentAs= CreateInstanceModel.class)
  @Override
  public void setCreationList(List<ICreateInstanceModel> creationList)
  {
    this.creationList = creationList;
  }
}
