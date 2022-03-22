package com.cs.core.runtime.interactor.model.configuration;

import java.util.ArrayList;
import java.util.List;

public class RelationshipInstanceElementsModel implements IRelationshipInstanceElementsModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    linkedElements   = new ArrayList<>();
  
  @Override
  public List<String> getLinkedElements()
  {
    return linkedElements;
  }
  
  @Override
  public void setLinkedElements(List<String> linkedElements)
  {
    this.linkedElements = linkedElements;
  }
}
