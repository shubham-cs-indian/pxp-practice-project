package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IRelationshipInstanceElementsModel extends IModel {
  
  public static String LINKED_ELEMENTS = "linkedElements";
  
  public List<String> getLinkedElements();
  
  public void setLinkedElements(List<String> linkedElements);
}
