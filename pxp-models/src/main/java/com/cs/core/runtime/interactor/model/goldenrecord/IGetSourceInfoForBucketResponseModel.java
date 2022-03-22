package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IGoldenRecordSourceInfoModel;

import java.util.List;

public interface IGetSourceInfoForBucketResponseModel extends IModel {
  
  public static final String SOURCE_INSTANCES        = "sourceInstances";
  public static final String GOLDEN_RECORD_ID        = "goldenRecordId";
  public static final String CONFIG_DETAILS          = "configDetails";
  public static final String RULE_ID                 = "ruleId";
  public static final String GOLDEN_RECORD_LANGUAGES = "goldenRecordLanguages";
  public static final String CREATION_LANGUAGE       = "creationLanguage";
  
  public List<IGoldenRecordSourceInfoModel> getSourceInstances();
  
  public void setSourceInstances(List<IGoldenRecordSourceInfoModel> sourceInstances);
  
  public String getGoldenRecordId();
  
  public void setGoldenRecordId(String goldenRecordId);
  
  public IConfigDetailsForGetSourceInfoForBucketResponseModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGetSourceInfoForBucketResponseModel configDetails);
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public List<String> getGoldenRecordLanguages();
  
  public void setGoldenRecordLanguages(List<String> goldenRecordLanguages);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
}
