package com.cs.core.runtime.interactor.entity.propertyinstance;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
    property = "baseType", visible = true)
public interface IPropertyInstance extends IRuntimeEntity {
  
  public static final String KLASS_INSTANCE_VERSION = "klassInstanceVersion";
  public static final String BASE_TYPE              = "baseType";
  public static final String NOTIFICATION           = "notification";
  public static final String KLASS_INSTANCE_ID      = "klassInstanceId";
  public static final String VARIANT_INSTANCE_ID    = "variantInstanceId";
  
  public Long getKlassInstanceVersion();
  
  public void setKlassInstanceVersion(Long klassInstanceVersion);
  
  public String getBaseType();
  
  public void setBaseType(String basetype);
  
  public Map<String, Object> getNotification();
  
  public void setNotification(Map<String, Object> notification);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getVariantInstanceId();
  
  public void setVariantInstanceId(String variantInstanceId);
}
