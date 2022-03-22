package com.cs.core.runtime.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.runtime.interactor.entity.contentidentifier.IContextStructureInfo;

public interface IContext extends IEntity {
  
  public static final String FIELD_INFO        = "fieldInfo";
  public static final String FIELD_TYPE        = "fieldType";
  public static final String STRUCTURE_INFO    = "structureInfo";
  public static final String KLASS_INSTANCE_ID = "klassInstanceId";
  
  public String getFieldInfo();
  
  public void setFieldInfo(String fieldInfo);
  
  public String getFieldType();
  
  public void setFieldType(String fieldType);
  
  public IContextStructureInfo getStructureInfo();
  
  public void setStructureInfo(IContextStructureInfo structureInfo);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
}
