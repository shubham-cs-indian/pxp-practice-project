package com.cs.core.config.interactor.entity.goldenrecord;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.List;

public interface IMergeEffectType extends IConfigEntity {
  
  public static final String TYPE         = "type";
  public static final String ENTITY_ID    = "entityId";
  public static final String ENTITY_TYPE  = "entityType";
  public static final String SUPPLIER_IDS = "supplierIds";
  
  public String getType();
  
  public void setType(String type);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getEntityType();
  
  public void setEntityType(String type);
  
  public List<String> getSupplierIds();
  
  public void setSupplierIds(List<String> supplierIds);
}
