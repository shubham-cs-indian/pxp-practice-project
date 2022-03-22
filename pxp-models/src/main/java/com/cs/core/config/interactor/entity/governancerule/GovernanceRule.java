package com.cs.core.config.interactor.entity.governancerule;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GovernanceRule implements IGovernanceRule {
  
  private static final long                         serialVersionUID = 1L;
  
  protected String                                  id;
  protected String                                  label;
  protected Long                                    versionId;
  protected Long                                    versionTimestamp;
  protected String                                  lastModifiedBy;
  protected List<IGovernanceRuleIntermediateEntity> attributes;
  protected List<IGovernanceRuleIntermediateEntity> relationships;
  protected List<IGovernanceRuleIntermediateEntity> roles;
  protected List<IGovernanceRuleTags>               tags;
  protected Boolean                                 isStandard       = false;
  protected String                                  type;
  protected List<String>                            klassIds;
  protected List<String>                            taxonomyIds;
  protected String                                  code;
  
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
  public List<IGovernanceRuleIntermediateEntity> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    return roles;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setRoles(List<IGovernanceRuleIntermediateEntity> roles)
  {
    this.roles = roles;
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
  public List<IGovernanceRuleIntermediateEntity> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<>();
    }
    return relationships;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setRelationships(List<IGovernanceRuleIntermediateEntity> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<IGovernanceRuleIntermediateEntity>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setAttributes(List<IGovernanceRuleIntermediateEntity> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<IGovernanceRuleTags> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleTags.class)
  @Override
  public void setTags(List<IGovernanceRuleTags> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
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
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return taxonomyIds;
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.taxonomyIds = taxonomyIds;
  }
}
