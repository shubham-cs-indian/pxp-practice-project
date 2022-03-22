package com.cs.core.runtime.interactor.model.goldenrecord;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetValueFromSourcesRequestModel implements IGetValueFromSourcesRequestModel {
  
  private static final long    serialVersionUID = 1L;
  protected String             bucketId;
  protected String             languageCode;
  protected IPropertyInfoModel propertyInfo;
  protected String             goldenRecordId;
  
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
  public IPropertyInfoModel getPropertyInfo()
  {
    return propertyInfo;
  }
  
  @JsonDeserialize(as = PropertyInfoModel.class)
  @Override
  public void setPropertyInfo(IPropertyInfoModel propertyInfo)
  {
    this.propertyInfo = propertyInfo;
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
