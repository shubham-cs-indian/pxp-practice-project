package com.cs.core.config.interactor.model.taxonomyhierarchy;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class ConfigTaxonomyHierarchyInformationModel extends ConfigEntityInformationModel
    implements IConfigTaxonomyHierarchyInformationModel {
  
  private static final long serialVersionUID = 1L;
  private String            parentId;
  protected String          taxonomyType;
  private String            baseType;
  
  @Override
  public String getParentId()
  {
    return parentId;
  }
  
  @Override
  public void setParentId(String parentId)
  {
    this.parentId = parentId;
  }
  
  @Override
  public void setTaxonomyType(String taxonomyType)
  {
    this.taxonomyType = taxonomyType;
  }
  
  @Override
  public String getTaxonomyType()
  {
    return taxonomyType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
}