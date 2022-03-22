package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

public interface IGetConfigDataTagValuesPaginationModel
    extends IGetConfigDataEntityPaginationModel {
  
  public static final String TAG_GROUP_ID          = "tagGroupId";
  public static final String ELEMENT_ID            = "elementId";
  public static final String KLASS_INSTANCE_ID     = "klassInstanceId";
  public static final String BASE_TYPE             = "baseType";
  public static final String KLASS_TYPES           = "klassTypes";
  public static final String SELECTED_TAXONOMY_IDS = "selectedTaxonomyIds";
  public static final String TAXONOMY_ID           = "taxonomyId";
  public static final String CONTEXT_ID            = "contextId";
  
  public String getTagGroupId();
  
  public void setTagGroupId(String tagGroupId);
  
  public String getElementId();
  
  public void setElementId(String elementId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getKlassTypes();
  
  public void setKlassTypes(List<String> klassTypes);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getTaxonomyId();
  
  public void setTaxonomyId(String taxonomyId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
}
