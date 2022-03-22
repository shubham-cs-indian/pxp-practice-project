package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public interface IMasterTaxonomy extends ITagTaxonomy {
  
  // public static final String IS_ATTRIBUTION_TAXONOMY =
  // "isAttributionTaxonomy";
  
  /*  public void setIsAttributionTaxonomy(Boolean isAttributionTaxonomy);
  public Boolean getIsAttributionTaxonomy();*/
  
}
