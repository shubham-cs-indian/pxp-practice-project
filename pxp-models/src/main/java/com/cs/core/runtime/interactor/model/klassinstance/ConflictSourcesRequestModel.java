package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

public class ConflictSourcesRequestModel implements IConflictSourcesRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    klasses;
  protected List<String>    taxonomies;
  protected List<String>    relationships;
  protected List<String>    instances;
  protected List<String>    variantContext;
  protected List<String>    languages;
  
  @Override
  public List<String> getKlasses()
  {
    if (klasses == null) {
      klasses = new ArrayList<>();
    }
    return klasses;
  }
  
  @Override
  public void setKlasses(List<String> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public List<String> getTaxonomies()
  {
    if (taxonomies == null) {
      taxonomies = new ArrayList<>();
    }
    return taxonomies;
  }
  
  @Override
  public void setTaxonomies(List<String> taxonomies)
  {
    this.taxonomies = taxonomies;
  }
  
  @Override
  public List<String> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<>();
    }
    return relationships;
  }
  
  @Override
  public void setRelationships(List<String> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public List<String> getVariantContexts()
  {
    if (variantContext == null) {
      variantContext = new ArrayList<>();
    }
    return variantContext;
  }
  
  @Override
  public void setVariantContexts(List<String> variantContext)
  {
    this.variantContext = variantContext;
  }
  
  @Override
  public List<String> getContents()
  {
    if (instances == null) {
      instances = new ArrayList<>();
    }
    return instances;
  }
  
  @Override
  public void setContents(List<String> instances)
  {
    this.instances = instances;
  }
  
  @Override
  public List<String> getLanguages()
  {
    if (languages == null) {
      languages = new ArrayList<>();
    }
    return languages;
  }
  
  @Override
  public void setLanguages(List<String> languages)
  {
    this.languages = languages;
  }
}
