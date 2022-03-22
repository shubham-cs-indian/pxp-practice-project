package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.klassinstance.AbstractKlassInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashSet;
import java.util.Set;

public class InstanceGetByIdModel implements IInstanceGetByIdModel {
  
  private static final long serialVersionUID = 1L;
  
  protected IKlassInstance  klassInstance;
  protected Set<String>     languageCodeForVersions;
  
  @Override
  public IKlassInstance getInstance()
  {
    return klassInstance;
  }
  
  @JsonDeserialize(as = AbstractKlassInstanceModel.class)
  @Override
  public void setInstance(IKlassInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public Set<String> getLanguageCodeForVersions()
  {
    if (languageCodeForVersions == null) {
      languageCodeForVersions = new HashSet<>();
    }
    return languageCodeForVersions;
  }
  
  @Override
  public void setLanguageCodeForVersions(Set<String> languageCodeForVersions)
  {
    this.languageCodeForVersions = languageCodeForVersions;
  }
}
