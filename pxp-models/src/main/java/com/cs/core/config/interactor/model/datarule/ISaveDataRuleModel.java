package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveDataRuleModel extends IModel {
  
  public static final String ID                             = "id";
  public static final String LABEL                          = "label";
  public static final String ADDED_ORGANIZATION_IDS         = "addedOrganizationIds";
  public static final String DELETED_ORGANIZATION_IDS       = "deletedOrganizationIds";
  public static final String PHYSICAL_CATALOG_IDS           = "physicalCatalogIds";
  public static final String ADDED_ENDPOINTS                = "addedEndpoints";
  public static final String DELETED_ENDPOINTS              = "deletedEndpoints";
  public static final String ADDED_LANGUAGES                = "addedLanguages";
  public static final String DELETED_LANGUAGES              = "deletedLanguages";
  public static final String AVAILABLE_PHYSICAL_CATALOG_IDS = "availablePhysicalCatalogIds";
  public static final String ADDED_RULE_VIOLATIONS = "addedRuleViolations";
  public String getId();
  
  public void setId(String id);
  
  public List<IDataRuleIntermediateEntitys> getAddedAttributeRules();
  
  public void setAddedAttributeRules(List<IDataRuleIntermediateEntitys> addedAttributeRules);
  
  public List<String> getDeletedAttributeRules();
  
  public void setDeletedAttributeRules(List<String> deletedAttributeRules);
  
  public List<IModifiedRuleEntityModel> getModifiedAttributeRules();
  
  public void setModifiedAttributeRules(List<IModifiedRuleEntityModel> modifiedAttributeRules);
  
  public List<IDataRuleTags> getAddedTagRules();
  
  public void setAddedTagRules(List<IDataRuleTags> addedTagRules);
  
  public List<String> getDeletedTagRules();
  
  public void setDeletedTagRules(List<String> deletedTagRules);
  
  public List<IModifiedTagRuleEntityModel> getModifiedTagRules();
  
  public void setModifiedTagRules(List<IModifiedTagRuleEntityModel> modifiedTagRules);
  
  public List<IDataRuleIntermediateEntitys> getAddedRoleRules();
  
  public void setAddedRoleRules(List<IDataRuleIntermediateEntitys> addedRoleRules);
  
  public List<String> getDeletedRoleRules();
  
  public void setDeletedRoleRules(List<String> deletedRoleRules);
  
  public List<IModifiedRuleEntityModel> getModifiedRoleRules();
  
  public void setModifiedRoleRules(List<IModifiedRuleEntityModel> modifiedRoleRules);
  
  public List<IDataRuleIntermediateEntitys> getAddedRelationshipRules();
  
  public void setAddedRelationshipRules(List<IDataRuleIntermediateEntitys> addedRelationshipRules);
  
  public List<String> getDeletedRelationshipRules();
  
  public void setDeletedRelationshipRules(List<String> deletedRelationshipRules);
  
  public List<IModifiedRuleEntityModel> getModifiedRelationshipRules();
  
  public void setModifiedRelationshipRules(
      List<IModifiedRuleEntityModel> modifiedRelationshipRules);
  
  public List<String> getAddedTypes();
  
  public void setAddedTypes(List<String> addedTypes);
  
  public List<String> getDeletedTypes();
  
  public void setDeletedTypes(List<String> deletedTypes);
  
  /* public List<IModifiedRuleEntityModel> getModifiedTypeRules();
  public void setModifiedTypeRules(List<IModifiedRuleEntityModel> modifiedTypeRules);*/
  
  public List<IRuleViolationEntity> getModifiedRuleViolations();
  
  public void setModifiedRuleViolations(List<IRuleViolationEntity> modifiedRuleViolations);
  
  public List<String> getDeletedRuleViolations();
  
  public void setDeletedRuleViolations(List<String> deletedRuleViolations);
  
  public List<IRuleViolationEntity> getAddedRuleViolations();
  
  public void setAddedRuleViolations(List<IRuleViolationEntity> addedRuleViolations);
  
  public List<? extends INormalization> getModifiedNormalizations();
  
  public void setModifiedNormalizations(List<? extends INormalization> modifiedNormalizations);
  
  public List<String> getDeletedNormalizations();
  
  public void setDeletedNormalizations(List<String> deletedNormalizations);
  
  public List<? extends INormalization> getAddedNormalizations();
  
  public void setAddedNormalizations(List<? extends INormalization> addedNormalizations);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<String> getAddedTaxonomies();
  
  public void setAddedTaxonomies(List<String> addedTaxonomies);
  
  public List<String> getDeletedTaxonomies();
  
  public void setDeletedTaxonomies(List<String> deletedTaxonomies);
  
  public List<String> getAddedOrganizationIds();
  
  public void setAddedOrganizationIds(List<String> addedOrganizationIds);
  
  public List<String> getDeletedOrganizationIds();
  
  public void setDeletedOrganizationIds(List<String> deletedOrganizationIds);
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getAddedEndpoints();
  
  public void setAddedEndpoints(List<String> addedEndpoints);
  
  public List<String> getDeletedEndpoints();
  
  public void setDeletedEndpoints(List<String> deletedEndpoints);
  
  public List<String> getAddedLanguages();
  
  public void setAddedLanguages(List<String> addedLanguages);
  
  public List<String> getDeletedLanguages();
  
  public void setDeletedLanguages(List<String> deletedLanguages);
  
  public List<String> getAvailablePhysicalCatalogIds();
  
  public void setAvailablePhysicalCatalogIds(List<String> allAvailablePhysicalCatalogIds);
}
