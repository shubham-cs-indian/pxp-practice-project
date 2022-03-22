package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;

import java.util.Set;

public interface IInstanceGetByIdModel extends IModel {
  
  public static String INSTANCE                  = "instance";
  public static String LANGUAGECODE_FOR_VERSIONS = "languageCodeForVersions";
  
  public IKlassInstance getInstance();
  
  public void setInstance(IKlassInstance klassInstance);
  
  public Set<String> getLanguageCodeForVersions();
  
  public void setLanguageCodeForVersions(Set<String> languageCodeForVersions);
}
