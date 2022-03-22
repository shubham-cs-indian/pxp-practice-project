package com.cs.core.runtime.interactor.entity.propertyinstance;

import java.util.HashMap;
import java.util.Map;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;

public class PropertyInstance extends AbstractRuntimeEntity implements IPropertyInstance {
  
  private static final long     serialVersionUID = 1L;
  
  protected String              id;
  protected Long                klassInstanceVersion;
  protected String              baseType;
  protected Map<String, Object> notification;
  protected String              klassInstanceId;
  protected String              variantInstanceId;
  
  @Override
  public Long getKlassInstanceVersion()
  {
    return klassInstanceVersion;
  }
  
  @Override
  public void setKlassInstanceVersion(Long klassInstanceVersion)
  {
    this.klassInstanceVersion = klassInstanceVersion;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Map<String, Object> getNotification()
  {
    if (notification == null) {
      notification = new HashMap<String, Object>();
    }
    return notification;
  }
  
  @Override
  public void setNotification(Map<String, Object> notification)
  {
    this.notification = notification;
  }
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.variantInstanceId = variantInstanceId;
  }
}
