package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.runtime.interactor.entity.language.ILanguageKlassInstance;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;

import java.util.List;

public interface ILanguageComparisonResponseModel extends IGetKlassInstanceCustomTabModel {
  
  public static final String LANGUAGE_INSTANCES = "languageInstances";
  
  public List<ILanguageKlassInstance> getLanguageInstances();
  
  public void setLanguageInstances(List<ILanguageKlassInstance> languageInstances);
}
