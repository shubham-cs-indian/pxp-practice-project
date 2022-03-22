package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.versionrollback.IPropertyCouplingInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

import java.util.List;
import java.util.Map;

public interface IGetConfigDetailsForVersionRollbackModel extends IModel {
  
  String REFERENCED_KLASSES                                      = "referencedKlasses";
  String REFERENCED_ELEMENTS                                     = "referencedElements";
  String REFERENCED_ATTRIBUTES                                   = "referencedAttributes";
  String REFERENCED_TAGS                                         = "referencedTags";
  String NUMBER_OF_VERSIONS_TO_MAINTAIN                          = "numberOfVersionsToMaintain";
  String REFERENCED_RELATIONSHIPS                                = "referencedRelationships";
  String REFERENCED_VARIANT_CONTEXTS                             = "referencedVariantContexts";
  String REFERENCED_TAXONOMIES                                   = "referencedTaxonomies";
  String REFERENCED_LIFECYCLE_STATUS_TAGS                        = "referencedLifeCycleStatusTags";
  String REFERENCED_NATURE_RELATIONSHIPS                         = "referencedNatureRelationships";
  String REFERENCED_RELATIONSHIPS_PROPERTIES                     = "referencedRelationshipProperties";
  String REFERENCED_DATA_RULES                                   = "referencedDataRules";
  String TYPEID_IDENTIFIER_ATTRIBUTEIDS                          = "typeIdIdentifierAttributeIds";
  String TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  String REFERENCED_PROPERTY_COUPLING_INFORMATION                = "referencedPropertyCouplingInformation";
  String REFERENCED_LANGUAGES                                    = "referencedLanguages";
  String VERSIONABLE_ATTRIBUTES                                  = "versionableAttributes";
  String VERSIONABLE_TAGS                                        = "versionableTags";
  String MANDATORY_ATTRIBUTE_IDS                                 = "mandatoryAttributeIds";
  String MANDATORY_TAG_IDS                                       = "mandatoryTagIds";
  String SHOULD_ATTRIBUTE_IDS                                    = "shouldAttributeIds";
  String SHOULD_TAG_IDS                                          = "shouldTagIds";
  String IS_LANGUAGE_HIERARCHY_PRESENT                           = "isLanguageHierarchyPresent";
  
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
  
  public Map<String, IReferencedRelationshipModel> getReferencedRelationships();
  
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipModel> referencedRelationships);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public List<String> getReferencedLifeCycleStatusTags();
  
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  // key : propertyId
  public Map<String, Map<String, IPropertyCouplingInformationModel>> getReferencedPropertyCouplingInformation();
  
  public void setReferencedPropertyCouplingInformation(
      Map<String, Map<String, IPropertyCouplingInformationModel>> referencedPropertyCouplingInformation);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantWithIsAutoCreate();
  
  public void setTechnicalImageVariantWithIsAutoCreate(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantWithIsAutoCreate);
  
  public Map<String, IGetLanguagesInfoModel> getReferencedLanguages();
  
  public void setReferencedLanguages(Map<String, IGetLanguagesInfoModel> referencedLanguages);
  
  public List<String> getVersionableAttributes();
  
  public void setVersionableAttributes(List<String> versionableAttributes);
  
  public List<String> getVersionableTags();
  
  public void setVersionableTags(List<String> versionableTags);
  
  public List<String> getMandatoryAttributeIds();
  
  public void setMandatoryAttributeIds(List<String> mandatoryAttributeIds);
  
  public List<String> getMandatoryTagIds();
  
  public void setMandatoryTagIds(List<String> mandatoryTagIds);
  
  public List<String> getShouldAttributeIds();
  
  public void setShouldAttributeIds(List<String> shouldAttributeIds);
  
  public List<String> getShouldTagIds();
  
  public void setShouldTagIds(List<String> shouldTagIds);
  
  public Boolean getIsLanguageHierarchyPresent();
  
  public void setIsLanguageHierarchyPresent(Boolean isLanguageHierarchyPresent);
}
