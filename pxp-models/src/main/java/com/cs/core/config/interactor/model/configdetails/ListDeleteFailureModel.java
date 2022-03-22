package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IListDeleteFailureModel;

public class ListDeleteFailureModel implements IListDeleteFailureModel {
  
  protected String       id;
  protected List<String> linkedItemsLabel;
  protected String       message;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<String> getLinkedItemLabels()
  {
    return linkedItemsLabel;
  }
  
  @Override
  public void setLinkedItemsLabel(List<String> linkedItemsLabel)
  {
    this.linkedItemsLabel = linkedItemsLabel;
  }
  
  @Override
  public String getMessage()
  {
    return message;
  }
  
  @Override
  public void setMessage(String message)
  {
    this.message = message;
  }
}
