package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IConfigTaxonomyHierarchyInformationModel extends IConfigEntityInformationModel {
  
  public static final String PARENT_ID     = "parentId";
  public static final String TAXONOMY_TYPE = "taxonomyType";
  public static final String BASETYPE      = "baseType";
  
  public void setParentId(String parentId);
  
  public String getParentId();
  
  public void setTaxonomyType(String taxonomyType);
  
  public String getTaxonomyType();
  
  public void setBaseType(String baseType);
  
  public String getBaseType();
}
