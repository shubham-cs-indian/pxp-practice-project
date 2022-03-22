package com.cs.core.config.interactor.model.taxonomy;

public class GetTaxonomyRequestModel implements IGetTaxonomyRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          taxonomyType;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getTaxonomyType()
  {
    return taxonomyType;
  }
  
  @Override
  public void setTaxonomyType(String taxonomyType)
  {
    this.taxonomyType = taxonomyType;
  }
}
