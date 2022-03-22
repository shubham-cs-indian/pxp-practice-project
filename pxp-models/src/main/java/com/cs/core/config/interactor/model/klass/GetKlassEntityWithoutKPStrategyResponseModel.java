package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.relationship.IRelationshipPropertiesToInheritModel;
import com.cs.core.config.interactor.model.relationship.ISideInfoForRelationshipInheritanceModel;
import com.cs.core.config.interactor.model.relationship.SideInfoForRelationshipInheritanceModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.runtime.interactor.model.context.ContextKlassSavePropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.ContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipPropertiesToInheritModel;
import com.cs.core.runtime.interactor.model.relationship.SideInfoForRelationshipDataTransferModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetKlassEntityWithoutKPStrategyResponseModel extends ConfigResponseWithAuditLogModel
    implements IGetKlassEntityWithoutKPStrategyResponseModel {
  
  private static final long                                       serialVersionUID              = 1L;
  protected IKlass                                                entity;
  protected List<IDefaultValueChangeModel>                        defaultValuesDiff             = new ArrayList<>();
  protected List<String>                                          deletedNatureRelationshipIds  = new ArrayList<>();
  protected List<String>                                          deletedRelationshipIds        = new ArrayList<>();
  protected Map<String, List<String>>                             deletedPropertiesFromSource;
  protected IRelationshipPropertiesToInheritModel                 addedElements;
  protected IRelationshipPropertiesToInheritModel                 deletedElements;
  protected IRelationshipPropertiesToInheritModel                 modifiedElements;
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutCreateEnable;
  protected IGetKlassEntityConfigDetailsModel                     configDetails;
  protected IContextKlassSavePropertiesToInheritModel             contextKlassSavePropertiesToInherit;
  protected Boolean                                               isIndentifierAttributeChanged = false;
  protected List<IContextInfoForContextualDataTransferModel>      contextKlassSavePropertiesToDataTransfer;
  protected List<ISideInfoForRelationshipDataTransferModel>       relationshipDataForTransfer;
  protected List<ISideInfoForRelationshipInheritanceModel>        relationshipDataForRelationshipInheritance;
  protected Boolean                                               isRemoveTaxonomyConflictsRequired = false;
  protected List<Long>                                            removedContextClassifierIIDs;
  protected IRemoveAttributeVariantContextModel                   removedAttributeVariantContexts; 
  protected List<Long>                                            updatedMandatoryPropertyIIDs      = new ArrayList<>();
  protected List<Long>                                            propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
  protected List<Long>                                            propertyIIDsToRemoveProductIdentifier   = new ArrayList<Long>();
  protected List<String>                                          addedCalculatedAttributeIds;
  
  @Override
  public IKlass getEntity()
  {
    return this.entity;
  }
  
  @Override
  public void setEntity(IKlass entity)
  {
    this.entity = entity;
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    return defaultValuesDiff;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    
    return deletedNatureRelationshipIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
  @Override
  public List<String> getDeletedRelationshipIds()
  {
    
    return deletedRelationshipIds;
  }
  
  @Override
  public void setDeletedRelationshipIds(List<String> deletedRelationshipIds)
  {
    this.deletedRelationshipIds = deletedRelationshipIds;
  }
  
  @Override
  public Map<String, List<String>> getDeletedPropertiesFromSource()
  {
    if (deletedPropertiesFromSource == null) {
      deletedPropertiesFromSource = new HashMap<>();
    }
    return deletedPropertiesFromSource;
  }
  
  @Override
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource)
  {
    this.deletedPropertiesFromSource = deletedPropertiesFromSource;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getAddedElements()
  {
    return addedElements;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setAddedElements(IRelationshipPropertiesToInheritModel addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getModifiedElements()
  {
    return modifiedElements;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setModifiedElements(IRelationshipPropertiesToInheritModel modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
  @Override
  public IRelationshipPropertiesToInheritModel getDeletedElements()
  {
    return deletedElements;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipPropertiesToInheritModel.class)
  public void setDeletedElements(IRelationshipPropertiesToInheritModel deletedElements)
  {
    this.deletedElements = deletedElements;
  }
  
  @Override
  public IGetKlassEntityConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = GetKlassEntityConfigDetailsModel.class)
  @Override
  public void setConfigDetails(IGetKlassEntityConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public IContextKlassSavePropertiesToInheritModel getContextKlassSavePropertiesToInherit()
  {
    return contextKlassSavePropertiesToInherit;
  }
  
  @Override
  @JsonDeserialize(as = ContextKlassSavePropertiesToInheritModel.class)
  public void setContextKlassSavePropertiesToInherit(
      IContextKlassSavePropertiesToInheritModel contextKlassSavePropertiesToInherit)
  {
    this.contextKlassSavePropertiesToInherit = contextKlassSavePropertiesToInherit;
  }
  
  @Override
  public Boolean getIsIndentifierAttributeChanged()
  {
    return isIndentifierAttributeChanged;
  }
  
  @Override
  public void setIsIndentifierAttributeChanged(Boolean isIndentifierAttributeChanged)
  {
    this.isIndentifierAttributeChanged = isIndentifierAttributeChanged;
  }
  
  public List<IContextInfoForContextualDataTransferModel> getContextKlassSavePropertiesToDataTransfer()
  {
    if (contextKlassSavePropertiesToDataTransfer == null) {
      contextKlassSavePropertiesToDataTransfer = new ArrayList<>();
    }
    return contextKlassSavePropertiesToDataTransfer;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContextInfoForContextualDataTransferModel.class)
  public void setContextKlassSavePropertiesToDataTransfer(
      List<IContextInfoForContextualDataTransferModel> contextKlassSavePropertiesToDataTransfer)
  {
    this.contextKlassSavePropertiesToDataTransfer = contextKlassSavePropertiesToDataTransfer;
  }
  
  @Override
  public List<ISideInfoForRelationshipDataTransferModel> getRelationshipDataForTransfer()
  {
    if (relationshipDataForTransfer == null) {
      relationshipDataForTransfer = new ArrayList<>();
    }
    return relationshipDataForTransfer;
  }
  
  @Override
  @JsonDeserialize(contentAs = SideInfoForRelationshipDataTransferModel.class)
  public void setRelationshipDataForTransfer(
      List<ISideInfoForRelationshipDataTransferModel> relationshipDataForTransfer)
  {
    this.relationshipDataForTransfer = relationshipDataForTransfer;
  }
  
  @Override
  public List<ISideInfoForRelationshipInheritanceModel> getRelationshipDataForRelationshipInheritance()
  {
    if (relationshipDataForRelationshipInheritance == null) {
      relationshipDataForRelationshipInheritance = new ArrayList<>();
    }
    return relationshipDataForRelationshipInheritance;
  }
  
  @Override
  @JsonDeserialize(contentAs = SideInfoForRelationshipInheritanceModel.class)
  public void setRelationshipDataForRelationshipInheritance(
      List<ISideInfoForRelationshipInheritanceModel> relationshipDataForRelationshipInheritance)
  {
    this.relationshipDataForRelationshipInheritance = relationshipDataForRelationshipInheritance;
  }
  
  @Override
  public Boolean getIsRemoveTaxonomyConflictsRequired()
  {
    return isRemoveTaxonomyConflictsRequired;
  }

  @Override
  public void setIsRemoveTaxonomyConflictsRequired(Boolean isRemoveTaxonomyConflictsRequired)
  {
    this.isRemoveTaxonomyConflictsRequired = isRemoveTaxonomyConflictsRequired;
  }

  @Override
  public IRemoveAttributeVariantContextModel getRemovedAttributeVariantContexts()
  {
    return removedAttributeVariantContexts;
  }

  @JsonDeserialize(as = RemoveAttributeVariantContextModel.class)
  @Override
  public void setRemovedAttributeVariantContexts(
      IRemoveAttributeVariantContextModel removedAttributeVariantContexts)
  {
    this.removedAttributeVariantContexts = removedAttributeVariantContexts;
  }
  
  public List<Long> getRemovedContextClassifierIIDs()
  {
    if(removedContextClassifierIIDs == null) {
      removedContextClassifierIIDs = new ArrayList<>();
    }
    return removedContextClassifierIIDs;
  }

  @Override
  public void setRemovedContextClassifierIIDs(List<Long> removedContextClassifierIIDs)
  {
    this.removedContextClassifierIIDs = removedContextClassifierIIDs;
  }

  @Override
  public List<Long> getUpdatedMandatoryPropertyIIDs()
  {
    return updatedMandatoryPropertyIIDs;
  }

  @Override
  public void setUpdatedMandatoryPropertyIIDs(List<Long> mandatoryPropertyUpdatedIIDs)
  {
    this.updatedMandatoryPropertyIIDs = mandatoryPropertyUpdatedIIDs;
  }

  @Override
  public List<Long> getPropertyIIDsToEvaluateProductIdentifier()
  {
    return propertyIIDsToEvaluateProductIdentifier;
  }

  @Override
  public void setPropertyIIDsToEvaluateProductIdentifier(
      List<Long> propertyIIDsToEvaluateProductIdentifier)
  {
    this.propertyIIDsToEvaluateProductIdentifier = propertyIIDsToEvaluateProductIdentifier;
  }

  @Override
  public List<Long> getPropertyIIDsToRemoveProductIdentifier()
  {
    return propertyIIDsToRemoveProductIdentifier;
  }

  @Override
  public void setPropertyIIDsToRemoveProductIdentifier(
      List<Long> propertyIIDsToRemoveProductIdentifier)
  {
    this.propertyIIDsToRemoveProductIdentifier = propertyIIDsToRemoveProductIdentifier;
  }

  @Override
  public List<String> getAddedCalculatedAttributeIds()
  {
    if (addedCalculatedAttributeIds == null) {
      addedCalculatedAttributeIds = new ArrayList<>();
    }
    return addedCalculatedAttributeIds;
  }
  
  @Override
  public void setAddedCalculatedAttributeIds(List<String> addedCalculatedAttributeIds)
  {
    this.addedCalculatedAttributeIds = addedCalculatedAttributeIds;
  }
}
