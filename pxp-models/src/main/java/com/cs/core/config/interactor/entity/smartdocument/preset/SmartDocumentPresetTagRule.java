package com.cs.core.config.interactor.entity.smartdocument.preset;

import com.cs.core.config.interactor.entity.datarule.DataRuleTagValues;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SmartDocumentPresetTagRule implements ISmartDocumentPresetTagRule {
  
  private static final long          serialVersionUID = 1L;
  protected Long                     versionId;
  protected Long                     versionTimestamp;
  protected String                   lastModifiedBy;
  protected String                   code;
  protected String                   entityId;
  protected String                   id;
  protected String                   type;
  protected List<IDataRuleTagValues> tagValues        = new ArrayList<>();
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return versionTimestamp;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.versionTimestamp = versionTimestamp;
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
  public List<IDataRuleTagValues> getTagValues()
  {
    return tagValues;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleTagValues.class)
  public void setTagValues(List<IDataRuleTagValues> tagValues)
  {
    this.tagValues = tagValues;
  }
}
