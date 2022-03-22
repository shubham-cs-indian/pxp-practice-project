package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Set;

public interface IUpdateCRIIDInfoModel extends IModel {
  
  public static final String CONTENT_ID                           = "contentId";
  public static final String BASETYPE                             = "baseType";
  public static final String RELATIONSHIP_CRIIDS_TO_ADD           = "relationshipCriidsToAdd";
  public static final String RELATIONSHIP_CRIIDS_TO_REMOVE        = "relationshipCriidsToRemove";
  public static final String NATURE_RELATIONSHIP_CRIIDS_TO_ADD    = "natureRelationshipCriidsToAdd";
  public static final String NATURE_RELATIONSHIP_CRIIDS_TO_REMOVE = "natureRelationshipCriidsToRemove";
  public static final String DEFAULT_ASSET_INSTANCE_ID            = "defaultAssetInstanceId";
  public static final String REMOVED_ASSET_INSTANCE_IDS           = "removedAssetInstanceIds";
  public static final String CHANGED_RELATIONSHIP_IDS             = "changedRelationshipIds";
  public static final String CHANGED_NATURE_RELATIONSHIP_IDS      = "changedNatureRelationshipIds";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getRelationshipCriidsToAdd();
  
  public void setRelationshipCriidsToAdd(List<String> criidsToAdd);
  
  public List<String> getRelationshipCriidsToRemove();
  
  public void setRelationshipCriidsToRemove(List<String> criidsToRemove);
  
  public List<String> getNatureRelationshipCriidsToAdd();
  
  public void setNatureRelationshipCriidsToAdd(List<String> criidsToAdd);
  
  public List<String> getNatureRelationshipCriidsToRemove();
  
  public void setNatureRelationshipCriidsToRemove(List<String> criidsToRemove);
  
  public String getDefaultAssetInstanceId();
  
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId);
  
  public List<String> getRemovedAssetInstanceIds();
  
  public void setRemovedAssetInstanceIds(List<String> removedAssetInstanceIds);
  
  public Set<String> getChangedRelationshipIds();
  
  public void setChangedRelationshipIds(Set<String> changeRedlationshipIds);
  
  public Set<String> getChangedNatureRelationshipIds();
  
  public void setChangedNatureRelationshipIds(Set<String> changedNatureRelationshipIds);
}
