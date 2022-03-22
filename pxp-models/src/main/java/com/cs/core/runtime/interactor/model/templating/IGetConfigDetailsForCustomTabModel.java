package com.cs.core.runtime.interactor.model.templating;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.model.klass.IConflictingValueModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

public interface IGetConfigDetailsForCustomTabModel extends IGetConfigDetailsModel {
  
  public static final String CONFLICTING_VALUES                  = "conflictingValues";
  public static final String REFERENCED_NATURE_RELATIONSHIPS     = "referencedNatureRelationships";
  public static final String REFERENCED_RELATIONSHIPS_MAPPING    = "referencedRelationshipsMapping";
  public static final String REFERENCED_PROPERTY_COLLECTIONS     = "referencedPropertyCollections";
  public static final String REFERENCED_VARIANT_CONTEXTS         = "referencedVariantContexts";
  public static final String CONTEXT_ID                          = "contextId";
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  public static final String REFERENCED_RELATIONSHIPS            = "referencedRelationships";
  public static final String RELATIONSHIP_REFERENCED_ELEMENTS    = "relationshipReferencedElements";
  public static final String ELEMENTS_CONFLICTING_VALUES         = "elementsConflictingValues";
  public static final String KLASS_IDS_TO_ADD                    = "klassIdsToAdd";
  public static final String TAXONOMY_IDS_TO_ADD                 = "taxonomyIdsToAdd";
  public static final String VERSIONABLE_ATTRIBUTES              = "versionableAttributes";
  public static final String VERSIONABLE_TAGS                    = "versionableTags";
  public static final String MANDATORY_ATTRIBUTE_IDS             = "mandatoryAttributeIds";
  public static final String MANDATORY_TAG_IDS                   = "mandatoryTagIds";
  public static final String SHOULD_ATTRIBUTE_IDS                = "shouldAttributeIds";
  public static final String SHOULD_TAG_IDS                      = "shouldTagIds";
  public static final String IS_LANGUAGE_HIERARCHY_PRESENT       = "isLanguageHierarchyPresent";
  public static final String ROLE_ID_OF_CURRENT_USER             = "roleIdOfCurrentUser";
  public static final String SIDE2_LINKED_VARIANT_KR_IDS         = "side2LinkedVariantKrIds";
  public static final String LINKED_VARIANT_CODES                = "linkedVariantCodes";
  
  Map<String, IReferencedRelationshipModel> getReferencedRelationships();
  
  void setReferencedRelationships(
      Map<String, IReferencedRelationshipModel> referencedRelationships);
  
  IReferencedContextModel getReferencedVariantContexts();
  
  void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  Map<String, IConflictingValueModel> getConflictingValues();
  
  void setConflictingValues(Map<String, IConflictingValueModel> conflictingValues);
  
  Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  Map<String, String> getReferencedRelationshipsMapping();
  
  void setReferencedRelationshipsMapping(Map<String, String> referencedRelationshipsMapping);
  
  Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections();
  
  void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections);
  
  String getContextId();
  
  void setContextId(String contextId);
  
  Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  Map<String, IReferencedSectionElementModel> getRelationshipReferencedElements();
  
  void setRelationshipReferencedElements(
      Map<String, IReferencedSectionElementModel> relationshipReferencedElements);
  
  public Map<String, List<IElementConflictingValuesModel>> getElementsConflictingValues();
  
  public void setElementsConflictingValues(
      Map<String, List<IElementConflictingValuesModel>> elementsConflictingValues);
  
  public List<String> getKlassIdsToAdd();
  
  public void setKlassIdsToAdd(List<String> klassIdsToAdd);
  
  public List<String> getTaxonomyIdsToAdd();
  
  public void setTaxonomyIdsToAdd(List<String> taxonomyIdsToAdd);
  
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
  
  public String getRoleIdOfCurrentUser();
  
  public void setRoleIdOfCurrentUser(String roleIdsOfCurrentUser);
  
  public Boolean getIsLanguageHierarchyPresent();
  
  public void setIsLanguageHierarchyPresent(Boolean isLanguageHierarchyPresent);
  
  public List<String> getSide2LinkedVariantKrIds();
  
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds);
  
  public List<String> getLinkedVariantCodes();
  
  public void setLinkedVariantCodes(List<String> linkedVariantCodes);
}
