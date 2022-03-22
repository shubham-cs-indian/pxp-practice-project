package com.cs.core.config.interactor.entity.attribute;

public interface ICoverflowAttribute extends IAttribute {
  
  public Integer getNumberOfItemsAllowed();
  
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed);
}
