package com.cs.core.config.interactor.model.klass;

import java.util.List;

public interface IDefaultValueChangeModel extends IDefaultValueCouplingTypeModel {
  
  public static final String ENTITY_ID             = "entityId";
  public static final String KLASS_AND_CHILDRENIDS = "klassAndChildrenIds";
  public static final String TYPE                  = "type";
  public static final String SOURCE_TYPE           = "sourceType";
  public static final String PROPERTY_IID          = "propertyIID";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public List<String> getKlassAndChildrenIds();
  
  public void setKlassAndChildrenIds(List<String> klassAndChildrenIds);
  
  public String getType();
  
  public void setType(String type);
  
  public String getSourceType();
  
  public void setSourceType(String sourceType);
  
  public long getPropertyIID();
  
  public void setPropertyIID(long propertyIID);
}
