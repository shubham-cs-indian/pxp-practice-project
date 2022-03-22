package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IContextualDataPreparationModel extends IModel {
  
  public static final String SOURCE_CONTENT_ID = "sourceContentId";
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  public static final String BASETYPE          = "baseType";
  public static final String CONTEXTUAL_DATA   = "contextualData";
  
  public String getSourceContentId();
  
  public void setSourceContentId(String sourceContentId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Map<String, IPropagableContextualDataModel> getContextualData();
  
  public void setContextualData(Map<String, IPropagableContextualDataModel> contextualData);
}
