package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.language.GetLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForSwitchTypeResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ReferencedRelationshipPropertiesModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForSwitchTypeResponseModel
    implements IConfigDetailsForSwitchTypeResponseModel {
  
  private static final long                                     serialVersionUID = 1L;
  protected Map<String, List<String>>                           taxonomiesHierarchies;
  protected Map<String, List<IReferencedSectionElementModel>>   referencedElementsForSwitchType;
  protected Map<String, ITag>                                   referencedTags;
  protected Map<String, IAttribute>                             referencedAttributes;
  protected Integer                                             numberOfVersionsToMaintain;
  protected List<IDataRuleModel>                                referencedDataRules;
  protected Map<String, IRole>                                  referencedRoles;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  protected Map<String, ITaskModel>                             referencedTasks;
  protected Set<String>                                         personalTaskIds  = new HashSet<>();
  protected String                                              minorTaxonomyIdToRemove;
  protected Map<String, IReferencedArticleTaxonomyModel>        referencedTaxonomies;
  protected List<String>                                        klassIdsToAdd    = new ArrayList<>();
  protected List<String>                                        taxonomyIdsToAdd = new ArrayList<>();
  protected Map<String, IGetLanguagesInfoModel>                 referencedLanguages;
  protected Integer                                               numberOfVersionsToMaintainForParent;

  @Override
  public Map<String, List<String>> getTaxonomiesHierarchies()
  {
    if (taxonomiesHierarchies == null) {
      taxonomiesHierarchies = new HashMap<>();
    }
    return taxonomiesHierarchies;
  }
  
  @Override
  public void setTaxonomiesHierarchies(Map<String, List<String>> taxonomiesHierarchies)
  {
    this.taxonomiesHierarchies = taxonomiesHierarchies;
  }
  
  @Override
  public Map<String, List<IReferencedSectionElementModel>> getReferencedElementsForSwitchType()
  {
    if (referencedElementsForSwitchType == null) {
      referencedElementsForSwitchType = new HashMap<>();
    }
    return referencedElementsForSwitchType;
  }
  
  @Override
  @JsonDeserialize(contentUsing = ReferencedElementListCustomDeserializer.class)
  public void setReferencedElementsForSwitchType(
      Map<String, List<IReferencedSectionElementModel>> referencedElementsForSwitchType)
  {
    this.referencedElementsForSwitchType = referencedElementsForSwitchType;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    if (referencedTags == null) {
      referencedTags = new HashMap<>();
    }
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    if (referencedAttributes == null) {
      referencedAttributes = new HashMap<>();
    }
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements)
  {
    this.referencedAttributes = referencedElements;
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
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties()
  {
    return referencedRelationshipProperties;
  }
  
  @JsonDeserialize(contentAs = ReferencedRelationshipPropertiesModel.class)
  @Override
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties)
  {
    this.referencedRelationshipProperties = referencedRelationshipProperties;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  /* Ignored fields */
  
  @JsonIgnore
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getReferencedLifeCycleStatusTags()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public ITemplate getReferencedTemplate()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setReferencedTemplate(ITemplate referencedTemplate)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<IConfigEntityInformationModel> getReferencedTemplates()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setReferencedTemplates(List<IConfigEntityInformationModel> referencedTemplates)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getLanguageContextIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLanguageContextIds(List<String> languageContextIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getVariantContextIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVariantContextIds(List<String> variantContextIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public List<String> getPromotionVersionContextIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setPromotionVersionContextIds(List<String> promotionVersionContextIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public IReferencedTemplatePermissionModel getReferencedPermissions()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setReferencedPermissions(IReferencedTemplatePermissionModel referencedPermissions)
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
  public String getMinorTaxonomyIdToRemove()
  {
    return minorTaxonomyIdToRemove;
  }
  
  @Override
  public void setMinorTaxonomyIdToRemove(String minorTaxonomyIdToRemove)
  {
    this.minorTaxonomyIdToRemove = minorTaxonomyIdToRemove;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getKlassIdsToAdd()
  {
    return klassIdsToAdd;
  }
  
  @Override
  public void setKlassIdsToAdd(List<String> klassIdsToAdd)
  {
    this.klassIdsToAdd = klassIdsToAdd;
  }
  
  @Override
  public List<String> getTaxonomyIdsToAdd()
  {
    return taxonomyIdsToAdd;
  }
  
  @Override
  public void setTaxonomyIdsToAdd(List<String> taxonomyIdsToAdd)
  {
    this.taxonomyIdsToAdd = taxonomyIdsToAdd;
  }
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    // TODO Auto-generated method stub
    
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
