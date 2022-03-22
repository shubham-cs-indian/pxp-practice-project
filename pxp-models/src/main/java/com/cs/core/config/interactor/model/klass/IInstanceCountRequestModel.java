package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IInstanceCountRequestModel extends IModel {
  
  String TYPE_ID_VS_CHILD_KLASS_IDS        = "typeIdVsChildKlassIds";
  String KLASS_TYPE                        = "klassType";
  String TAXONOMY_ID_VS_CHILD_TAXONOMY_IDS = "taxonomyIdVsChildTaxonomyIds";
  
  public Map<String, List<String>> getTypeIdVsChildKlassIds();
  
  public void setTypeIdVsChildKlassIds(Map<String, List<String>> typeIdVsKlassIds);
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
  
  public Map<String, List<String>> getTaxonomyIdVsChildTaxonomyIds();
  
  public void setTaxonomyIdVsChildTaxonomyIds(
      Map<String, List<String>> taxonomyIdVsChildTaxonomyIds);
}
