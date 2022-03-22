package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IContextKlassSavePropertiesToInheritModel extends IModel {
  
  public static final String TAXONOMY_ID       = "taxonomyId";
  public static final String KLASS_ID          = "klassId";
  public static final String MODIFIED_CONTEXTS = "modifiedContexts";
  public static final String ADDED_CONTEXTS    = "addedContexts";
  
  public String getTaxonomyId();
  
  public void setTaxonmyId(String taxonomyId);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
  
  public Map<String, IModifiedContextKlassModel> getModifiedContexts();
  
  public void setModifiedContexts(Map<String, IModifiedContextKlassModel> modifiedContexts);
  
  public Map<String, IContextKlassModel> getAddedContexts();
  
  public void setAddedContexts(Map<String, IContextKlassModel> addedContexts);
}
