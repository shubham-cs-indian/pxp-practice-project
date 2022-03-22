package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.GovernanceRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.governancerule.GovernanceRuleTags;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleIntermediateEntity;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleTags;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedKPIRuleModel implements IModifiedKPIRuleModel {
  
  private static final long                             serialVersionUID = 1L;
  protected String                                      id;
  protected String                                      label;
  protected String                                      type;
  protected List<String>                                addedKlassIds;
  protected List<String>                                deletedKlassIds;
  protected List<String>                                addedTaxonomyIds;
  protected List<String>                                deletedTaxonomyIds;
  protected List<IGovernanceRuleIntermediateEntity>     addedAttributeRules;
  protected List<String>                                deletedAttributeRules;
  protected List<IModifiedGovernanceRuleEntityModel>    modifiedAttributeRules;
  protected List<IGovernanceRuleTags>                   addedTagRules;
  protected List<String>                                deletedTagRules;
  protected List<IModifiedGovernanceTagRuleEntityModel> modifiedTagRules;
  protected List<IGovernanceRuleIntermediateEntity>     addedRoleRules;
  protected List<String>                                deletedRoleRules;
  protected List<IModifiedGovernanceRuleEntityModel>    modifiedRoleRules;
  protected List<IGovernanceRuleIntermediateEntity>     addedRelationshipRules;
  protected List<String>                                deletedRelationshipRules;
  protected List<IModifiedGovernanceRuleEntityModel>    modifiedRelationshipRules;
  
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
  public List<IGovernanceRuleIntermediateEntity> getAddedAttributeRules()
  {
    if (addedAttributeRules == null) {
      addedAttributeRules = new ArrayList<>();
    }
    return addedAttributeRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setAddedAttributeRules(List<IGovernanceRuleIntermediateEntity> addedAttributeRules)
  {
    this.addedAttributeRules = addedAttributeRules;
  }
  
  @Override
  public List<String> getDeletedAttributeRules()
  {
    if (deletedAttributeRules == null) {
      deletedAttributeRules = new ArrayList<>();
    }
    return deletedAttributeRules;
  }
  
  @Override
  public void setDeletedAttributeRules(List<String> deletedAttributeRules)
  {
    this.deletedAttributeRules = deletedAttributeRules;
  }
  
  @Override
  public List<IModifiedGovernanceRuleEntityModel> getModifiedAttributeRules()
  {
    if (modifiedAttributeRules == null) {
      modifiedAttributeRules = new ArrayList<>();
    }
    return modifiedAttributeRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedGovernanceRuleEntityModel.class)
  @Override
  public void setModifiedAttributeRules(
      List<IModifiedGovernanceRuleEntityModel> modifiedAttributeRules)
  {
    this.modifiedAttributeRules = modifiedAttributeRules;
  }
  
  @Override
  public List<IGovernanceRuleTags> getAddedTagRules()
  {
    if (addedTagRules == null) {
      addedTagRules = new ArrayList<>();
    }
    return addedTagRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleTags.class)
  @Override
  public void setAddedTagRules(List<IGovernanceRuleTags> addedTagRules)
  {
    this.addedTagRules = addedTagRules;
  }
  
  @Override
  public List<String> getDeletedTagRules()
  {
    if (deletedTagRules == null) {
      deletedTagRules = new ArrayList<>();
    }
    return deletedTagRules;
  }
  
  @Override
  public void setDeletedTagRules(List<String> deletedTagRules)
  {
    this.deletedTagRules = deletedTagRules;
  }
  
  @Override
  public List<IModifiedGovernanceTagRuleEntityModel> getModifiedTagRules()
  {
    if (modifiedTagRules == null) {
      modifiedTagRules = new ArrayList<>();
    }
    return modifiedTagRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedGovernanceTagRuleEntityModel.class)
  @Override
  public void setModifiedTagRules(List<IModifiedGovernanceTagRuleEntityModel> modifiedTagRules)
  {
    this.modifiedTagRules = modifiedTagRules;
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getAddedRoleRules()
  {
    if (addedRoleRules == null) {
      addedRoleRules = new ArrayList<>();
    }
    return addedRoleRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setAddedRoleRules(List<IGovernanceRuleIntermediateEntity> addedRoleRules)
  {
    this.addedRoleRules = addedRoleRules;
  }
  
  @Override
  public List<String> getDeletedRoleRules()
  {
    if (deletedRoleRules == null) {
      deletedRoleRules = new ArrayList<>();
    }
    return deletedRoleRules;
  }
  
  @Override
  public void setDeletedRoleRules(List<String> deletedRoleRules)
  {
    this.deletedRoleRules = deletedRoleRules;
  }
  
  @Override
  public List<IModifiedGovernanceRuleEntityModel> getModifiedRoleRules()
  {
    if (modifiedRoleRules == null) {
      modifiedRoleRules = new ArrayList<>();
    }
    return modifiedRoleRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedGovernanceRuleEntityModel.class)
  @Override
  public void setModifiedRoleRules(List<IModifiedGovernanceRuleEntityModel> modifiedRoleRules)
  {
    this.modifiedRoleRules = modifiedRoleRules;
  }
  
  @Override
  public List<IGovernanceRuleIntermediateEntity> getAddedRelationshipRules()
  {
    if (addedRelationshipRules == null) {
      addedRelationshipRules = new ArrayList<>();
    }
    return addedRelationshipRules;
  }
  
  @JsonDeserialize(contentAs = GovernanceRuleIntermediateEntity.class)
  @Override
  public void setAddedRelationshipRules(
      List<IGovernanceRuleIntermediateEntity> addedRelationshipRules)
  {
    this.addedRelationshipRules = addedRelationshipRules;
  }
  
  @Override
  public List<String> getDeletedRelationshipRules()
  {
    if (deletedRelationshipRules == null) {
      deletedRelationshipRules = new ArrayList<>();
    }
    return deletedRelationshipRules;
  }
  
  @Override
  public void setDeletedRelationshipRules(List<String> deletedRelationshipRules)
  {
    this.deletedRelationshipRules = deletedRelationshipRules;
  }
  
  @Override
  public List<IModifiedGovernanceRuleEntityModel> getModifiedRelationshipRules()
  {
    if (modifiedRelationshipRules == null) {
      modifiedRelationshipRules = new ArrayList<>();
    }
    return modifiedRelationshipRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedGovernanceRuleEntityModel.class)
  @Override
  public void setModifiedRelationshipRules(
      List<IModifiedGovernanceRuleEntityModel> modifiedRelationshipRules)
  {
    this.modifiedRelationshipRules = modifiedRelationshipRules;
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
  public List<String> getAddedKlassIds()
  {
    return addedKlassIds;
  }
  
  @Override
  public void setAddedKlassIds(List<String> addedKlassIds)
  {
    this.addedKlassIds = addedKlassIds;
  }
  
  @Override
  public List<String> getDeletedKlassIds()
  {
    return deletedKlassIds;
  }
  
  @Override
  public void setDeletedKlassIds(List<String> deletedKlassIds)
  {
    this.deletedKlassIds = deletedKlassIds;
  }
  
  public List<String> getAddedTaxonomyIds()
  {
    return addedTaxonomyIds;
  }
  
  @Override
  public void setAddedTaxonomyIds(List<String> addedTaxonomyIds)
  {
    this.addedTaxonomyIds = addedTaxonomyIds;
  }
  
  @Override
  public List<String> getDeletedTaxonomyIds()
  {
    return deletedTaxonomyIds;
  }
  
  @Override
  public void setDeletedTaxonomyIds(List<String> deletedTaxonomyIds)
  {
    this.deletedTaxonomyIds = deletedTaxonomyIds;
  }
}
