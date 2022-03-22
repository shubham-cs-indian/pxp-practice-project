package com.cs.core.runtime.interactor.model.propagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IEvaluateIdentifierAttributesStrategyModel extends IModel {
  
  public static final String KLASS_INSTANCE_ID                 = "klassInstanceId";
  public static final String KLASS_ID_IDENTIFIER_ATTRIBUTE_IDS = "klassIdIdentifierAttributeIds";
  public static final String BASE_TYPE                         = "baseType";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public Map<String, List<String>> getKlassIdIdentifierAttributeIds();
  
  public void setKlassIdIdentifierAttributeIds(
      Map<String, List<String>> klassIdIdentifierAttributeIds);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
}
