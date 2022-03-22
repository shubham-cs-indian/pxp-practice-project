package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IIdBucketIdModel extends IModel {
  
  public static final String ID               = "id";
  public static final String BUCKET_ID        = "bucketId";
  public static final String GOLDEN_RECORD_ID = "goldenRecordId";
  
  public String getId();
  
  public void setId(String id);
  
  public String getBucketId();
  
  public void setBucketId(String bucketId);
  
  public String getGoldenRecordId();
  
  public void setGoldenRecordId(String goldenRecordId);
}
