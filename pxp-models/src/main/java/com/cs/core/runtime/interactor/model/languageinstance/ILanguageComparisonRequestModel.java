package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestStrategyModelForCustomTab;

import java.util.List;

public interface ILanguageComparisonRequestModel
    extends IGetInstanceRequestStrategyModelForCustomTab {
  
  public static final String LANGUAGES = "languages";
  
  public List<String> getLanguages();
  
  public void setLanguages(List<String> languages);
}
