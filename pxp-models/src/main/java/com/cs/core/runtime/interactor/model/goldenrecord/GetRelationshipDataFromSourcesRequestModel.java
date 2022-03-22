package com.cs.core.runtime.interactor.model.goldenrecord;

public class GetRelationshipDataFromSourcesRequestModel
    implements IGetRelationshipDataFromSourcesRequestModel {
  
  private static final long serialVersionUID     = 1L;
  protected String          bucketId;
  protected String          languageCode;
  protected String          relationshipId;
  protected String          sideId;
  protected Boolean         isNatureRelationship = false;
  protected Integer         from;
  protected Integer         size;
  protected String          goldenRecordId;
  
  @Override
  public String getBucketId()
  {
    return bucketId;
  }
  
  @Override
  public void setBucketId(String bucketId)
  {
    this.bucketId = bucketId;
  }
  
  @Override
  public String getLanguageCode()
  {
    return languageCode;
  }
  
  @Override
  public void setLanguageCode(String languageCode)
  {
    this.languageCode = languageCode;
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
  public Boolean getIsNatureRelationship()
  {
    return isNatureRelationship;
  }
  
  @Override
  public void setIsNatureRelationship(Boolean isNatureRelationship)
  {
    this.isNatureRelationship = isNatureRelationship;
  }
  
  @Override
  public Integer getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Integer getSize()
  {
    return size;
  }
  
  @Override
  public void setSize(Integer size)
  {
    this.size = size;
  }
  
  @Override
  public String getGoldenRecordId()
  {
    return goldenRecordId;
  }
  
  @Override
  public void setGoldenRecordId(String goldenRecordId)
  {
    this.goldenRecordId = goldenRecordId;
  }
}
