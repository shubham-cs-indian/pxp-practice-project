package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.role.Role;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.Template;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.language.GetLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.task.TaskModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.template.ConfigEntityTypeInformationModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.ReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.*;

public abstract class AbstractGetConfigDetailsModel implements IGetConfigDetailsModel {
  
  private static final long                                       serialVersionUID = 1L;
  
  protected Map<String, IReferencedKlassDetailStrategyModel>      referencedKlasses;
  protected Map<String, IReferencedSectionElementModel>           referencedElements;
  protected Map<String, IAttribute>                               referencedAttributes;
  protected Map<String, ITag>                                     referencedTags;
  protected Integer                                               numberOfVersionsToMaintain;
  protected Map<String, IReferencedArticleTaxonomyModel>          referencedTaxonomies;
  protected Map<String, List<String>>                             taxonomiesHierarchies;
  protected ITemplate                                             referencedTemplate;
  protected List<IConfigEntityInformationModel>                   referencedTemplates;
  protected List<String>                                          referencedLifeCycleStatusTags;
  protected List<IDataRuleModel>                                  referencedDataRules;
  protected List<String>                                          languageContextIds;
  protected List<String>                                          variantContextIds;
  protected List<String>                                          promotionVersionContextIds;
  protected IReferencedTemplatePermissionModel                    referencedPermissions;
  protected Map<String, ITaskModel>                               referencedTasks;
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithIsAutoCreate;
  protected Set<String>                                           personalTaskIds;
  protected IReferencedContextModel                               referencedVariantContexts;
  protected Map<String, IRole>                                    referencedRoles;
  protected Map<String, List<String>>                             typeIdIdentifierAttributeIds;
  protected Map<String, IGetLanguagesInfoModel>                   referencedLanguages;
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
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses)
  {
    this.referencedKlasses = klasses;
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
  public ITemplate getReferencedTemplate()
  {
    return referencedTemplate;
  }
  
  @Override
  @JsonDeserialize(as = Template.class)
  public void setReferencedTemplate(ITemplate referencedTemplate)
  {
    this.referencedTemplate = referencedTemplate;
  }
  
  @Override
  public List<IConfigEntityInformationModel> getReferencedTemplates()
  {
    return referencedTemplates;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityTypeInformationModel.class)
  public void setReferencedTemplates(List<IConfigEntityInformationModel> referencedTemplates)
  {
    this.referencedTemplates = referencedTemplates;
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
  public List<String> getLanguageContextIds()
  {
    return languageContextIds;
  }
  
  @Override
  public void setLanguageContextIds(List<String> languageContextIds)
  {
    this.languageContextIds = languageContextIds;
  }
  
  @Override
  public List<String> getVariantContextIds()
  {
    return variantContextIds;
  }
  
  @Override
  public void setVariantContextIds(List<String> variantContextIds)
  {
    this.variantContextIds = variantContextIds;
  }
  
  @Override
  public List<String> getPromotionVersionContextIds()
  {
    return promotionVersionContextIds;
  }
  
  @Override
  public void setPromotionVersionContextIds(List<String> promotionVersionContextIds)
  {
    this.promotionVersionContextIds = promotionVersionContextIds;
  }
  
  @Override
  public Map<String, ITaskModel> getReferencedTasks()
  {
    if (referencedTasks == null) {
      referencedTasks = new HashMap<>();
    }
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
    return technicalImageVariantWithIsAutoCreate;
  }
  
  @Override
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithIsAutoCreate)
  {
    this.technicalImageVariantWithIsAutoCreate = technicalImageVariantWithIsAutoCreate;
  }
  
  @Override
  public Set<String> getPersonalTaskIds()
  {
    if (personalTaskIds == null) {
      personalTaskIds = new HashSet<>();
    }
    return personalTaskIds;
  }
  
  @Override
  public void setPersonalTaskIds(Set<String> personalTaskIds)
  {
    this.personalTaskIds = personalTaskIds;
  }
  
  @Override
  public IReferencedContextModel getReferencedVariantContexts()
  {
    if(referencedVariantContexts == null) {
      return new ReferencedContextModel();
    }
    return referencedVariantContexts;
  }
  
  @Override
  @JsonDeserialize(as = ReferencedContextModel.class)
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts)
  {
    this.referencedVariantContexts = referencedVariantContexts;
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
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds()
  {
    return typeIdIdentifierAttributeIds;
  }
  
  @Override
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds)
  {
    this.typeIdIdentifierAttributeIds = typeIdIdentifierAttributeIds;
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
