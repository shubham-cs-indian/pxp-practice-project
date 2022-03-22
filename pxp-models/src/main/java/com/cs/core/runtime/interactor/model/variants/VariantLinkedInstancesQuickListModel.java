package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeStrategyModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VariantLinkedInstancesQuickListModel extends GetKlassInstanceTreeStrategyModel
    implements IVariantLinkedInstancesQuickListModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          contextId;
  protected List<String>    entities         = new ArrayList<>();;
  protected String          baseType;
  protected List<String>    linkedInstances;
  protected String          entityId;
  protected Set<String>     allowedEntities  = new HashSet<String>();
  
  @Override
  public Set<String> getAllowedEntities()
  {
    return allowedEntities;
  }
  
  @Override
  public void setAllowedEntities(Set<String> allowedEntities)
  {
    this.allowedEntities = allowedEntities;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getEntities()
  {
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public List<String> getLinkedInstances()
  {
    if (linkedInstances == null) {
      return new ArrayList<String>();
    }
    return linkedInstances;
  }
  
  @Override
  public void setLinkedInstances(List<String> linkedInstances)
  {
    this.linkedInstances = linkedInstances;
  }
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
}
