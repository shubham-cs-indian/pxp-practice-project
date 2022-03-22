package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetValueFromSourcesRequestModel extends IModel {
  
  public static final String BUCKET_ID        = "bucketId";
  public static final String LANGUAGE_CODE    = "languageCode";
  public static final String PROPERTY_INFO    = "propertyInfo";
  public static final String GOLDEN_RECORD_ID = "goldenRecordId";
 
  
  public String getBucketId();
  public void setBucketId(String bucketId);
  
  public String getLanguageCode();
  public void setLanguageCode(String languageCode);
  
  public IPropertyInfoModel getPropertyInfo();
  public void setPropertyInfo(IPropertyInfoModel propertyInfo);
  
  public String getGoldenRecordId();
  public void setGoldenRecordId(String goldenRecordId);
  
}
