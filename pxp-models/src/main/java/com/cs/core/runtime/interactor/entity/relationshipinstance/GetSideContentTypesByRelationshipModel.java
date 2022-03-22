package com.cs.core.runtime.interactor.entity.relationshipinstance;

public class GetSideContentTypesByRelationshipModel implements IGetSideContentTypesByRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  protected Long            contentId;
  protected String          sideId;
  protected String          relationshipId;
  
  @Override
  public Long getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(Long contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
}
