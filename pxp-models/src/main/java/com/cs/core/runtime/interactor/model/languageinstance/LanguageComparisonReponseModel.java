package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.runtime.interactor.entity.language.ILanguageKlassInstance;
import com.cs.core.runtime.interactor.entity.language.LanguageKlassInstance;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class LanguageComparisonReponseModel extends GetKlassInstanceForCustomTabModel
    implements ILanguageComparisonResponseModel {
  
  private static final long              serialVersionUID = 1L;
  
  protected List<ILanguageKlassInstance> languageInstances;
  
  @Override
  public List<ILanguageKlassInstance> getLanguageInstances()
  {
    return languageInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = LanguageKlassInstance.class)
  public void setLanguageInstances(List<ILanguageKlassInstance> languageInstances)
  {
    this.languageInstances = languageInstances;
  }
}
