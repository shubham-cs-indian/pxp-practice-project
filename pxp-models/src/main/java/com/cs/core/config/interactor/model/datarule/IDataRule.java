package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.config.interactor.model.rulelist.IRuleList;

import java.util.List;

public interface IDataRule extends IEntity {
  
  public static final String LABEL                 = "label";
  public static final String ATTRIBUTES            = "attributes";
  public static final String RELATIONSHIPS         = "relationships";
  public static final String ROLES                 = "roles";
  public static final String TYPES                 = "types";
  public static final String RULE_LIST             = "ruleList";
  public static final String TAGS                  = "tags";
  public static final String RULE_VIOLATIONS       = "ruleViolations";
  public static final String NORMALIZATIONS        = "normalizations";
  public static final String IS_STANDARD           = "isStandard";
  public static final String TAXONOMIES            = "taxonomies";
  public static final String CODE                  = "code";
  public static final String TYPE                  = "type";
  public static final String PHYSICAL_CATALOG_IDS  = "physicalCatalogIds";
  public static final String ORGANIZATIONS         = "organizations";
  public static final String ENDPOINTS             = "endpoints";
  public static final String IS_LANGUAGE_DEPENDENT = "isLanguageDependent";
  public static final String LANGUAGES             = "languages";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IDataRuleIntermediateEntitys> getAttributes();
  
  public void setAttributes(List<IDataRuleIntermediateEntitys> attributes);
  
  public List<IDataRuleIntermediateEntitys> getRelationships();
  
  public void setRelationships(List<IDataRuleIntermediateEntitys> relationships);
  
  public List<IDataRuleIntermediateEntitys> getRoles();
  
  public void setRoles(List<IDataRuleIntermediateEntitys> roles);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public List<IRuleList> getReferencedRuleList();
  
  public void setReferencedRuleList(List<IRuleList> ruleList);
  
  public List<IDataRuleTags> getTags();
  
  public void setTags(List<IDataRuleTags> tags);
  
  public List<IRuleViolationEntity> getRuleViolations();
  
  public void setRuleViolations(List<IRuleViolationEntity> ruleViolations);
  
  public List<? extends INormalization> getNormalizations();
  
  public void setNormalizations(List<? extends INormalization> normalizations);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
  
  public List<String> getTaxonomies();
  
  public void setTaxonomies(List<String> taxonomies);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getOrganizations();
  
  public void setOrganizations(List<String> organizations);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getEndpoints();
  
  public void setEndpoints(List<String> endpoints);
  
  public Boolean getIsLanguageDependent();
  
  public void setIsLanguageDependent(Boolean isLanguageDependent);
  
  public List<String> getLanguages();
  
  public void setLanguages(List<String> languages);
}
