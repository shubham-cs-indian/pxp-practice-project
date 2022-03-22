package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.propertycollection.ISectionTaxonomy;

public interface IReferencedSectionTaxonomyModel
    extends IReferencedSectionElementModel, ISectionTaxonomy {
  
  public static final String IS_MULTI_SELECT = "isMultiselect";
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
}
