package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public interface IBulkCreateInstanceModel extends IModel {

  public List<ICreateInstanceModel> getCreationList();
  public void setCreationList(List<ICreateInstanceModel> creationList);

}
