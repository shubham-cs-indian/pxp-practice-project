package com.cs.core.config.interactor.entity.governancerule;

import java.util.ArrayList;
import java.util.List;

public class GovernanceRuleEntityRule implements IGovernanceRuleEntityRule {
  
  private static final long serialVersionUID            = 1L;
  
  protected String          type;
  protected String          id;
  protected String          to;
  protected String          from;
  protected List<String>    values;
  protected String          attributeLinkId;
  protected Long            versionId;
  protected Long            versionTimestamp;
  protected String          lastModifiedBy;
  protected Boolean         shouldCompareWithSystemDate = false;
  protected String          code;
  
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
  public List<String> getValues()
  {
    if (values == null) {
      values = new ArrayList<>();
    }
    return values;
  }
  
  @Override
  public void setValues(List<String> values)
  {
    this.values = values;
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
  public String getAttributeLinkId()
  {
    return attributeLinkId;
  }
  
  @Override
  public void setAttributeLinkId(String attributeLinkId)
  {
    this.attributeLinkId = attributeLinkId;
  }
  
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
