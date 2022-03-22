package com.cs.core.runtime.interactor.entity.searchable;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;

public interface ISearchableAttributeInstance extends IRuntimeEntity {
  
  public static final String VALUE                 = "value";
  public static final String VALUE_AS_NUMBER       = "valueAsNumber";
  public static final String IS_MANDATORY_VIOLATED = "isMandatoryViolated";
  public static final String IS_SHOULD_VIOLATED    = "isShouldViolated";
  public static final String KLASS_INSTANCE_ID     = "klassInstanceId";
  public static final String ATTRIBUTE_ID          = "attributeId";
  public static final String CODE                  = "code";
  public static final String IS_UNIQUE             = "isUnique";
  public static final String VARIANT_INSTANCE_ID   = "variantInstanceId";
  
  public String getValue();
  
  public void setValue(String baseType);
  
  public Double getValueAsNumber();
  
  public void setValueAsNumber(Double valueAsNumber);
  
  public Boolean getIsMandatoryViolated();
  
  public void setIsMandatoryViolated(Boolean isMandatoryViolated);
  
  public Boolean getIsShouldViolated();
  
  public void setIsShouldViolated(Boolean isShouldViolated);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getAttributeId();
  
  public void setAttributeId(String attributeId);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Integer getIsUnique();
  
  public void setIsUnique(Integer isUnique);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
}
