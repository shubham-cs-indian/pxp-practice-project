package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;

public interface IReferencedSectionAttributeModel
    extends IReferencedSectionElementModel, ISectionAttribute {
  
  public static final String NUMBER_OF_ITEMS_ALLOWED = "numberOfItemsAllowed";
  
  public Integer getNumberOfItemsAllowed();
  
  public void setNumberOfItemsAllowed(Integer numberOfItemsAllowed);
}
