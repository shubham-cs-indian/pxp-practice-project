package com.cs.core.runtime.interactor.model.templating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ConflictingValueModel;
import com.cs.core.config.interactor.model.klass.IConflictingValueModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.relationship.ReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.datarule.ElementConflictingValuesCustomDeserializer;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ReferencedRelationshipPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetConfigDetailsForCustomTabModel extends AbstractGetConfigDetailsModel
    implements IGetConfigDetailsForCustomTabModel {
  
  private static final long                                     serialVersionUID           = 1L;
  
  protected Map<String, IConflictingValueModel>                 conflictingValues;
  protected Map<String, IGetReferencedNatureRelationshipModel>  referencedNatureRelationships;
  protected Map<String, String>                                 referencedRelationshipsMapping;
  protected String                                              contextId;
  protected Map<String, IReferencedPropertyCollectionModel>     referencedSections;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  protected Map<String, IReferencedSectionElementModel>         relationshipReferencedElements;
  protected Map<String, IReferencedRelationshipModel>           referencedRelationships;
  protected Map<String, List<IElementConflictingValuesModel>>   elementsConflictingValues;
  protected List<String>                                        klassIdsToAdd              = new ArrayList<>();
  protected List<String>                                        taxonomyIdsToAdd           = new ArrayList<>();
  protected List<String>                                        versionableAttributes;
  protected List<String>                                        versionableTags;
  protected List<String>                                        mandatoryAttributeIds;
  protected List<String>                                        shouldAttributeIds;
  protected List<String>                                        mandatoryTagIds;
  protected List<String>                                        shouldTagIds;
  protected String                                              roleIdOfCurrentUser;
  protected Boolean                                             isLanguageHierarchyPresent = false;
  protected List<String>                                        side2LinkedVariantKrIds;
  protected List<String>                                        linkedVariantCodes;
  
  @Override
  public Map<String, IReferencedRelationshipModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedRelationshipModel.class)
  public void setReferencedRelationships(
      Map<String, IReferencedRelationshipModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public Map<String, IConflictingValueModel> getConflictingValues()
  {
    return conflictingValues;
  }
  
  @JsonDeserialize(contentAs = ConflictingValueModel.class)
  @Override
  public void setConflictingValues(Map<String, IConflictingValueModel> conflictingValues)
  {
    this.conflictingValues = conflictingValues;
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
  public String getContextId()
  {
    return contextId;
  }
  
  @Override
  public void setContextId(String contextId)
  {
    this.contextId = contextId;
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
  public Map<String, IReferencedSectionElementModel> getRelationshipReferencedElements()
  {
    return relationshipReferencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setRelationshipReferencedElements(
      Map<String, IReferencedSectionElementModel> relationshipReferencedElements)
  {
    this.relationshipReferencedElements = relationshipReferencedElements;
  }
  
  @Override
  public Map<String, List<IElementConflictingValuesModel>> getElementsConflictingValues()
  {
    return elementsConflictingValues;
  }
  
  @Override
  @JsonDeserialize(contentUsing = ElementConflictingValuesCustomDeserializer.class)
  public void setElementsConflictingValues(
      Map<String, List<IElementConflictingValuesModel>> elementsConflictingValues)
  {
    this.elementsConflictingValues = elementsConflictingValues;
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
  public List<String> getVersionableAttributes()
  {
    if (versionableAttributes == null) {
      versionableAttributes = new ArrayList<>();
    }
    return versionableAttributes;
  }
  
  @Override
  public void setVersionableAttributes(List<String> versionableAttributes)
  {
    this.versionableAttributes = versionableAttributes;
  }
  
  @Override
  public List<String> getVersionableTags()
  {
    if (versionableTags == null) {
      versionableTags = new ArrayList<>();
    }
    return versionableTags;
  }
  
  @Override
  public void setVersionableTags(List<String> versionableTags)
  {
    this.versionableTags = versionableTags;
  }
  
  @Override
  public List<String> getMandatoryAttributeIds()
  {
    if (mandatoryAttributeIds == null) {
      mandatoryAttributeIds = new ArrayList<>();
    }
    return mandatoryAttributeIds;
  }
  
  @Override
  public void setMandatoryAttributeIds(List<String> mandatoryAttributeIds)
  {
    this.mandatoryAttributeIds = mandatoryAttributeIds;
  }
  
  @Override
  public List<String> getMandatoryTagIds()
  {
    if (mandatoryTagIds == null) {
      mandatoryTagIds = new ArrayList<>();
    }
    return mandatoryTagIds;
  }
  
  @Override
  public void setMandatoryTagIds(List<String> mandatoryTagIds)
  {
    this.mandatoryTagIds = mandatoryTagIds;
  }
  
  @Override
  public List<String> getShouldAttributeIds()
  {
    if (shouldAttributeIds == null) {
      shouldAttributeIds = new ArrayList<>();
    }
    return shouldAttributeIds;
  }
  
  @Override
  public void setShouldAttributeIds(List<String> shouldAttributeIds)
  {
    this.shouldAttributeIds = shouldAttributeIds;
  }
  
  @Override
  public List<String> getShouldTagIds()
  {
    if (shouldTagIds == null) {
      shouldTagIds = new ArrayList<>();
    }
    return shouldTagIds;
  }
  
  @Override
  public void setShouldTagIds(List<String> shouldTagIds)
  {
    this.shouldTagIds = shouldTagIds;
  }
  
  @Override
  public String getRoleIdOfCurrentUser()
  {
    return roleIdOfCurrentUser;
  }
  
  @Override
  public void setRoleIdOfCurrentUser(String roleIdOfCurrentUser)
  {
    this.roleIdOfCurrentUser = roleIdOfCurrentUser;
  }
  
  public Boolean getIsLanguageHierarchyPresent()
  {
    return isLanguageHierarchyPresent;
  }
  
  @Override
  public void setIsLanguageHierarchyPresent(Boolean isLanguageHierarchyPresent)
  {
    this.isLanguageHierarchyPresent = isLanguageHierarchyPresent;
  }
  
  @Override
  public List<String> getSide2LinkedVariantKrIds()
  {
    if (side2LinkedVariantKrIds == null) {
      side2LinkedVariantKrIds = new ArrayList<String>();
    }
    return side2LinkedVariantKrIds;
  }
  
  @Override
  public void setSide2LinkedVariantKrIds(List<String> side2LinkedVariantKrIds)
  {
    this.side2LinkedVariantKrIds = side2LinkedVariantKrIds;
  }

  @Override
  public List<String> getLinkedVariantCodes()
  {
    if(linkedVariantCodes == null) {
      linkedVariantCodes = new ArrayList<>();
    }
    return linkedVariantCodes;
  }

  @Override
  public void setLinkedVariantCodes(List<String> linkedVariantCodes)
  {
    this.linkedVariantCodes = linkedVariantCodes;
  }
  
}
