package com.cs.core.config.interactor.model;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IRuntimeCleanupModel extends IModel {
  
  public static final String ID                                  = "id";
  public static final String BASETYPE                            = "baseType";
  public static final String KLASS_IDS                           = "klassIds";
  public static final String TAXONOMY_IDS                        = "taxonomyIds";
  public static final String REMOVED_KLASS_IDS                   = "removedKlassIds";
  public static final String REMOVED_TAXONOMY_IDS                = "removedTaxonomyIds";
  public static final String LANGUAGE_CODES                      = "languageCodes";
  public static final String DELETED_PROPERTY_INSTANCE_IDS       = "deletedPropertyInstanceIds";
  
  public String getId();
  public void setId(String id);
  
  public String getBaseType();
  public void setBaseType(String baseType);

  public List<String> getKlassIds();
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getRemovedKlassIds();
  public void setRemovedKlassIds(List<String> removedKlassIds);
  
  public List<String> getRemovedTaxonomyIds();
  public void setRemovedTaxonomyIds(List<String> removedTaxonomyIds);

  public List<String> getLanguageCodes();
  public void setLanguageCodes(List<String> languageCodes);
  
  public List<String> getDeletedPropertyInstanceIds();
  public void setDeletedPropertyInstanceIds(List<String> deletedPropertyInstanceIds);
  
}