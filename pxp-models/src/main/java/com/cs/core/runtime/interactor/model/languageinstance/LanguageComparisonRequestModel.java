package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.runtime.interactor.model.instance.GetInstanceRequestStrategyModelForCustomTab;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceForLanguageComparisonRequestModel;

import java.util.List;

public class LanguageComparisonRequestModel extends GetInstanceRequestStrategyModelForCustomTab
    implements ILanguageComparisonRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    languages;
  
  public LanguageComparisonRequestModel()
  {
  }
  
  public LanguageComparisonRequestModel(IGetInstanceForLanguageComparisonRequestModel model)
  {
    super(model);
    languages = model.getLanguagesToBeCompared();
  }
  
  @Override
  public List<String> getLanguages()
  {
    return languages;
  }
  
  @Override
  public void setLanguages(List<String> languages)
  {
    this.languages = languages;
  }
}
