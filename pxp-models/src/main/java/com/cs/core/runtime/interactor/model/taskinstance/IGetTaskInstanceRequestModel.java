package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetTaskInstanceRequestModel extends IModel {
  
  public static final String CONTENT_ID = "contentId";
  public static final String VARIANT_ID = "variantId";
  public static final String ELEMENT_ID = "elementId";
  public static final String GETALL     = "getAll";
  public static final String COUNT      = "count";
  public static final String BASE_TYPE  = "baseType";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getElementId();
  
  public void setElementId(String elementId);
  
  public String getVariantId();
  
  public void setVariantId(String variantId);
  
  public Boolean getGetAll();
  
  public void setGetAll(Boolean getAll);
  
  public Boolean getCount();
  
  public void setCount(Boolean count);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
