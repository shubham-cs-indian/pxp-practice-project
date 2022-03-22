package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IGetConfigDetailsForCreateVariantModel extends IModel {
  
  public static final String REFERENCED_PROPERTY_COLLECTIONS                      = "referencedPropertyCollections";
  public static final String REFERENCED_VARIANT_CONTEXTS                          = "referencedVariantContexts";
  public static final String REFRENCED_KLASSES                                    = "referencedKlasses";
  public static final String REFERENCED_ELEMENTS                                  = "referencedElements";
  public static final String REFERENCED_ATTRIBUTES                                = "referencedAttributes";
  public static final String REFERENCED_TAGS                                      = "referencedTags";
  public static final String REFERENCED_ROLES                                     = "referencedRoles";
  public static final String EVENT_IDS                                            = "eventIds";
  public static final String REFERENCED_LIFECYCLE_STATUS_TAGS                     = "referencedLifeCycleStatusTags";
  public static final String REFERENCED_DATA_RULES                                = "referencedDataRules";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN                       = "numberOfVersionsToMaintain";
  public static final String REFERENCED_TAXONOMIES                                = "referencedTaxonomies";
  public static final String TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  
  // key:propertyCollectionId
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
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
  
  public List<String> getReferencedLifeCycleStatusTags();
  
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags);
  
  public List<String> getEventIds();
  
  public void setEventIds(List<String> eventIds);
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
  
  // key:roleId
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
}
