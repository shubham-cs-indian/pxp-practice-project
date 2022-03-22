package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdAndNameModel;

import java.util.Map;

public interface IGetConflictSourcesInformationModel extends IModel {
  
  public static final String KLASSES          = "klasses";
  public static final String TAXONOMIES       = "taxonomies";
  public static final String RELATIONSHIPS    = "relationships";
  public static final String VARIANT_CONTEXTS = "variantContexts";
  public static final String CONTENTS         = "contents";
  public static final String LANGUAGES        = "languages";
  
  public Map<String, IIdLabelCodeModel> getKlasses();
  
  public void setKlasses(Map<String, IIdLabelCodeModel> klasses);
  
  public Map<String, IIdLabelCodeModel> getTaxonomies();
  
  public void setTaxonomies(Map<String, IIdLabelCodeModel> taxonomies);
  
  public Map<String, IIdLabelCodeModel> getRelationships();
  
  public void setRelationships(Map<String, IIdLabelCodeModel> relationships);
  
  public Map<String, IIdLabelCodeModel> getVariantContexts();
  
  public void setVariantContexts(Map<String, IIdLabelCodeModel> variantContexts);
  
  public Map<String, IIdAndNameModel> getContents();
  
  public void setContents(Map<String, IIdAndNameModel> contents);
  
  public Map<String, IIdLabelCodeModel> getLanguages();
  
  public void setLanguages(Map<String, IIdLabelCodeModel> languages);
}
