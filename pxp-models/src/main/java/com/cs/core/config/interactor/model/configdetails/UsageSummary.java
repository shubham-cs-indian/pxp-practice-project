package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UsageSummary implements IUsageSummary {
  
  protected String        id;
  protected String        label;
  protected String        code;
  protected Integer       totalCount;
  protected List<IUsedBy> usedBy;
  
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
  public Integer getTotalCount()
  {
    return totalCount;
  }
  
  @Override
  public void setTotalCount(Integer totalCount)
  {
    this.totalCount = totalCount;
  }
  
  @Override
  public List<IUsedBy> getUsedBy()
  {
    return usedBy;
  }
  
  @Override
  @JsonDeserialize(contentAs = UsedBy.class)
  public void setUsedBy(List<IUsedBy> usedBy)
  {
    this.usedBy = usedBy;
  }
}
