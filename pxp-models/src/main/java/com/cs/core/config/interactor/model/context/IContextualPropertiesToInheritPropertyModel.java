package com.cs.core.config.interactor.model.context;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IContextualPropertiesToInheritPropertyModel extends IModel {
  
  public static final String PARENT_CONTENT_ID = "parentContentId";
  public static final String CONTEXT_ID        = "contextId";
  public static final String COUPLING_TYPE     = "couplingType";
  
  public String getParentContentId();
  
  public void setParentContentId(String parentContentId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getCouplingType();
  
  public void setCouplingType(String couplingType);
}
