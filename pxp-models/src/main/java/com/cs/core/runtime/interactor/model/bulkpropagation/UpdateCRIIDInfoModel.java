package com.cs.core.runtime.interactor.model.bulkpropagation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UpdateCRIIDInfoModel implements IUpdateCRIIDInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String          contentId;
  protected String          baseType;
  protected List<String>    relationshipCriidsToAdd;
  protected List<String>    relationshipCriidsToRemove;
  protected List<String>    natureRelationshipCriidsToAdd;
  protected List<String>    natureRelationshipCriidsToRemove;
  protected String          defaultAssetInstanceId;
  protected List<String>    removedAssetInstanceIds;
  protected Set<String>     changedRelationshipIds;
  protected Set<String>     changedNatureRelationshipIds;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
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
  
  public List<String> getRelationshipCriidsToAdd()
  {
    if (relationshipCriidsToAdd == null) {
      relationshipCriidsToAdd = new ArrayList<>();
    }
    return relationshipCriidsToAdd;
  }
  
  @Override
  public void setRelationshipCriidsToAdd(List<String> relationshipCriidsToAdd)
  {
    this.relationshipCriidsToAdd = relationshipCriidsToAdd;
  }
  
  @Override
  public List<String> getRelationshipCriidsToRemove()
  {
    if (relationshipCriidsToRemove == null) {
      relationshipCriidsToRemove = new ArrayList<>();
    }
    return relationshipCriidsToRemove;
  }
  
  @Override
  public void setRelationshipCriidsToRemove(List<String> relationshipCriidsToRemove)
  {
    this.relationshipCriidsToRemove = relationshipCriidsToRemove;
  }
  
  @Override
  public List<String> getNatureRelationshipCriidsToAdd()
  {
    if (natureRelationshipCriidsToAdd == null) {
      natureRelationshipCriidsToAdd = new ArrayList<>();
    }
    return natureRelationshipCriidsToAdd;
  }
  
  @Override
  public void setNatureRelationshipCriidsToAdd(List<String> natureRelationshipCriidsToAdd)
  {
    this.natureRelationshipCriidsToAdd = natureRelationshipCriidsToAdd;
  }
  
  @Override
  public List<String> getNatureRelationshipCriidsToRemove()
  {
    if (natureRelationshipCriidsToRemove == null) {
      natureRelationshipCriidsToRemove = new ArrayList<>();
    }
    return natureRelationshipCriidsToRemove;
  }
  
  @Override
  public void setNatureRelationshipCriidsToRemove(List<String> natureRelationshipCriidsToRemove)
  {
    this.natureRelationshipCriidsToRemove = natureRelationshipCriidsToRemove;
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return defaultAssetInstanceId;
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    this.defaultAssetInstanceId = defaultAssetInstanceId;
  }
  
  @Override
  public List<String> getRemovedAssetInstanceIds()
  {
    if (removedAssetInstanceIds == null) {
      removedAssetInstanceIds = new ArrayList<>();
    }
    return removedAssetInstanceIds;
  }
  
  @Override
  public void setRemovedAssetInstanceIds(List<String> removedAssetInstanceIds)
  {
    this.removedAssetInstanceIds = removedAssetInstanceIds;
  }
  
  @Override
  public Set<String> getChangedRelationshipIds()
  {
    if (changedRelationshipIds == null) {
      changedRelationshipIds = new HashSet<>();
    }
    return changedRelationshipIds;
  }
  
  @Override
  public void setChangedRelationshipIds(Set<String> changedRelationshipIds)
  {
    this.changedRelationshipIds = changedRelationshipIds;
  }
  
  @Override
  public Set<String> getChangedNatureRelationshipIds()
  {
    if (changedNatureRelationshipIds == null) {
      changedNatureRelationshipIds = new HashSet<>();
    }
    return changedNatureRelationshipIds;
  }
  
  @Override
  public void setChangedNatureRelationshipIds(Set<String> changedNatureRelationshipIds)
  {
    this.changedNatureRelationshipIds = changedNatureRelationshipIds;
  }
}
