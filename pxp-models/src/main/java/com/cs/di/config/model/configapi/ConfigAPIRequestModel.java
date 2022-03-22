package com.cs.di.config.model.configapi;

import com.cs.di.workflow.constants.ConfigNodeType;
import com.cs.di.workflow.constants.EntityAction;

public class ConfigAPIRequestModel implements IConfigAPIRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected ConfigNodeType  nodeType;
  protected EntityAction    action;
  protected String          data;
  
  public ConfigAPIRequestModel() {
    super();
  }
  
  public ConfigAPIRequestModel(ConfigNodeType nodeType, EntityAction action, String data)
  {
    super();
    this.nodeType = nodeType;
    this.action = action;
    this.data = data;
  }

  @Override
  public ConfigNodeType getNodeType() {
    return nodeType;
  }

  @Override
  public void setConfigNodeType(ConfigNodeType nodeType) {
    this.nodeType = nodeType;
  }

  @Override
  public EntityAction getAction()
  {
    return action;
  }
  
  @Override
  public void setAction(EntityAction action)
  {
    this.action = action;
  }
  
  @Override
  public String getData()
  {
    return data;
  }
  
  @Override
  public void setData(String data)
  {
    this.data = data;
  }
}
