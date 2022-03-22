package com.cs.core.runtime.interactor.model.variants;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IConfigDetailsForGetVariantInstancesInTableViewRequestModel extends IModel {
  
  public static final String CONTEXT_ID          = "contextId";
  public static final String USER_ID             = "userId";
  public static final String PROPERTY_IDS        = "propertyIds";
  public static final String PARENT_KLASS_IDS    = "parentKlassIds";
  public static final String PARENT_TAXONOMY_IDS = "parentTaxonomyIds";
  public static final String BASE_TYPE           = "baseType";
  public static final String MODULE_ID           = "moduleId";
  String                     INSTANCE_ID_VS_OTHER_CLASSIFIERS = "instanceIdVsOtherClassifiers";
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public List<String> getPropertyIds();
  
  public void setPropertyIds(List<String> propertyIds);
  
  public List<String> getParentKlassIds();
  
  public void setParentKlassIds(List<String> parentKlassIds);
  
  public List<String> getParentTaxonomyIds();
  
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
  
  Map<Long, Map<String, List<String>>> getInstanceIdVsOtherClassifiers();
  
  void setInstanceIdVsOtherClassifiers(Map<Long, Map<String,List<String>>> instanceIdVsOtherClassifiers);
}
