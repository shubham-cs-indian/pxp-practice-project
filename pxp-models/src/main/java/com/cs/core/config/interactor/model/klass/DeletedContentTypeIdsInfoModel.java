package com.cs.core.config.interactor.model.klass;

public class DeletedContentTypeIdsInfoModel extends ContentTypeIdsInfoModel
    implements IDeletedContentTypeIdsInfoModel {
  
  private static final long serialVersionUID = 1L;
  protected String          criidToRemove;
  protected String          natureCriidToRemove;
  protected String          changedNatureRelationshipId;
  protected String          changedRelationshipId;
  
  @Override
  public String getCriidToRemove()
  {
    return criidToRemove;
  }
  
  @Override
  public void setCriidToRemove(String criidToRemove)
  {
    this.criidToRemove = criidToRemove;
  }
  
  @Override
  public String getNatureCriidToRemove()
  {
    return natureCriidToRemove;
  }
  
  @Override
  public void setNatureCriidToRemove(String natureCriidToRemove)
  {
    this.natureCriidToRemove = natureCriidToRemove;
  }
  
  @Override
  public String getChangedNatureRelationshipId()
  {
    return changedNatureRelationshipId;
  }
  
  @Override
  public void setChangedNatureRelationshipId(String changedNatureRelationshipId)
  {
    this.changedNatureRelationshipId = changedNatureRelationshipId;
  }
  
  @Override
  public String getChangedRelationshipId()
  {
    return changedRelationshipId;
  }
  
  @Override
  public void setChangedRelationshipId(String changedRelationshipId)
  {
    this.changedRelationshipId = changedRelationshipId;
  }
}
