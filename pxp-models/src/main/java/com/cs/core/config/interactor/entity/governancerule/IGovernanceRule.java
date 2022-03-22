package com.cs.core.config.interactor.entity.governancerule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface IGovernanceRule extends IEntity {
  
  public static final String LABEL         = "label";
  public static final String ATTRIBUTES    = "attributes";
  public static final String RELATIONSHIPS = "relationships";
  public static final String ROLES         = "roles";
  public static final String TAGS          = "tags";
  public static final String IS_STANDARD   = "isStandard";
  public static final String TYPE          = "type";
  public static final String KLASS_IDS     = "klassIds";
  public static final String TAXONOMY_IDS  = "taxonomyIds";
  public static final String CODE          = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IGovernanceRuleIntermediateEntity> getAttributes();
  
  public void setAttributes(List<IGovernanceRuleIntermediateEntity> attributes);
  
  public List<IGovernanceRuleIntermediateEntity> getRelationships();
  
  public void setRelationships(List<IGovernanceRuleIntermediateEntity> relationships);
  
  public List<IGovernanceRuleIntermediateEntity> getRoles();
  
  public void setRoles(List<IGovernanceRuleIntermediateEntity> roles);
  
  public List<IGovernanceRuleTags> getTags();
  
  public void setTags(List<IGovernanceRuleTags> tags);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
}
