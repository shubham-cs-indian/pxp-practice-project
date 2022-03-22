package com.cs.core.runtime.interactor.model.context;

import com.cs.core.config.interactor.model.context.IContextualPropertiesToInheritPropertyModel;

public abstract class AbstractContextualPropertiesToInheritPropertyModel
    implements IContextualPropertiesToInheritPropertyModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          parentContentId;
  protected String          contextId;
  protected String          couplingType;
  
  @Override
  public String getParentContentId()
  {
    return parentContentId;
  }
  
  @Override
  public void setParentContentId(String parentContentId)
  {
    this.parentContentId = parentContentId;
  }
  
  @Override
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
  }
  
  @Override
  public String getCouplingType()
  {
    return couplingType;
  }
  
  @Override
  public void setCouplingType(String couplingType)
  {
    this.couplingType = couplingType;
  }
}
