package com.cs.core.runtime.interactor.model.propagation;

import java.util.ArrayList;
import java.util.List;

public class AttributeDetailsForUniquenessEvaluationModel
    implements IAttributeDetailsForUniquenessEvaluationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          attributeId;
  protected String          instanceIdToAdd;
  protected String          instanceIdToRemove;
  protected List<String>    typeIds;
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public String getInstanceIdToAdd()
  {
    return instanceIdToAdd;
  }
  
  @Override
  public void setInstanceIdToAdd(String instanceIdToAdd)
  {
    this.instanceIdToAdd = instanceIdToAdd;
  }
  
  @Override
  public String getInstanceIdToRemove()
  {
    return instanceIdToRemove;
  }
  
  @Override
  public void setInstanceIdToRemove(String instanceIdToRemove)
  {
    this.instanceIdToRemove = instanceIdToRemove;
  }
  
  @Override
  public List<String> getTypeIds()
  {
    if (typeIds == null) {
      typeIds = new ArrayList<>();
    }
    return typeIds;
  }
  
  @Override
  public void setTypeIds(List<String> typeIds)
  {
    this.typeIds = typeIds;
  }
}
