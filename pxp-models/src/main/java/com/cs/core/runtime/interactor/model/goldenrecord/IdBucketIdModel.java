package com.cs.core.runtime.interactor.model.goldenrecord;

public class IdBucketIdModel implements IIdBucketIdModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          bucketId;
  protected String          goldenRecordId;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
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
