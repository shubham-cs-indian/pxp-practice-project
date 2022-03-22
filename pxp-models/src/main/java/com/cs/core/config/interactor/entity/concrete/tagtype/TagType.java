package com.cs.core.config.interactor.entity.concrete.tagtype;

import com.cs.core.config.interactor.entity.tag.ITagType;
import com.cs.core.config.interactor.entity.tag.ITagValue;

import java.util.ArrayList;
import java.util.List;

public class TagType implements ITagType {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  
  protected Long            versionId;
  
  protected Long            versionTimestamp;
  
  protected String          label;
  
  protected Boolean         isRange          = false;
  
  protected List<ITagValue> tagValues        = new ArrayList<>();
  
  protected String          lastModifiedBy;
  protected String          code;
  
  public TagType()
  {
  }
  
  public TagType(String label)
  {
    this.label = label;
  }
  
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
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
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
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsRange()
  {
    return this.isRange;
  }
  
  @Override
  public void setIsRange(Boolean isRange)
  {
    this.isRange = isRange;
  }
  
  @Override
  public List<ITagValue> getTagValues()
  {
    return this.tagValues;
  }
  
  @Override
  public void setTagValues(List<ITagValue> tagValues)
  {
    this.tagValues = tagValues;
  }
}
