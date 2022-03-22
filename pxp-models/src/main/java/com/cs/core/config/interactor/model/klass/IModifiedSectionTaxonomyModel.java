package com.cs.core.config.interactor.model.klass;

public interface IModifiedSectionTaxonomyModel extends IModifiedSectionElementModel {
  
  public static final String IS_MULTI_SELECT = "isMultiselect";
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
}
