package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdAndNameModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdAndNameModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GetConflictSourcesInformationModel implements IGetConflictSourcesInformationModel {
  
  private static final long                serialVersionUID = 1L;
  protected Map<String, IIdLabelCodeModel> klasses;
  protected Map<String, IIdLabelCodeModel> taxonomies;
  protected Map<String, IIdLabelCodeModel> relationships;
  protected Map<String, IIdAndNameModel>   instances;
  protected Map<String, IIdLabelCodeModel> variantContext;
  protected Map<String, IIdLabelCodeModel> languages;
  
  @Override
  public Map<String, IIdLabelCodeModel> getKlasses()
  {
    if (klasses == null) {
      klasses = new HashMap<>();
    }
    return klasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setKlasses(Map<String, IIdLabelCodeModel> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getTaxonomies()
  {
    if (taxonomies == null) {
      taxonomies = new HashMap<>();
    }
    return taxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setTaxonomies(Map<String, IIdLabelCodeModel> taxonomies)
  {
    this.taxonomies = taxonomies;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getRelationships()
  {
    if (relationships == null) {
      relationships = new HashMap<>();
    }
    return relationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setRelationships(Map<String, IIdLabelCodeModel> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getVariantContexts()
  {
    if (variantContext == null) {
      variantContext = new HashMap<>();
    }
    return variantContext;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setVariantContexts(Map<String, IIdLabelCodeModel> variantContext)
  {
    this.variantContext = variantContext;
  }
  
  @Override
  public Map<String, IIdAndNameModel> getContents()
  {
    if (instances == null) {
      instances = new HashMap<>();
    }
    return instances;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndNameModel.class)
  public void setContents(Map<String, IIdAndNameModel> instances)
  {
    this.instances = instances;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getLanguages()
  {
    if (languages == null) {
      languages = new HashMap<>();
    }
    return languages;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setLanguages(Map<String, IIdLabelCodeModel> languages)
  {
    this.languages = languages;
  }
}
