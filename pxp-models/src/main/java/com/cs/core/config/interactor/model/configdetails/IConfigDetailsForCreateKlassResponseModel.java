package com.cs.core.config.interactor.model.configdetails;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

public interface IConfigDetailsForCreateKlassResponseModel extends IModel {
  
  public static final String REFERENCED_RELATIONSHIP_PROPERTIES                   = "referencedRelationshipProperties";
  public static final String EVENT_IDS                                            = "eventIds";
  public static final String REFERENCED_DATA_RULES                                = "referencedDataRules";
  public static final String REFERENCED_ATTRIBUTES                                = "referencedAttributes";
  public static final String REFERENCED_TAGS                                      = "referencedTags";
  public static final String REFERENCED_ELEMENTS                                  = "referencedElements";
  public static final String ELEMENTS_CONFLICTING_VALUES                          = "elementsConflictingValues";
  public static final String TYPEID_IDENTIFIER_ATTRIBUTEIDS                       = "typeIdIdentifierAttributeIds";
  public static final String REFERENCED_KLASSES                                   = "referencedKlasses";
  public static final String REFERENCED_NATURE_RELATIONSHIP                       = "referencedNatureRelationship";
  // for variant create task.
  public static final String VARIANT_CONTEXT_ID                                   = "variantContextId";
  public static final String REFERENCED_VARIANT_CONTEXTS                          = "referencedVariantContexts";
  public static final String REFERENCED_TAXONOMIES                                = "referencedTaxonomies";
  public static final String TAXONOMY_HIERARCHIES                                 = "taxonomiesHierarchies";
  
  public static final String TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE = "technicalImageVariantContextWithAutoCreateEnable";
  public static final String LINKED_VARIANTS_DIFF                                 = "linkedVariantDiff";
  public static final String TAXONOMY_IDS_TO_TRANSFER                             = "taxonomyIdsToTransfer";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN                       = "numberOfVersionToMaintain";
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public List<String> getEventIds();
  
  public void setEventIds(List<String> eventIds);
  
  public Map<String, List<IElementConflictingValuesModel>> getElementsConflictingValues();
  
  public void setElementsConflictingValues(
      Map<String, List<IElementConflictingValuesModel>> elementsConflictingValues);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  
  public Map<String, IReferencedNatureRelationshipModel> getReferencedNatureRelationship();
  
  public void setReferencedNatureRelationship(
      Map<String, IReferencedNatureRelationshipModel> referencedKlass);
  
  public String getVariantContextId();
  
  public void setVariantContextId(String variantContextId);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, List<String>> getTaxonomiesHierarchies();
  
  public void setTaxonomiesHierarchies(Map<String, List<String>> taxonomiesHierarchies);
  
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable();
  
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable);
  
  public List<String> getTaxonomyIdsToTransfer();
  
  public void setTaxonomyIdsToTransfer(List<String> taxonomyIdsToTransfer);
  
  public Integer getNumberOfVersionToMaintain();
  
  public void setNumberOfVersionToMaintain(Integer numberOfVersionToMaintain);
}
