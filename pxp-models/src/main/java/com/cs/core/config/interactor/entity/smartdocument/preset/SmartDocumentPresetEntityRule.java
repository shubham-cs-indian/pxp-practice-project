package com.cs.core.config.interactor.entity.smartdocument.preset;

import java.util.ArrayList;
import java.util.List;

public class SmartDocumentPresetEntityRule implements ISmartDocumentPresetEntityRule {
  
  private static final long serialVersionUID            = 1L;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected String          code;
  protected String          id;
  protected String          type;
  protected String          to;
  protected String          from;
  protected List<String>    values                      = new ArrayList<>();
  protected Boolean         shouldCompareWithSystemDate = false;
  
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
  public String getTo()
  {
    return to;
  }
  
  @Override
  public void setTo(String to)
  {
    this.to = to;
  }
  
  @Override
  public String getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(String from)
  {
    this.from = from;
  }
  
  @Override
  public List<String> getValues()
  {
    return values;
  }
  
  @Override
  public void setValues(List<String> values)
  {
    this.values = values;
  }
  
  @Override
  public Boolean getShouldCompareWithSystemDate()
  {
    return shouldCompareWithSystemDate;
  }
  
  @Override
  public void getShouldCompareWithSystemDate(Boolean shouldCompareWithSystemDate)
  {
    this.shouldCompareWithSystemDate = shouldCompareWithSystemDate;
  }
}
