package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;

public interface ITaxonomyConfigEntityInformationModel extends IConfigEntityTreeInformationModel {
  
  public static final String taxonomyType = "TAXONOMY_TYPE";
  
  public String getTaxonomyType();
  public void setTaxonomyType(String taxonomyType);
  
}
