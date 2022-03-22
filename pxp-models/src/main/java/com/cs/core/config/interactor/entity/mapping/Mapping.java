package com.cs.core.config.interactor.entity.mapping;

public class Mapping implements IMapping {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          label;
  protected String          type;
  protected Boolean         isDefault;
  protected String          indexName;
  protected String          code;
  protected String          mappingType;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public String getLabel()
  {
    
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsDefault()
  {
    return isDefault;
  }
  
  @Override
  public void setIsDefault(Boolean isDefault)
  {
    this.isDefault = isDefault;
  }
  
  @Override
  public String getType()
  {
    
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public String getIndexName()
  {
    
    return indexName;
  }
  
  @Override
  public void setIndexName(String indexName)
  {
    this.indexName = indexName;
  }
  
  @Override
  public String getMappingType()
  {
    return mappingType;
  }
  
  @Override
  public void setMappingType(String mappingType)
  {
    this.mappingType = mappingType;
  }
  
}
