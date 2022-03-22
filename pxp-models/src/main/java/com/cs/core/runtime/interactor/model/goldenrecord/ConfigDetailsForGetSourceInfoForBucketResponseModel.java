package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.config.interactor.model.language.GetLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigDetailsForGetSourceInfoForBucketResponseModel
    implements IConfigDetailsForGetSourceInfoForBucketResponseModel {
  
  private static final long                     serialVersionUID = 1L;
  protected Map<String, IGetLanguagesInfoModel> referencedLanguages;
  protected IIdLabelCodeModel                   goldenRecordRule;
  protected Map<String, IIdLabelCodeModel>      referencedOrganizations;
  
  @Override
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages()
  {
    return referencedLanguages;
  }
  
  @JsonDeserialize(contentAs = GetLanguagesInfoModel.class)
  @Override
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages)
  {
    this.referencedLanguages = referencedLanguages;
  }
  
  @Override
  public IIdLabelCodeModel getGoldenRecordRule()
  {
    return goldenRecordRule;
  }
  
  @Override
  @JsonDeserialize(as = IdLabelCodeModel.class)
  public void setGoldenRecordRule(IIdLabelCodeModel goldenRecordRule)
  {
    this.goldenRecordRule = goldenRecordRule;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedOrganizations()
  {
    return referencedOrganizations;
  }
  
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  @Override
  public void setReferencedOrganizations(Map<String, IIdLabelCodeModel> referencedOrganizations)
  {
    this.referencedOrganizations = referencedOrganizations;
  }
}
