package com.cs.core.runtime.interactor.model.klassinstance;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class TypesTaxonomiesModel implements ITypesTaxonomiesModel {
  
  private static final long    serialVersionUID = 1L;
  protected Collection<String> types;
  protected List<String>       taxonomyIds;
  
  public TypesTaxonomiesModel()
  {
  }
  
  public TypesTaxonomiesModel(Collection<String> types, List<String> taxonomyIds)
  {
    this.types = types;
    this.taxonomyIds = taxonomyIds;
  }
  
  @Override
  public Collection<String> getTypes()
  {
    if (types == null) {
      types = new HashSet<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(Collection<String> types)
  {
    this.types = types;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
  
}
