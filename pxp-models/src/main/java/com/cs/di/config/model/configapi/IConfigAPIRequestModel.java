package com.cs.di.config.model.configapi;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.workflow.constants.ConfigNodeType;
import com.cs.di.workflow.constants.EntityAction;

public interface IConfigAPIRequestModel extends IModel {

  ConfigNodeType getNodeType();

  void setConfigNodeType(ConfigNodeType nodeType);
  
  EntityAction getAction();
  
  void setAction(EntityAction action);
  
  String getData();
  
  void setData(String data);
}
