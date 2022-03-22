package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IConfigDetailsForGetPropertiesVariantInstancesInTableViewStrategyRequestModel
    extends IModel {
  
  public static String CONTEXT_ID      = "contextId";
  public static String CURRENT_USER_ID = "currentUserId";
  public static String ATTRIBUTE_ID    = "attributeId";
  public static String KLASS_IDS       = "klassIds";
  public static String TEMPLATE_ID     = "templateId";
  public static String TAXONOMY_IDS    = "taxonomyIds";
  public static String NATURE_KLASS_ID = "natureKlassId";
  
  public String getTemplateId();
  
  public void setTemplateId(String templateId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getCurrentUserId();
  
  public void setCurrentUserId(String currentUserId);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getNatureKlassId();
  
  public void setNatureKlassId(String natureKlassId);
}
