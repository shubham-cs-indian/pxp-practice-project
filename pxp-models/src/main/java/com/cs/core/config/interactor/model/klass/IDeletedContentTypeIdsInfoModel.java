package com.cs.core.config.interactor.model.klass;

public interface IDeletedContentTypeIdsInfoModel extends IContentTypeIdsInfoModel {
  
  String CRIID_TO_REMOVE                = "criidToRemove";
  String NATURE_CRIID_TO_REMOVE         = "natureCriidToRemove";
  String CHANGED_RELATIONSHIP_ID        = "changedRelationshipId";
  String CHANGED_NATURE_RELATIONSHIP_ID = "changedNatureRelationshipId";
  
  public String getCriidToRemove();
  
  public void setCriidToRemove(String criidToRemove);
  
  public String getNatureCriidToRemove();
  
  public void setNatureCriidToRemove(String natureCriidToRemove);
  
  public String getChangedRelationshipId();
  
  public void setChangedRelationshipId(String changedRelationshipId);
  
  public String getChangedNatureRelationshipId();
  
  public void setChangedNatureRelationshipId(String changedNatureRelationshipId);
}
