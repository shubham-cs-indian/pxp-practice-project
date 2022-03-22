package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ReferencedRelationshipPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class GetConfigDetailsForCreateBulkCloneModel
    implements IGetConfigDetailsForCreateBulkCloneModel {
  
  private static final long                                     serialVersionUID = 1L;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  protected List<String>                                        sideIds;
  
  @Override
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  @Override
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
  
  @Override
  public List<String> getSideIds()
  {
    return sideIds;
  }
  
  @Override
  public void setSideIds(List<String> sideIds)
  {
    this.sideIds = sideIds;
  }
}
