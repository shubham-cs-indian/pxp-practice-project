package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.governancerule.*;
import com.cs.core.runtime.interactor.model.configuration.AbstractAdditionalPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GovernanceRuleModel extends AbstractAdditionalPropertiesModel
    implements IGovernanceRuleModel {
  
  private static final long                      serialVersionUID = 1L;
  protected IGovernanceRule                      entity;
  protected String                               userId;
  protected IConfigDetailsForGovernanceRuleModel configDetails;
  protected String                               kpiId;
  
  public GovernanceRuleModel()
  {
    this.entity = new GovernanceRule();
  }
  
  public GovernanceRuleModel(IGovernanceRule dataRule)
  {
    this.entity = dataRule;
  }
  
  @Override
  public IEntity getEntity()
  {
    return this.entity;
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getRoles()
  {
    return this.entity.getRoles();
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setRoles(List<IGovernanceRuleIntermediateEntity> roles)
  {
    this.entity.setRoles(roles);
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getAttributes()
  {
    return this.entity.getAttributes();
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setAttributes(List<IGovernanceRuleIntermediateEntity> attributes)
  {
    this.entity.setAttributes(attributes);
  }
  
  @Override
  public String getLabel()
  {
    return this.entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    this.entity.setLabel(label);
  }
  
  @Override
  public String getId()
  {
    return this.entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    this.entity.setId(id);
  }
  
  @Override
  public Long getVersionId()
  {
    return this.entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.entity.setVersionId(versionId);
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return this.entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    this.entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return this.entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    this.entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getRelationships()
  {
    return this.entity.getRelationships();
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setRelationships(List<IGovernanceRuleIntermediateEntity> relationships)
  {
    this.entity.setRelationships(relationships);
  }
  
  @Override
  public List<IGovernanceRuleTags> getTags()
  {
    return this.entity.getTags();
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleTags.class)
  @Override
  public void setTags(List<IGovernanceRuleTags> tags)
  {
    this.entity.setTags(tags);
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }
  
  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return entity.getIsStandard();
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    entity.setIsStandard(isStandard);
  }
  
  @Override
  public IConfigDetailsForGovernanceRuleModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGovernanceRuleModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGovernanceRuleModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public String getType()
  {
    return this.entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    this.entity.setType(type);
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return this.entity.getKlassIds();
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.entity.setKlassIds(klassIds);
  }
  
  @Override
  public List<String> getTaxonomyIds()
  {
    return this.entity.getTaxonomyIds();
  }
  
  @Override
  public void setTaxonomyIds(List<String> taxonomyIds)
  {
    this.entity.setTaxonomyIds(taxonomyIds);
    ;
  }
  
  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
  
  @Override
  public String getCode()
  {
    return this.entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    this.entity.setCode(code);
  }
}
