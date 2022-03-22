package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IRelationshipInstanceElementsWithContextInfoModel extends IModel {
  
  public static String LINKED_ELEMENTS = "linkedElements";
  
  public List<IIdAndContextModel> getLinkedElements();
  
  public void setLinkedElements(List<IIdAndContextModel> linkedElements);
}
