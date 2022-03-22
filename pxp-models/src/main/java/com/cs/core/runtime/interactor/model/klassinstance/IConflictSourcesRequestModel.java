package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConflictSourcesRequestModel extends IModel {
  
  public static final String KLASSES          = "klasses";
  public static final String TAXONOMIES       = "taxonomies";
  public static final String RELATIONSHIPS    = "relationships";
  public static final String VARIANT_CONTEXTS = "variantContexts";
  public static final String CONTENTS         = "contents";
  public static final String LANGUAGES        = "languages";
  
  public List<String> getKlasses();
  
  public void setKlasses(List<String> klasses);
  
  public List<String> getTaxonomies();
  
  public void setTaxonomies(List<String> taxonomies);
  
  public List<String> getRelationships();
  
  public void setRelationships(List<String> relationships);
  
  public List<String> getVariantContexts();
  
  public void setVariantContexts(List<String> variantContexts);
  
  public List<String> getContents();
  
  public void setContents(List<String> contents);
  
  public List<String> getLanguages();
  
  public void setLanguages(List<String> languages);
}
