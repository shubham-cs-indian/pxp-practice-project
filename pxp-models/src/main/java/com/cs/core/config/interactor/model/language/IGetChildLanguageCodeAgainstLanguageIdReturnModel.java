package com.cs.core.config.interactor.model.language;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetChildLanguageCodeAgainstLanguageIdReturnModel extends IModel {
  
  public static String CHILD_LANGUAGE_CODE_AGAINST_LANGUAGE_ID = "childLanguageCodeAgainstLanguageId";
  
  public Map<String, List<String>> getChildLanguageCodeAgainstLanguageId();
  
  public void setChildLanguageCodeAgainstLanguageId(Map<String, List<String>> childLanguageCodeAgainstLanguageId);
}
