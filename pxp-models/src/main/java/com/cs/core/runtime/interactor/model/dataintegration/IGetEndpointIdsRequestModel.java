package com.cs.core.runtime.interactor.model.dataintegration;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetEndpointIdsRequestModel extends IModel {
  
  public static String       PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static String       USER_ID             = "userId";
  public static String       TRIGGERING_TYPE     = "triggeringType";
  public static final String KLASS_IDS           = "klassIds";
  public static final String TAXONOMY_IDS        = "taxonomyIds";
  public static final String ATTRIBUTE_IDS       = "attributeIds";
  public static final String TAG_IDS             = "tagIds";
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public String getTriggeringType();
  
  public void setTriggeringType(String triggeringType);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getAttributeIds();
  
  public void setAttributeIds(List<String> attributeIds);
  
  public List<String> getTagIds();
  
  public void setTagIds(List<String> tagIds);
}
