package com.cs.core.runtime.interactor.model.datarule;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IAddedRemovedElementsModel extends IModel {
  
  public static final String ADDED_ELEMENTS   = "addedElements";
  public static final String REMOVED_ELEMENTS = "removedElements";
  
  public List<Long> getAddedElements();
  
  public void setAddedElements(List<Long> addedElements);
  
  public List<Long> getRemovedElements();
  
  public void setRemovedElements(List<Long> removedElements);
}
