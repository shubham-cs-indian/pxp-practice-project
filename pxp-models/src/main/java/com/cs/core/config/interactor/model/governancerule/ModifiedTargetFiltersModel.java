package com.cs.core.config.interactor.model.governancerule;

import java.util.List;

public class ModifiedTargetFiltersModel implements IModifiedTargetFiltersModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    addedKlassIds;
  protected List<String>    deletedKlassIds;
  protected List<String>    addedTaxonomyIds;
  protected List<String>    deletedTaxonomyIds;
  
  @Override
  public List<String> getAddedKlassIds()
  {
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }
  
  @Override
  public List<String> getDeletedKlassIds()
  {
    return deletedKlassIds;
  }
  
  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    this.deletedKlassIds = deletedKlassIds;
  }
  
  @Override
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
}
