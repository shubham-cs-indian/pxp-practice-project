package com.cs.core.runtime.interactor.model.instance;

import java.util.List;

public interface IGetInstanceForLanguageComparisonRequestModel
    extends IGetInstanceRequestStrategyModelForCustomTab {
  
  public static final String LANGUAGES_TO_BE_COMPARED = "languagesToBeCompared";
  
  public List<String> getLanguagesToBeCompared();
  
  public void setLanguagesToBeCompared(List<String> languagesToBeCompared);
}
