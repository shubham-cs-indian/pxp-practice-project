package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddedDeletedTypesModel implements IAddedDeletedTypesModel {
  
  private static final long     serialVersionUID     = 1L;
  protected List<String>        addedKlassIds        = new ArrayList<>();
  protected List<String>        addedTaxonomyIds     = new ArrayList<>();
  protected Map<String, Object> referencedTaxonomies = new HashMap<>();
  
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
  public Map<String, Object> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  public void setReferencedTaxonomies(Map<String, Object> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
}
