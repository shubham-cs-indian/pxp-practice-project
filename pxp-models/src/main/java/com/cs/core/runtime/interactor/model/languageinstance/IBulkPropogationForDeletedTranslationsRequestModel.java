package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IBulkPropogationForDeletedTranslationsRequestModel extends IModel {
  
  String BASE_TYPE      = "baseType";
  String CONTENT_ID     = "contentId";
  String LANGUAGE_CODES = "languageCodes";
  String VARIANT_IDS    = "variantIds";
  String CRIIDS         = "criids";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public List<String> getVariantIds();
  
  public void setVariantIds(List<String> unmodifiedVariantIds);
  
  public List<String> getCriids();
  
  public void setCriids(List<String> unmodifiedCriids);
  
  public String getBaseType();
  
  public void setBaseType(String contentBaseType);
}
