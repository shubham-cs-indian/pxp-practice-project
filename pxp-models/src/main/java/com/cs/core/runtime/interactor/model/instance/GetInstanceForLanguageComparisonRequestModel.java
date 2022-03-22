package com.cs.core.runtime.interactor.model.instance;

import java.util.List;

public class GetInstanceForLanguageComparisonRequestModel
    extends GetInstanceRequestStrategyModelForCustomTab
    implements IGetInstanceForLanguageComparisonRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    languagesToBeCompared;
  
  @Override
  public List<String> getLanguagesToBeCompared()
  {
    return languagesToBeCompared;
  }
  
  @Override
  public void setLanguagesToBeCompared(List<String> languagesToBeCompared)
  {
    this.languagesToBeCompared = languagesToBeCompared;
  }
}
