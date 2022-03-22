package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.task.ITaskModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetConfigDetailsModel extends IModel {
  
  public static final String REFERENCED_KLASSES                                      = "referencedKlasses";
  public static final String REFERENCED_ELEMENTS                                     = "referencedElements";
  public static final String REFERENCED_ATTRIBUTES                                   = "referencedAttributes";
  public static final String REFERENCED_TAGS                                         = "referencedTags";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN                          = "numberOfVersionsToMaintain";
  public static final String REFERENCED_TAXONOMIES                                   = "referencedTaxonomies";
  public static final String TAXONOMY_HIERARCHIES                                    = "taxonomiesHierarchies";
  public static final String REFERENCED_TEMPLATE                                     = "referencedTemplate";
  public static final String REFERENCED_TEMPLATES                                    = "referencedTemplates";
  public static final String REFERENCED_LIFECYCLE_STATUS_TAGS                        = "referencedLifeCycleStatusTags";
  public static final String REFERENCED_DATA_RULES                                   = "referencedDataRules";
  public static final String LANGAUGE_CONTEXT_IDS                                    = "languageContextIds";
  public static final String VARIANT_CONTEXT_IDS                                     = "variantContextIds";
  public static final String PROMOTION_VERSION_CONTEXT_IDS                           = "promotionVersionContextIds";
  public static final String REFERENCED_PERMISSIONS                                  = "referencedPermissions";
  public static final String REFERENCED_TASKS                                        = "referencedTasks";
  public static final String TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  public static final String PERSONAL_TASK_IDS                                       = "personalTaskIds";
  public static final String REFERENCED_VARIANT_CONTEXTS                             = "referencedVariantContexts";
  public static final String REFERENCED_ROLES                                        = "referencedRoles";
  public static final String TYPEID_IDENTIFIER_ATTRIBUTEIDS                          = "typeIdIdentifierAttributeIds";
  public static final String DERIVABLE_PROPERTIES                                    = "derivableProperties";
  public static final String REFERENCED_LANGUAGES                                    = "referencedLanguages";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN_FOR_PARENT               = "numberOfVersionsToMaintainForParent";

  // key:klassId
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses);
  
  // key:propertyId[attributeId, tagId, roleId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  // key:attributeId
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public List<String> getReferencedLifeCycleStatusTags();
  
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags);
  
  public Map<String, List<String>> getTaxonomiesHierarchies();
  
  public void setTaxonomiesHierarchies(Map<String, List<String>> taxonomiesHierarchies);
  
  public ITemplate getReferencedTemplate();
  
  public void setReferencedTemplate(ITemplate referencedTemplate);
  
  public List<IConfigEntityInformationModel> getReferencedTemplates();
  
  public void setReferencedTemplates(List<IConfigEntityInformationModel> referencedTemplates);
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  public List<String> getLanguageContextIds();
  
  public void setLanguageContextIds(List<String> languageContextIds);
  
  public List<String> getVariantContextIds();
  
  public void setVariantContextIds(List<String> variantContextIds);
  
  public List<String> getPromotionVersionContextIds();
  
  public void setPromotionVersionContextIds(List<String> promotionVersionContextIds);
  
  public IReferencedTemplatePermissionModel getReferencedPermissions();
  
  public void setReferencedPermissions(IReferencedTemplatePermissionModel referencedPermissions);
  
  public Map<String, ITaskModel> getReferencedTasks();
  
  public void setReferencedTasks(Map<String, ITaskModel> referencedTasks);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
  
  public Set<String> getPersonalTaskIds();
  
  public void setPersonalTaskIds(Set<String> personalTaskIds);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  // key:roleId
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages);
  
  public Integer getNumberOfVersionsToMaintainForParent();
  
  public void setNumberOfVersionsToMaintainForParent(Integer numberOfVersionsToMaintainForParent);
}
