package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.Relationship;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.language.GetLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public class GetMultiClassificationKlassDetailsModel
    implements IGetMultiClassificationKlassDetailsModel {
  
  private static final long                                    serialVersionUID = 1L;
  
  protected Map<String, IReferencedKlassDetailStrategyModel>   klasses;
  protected Map<String, IReferencedPropertyCollectionModel>    referencedSections;
  protected Map<String, IReferencedSectionElementModel>        referencedElements;
  protected Map<String, IAttribute>                            referencedAttributes;
  protected Map<String, ITag>                                  referencedTags;
  protected Map<String, IRole>                                 referencedRoles;
  protected List<IDataRuleModel>                               referencedDataRules;
  protected IReferencedRolePermissionModel                     referencedPermission;
  protected Integer                                            numberOfVersionsToMaintain;
  protected Map<String, Set<String>>                           notificationSettings;
  protected Map<String, IRelationship>                         referencedRelationships;
  protected IReferencedContextModel                            referencedVariantContexts;
  protected Map<String, IReferencedArticleTaxonomyModel>       referencedTaxonomies;
  protected IGlobalPermission                                  globalPermission;
  protected Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships;
  protected List<String>                                       referencedLifeCycleStatusTags;
  protected Map<String, String>                                referencedRelationshipsMapping;
  protected Map<String, List<String>>                          taxonomiesHierarchies;
  protected Boolean                                            canCreateNature  = true;
  protected IReferencedTemplatePermissionModel                 referencedPermissions;
  protected Map<String, ITaskModel>                            referencedTasks;
  protected Set<String>                                        personalTaskIds  = new HashSet<>();
  protected IXRayConfigDetailsModel                            xRayConfigDetails;
  protected Map<String, IGetLanguagesInfoModel>                referencedLanguages;
  protected Integer                                               numberOfVersionsToMaintainForParent;

  @Override
  public IReferencedTemplatePermissionModel getReferencedPermissions()
  {
    return referencedPermissions;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedTemplatePermissionModel.class)
  public void setReferencedPermissions(IReferencedTemplatePermissionModel referencedPermissions)
  {
    this.referencedPermissions = referencedPermissions;
  }
  
  @Override
  public List<String> getReferencedLifeCycleStatusTags()
  {
    if (referencedLifeCycleStatusTags == null) {
      referencedLifeCycleStatusTags = new ArrayList<>();
    }
    return referencedLifeCycleStatusTags;
  }
  
  @Override
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags)
  {
    this.referencedLifeCycleStatusTags = referencedLifeCycleStatusTags;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return klasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections()
  {
    return referencedSections;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedPropertyCollectionModel.class)
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections)
  {
    this.referencedSections = referencedSections;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    this.referencedElements = referencedElements;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  @JsonDeserialize(contentAs = Role.class)
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public List<IDataRuleModel> getReferencedDataRules()
  {
    if (referencedDataRules == null) {
      referencedDataRules = new ArrayList<>();
    }
    return referencedDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public IReferencedRolePermissionModel getReferencedPermission()
  {
    return referencedPermission;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedRolePermissionModel.class)
  public void setReferencedPermission(IReferencedRolePermissionModel referencedPermissions)
  {
    this.referencedPermission = referencedPermissions;
  }
  
  @Override
  public Integer getNumberOfVersionsToMaintain()
  {
    return numberOfVersionsToMaintain;
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain)
  {
    this.numberOfVersionsToMaintain = numberOfVersionsToMaintain;
  }
  
  @Override
  public Map<String, Set<String>> getNotificationSettings()
  {
    return notificationSettings;
  }
  
  @Override
  public void setNotificationSettings(Map<String, Set<String>> notificationSettings)
  {
    this.notificationSettings = notificationSettings;
  }
  
  @Override
  public Map<String, IRelationship> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = Relationship.class)
  public void setReferencedRelationships(Map<String, IRelationship> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    
    return referencedVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedContextModel.class)
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    this.referencedVariantContexts = referencedVariantContexts;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
  }
  
  @Override
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetReferencedNatureRelationshipModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, String> getReferencedRelationshipsMapping()
  {
    return referencedRelationshipsMapping;
  }
  
  @Override
  public void setReferencedRelationshipsMapping(Map<String, String> referencedRelationshipsMapping)
  {
    this.referencedRelationshipsMapping = referencedRelationshipsMapping;
  }
  
  @Override
  public Map<String, List<String>> getTaxonomiesHierarchies()
  {
    return taxonomiesHierarchies;
  }
  
  @Override
  public void setTaxonomiesHierarchies(Map<String, List<String>> taxonomiesHierarchies)
  {
    this.taxonomiesHierarchies = taxonomiesHierarchies;
  }
  
  @Override
  public Boolean getCanCreateNature()
  {
    return canCreateNature;
  }
  
  @Override
  public void setCanCreateNature(Boolean canCreateNature)
  {
    this.canCreateNature = canCreateNature;
  }
  
  @Override
  public ITemplate getReferencedTemplate()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setReferencedTemplate(ITemplate referencedTemplate)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<IConfigEntityInformationModel> getReferencedTemplates()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setReferencedTemplates(List<IConfigEntityInformationModel> referencedTemplates)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getLanguageContextIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLanguageContextIds(List<String> languageContextIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getVariantContextIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVariantContextIds(List<String> variantContextIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getPromotionVersionContextIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPromotionVersionContextIds(List<String> promotionVersionContextIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Map<String, ITaskModel> getReferencedTasks()
  {
    return referencedTasks;
  }
  
  @JsonDeserialize(contentAs = TaskModel.class)
  @Override
  public void setReferencedTasks(Map<String, ITaskModel> referencedTasks)
  {
    this.referencedTasks = referencedTasks;
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Set<String> getPersonalTaskIds()
  {
    return personalTaskIds;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    this.personalTaskIds = personalTaskIds;
  }
  
  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  @Override
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
  
  @Override
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages()
  {
    return referencedLanguages;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetLanguagesInfoModel.class)
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages)
  {
    this.referencedLanguages = referencedLanguages;
  }
  
  @Override
  public Integer getNumberOfVersionsToMaintainForParent()
  {
    return numberOfVersionsToMaintainForParent;
  }
  
  @Override
  public void setNumberOfVersionsToMaintainForParent(Integer numberOfVersionsToMaintainForParent)
  {
    this.numberOfVersionsToMaintainForParent = numberOfVersionsToMaintainForParent;
  }
}
