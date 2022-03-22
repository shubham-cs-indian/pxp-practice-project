package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetRelationshipDataFromSourcesRequestModel extends IModel {
  
  public static final String BUCKET_ID              = "bucketId";
  public static final String LANGUAGE_CODE          = "languageCode";
  public static final String RELATIONSHIP_ID        = "relationshipId";
  public static final String IS_NATURE_RELATIONSHIP = "isNatureRelationship";
  public static final String SIDE_ID                = "sideId";
  public static final String FROM                   = "from";
  public static final String SIZE                   = "size";
  public static final String GOLDEN_RECORD_ID       = "goldenRecordId";
  
  public String getBucketId();
  
  public void setBucketId(String bucketId);
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languageCode);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public Boolean getIsNatureRelationship();
  
  public void setIsNatureRelationship(Boolean isNatureRelationship);
  
  public String getSideId();
  
  public void setSideId(String bucketId);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public String getGoldenRecordId();
  
  public void setGoldenRecordId(String goldenRecordId);
}
