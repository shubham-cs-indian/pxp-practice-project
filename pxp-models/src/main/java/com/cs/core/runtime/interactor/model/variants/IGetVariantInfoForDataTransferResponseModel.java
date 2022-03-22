package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetVariantInfoForDataTransferResponseModel extends IModel {
  
  public static final String CONTENT_ID = "contentId";
  public static final String CONTEXT_ID = "contextId";
  public static final String BASETYPE   = "baseType";
  public static final String KLASS_ID   = "klassId";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public String getContextId();
  
  public void setContextId(String contextId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getKlassId();
  
  public void setKlassId(String klassId);
}
