package com.cs.core.config.interactor.model.datarule;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.entity.datarule.DataRuleTagValues;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTagRule;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTags;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DataRuleTagRule implements IDataRuleTagRule {
  
  private static final long serialVersionUID = 1L;
  
  protected String                   id;
  protected String                   type;
  protected String                   color;
  protected String                   description;
  protected List<IDataRuleTagValues> tagValues;
  protected Long                     versionId;
  protected Long                     versionTimestamp;
  protected String                   lastModifiedBy;
  protected String                   code;
  
  public DataRuleTagRule() {
    
  } 
  
  public DataRuleTagRule(IGovernanceRuleTagRule governanceRule) {
    this.id = governanceRule.getId();
    this.type = governanceRule.getType();
    this.color = governanceRule.getColor();
    this.description = governanceRule.getDescription();
    this.tagValues = governanceRule.getTagValues();         
    this.code = governanceRule.getCode();              
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
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
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
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public List<IDataRuleTagValues> getTagValues()
  {
    if (tagValues == null) {
      tagValues = new ArrayList<>();
    }
    return tagValues;
  }
  
  @JsonDeserialize(contentAs = DataRuleTagValues.class)
  @Override
  public void setTagValues(List<IDataRuleTagValues> tagValues)
  {
    this.tagValues = tagValues;
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
}
