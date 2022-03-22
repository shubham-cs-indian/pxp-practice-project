package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.datarule.INormalization;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveDataRuleModel implements ISaveDataRuleModel {
  
  /**
   *
   */
  private static final long                    serialVersionUID = 1L;
  
  protected String                             id;
  protected String                             label;
  protected List<IDataRuleIntermediateEntitys> addedAttributeRules;
  protected List<String>                       deletedAttributeRules;
  protected List<IModifiedRuleEntityModel>     modifiedAttributeRules;
  protected List<IDataRuleTags>                addedTagRules;
  protected List<String>                       deletedTagRules;
  protected List<IModifiedTagRuleEntityModel>  modifiedTagRules;
  protected List<IDataRuleIntermediateEntitys> addedRoleRules;
  protected List<String>                       deletedRoleRules;
  protected List<IModifiedRuleEntityModel>     modifiedRoleRules;
  protected List<IDataRuleIntermediateEntitys> addedRelationshipRules;
  protected List<String>                       deletedRelationshipRules;
  protected List<IModifiedRuleEntityModel>     modifiedRelationshipRules;
  /* protected List<IDataRuleIntermediateEntitys> addedTypeRules;
  protected List<String>                       deletedTypeRules;*/
  // protected List<IModifiedRuleEntityModel> modifiedTypeRules;
  protected List<IRuleViolationEntity>         addedRuleViolations;
  protected List<String>                       deletedRuleViolations;
  protected List<IRuleViolationEntity>         modifiedRuleViolations;
  protected List<? extends INormalization>     addedNormalizations;
  protected List<String>                       deletedNormalizations;
  protected List<? extends INormalization>     modifiedNormalizations;
  protected List<String>                       addedTypes;
  protected List<String>                       deletedTypes;
  protected List<String>                       addedTaxonomies;
  protected List<String>                       deletedTaxonomies;
  protected List<String>                       addedOrganizationIds;
  protected List<String>                       deletedOrganizationIds;
  protected List<String>                       physicalCatalogIds;
  protected List<String>                       addedEndpoints;
  protected List<String>                       deletedEndpoints;
  protected List<String>                       addedLanguages;
  protected List<String>                       deletedLanguages;
  protected List<String>                       availablePhysicalCatalogIds;
  
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
  public List<IDataRuleIntermediateEntitys> getAddedAttributeRules()
  {
    if (addedAttributeRules == null) {
      addedAttributeRules = new ArrayList<>();
    }
    return addedAttributeRules;
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setAddedAttributeRules(List<IDataRuleIntermediateEntitys> addedAttributeRules)
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
  public List<IModifiedRuleEntityModel> getModifiedAttributeRules()
  {
    if (modifiedAttributeRules == null) {
      modifiedAttributeRules = new ArrayList<>();
    }
    return modifiedAttributeRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedRuleEntityModel.class)
  @Override
  public void setModifiedAttributeRules(List<IModifiedRuleEntityModel> modifiedAttributeRules)
  {
    this.modifiedAttributeRules = modifiedAttributeRules;
  }
  
  @Override
  public List<IDataRuleTags> getAddedTagRules()
  {
    if (addedTagRules == null) {
      addedTagRules = new ArrayList<>();
    }
    return addedTagRules;
  }
  
  @JsonDeserialize(contentAs = DataRuleTags.class)
  @Override
  public void setAddedTagRules(List<IDataRuleTags> addedTagRules)
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
  public List<IModifiedTagRuleEntityModel> getModifiedTagRules()
  {
    if (modifiedTagRules == null) {
      modifiedTagRules = new ArrayList<>();
    }
    return modifiedTagRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedTagRuleEntityModel.class)
  @Override
  public void setModifiedTagRules(List<IModifiedTagRuleEntityModel> modifiedTagRules)
  {
    this.modifiedTagRules = modifiedTagRules;
  }
  
  @Override
  public List<IDataRuleIntermediateEntitys> getAddedRoleRules()
  {
    if (addedRoleRules == null) {
      addedRoleRules = new ArrayList<>();
    }
    return addedRoleRules;
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setAddedRoleRules(List<IDataRuleIntermediateEntitys> addedRoleRules)
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
  public List<IModifiedRuleEntityModel> getModifiedRoleRules()
  {
    if (modifiedRoleRules == null) {
      modifiedRoleRules = new ArrayList<>();
    }
    return modifiedRoleRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedRuleEntityModel.class)
  @Override
  public void setModifiedRoleRules(List<IModifiedRuleEntityModel> modifiedRoleRules)
  {
    this.modifiedRoleRules = modifiedRoleRules;
  }
  
  @Override
  public List<IDataRuleIntermediateEntitys> getAddedRelationshipRules()
  {
    if (addedRelationshipRules == null) {
      addedRelationshipRules = new ArrayList<>();
    }
    return addedRelationshipRules;
  }
  
  @JsonDeserialize(contentAs = DataRuleIntermediateEntitys.class)
  @Override
  public void setAddedRelationshipRules(List<IDataRuleIntermediateEntitys> addedRelationshipRules)
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
  public List<IModifiedRuleEntityModel> getModifiedRelationshipRules()
  {
    if (modifiedRelationshipRules == null) {
      modifiedRelationshipRules = new ArrayList<>();
    }
    return modifiedRelationshipRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedRuleEntityModel.class)
  @Override
  public void setModifiedRelationshipRules(List<IModifiedRuleEntityModel> modifiedRelationshipRules)
  {
    this.modifiedRelationshipRules = modifiedRelationshipRules;
  }
  
  @Override
  public List<String> getAddedTypes()
  {
    if (addedTypes == null) {
      addedTypes = new ArrayList<>();
    }
    return addedTypes;
  }
  
  @Override
  public void setAddedTypes(List<String> addedTypes)
  {
    this.addedTypes = addedTypes;
  }
  
  @Override
  public List<String> getDeletedTypes()
  {
    if (deletedTypes == null) {
      deletedTypes = new ArrayList<>();
    }
    return deletedTypes;
  }
  
  @Override
  public void setDeletedTypes(List<String> deletedTypes)
  {
    this.deletedTypes = deletedTypes;
  }
  
  /*@Override
  public List<IModifiedRuleEntityModel> getModifiedTypeRules()
  {
    if (modifiedTypeRules == null) {
      modifiedTypeRules = new ArrayList<>();
    }
    return modifiedTypeRules;
  }
  
  @JsonDeserialize(contentAs = ModifiedRuleEntityModel.class)
  @Override
  public void setModifiedTypeRules(List<IModifiedRuleEntityModel> modifiedTypeRules)
  {
    this.modifiedTypeRules = modifiedTypeRules;
  }
  */
  @Override
  public List<? extends INormalization> getAddedNormalizations()
  {
    if (addedNormalizations == null) {
      addedNormalizations = new ArrayList<>();
    }
    return addedNormalizations;
  }
  
  @JsonDeserialize(contentAs = Normalization.class)
  @Override
  public void setAddedNormalizations(List<? extends INormalization> addedNormalizations)
  {
    this.addedNormalizations = addedNormalizations;
  }
  
  @Override
  public List<String> getDeletedNormalizations()
  {
    if (deletedNormalizations == null) {
      deletedNormalizations = new ArrayList<>();
    }
    return deletedNormalizations;
  }
  
  @Override
  public void setDeletedNormalizations(List<String> deletedNormalizations)
  {
    this.deletedNormalizations = deletedNormalizations;
  }
  
  @Override
  public List<? extends INormalization> getModifiedNormalizations()
  {
    if (modifiedNormalizations == null) {
      modifiedNormalizations = new ArrayList<>();
    }
    return modifiedNormalizations;
  }
  
  @JsonDeserialize(contentAs = Normalization.class)
  @Override
  public void setModifiedNormalizations(List<? extends INormalization> modifiedNormalizations)
  {
    this.modifiedNormalizations = modifiedNormalizations;
  }
  
  @Override
  public List<IRuleViolationEntity> getAddedRuleViolations()
  {
    if (addedRuleViolations == null) {
      addedRuleViolations = new ArrayList<>();
    }
    return addedRuleViolations;
  }
  
  @JsonDeserialize(contentAs = RuleViolationEntity.class)
  @Override
  public void setAddedRuleViolations(List<IRuleViolationEntity> addedRuleViolations)
  {
    this.addedRuleViolations = addedRuleViolations;
  }
  
  @Override
  public List<String> getDeletedRuleViolations()
  {
    if (deletedRuleViolations == null) {
      deletedRuleViolations = new ArrayList<>();
    }
    return deletedRuleViolations;
  }
  
  @Override
  public void setDeletedRuleViolations(List<String> deletedRuleViolations)
  {
    this.deletedRuleViolations = deletedRuleViolations;
  }
  
  @Override
  public List<IRuleViolationEntity> getModifiedRuleViolations()
  {
    if (modifiedRuleViolations == null) {
      modifiedRuleViolations = new ArrayList<>();
    }
    return modifiedRuleViolations;
  }
  
  @JsonDeserialize(contentAs = RuleViolationEntity.class)
  @Override
  public void setModifiedRuleViolations(List<IRuleViolationEntity> modifiedRuleViolations)
  {
    this.modifiedRuleViolations = modifiedRuleViolations;
  }
  
  @Override
  public List<String> getAddedTaxonomies()
  {
    if (addedTaxonomies == null) {
      addedTaxonomies = new ArrayList<>();
    }
    return addedTaxonomies;
  }
  
  @Override
  public void setAddedTaxonomies(List<String> addedTaxonomies)
  {
    this.addedTaxonomies = addedTaxonomies;
  }
  
  @Override
  public List<String> getDeletedTaxonomies()
  {
    if (deletedTaxonomies == null) {
      deletedTaxonomies = new ArrayList<>();
    }
    return deletedTaxonomies;
  }
  
  @Override
  public void setDeletedTaxonomies(List<String> deletedTaxonomies)
  {
    this.deletedTaxonomies = deletedTaxonomies;
  }
  
  @Override
  public List<String> getAddedOrganizationIds()
  {
    if (addedOrganizationIds == null) {
      return new ArrayList<>();
    }
    return addedOrganizationIds;
  }
  
  @Override
  public void setAddedOrganizationIds(List<String> addedOrganizationIds)
  {
    this.addedOrganizationIds = addedOrganizationIds;
  }
  
  @Override
  public List<String> getDeletedOrganizationIds()
  {
    if (deletedOrganizationIds == null) {
      return new ArrayList<>();
    }
    return deletedOrganizationIds;
  }
  
  @Override
  public void setDeletedOrganizationIds(List<String> deletedOrganizationIds)
  {
    this.deletedOrganizationIds = deletedOrganizationIds;
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
  public List<String> getAddedEndpoints()
  {
    if (addedEndpoints == null) {
      addedEndpoints = new ArrayList<>();
    }
    return addedEndpoints;
  }
  
  @Override
  public void setAddedEndpoints(List<String> addedEndpoints)
  {
    this.addedEndpoints = addedEndpoints;
  }
  
  @Override
  public List<String> getDeletedEndpoints()
  {
    if (deletedEndpoints == null) {
      deletedEndpoints = new ArrayList<>();
    }
    return deletedEndpoints;
  }
  
  @Override
  public void setDeletedEndpoints(List<String> deletedEndpoints)
  {
    this.deletedEndpoints = deletedEndpoints;
  }
  
  @Override
  public List<String> getAddedLanguages()
  {
    if (addedLanguages == null) {
      addedLanguages = new ArrayList<>();
    }
    return addedLanguages;
  }
  
  @Override
  public void setAddedLanguages(List<String> addedLanguages)
  {
    this.addedLanguages = addedLanguages;
  }
  
  @Override
  public List<String> getDeletedLanguages()
  {
    if (deletedLanguages == null) {
      deletedLanguages = new ArrayList<>();
    }
    return deletedLanguages;
  }
  
  @Override
  public void setDeletedLanguages(List<String> deletedLanguages)
  {
    this.deletedLanguages = deletedLanguages;
  }
  
  @Override
  public List<String> getAvailablePhysicalCatalogIds()
  {
    if (availablePhysicalCatalogIds == null) {
      availablePhysicalCatalogIds = new ArrayList<>();
    }
    return availablePhysicalCatalogIds;
  }
  
  @Override
  public void setAvailablePhysicalCatalogIds(List<String> availablePhysicalCatalogIds)
  {
    this.availablePhysicalCatalogIds = availablePhysicalCatalogIds;
  }
}
