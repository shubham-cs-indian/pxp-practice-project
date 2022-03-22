package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteTranslationRequestModel extends IModel {
  
  public static final String CONTENT_ID     = "contentId";
  public static final String LANGUAGE_CODES = "languageCodes";
  
  public long getContentId();
  
  public void setContentId(long contentId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
}
