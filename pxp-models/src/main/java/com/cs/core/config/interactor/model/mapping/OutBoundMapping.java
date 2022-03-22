package com.cs.core.config.interactor.model.mapping;

import com.cs.core.config.interactor.entity.mapping.Mapping;

public class OutBoundMapping extends Mapping implements IOutBoundMapping {
  
  private static final long serialVersionUID                = 1L;
  protected Boolean         isAllClassesSelected            = true;
  protected Boolean         isAllTaxonomiesSelected         = true;
  protected Boolean         isAllPropertyCollectionSelected = true;
  protected Boolean         isAllRelationshipsSelected      = true;
  protected Boolean         isAllContextsSelected           = true;
  
  @Override
  public Boolean getIsAllClassesSelected()
  {
    // TODO Auto-generated method stub
    return isAllClassesSelected;
  }
  
  @Override
  public void setIsAllClassesSelected(Boolean isAllClassesSelected)
  {
    
    this.isAllClassesSelected = isAllClassesSelected;
  }
  
  @Override
  public Boolean getIsAllTaxonomiesSelected()
  {
    
    return isAllTaxonomiesSelected;
  }
  
  @Override
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected)
  {
    
    this.isAllTaxonomiesSelected = isAllTaxonomiesSelected;
  }
  
  @Override
  public Boolean getIsAllPropertyCollectionSelected()
  {
    // TODO Auto-generated method stub
    return isAllPropertyCollectionSelected;
  }
  
  @Override
  public void setIsAllPropertyCollectionSelected(Boolean isAllPropertyCollectionSelected)
  {
    this.isAllPropertyCollectionSelected = isAllPropertyCollectionSelected;
  }

  @Override
  public Boolean getIsAllRelationshipsSelected()
  {
    return isAllRelationshipsSelected;
  }

  @Override
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected)
  {
    this.isAllRelationshipsSelected = isAllRelationshipsSelected;
  }

  @Override
  public Boolean getIsAllContextsSelected()
  {
    return isAllContextsSelected;
  }

  @Override
  public void setIsAllContextsSelected(Boolean isAllContextsSelected)
  {
    this.isAllContextsSelected = isAllContextsSelected;
  }
}
