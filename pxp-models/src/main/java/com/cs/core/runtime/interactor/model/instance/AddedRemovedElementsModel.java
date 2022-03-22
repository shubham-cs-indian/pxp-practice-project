package com.cs.core.runtime.interactor.model.instance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.datarule.IAddedRemovedElementsModel;

public class AddedRemovedElementsModel implements IAddedRemovedElementsModel {
  
  private static final long serialVersionUID = 1L;
  protected List<Long> addedElements;
  protected List<Long> removedElements;
  
  @Override
  public List<Long> getAddedElements()
  {
    if (addedElements == null) {
      addedElements = new ArrayList<>();
    }
    return addedElements;
  }
  
  @Override
  public void setAddedElements(List<Long> addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public List<Long> getRemovedElements()
  {
    if (removedElements == null) {
      removedElements = new ArrayList<>();
    }
    return removedElements;
  }
  

  @Override
  public void setRemovedElements(List<Long> removedElements)
  {
    this.removedElements = removedElements;
  }
}
