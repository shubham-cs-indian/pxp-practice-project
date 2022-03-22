package com.cs.core.runtime.interactor.model.versionrollback;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IInstancesToRestoreModel extends IModel {
  
  public static final String ID           = "id";
  public static final String BASETYPE     = "baseType";
  public static final String KLASS_IDS    = "klassIds";
  public static final String TAXONOMY_IDS = "taxonomyIds";
  
  public String getId();
  
  public void setId(String id);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
}
