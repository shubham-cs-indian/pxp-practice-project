package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTags;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IModifiedKPIRuleModel extends IModel {
  
  public static final String ID                          = "id";
  public static final String LABEL                       = "label";
  public static final String TYPE                        = "type";
  public static final String ADDED_KLASS_IDS             = "addedKlassIds";
  public static final String DELETED_KLASS_IDS           = "deletedKlassIds";
  public static final String ADDED_TAXONOMY_IDS          = "addedTaxonomyIds";
  public static final String DELETED_TAXONOMY_IDS        = "deletedTaxonomyIds";
  public static final String ADDED_ATTRIBUTE_RULES       = "addedAttributeRules";
  public static final String MODIFIED_ATTRIBUTE_RULES    = "modifiedAttributeRules";
  public static final String DELETED_ATTRIBUTE_RULES     = "deletedAttributeRules";
  public static final String ADDED_TAG_RULES             = "addedTagRules";
  public static final String DELETED_TAG_RULES           = "deletedTagRules";
  public static final String MODIFIED_TAG_RULES          = "modifiedTagRules";
  public static final String ADDED_ROLE_RULES            = "addedRoleRules";
  public static final String DELETED_ROLE_RULES          = "deletedRoleRules";
  public static final String MODIFIED_ROLE_RULES         = "modifiedRoleRules";
  public static final String ADDED_RELATIONSHIP_RULES    = "addedRelationshipRules";
  public static final String DELETED_RELATIONSHIP_RULES  = "deletedRelationshipRules";
  public static final String MODIFIED_RELATIONSHIP_RULES = "modifiedRelationshipRules";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getType();
  
  public void setType(String type);
  
  public List<String> getAddedKlassIds();
  
  public void setAddedKlassIds(List<String> addedKlassIds);
  
  public List<String> getDeletedKlassIds();
  
  public void setDeletedKlassIds(List<String> deletedKlassIds);
  
  public List<String> getAddedTaxonomyIds();
  
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds);
  
  public List<String> getDeletedTaxonomyIds();
  
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds);
  
  public List<IGovernanceRuleIntermediateEntity> getAddedAttributeRules();
  
  public void setAddedAttributeRules(List<IGovernanceRuleIntermediateEntity> addedAttributeRules);
  
  public List<String> getDeletedAttributeRules();
  
  public void setDeletedAttributeRules(List<String> deletedAttributeRules);
  
  public List<IModifiedGovernanceRuleEntityModel> getModifiedAttributeRules();
  
  public void setModifiedAttributeRules(
      List<IModifiedGovernanceRuleEntityModel> modifiedAttributeRules);
  
  public List<IGovernanceRuleTags> getAddedTagRules();
  
  public void setAddedTagRules(List<IGovernanceRuleTags> addedTagRules);
  
  public List<String> getDeletedTagRules();
  
  public void setDeletedTagRules(List<String> deletedTagRules);
  
  public List<IModifiedGovernanceTagRuleEntityModel> getModifiedTagRules();
  
  public void setModifiedTagRules(List<IModifiedGovernanceTagRuleEntityModel> modifiedTagRules);
  
  public List<IGovernanceRuleIntermediateEntity> getAddedRoleRules();
  
  public void setAddedRoleRules(List<IGovernanceRuleIntermediateEntity> addedRoleRules);
  
  public List<String> getDeletedRoleRules();
  
  public void setDeletedRoleRules(List<String> deletedRoleRules);
  
  public List<IModifiedGovernanceRuleEntityModel> getModifiedRoleRules();
  
  public void setModifiedRoleRules(List<IModifiedGovernanceRuleEntityModel> modifiedRoleRules);
  
  public List<IGovernanceRuleIntermediateEntity> getAddedRelationshipRules();
  
  public void setAddedRelationshipRules(
      List<IGovernanceRuleIntermediateEntity> addedRelationshipRules);
  
  public List<String> getDeletedRelationshipRules();
  
  public void setDeletedRelationshipRules(List<String> deletedRelationshipRules);
  
  public List<IModifiedGovernanceRuleEntityModel> getModifiedRelationshipRules();
  
  public void setModifiedRelationshipRules(
      List<IModifiedGovernanceRuleEntityModel> modifiedRelationshipRules);
}
