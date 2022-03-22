package com.cs.core.config.interactor.entity.propertycollection;

import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;

public interface ISectionTaxonomy extends ISectionElement {
  
  public static final String TAXONOMY        = "taxonomy";
  public static final String IS_MULTI_SELECT = "isMultiselect";
  
  public ITaxonomy getTaxonomy();
  
  public void setTaxonomy(ITaxonomy taxonomy);
  
  public Boolean getIsMultiselect();
  
  public void setIsMultiselect(Boolean isMultiselect);
}
