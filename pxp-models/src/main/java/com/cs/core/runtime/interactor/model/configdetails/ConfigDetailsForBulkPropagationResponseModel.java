package com.cs.core.runtime.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.globalpermissions.ReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.ReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.tag.DataRuleModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.KlassDetailsForBulkPropagationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassDetailsForBulkPropagationModel;
import com.cs.core.runtime.interactor.model.relationship.GetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.relationship.ReferencedRelationshipPropertiesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForBulkPropagationResponseModel
    implements IConfigDetailsForBulkPropagationResponseModel {
  
  private static final long                                     serialVersionUID           = 1L;
  protected Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties;
  protected Map<String, IKlassDetailsForBulkPropagationModel>   entityDetails;
  protected Map<String, IAttribute>                             referencedAttributes;
  protected Map<String, IDataRuleModel>                         referencedDataRules;
  protected Map<String, IGetReferencedNatureRelationshipModel>  referencedNatureRelationships;
  protected Map<String, IReferencedSectionElementModel>         referencedElements;
  protected List<IDefaultValueChangeModel>                      defaultValuesDiff;
  protected Map<String, Object>                                 referencedTaxonomies;
  protected Map<String, List<String>>                           typeIdIdentifierAttributeIds;
  protected Integer                                             numberOfVersionsToMaintain;
  protected Map<String, ITag>                                   referencedTags;
  protected List<String>                                        versionableAttributes;
  protected List<String>                                        versionableTags;
  protected List<String>                                        mandatoryAttributeIds;
  protected List<String>                                        shouldAttributeIds;
  protected List<String>                                        mandatoryTagIds;
  protected List<String>                                        shouldTagIds;
  protected Boolean                                             isLanguageHierarchyPresent = false;
  protected IReferencedTemplatePermissionModel                  referencedPermissions;
  protected Map<String, IReferencedPropertyCollectionModel>     referencedSections;

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
  public Map<String, IKlassDetailsForBulkPropagationModel> getEntityDetails()
  {
    return entityDetails;
  }
  
  @JsonDeserialize(contentAs = KlassDetailsForBulkPropagationModel.class)
  @Override
  public void setEntityDetails(Map<String, IKlassDetailsForBulkPropagationModel> entityDetails)
  {
    this.entityDetails = entityDetails;
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
  public Map<String, IDataRuleModel> getReferencedDataRules()
  {
    return referencedDataRules;
  }
  
  @Override
  @JsonDeserialize(contentAs = DataRuleModel.class)
  public void setReferencedDataRules(Map<String, IDataRuleModel> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
  
  @Override
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @JsonDeserialize(contentAs = GetReferencedNatureRelationshipModel.class)
  @Override
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
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
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    if (defaultValuesDiff == null) {
      defaultValuesDiff = new ArrayList<>();
    }
    return defaultValuesDiff;
  }
  
  @Override
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  @Override
  public Map<String, Object> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @Override
  public void setReferencedTaxonomies(Map<String, Object> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
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
  
}
