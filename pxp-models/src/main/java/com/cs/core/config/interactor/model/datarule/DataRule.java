package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.model.rulelist.IRuleList;
import com.cs.core.config.interactor.model.rulelist.RuleList;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DataRule implements IDataRule {
  
  private static final long                    serialVersionUID = 1L;
  
  protected String                             id;
  protected String                             label;
  protected Long                               versionId;
  protected Long                               versionTimestamp;
  protected String                             lastModifiedBy;
  protected List<IDataRuleIntermediateEntitys> attributes;
  protected List<IDataRuleIntermediateEntitys> relationships;
  protected List<IDataRuleIntermediateEntitys> roles;
  protected List<String>                       types;
  protected List<IRuleList>                    referencedRuleList;
  protected List<IDataRuleTags>                tags;
  protected List<IRuleViolationEntity>         ruleViolations;
  protected List<? extends INormalization>     normalizations;
  protected Boolean                            isStandard       = false;
  protected List<String>                       taxonomies;
  protected String                             code;
  protected String                             type;
  protected List<String>                       organizations;
  protected List<String>                       physicalCatalogIds;
  protected List<String>                       endpoints;
  protected Boolean                            isLanguageDependent;
  protected List<String>                       languages;
  
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
  public List<IDataRuleIntermediateEntitys> getRoles()
  {
    if (roles == null) {
      roles = new ArrayList<>();
    }
    return roles;
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setRoles(List<IDataRuleIntermediateEntitys> roles)
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
  public List<IDataRuleIntermediateEntitys> getRelationships()
  {
    if (relationships == null) {
      relationships = new ArrayList<>();
    }
    return relationships;
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setRelationships(List<IDataRuleIntermediateEntitys> relationships)
  {
    this.relationships = relationships;
  }
  
  @Override
  public List<IDataRuleIntermediateEntitys> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<IDataRuleIntermediateEntitys>();
    }
    return attributes;
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setAttributes(List<IDataRuleIntermediateEntitys> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public List<String> getTypes()
  {
    if (types == null) {
      types = new ArrayList<>();
    }
    return types;
  }
  
  @Override
  public void setTypes(List<String> types)
  {
    this.types = types;
  }
  
  @Override
  public List<IRuleList> getReferencedRuleList()
  {
    if (referencedRuleList == null) {
      referencedRuleList = new ArrayList<>();
    }
    return referencedRuleList;
  }
  
  @JsonDeserialize(contentAs = RuleList.class)
  @Override
  public void setReferencedRuleList(List<IRuleList> ruleList)
  {
    this.referencedRuleList = ruleList;
  }
  
  @Override
  public List<IDataRuleTags> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @JsonDeserialize(contentAs = DataRuleTags.class)
  @Override
  public void setTags(List<IDataRuleTags> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IRuleViolationEntity> getRuleViolations()
  {
    if (ruleViolations == null) {
      ruleViolations = new ArrayList<>();
    }
    return ruleViolations;
  }
  
  @Override
  public void setRuleViolations(List<IRuleViolationEntity> ruleViolations)
  {
    this.ruleViolations = ruleViolations;
  }
  
  @Override
  public List<? extends INormalization> getNormalizations()
  {
    if (normalizations == null) {
      normalizations = new ArrayList<>();
    }
    return normalizations;
  }
  
  @JsonDeserialize(contentAs = Normalization.class)
  @Override
  public void setNormalizations(List<? extends INormalization> normalizations)
  {
    this.normalizations = normalizations;
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
  public List<String> getTaxonomies()
  {
    if (taxonomies == null) {
      taxonomies = new ArrayList<>();
    }
    return taxonomies;
  }
  
  @Override
  public void setTaxonomies(List<String> taxonomies)
  {
    this.taxonomies = taxonomies;
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
  public List<String> getOrganizations()
  {
    return organizations;
  }
  
  @Override
  public void setOrganizations(List<String> organizations)
  {
    this.organizations = organizations;
  }
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    if (physicalCatalogIds == null) {
      physicalCatalogIds = new ArrayList<>();
    }
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public List<String> getEndpoints()
  {
    if (endpoints == null) {
      endpoints = new ArrayList<>();
    }
    return endpoints;
  }
  
  @Override
  public void setEndpoints(List<String> endpoints)
  {
    this.endpoints = endpoints;
  }
  
  @Override
  public Boolean getIsLanguageDependent()
  {
    if (isLanguageDependent == null) {
      isLanguageDependent = false;
    }
    return isLanguageDependent;
  }
  
  @Override
  public void setIsLanguageDependent(Boolean isLanguageDependent)
  {
    this.isLanguageDependent = isLanguageDependent;
  }
  
  @Override
  public List<String> getLanguages()
  {
    return languages;
  }
  
  @Override
  public void setLanguages(List<String> languages)
  {
    this.languages = languages;
  }
}
