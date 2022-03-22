package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassDetailsForBulkPropagationModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;

import java.util.List;
import java.util.Map;

public interface IConfigDetailsForBulkPropagationResponseModel extends IModel {
  
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  public static final String ENTITY_DETAILS                      = "entityDetails";
  public static final String REFERENCED_DATA_RULES               = "referencedDataRules";
  public static final String REFERENCED_ATTRIBUTES               = "referencedAttributes";
  public static final String REFERENCED_NATURE_RELATIONSHIPS     = "referencedNatureRelationships";
  public static final String REFERENCED_ELEMENTS                 = "referencedElements";
  public static final String DEFAULT_VALUES_DIFF                 = "defaultValuesDiff";
  public static final String REFERENCED_TAXONOMIES               = "referencedTaxonomies";
  public static final String TYPE_ID_IDENTIFIER_ATTRIBUTE_IDS    = "typeIdIdentifierAttributeIds";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN      = "numberOfVersionsToMaintain";
  public static final String REFERENCED_TAGS                     = "referencedTags";
  public static final String VERSIONABLE_ATTRIBUTES              = "versionableAttributes";
  public static final String VERSIONABLE_TAGS                    = "versionableTags";
  public static final String MANDATORY_ATTRIBUTE_IDS             = "mandatoryAttributeIds";
  public static final String MANDATORY_TAG_IDS                   = "mandatoryTagIds";
  public static final String SHOULD_ATTRIBUTE_IDS                = "shouldAttributeIds";
  public static final String SHOULD_TAG_IDS                      = "shouldTagIds";
  public static final String IS_LANGUAGE_HIERARCHY_PRESENT       = "isLanguageHierarchyPresent";
  public static final String REFERENCED_PERMISSIONS              = "referencedPermissions";
  public static final String REFERENCED_PROPERTY_COLLECTIONS     = "referencedPropertyCollections";

  // key:relationshipId
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  // key:klassId
  public Map<String, IKlassDetailsForBulkPropagationModel> getEntityDetails();
  
  public void setEntityDetails(Map<String, IKlassDetailsForBulkPropagationModel> klassDetails);
  
  // key:attributeId
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  // key:dataRuleId
  public Map<String, IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(Map<String, IDataRuleModel> referencedDataRules);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  // key:propertyId[attributeId, tagId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public List<IDefaultValueChangeModel> getDefaultValuesDiff();
  
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff);
  
  // key:taxonomyId value:parentIds list
  public Map<String, Object> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, Object> referencedTaxonomies);
  
  // key:klassId/taxonomyId
  public Map<String, List<String>> getTypeIdIdentifierAttributeIds();
  
  public void setTypeIdIdentifierAttributeIds(
      Map<String, List<String>> typeIdIdentifierAttributeIds);
  
  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
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
  
  public IReferencedTemplatePermissionModel getReferencedPermissions();
  
  public void setReferencedPermissions(IReferencedTemplatePermissionModel referencedPermissions);
  
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections);
  
  
}
