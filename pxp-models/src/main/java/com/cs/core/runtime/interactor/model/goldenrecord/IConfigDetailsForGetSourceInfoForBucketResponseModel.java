package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigDetailsForGetSourceInfoForBucketResponseModel extends IModel {
  
  public static final String REFERENCED_LANGUAGES     = "referencedLanguages";
  public static final String REFERENCED_ORGANIZATIONS = "referencedOrganizations";
  public static final String GOLDEN_RECORD_RULE       = "goldenRecordRule";
  
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages);
  
  public IIdLabelCodeModel getGoldenRecordRule();
  
  public void setGoldenRecordRule(IIdLabelCodeModel goldenRecordRule);
  
  public Map<String, IIdLabelCodeModel> getReferencedOrganizations();
  
  public void setReferencedOrganizations(Map<String, IIdLabelCodeModel> referencedOrganizations);
}
