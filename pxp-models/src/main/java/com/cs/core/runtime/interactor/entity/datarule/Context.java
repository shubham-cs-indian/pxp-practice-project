package com.cs.core.runtime.interactor.entity.datarule;

import com.cs.core.runtime.interactor.entity.contentidentifier.ContextStructureInfo;
import com.cs.core.runtime.interactor.entity.contentidentifier.IContextStructureInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class Context implements IContext {
  
  private static final long       serialVersionUID = 1L;
  
  protected String                id;
  
  protected String                fieldInfo;
  
  protected String                fieldType;
  
  protected IContextStructureInfo structureInfo;
  
  protected String                klassInstanceId;
  
  protected Long                  versionId;
  
  protected Long                  versionTimestamp;
  
  protected String                lastModifiedBy;
  
  @Override
  public String getLastModifiedBy()
  {
    return lastModifiedBy;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.lastModifiedBy = lastModifiedBy;
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
  public String getFieldInfo()
  {
    return fieldInfo;
  }
  
  @Override
  public void setFieldInfo(String fieldInfo)
  {
    this.fieldInfo = fieldInfo;
  }
  
  @Override
  public String getFieldType()
  {
    return fieldType;
  }
  
  @Override
  public void setFieldType(String fieldType)
  {
    this.fieldType = fieldType;
  }
  
  @Override
  public IContextStructureInfo getStructureInfo()
  {
    return structureInfo;
  }
  
  @JsonDeserialize(as = ContextStructureInfo.class)
  @Override
  public void setStructureInfo(IContextStructureInfo structureInfo)
  {
    this.structureInfo = structureInfo;
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
  public Long getVersionId()
  {
    return this.versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return this.versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
  }
}
