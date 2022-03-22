package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGoldenRecordRuleKlassInstancesComparisonRequestModel extends IModel {
  
  public static final String GOLDEN_RECORD_ID   = "goldenRecordId";
  public static final String BUCKET_INSTANCE_ID = "bucketInstanceId";
  public static final String LANGUAGE_CODE      = "languageCode";
  
  public String getGoldenRecordId();
  
  public void setGoldenRecordId(String goldenRecordId);
  
  public String getBucketInstanceId();
  
  public void setBucketInstanceId(String bucketInstanceId);
  
  public String getLanguageCode();
  
  public void setLanguageCode(String languageCode);
}
