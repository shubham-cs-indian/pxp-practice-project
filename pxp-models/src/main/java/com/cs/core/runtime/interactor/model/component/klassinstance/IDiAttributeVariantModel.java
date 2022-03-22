package com.cs.core.runtime.interactor.model.component.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDiAttributeVariantModel extends IModel {
  
  public static String VARIANT_ID = "variantId";
  public static String CONTENT_ID = "contentId";
  public static String PARENT_ID  = "parentId";
  public static String CONTEXT    = "context";
  public static String PROPERTIES = "properties";
  public static String LABEL      = "label";
  
  public String getVariantId();
  
  public void setVariantId(String variantId);
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public IDiAttributeVariantContextModel getContext();
  
  public void setContext(IDiAttributeVariantContextModel context);
  
  public IDiAttributeVariantPropertiesModel getProperties();
  
  public void setProperties(IDiAttributeVariantPropertiesModel properties);
  
  public String getLabel();
  
  public void setLabel(String label);
}
