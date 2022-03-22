package com.cs.core.config.interactor.entity.mapping;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

public interface IMapping extends IConfigEntity {
  
  public static final String LABEL        = "label";
  public static final String TYPE         = "type";
  public static final String IS_DEFAULT   = "isDefault";
  public static final String INDEX_NAME   = "indexName";
  public static final String MAPPING_TYPE = "mappingType";
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getIsDefault();
  
  public void setIsDefault(Boolean isDefault);
  
  public String getIndexName();
  
  public void setIndexName(String indexName);
  
  public void setMappingType(String mappingType);
  
  public String getMappingType();
}
