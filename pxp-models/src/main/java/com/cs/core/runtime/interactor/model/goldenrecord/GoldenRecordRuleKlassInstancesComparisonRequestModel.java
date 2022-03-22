package com.cs.core.runtime.interactor.model.goldenrecord;

public class GoldenRecordRuleKlassInstancesComparisonRequestModel
    implements IGoldenRecordRuleKlassInstancesComparisonRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected String          goldenRecordId;
  protected String          bucketInstanceId;
  protected String          languageCode;
  
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
  
  @Override
  public String getBucketInstanceId()
  {
    return bucketInstanceId;
  }
  
  @Override
  public void setBucketInstanceId(String bucketInstanceId)
  {
    this.bucketInstanceId = bucketInstanceId;
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
}
