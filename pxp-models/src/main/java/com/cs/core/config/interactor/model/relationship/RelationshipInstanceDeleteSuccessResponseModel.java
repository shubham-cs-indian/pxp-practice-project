package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.model.klass.ContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipInstanceDeleteSuccessResponseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class RelationshipInstanceDeleteSuccessResponseModel
    implements IRelationshipInstanceDeleteSuccessResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<String>                   deletedRelationshipIds;
  protected List<String>                   deletedNatureRelationshipIds;
  protected List<IContentTypeIdsInfoModel> modifiedInstances;
  
  @Override
  public List<String> getDeletedRelationshipIds()
  {
    if (deletedRelationshipIds == null) {
      deletedRelationshipIds = new ArrayList<>();
    }
    return deletedRelationshipIds;
  }
  
  @Override
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds)
  {
    this.deletedRelationshipIds = deletedRelationshipIds;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    if (deletedNatureRelationshipIds == null) {
      deletedNatureRelationshipIds = new ArrayList<>();
    }
    return deletedNatureRelationshipIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
  @Override
  public List<IContentTypeIdsInfoModel> getModifiedInstances()
  {
    if (modifiedInstances == null) {
      modifiedInstances = new ArrayList<>();
    }
    return modifiedInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentTypeIdsInfoModel.class)
  public void setModifiedInstances(List<IContentTypeIdsInfoModel> modifiedInstances)
  {
    this.modifiedInstances = modifiedInstances;
  }
}
