package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.dynamichierarchy.GoldenRecordSourceInfoModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IGoldenRecordSourceInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetSourceInfoForBucketResponseModel implements IGetSourceInfoForBucketResponseModel {
  
  private static final long                                      serialVersionUID = 1L;
  protected List<IGoldenRecordSourceInfoModel>                   sourceInstances;
  protected String                                               goldenRecordId;
  protected IConfigDetailsForGetSourceInfoForBucketResponseModel configDetails;
  protected String                                               ruleId;
  protected List<String>                                         goldenRecordLanguages;
  protected String                                               creationLanguage;
  
  @Override
  public List<IGoldenRecordSourceInfoModel> getSourceInstances()
  {
    return sourceInstances;
  }
  
  @JsonDeserialize(contentAs = GoldenRecordSourceInfoModel.class)
  @Override
  public void setSourceInstances(List<IGoldenRecordSourceInfoModel> sourceInstances)
  {
    this.sourceInstances = sourceInstances;
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
  
  @Override
  public IConfigDetailsForGetSourceInfoForBucketResponseModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  public void setConfigDetails(IConfigDetailsForGetSourceInfoForBucketResponseModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
  
  @Override
  public List<String> getGoldenRecordLanguages()
  {
    return goldenRecordLanguages;
  }
  
  @Override
  public void setGoldenRecordLanguages(List<String> goldenRecordLanguages)
  {
    this.goldenRecordLanguages = goldenRecordLanguages;
  }
  
  @Override
  public String getCreationLanguage()
  {
    return creationLanguage;
  }
  
  @Override
  public void setCreationLanguage(String creationLanguage)
  {
    this.creationLanguage = creationLanguage;
  }
}
