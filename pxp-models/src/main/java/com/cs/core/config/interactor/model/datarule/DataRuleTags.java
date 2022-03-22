package com.cs.core.config.interactor.model.datarule;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTags;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DataRuleTags implements IDataRuleTags {
  
  private static final long        serialVersionUID = 1L;
  protected String                 entityId;
  protected String                 id;
  protected List<IDataRuleTagRule> tags;
  protected Long                   versionId;
  protected Long                   versionTimestamp;
  protected String                 lastModifiedBy;
  protected String                 code;
  
  public DataRuleTags() {
    
  }
  
  public DataRuleTags(IGovernanceRuleTags governanceRuleTags) {
    this.entityId = governanceRuleTags.getEntityId();        
    this.id = governanceRuleTags.getId();              
    this.tags = governanceRuleTags.getRules()
        .stream()
        .map(x -> new DataRuleTagRule(x))
        .collect(Collectors.toList());
    this.code = governanceRuleTags.getCode();            
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
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public List<IDataRuleTagRule> getRules()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = DataRuleTagRule.class)
  @Override
  public void setRules(List<IDataRuleTagRule> tags)
  {
    this.tags = tags;
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
