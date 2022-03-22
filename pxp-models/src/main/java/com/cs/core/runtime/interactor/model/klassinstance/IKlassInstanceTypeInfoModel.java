package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassInstanceTypeInfoModel extends IModel {
  
  public static final String KLASS_IDS             = "klassIds";
  public static final String TAXONOMY_IDS          = "taxonomyIds";
  public static final String PARENT_ID             = "parentId";
  public static final String PARENT_KLASS_IDS      = "parentKlassIds";
  public static final String PARENT_TAXONOMY_IDS   = "parentTaxonomyIds";
  public static final String LANGUAGE_CODES        = "languageCodes";
  public static final String PARENT_LANGUAGE_CODES = "parentLanguageCodes";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public List<String> getParentKlassIds();
  
  public void setParentKlassIds(List<String> parentKlassIds);
  
  public List<String> getParentTaxonomyIds();
  
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public List<String> getParentLanguageCodes();
  
  public void setParentLanguageCodes(List<String> parentLanguageCodes);
}
