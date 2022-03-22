package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetTypeInfoForSourcesRequestModel extends IModel {
  
  public static final String BUCKET_ID        = "bucketId";
  public static final String TYPE             = "type";
  public static final String LANGUAGE_CODE    = "languageCode";
  public static final String GOLDEN_RECORD_ID = "goldenRecordId";
  
  public String getBucketId();
  public void setBucketId(String bucketId);
  
  public String getType();
  public void setType(String type);
  
  public String getLanguageCode();
  public void setLanguageCode(String languageCode);
  
  public String getGoldenRecordId();
  public void setGoldenRecordId(String goldenRecordId);
}
