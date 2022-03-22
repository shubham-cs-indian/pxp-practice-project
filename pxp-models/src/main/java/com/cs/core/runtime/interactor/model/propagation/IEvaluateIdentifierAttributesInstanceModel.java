package com.cs.core.runtime.interactor.model.propagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IEvaluateIdentifierAttributesInstanceModel extends IModel {
  
  public static final String KLASS_INSTANCE_ID                = "klassInstanceId";
  public static final String BASE_TYPE                        = "baseType";
  public static final String IS_KLASS_INSTANCE_TYPE_CHANGED   = "isKlassInstanceTypeChanged";
  public static final String IS_IDENTIFIER_ATTRIBUTE_CHANGED  = "isIdentifierAttributeChanged";
  public static final String CHANGED_ATTRIBUTE_IDS            = "changedAttributeIds";
  public static final String TYPE_ID_IDENTIFIER_ATTRIBUTE_IDS = "typeIdIdentifierAttributeIds";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public Boolean getIsKlassInstanceTypeChanged();
  
  public void setIsKlassInstanceTypeChanged(Boolean isKlassInstanceTypeChanged);
  
  public Boolean getIsIdentifierAttributeChanged();
  
  public void setIsIdentifierAttributeChanged(Boolean isIdentifierAttributeChanged);
  
  public List<String> getChangedAttributeIds();
  
  public void setChangedAttributeIds(List<String> changedLanguageIndependentAttributeIds);
  
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
}
