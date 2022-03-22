package com.cs.core.runtime.interactor.model.filter;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class KlassInstanceQuickListModel extends GetKlassInstanceTreeStrategyModel
    implements IKlassInstanceQuickListModel {
  
  private static final long    serialVersionUID = 1L;
  protected Collection<String> relevanceTagIds;
  protected List<String>       allowedTypes;
  protected String             typeKlassId;
  protected Set<String>        entities;
  
  @Override
  public Collection<String> getRelevanceTagIds()
  {
    if (relevanceTagIds == null) {
      relevanceTagIds = new ArrayList<>();
    }
    return relevanceTagIds;
  }
  
  @Override
  public void setRelevanceTagIds(Collection<String> relevanceTagIds)
  {
    this.relevanceTagIds = relevanceTagIds;
  }
  
  @Override
  public List<String> getAllowedTypes()
  {
    if (allowedTypes == null) {
      allowedTypes = new ArrayList<>();
    }
    return allowedTypes;
  }
  
  @Override
  public void setAllowedTypes(List<String> klassIds)
  {
    this.allowedTypes = klassIds;
  }
  
  @Override
  public String getTypeKlassId()
  {
    return typeKlassId;
  }
  
  @Override
  public void setTypeKlassId(String id)
  {
    this.typeKlassId = id;
  }
  
  @Override
  public Set<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(Set<String> entities)
  {
    this.entities = entities;
  }
}
