package com.cs.core.runtime.interactor.model.configuration;

public class RelationshipLabelAndContextIdModel implements IRelationshipLabelAndContextIdModel {
  
  private static final long serialVersionUID = 1L;
  protected String          label;
  protected String          contextId;
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
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
}
