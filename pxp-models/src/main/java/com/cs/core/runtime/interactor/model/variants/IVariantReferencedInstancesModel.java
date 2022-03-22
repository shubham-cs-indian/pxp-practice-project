package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IVariantReferencedInstancesModel extends IModel {
  
  public static final String ID                        = "id";
  public static final String BASE_TYPE                 = "baseType";
  public static final String NAME                      = "name";
  public static final String DEFAULT_ASSET_INSTANCE_ID = "defaultAssetInstanceId";
  public static final String TYPES                     = "types";
  public static final String THUMB_KEY                 = "thumbKey";
  public static final String PROPERTIES                = "properties";
  public static final String TYPE                      = "type";
  public static final String TAXONOMY_IDS              = "taxonomyIds";
  
  public String getId();
  
  public void setId(String id);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getName();
  
  public void setName(String name);
  
  public String getDefaultAssetInstanceId();
  
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getThumbKey();
  
  public void setThumbKey(String thumbKey);
  
  public Map<String, String> getProperties();
  
  public void setProperties(Map<String, String> properties);
  
  public String getType();
  
  public void setType(String type);
}
