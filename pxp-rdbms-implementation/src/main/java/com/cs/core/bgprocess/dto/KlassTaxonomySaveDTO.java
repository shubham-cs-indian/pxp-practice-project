package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.bgprocess.idto.IKlassTaxonomySaveDTO;
import com.cs.core.bgprocess.idto.IRemoveAttributeVariantContextsDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.dto.ModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedCoupedPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;

public class KlassTaxonomySaveDTO extends InitializeBGProcessDTO implements IKlassTaxonomySaveDTO {
  
  private static final long                              serialVersionUID                               = 1L;
  private Set<String>                                    classifierCodes                                = new HashSet<>();
  private final List<Long>                               updatedMandatoryPropertyIIDs                   = new ArrayList<>();
  private final List<Long>                               propertyIIDsToEvaluateProductIdentifier        = new ArrayList<>();
  private final List<Long>                               propertyIIDsToRemoveProductIdentifier          = new ArrayList<>();
  private Long                                           savedKlassTaxonomyClassifierIID;
  private final List<Long>                               removedEmbeddedClassifierIIDs                  = new ArrayList<>();
  private final List<Long>                               deletedNatureRelationshipPropertyIIDs          = new ArrayList<>();
  private final List<Long>                               modifiedRelationshipIIDsForTaxonomyInheritance = new ArrayList<>();
  private final List<IRemoveAttributeVariantContextsDTO> removeAttributeVariantContextsDTO              = new ArrayList<>();
  private final Set<IModifiedCoupedPropertyDTO>          modifiedCoupledPropertyDTOs                    = new HashSet<>();
  private final List<Long>                               changedClassifiersForAttributeContexts         = new ArrayList<>();
  private final List<String>                             addedCalculatedAttributeIds                    = new ArrayList<>();
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    updatedMandatoryPropertyIIDs.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.UPDATED_MANDATORY_PROPERTY_IIDS)
        .forEach((iid) -> {
          updatedMandatoryPropertyIIDs.add((Long) iid);
        });
    classifierCodes.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.CLASSIFIER_CODES)
        .forEach((iid) -> {
          classifierCodes.add((String) iid);
        });
    propertyIIDsToEvaluateProductIdentifier.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER)
        .forEach((iid) -> {
          propertyIIDsToEvaluateProductIdentifier.add((Long) iid);
        });
    propertyIIDsToRemoveProductIdentifier.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER)
        .forEach((iid) -> {
          propertyIIDsToRemoveProductIdentifier.add((Long) iid);
        });
    savedKlassTaxonomyClassifierIID = json
        .getLong(IKlassTaxonomySaveDTO.SAVED_KLASS_TAXONOMY_CLASSIFIER_IID);
    removedEmbeddedClassifierIIDs.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.REMOVED_EMBEDDED_CLASSIFIER_IIDS)
        .forEach((iid) -> {
          removedEmbeddedClassifierIIDs.add((Long) iid);
        });
    deletedNatureRelationshipPropertyIIDs.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.DELETED_NATURE_RELATIONSHIP_PROPERTY_IIDS)
        .forEach((iid) -> {
          deletedNatureRelationshipPropertyIIDs.add((Long) iid);
        });
    modifiedRelationshipIIDsForTaxonomyInheritance.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.MODIFIED_RELATIONSHIP_IIDS_FOR_TAXONOMY_INHERITANCE)
    .forEach((iid) -> {
      modifiedRelationshipIIDsForTaxonomyInheritance.add((Long)iid);
    });
    
    modifiedCoupledPropertyDTOs.clear();
    for (Object recordJSON : json.getJSONArray(MODIFIED_COUPLED_PROPERTY_DTOS)) {
      IModifiedCoupedPropertyDTO coupedPropertyDTO = new ModifiedCoupedPropertyDTO();
      coupedPropertyDTO.fromJSON(recordJSON.toString());
      modifiedCoupledPropertyDTOs.add(coupedPropertyDTO);
    }
    this.changedClassifiersForAttributeContexts.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS)
    .forEach((iid) -> {
      changedClassifiersForAttributeContexts.add((Long)iid);
    });

    removeAttributeVariantContextsDTO.clear();
    json.getJSONArray(REMOVE_ATTRIBUTE_VARIANT_CONTEXTS_DTO).forEach(iid -> {
      IRemoveAttributeVariantContextsDTO childDTO = new RemoveAttributeVariantContextsDTO();
      try {
        childDTO.fromJSON(iid.toString());
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
      removeAttributeVariantContextsDTO.add(childDTO);
    });
    
    addedCalculatedAttributeIds.clear();
    json.getJSONArray(IKlassTaxonomySaveDTO.ADDED_CALCULATED_ATTRIBUTE_IDS).forEach(id -> {
      addedCalculatedAttributeIds.add((String) id);
    });
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        super.toJSONBuffer(),
        !classifierCodes.isEmpty() ? JSONBuilder.newJSONStringArray(
            IKlassTaxonomySaveDTO.CLASSIFIER_CODES, classifierCodes) : JSONBuilder.VOID_FIELD,
        !updatedMandatoryPropertyIIDs.isEmpty()
            ? JSONBuilder.newJSONLongArray(IKlassTaxonomySaveDTO.UPDATED_MANDATORY_PROPERTY_IIDS,
                updatedMandatoryPropertyIIDs)
            : JSONBuilder.VOID_FIELD,
        !propertyIIDsToEvaluateProductIdentifier.isEmpty() ? JSONBuilder.newJSONLongArray(
            IKlassTaxonomySaveDTO.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER,
            propertyIIDsToEvaluateProductIdentifier) : JSONBuilder.VOID_FIELD,
        !propertyIIDsToRemoveProductIdentifier.isEmpty() ? JSONBuilder.newJSONLongArray(
            IKlassTaxonomySaveDTO.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER,
            propertyIIDsToRemoveProductIdentifier) : JSONBuilder.VOID_FIELD,
        JSONBuilder.newJSONField(IKlassTaxonomySaveDTO.SAVED_KLASS_TAXONOMY_CLASSIFIER_IID,
            savedKlassTaxonomyClassifierIID),
        !removedEmbeddedClassifierIIDs.isEmpty()
            ? JSONBuilder.newJSONLongArray(IKlassTaxonomySaveDTO.REMOVED_EMBEDDED_CLASSIFIER_IIDS,
                removedEmbeddedClassifierIIDs)
            : JSONBuilder.VOID_FIELD,
        !deletedNatureRelationshipPropertyIIDs.isEmpty()
        ? JSONBuilder.newJSONLongArray(IKlassTaxonomySaveDTO.DELETED_NATURE_RELATIONSHIP_PROPERTY_IIDS,
            deletedNatureRelationshipPropertyIIDs)
        : JSONBuilder.VOID_FIELD,
        !modifiedRelationshipIIDsForTaxonomyInheritance.isEmpty()
        ? JSONBuilder.newJSONLongArray(IKlassTaxonomySaveDTO.MODIFIED_RELATIONSHIP_IIDS_FOR_TAXONOMY_INHERITANCE,
            modifiedRelationshipIIDsForTaxonomyInheritance)
        : JSONBuilder.VOID_FIELD,
        !modifiedCoupledPropertyDTOs.isEmpty() ? JSONBuilder.newJSONArray(MODIFIED_COUPLED_PROPERTY_DTOS, modifiedCoupledPropertyDTOs) : JSONBuilder.VOID_FIELD,
        !changedClassifiersForAttributeContexts.isEmpty()
        ? JSONBuilder.newJSONLongArray(IKlassTaxonomySaveDTO.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS,
            changedClassifiersForAttributeContexts)
        : JSONBuilder.VOID_FIELD,
        ! removeAttributeVariantContextsDTO.isEmpty() 
        ? JSONBuilder.newJSONArray(REMOVE_ATTRIBUTE_VARIANT_CONTEXTS_DTO, 
            removeAttributeVariantContextsDTO)
        : JSONBuilder.VOID_FIELD,
        !addedCalculatedAttributeIds.isEmpty() ? JSONBuilder
            .newJSONStringArray(IKlassTaxonomySaveDTO.ADDED_CALCULATED_ATTRIBUTE_IDS, addedCalculatedAttributeIds)
            : JSONBuilder.VOID_FIELD);
  }
  
  public StringBuffer assembleJSONForMapStringListString(Map<String, List<String>> map) {
    StringBuffer json = new StringBuffer();
    map.entrySet().forEach(entry -> json.append(JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONStringArray(entry.getKey(), entry.getValue()))));
    return json;
  }
  
  @Override
  public Set<String> getClassifierCodes()
  {
    return classifierCodes;
  }
  
  @Override
  public void setClassifierCodes(Set<String> classifierCodes)
  {
    this.classifierCodes = classifierCodes;
  }
  
  @Override
  public List<Long> getUpdatedMandatoryPropertyIIDs()
  {
    return updatedMandatoryPropertyIIDs;
  }
  
  @Override
  public void setUpdatedMandatoryPropertyIIDs(List<Long> updatedMandatoryPropertyIIDs)
  {
    this.updatedMandatoryPropertyIIDs.clear();
    this.updatedMandatoryPropertyIIDs.addAll(updatedMandatoryPropertyIIDs);
  }
  
  @Override
  public List<Long> getPropertyIIDsToEvaluateProductIdentifier()
  {
    return propertyIIDsToEvaluateProductIdentifier;
  }
  
  @Override
  public void setPropertyIIDsToEvaluateProductIdentifier(List<Long> propertyIIDsToEvaluateProductIdentifier)
  {
    this.propertyIIDsToEvaluateProductIdentifier.clear();
    this.propertyIIDsToEvaluateProductIdentifier.addAll(propertyIIDsToEvaluateProductIdentifier);
  }
  
  @Override
  public List<Long> getPropertyIIDsToRemoveProductIdentifier()
  {
    return propertyIIDsToRemoveProductIdentifier;
  }
  
  @Override
  public void setPropertyIIDsToRemoveProductIdentifier(List<Long> propertyIIDsToRemoveProductIdentifier)
  {
    this.propertyIIDsToRemoveProductIdentifier.clear();
    this.propertyIIDsToRemoveProductIdentifier.addAll(propertyIIDsToRemoveProductIdentifier);
  }
  
  @Override
  public Long getSavedKlassTaxonomyClassifierIID()
  {
    return savedKlassTaxonomyClassifierIID;
  }
  
  @Override
  public void setSavedKlassTaxonomyClassifierIID(Long savedKlassTaxonomyClassifierIID)
  {
    this.savedKlassTaxonomyClassifierIID = savedKlassTaxonomyClassifierIID;
  }
  
  @Override
  public List<Long> getRemovedEmbeddedClassifierIIDs()
  {
    return removedEmbeddedClassifierIIDs;
  }
  
  @Override
  public void setRemovedEmbeddedClassifierIIDs(List<Long> removedEmbeddedClassifierIIDs)
  {
    this.removedEmbeddedClassifierIIDs.clear();
    this.removedEmbeddedClassifierIIDs.addAll(removedEmbeddedClassifierIIDs);
  }
  
  @Override
  public List<Long> getDeletedNatureRelationshipPropertyIIDs()
  {
    return deletedNatureRelationshipPropertyIIDs;
  }
  
  @Override
  public void setDeletedNatureRelationshipPropertyIIDs(List<Long> deletedNatureRelationshipPropertyIIDs)
  {
    this.deletedNatureRelationshipPropertyIIDs.clear();
    this.deletedNatureRelationshipPropertyIIDs.addAll(deletedNatureRelationshipPropertyIIDs);
  }
  
  @Override
  public List<Long> getModifiedRelationshipIIDsForTaxonomyInheritance()
  {
    return modifiedRelationshipIIDsForTaxonomyInheritance;
  }
  
  @Override
  public void setModifiedRelationshipIIDsForTaxonomyInheritance(List<Long> modifiedRelationshipIIDsForTaxonomyInheritance)
  {
    this.modifiedRelationshipIIDsForTaxonomyInheritance.clear();
    this.modifiedRelationshipIIDsForTaxonomyInheritance.addAll(modifiedRelationshipIIDsForTaxonomyInheritance);
  }
  
  @Override
  public List<IRemoveAttributeVariantContextsDTO> getRemoveAttributeVariantContextsDTO()
  {
    return removeAttributeVariantContextsDTO;
  }
  
  @Override
  public void setRemoveAttributeVariantContextsDTO(List<IRemoveAttributeVariantContextsDTO> removedAttributeIdVsContextIds)
  {
    this.removeAttributeVariantContextsDTO.clear();
    this.removeAttributeVariantContextsDTO.addAll(removedAttributeIdVsContextIds);
  }
  
  @Override
  public List<Long> getChangedClassifiersForAttributeContexts()
  {
    return changedClassifiersForAttributeContexts;
  }
  
  @Override
  public void setChangedClassifiersForAttributeContexts(List<Long> changedClassifiersForAttributeContexts)
  {
    this.changedClassifiersForAttributeContexts.clear();
    this.changedClassifiersForAttributeContexts.addAll(changedClassifiersForAttributeContexts);
  }
  
  public List<String> getAddedCalculatedAttributeIds()
  {
    return addedCalculatedAttributeIds;
  }
  
  @Override
  public void setModifiedCoupledPropertyDTOs(Set<IModifiedCoupedPropertyDTO> modifiedCoupledPropertyDTOs)
  {
    this.modifiedCoupledPropertyDTOs.addAll(modifiedCoupledPropertyDTOs);
  }
  
  @Override
  public Set<IModifiedCoupedPropertyDTO> getModifiedCoupledPropertyDTOs()
  {
    return modifiedCoupledPropertyDTOs;
  }
  
  public void setAddedCalculatedAttributeIds(List<String> addedCalculatedAttributeIds)
  {
    this.addedCalculatedAttributeIds.clear();
    this.addedCalculatedAttributeIds.addAll(addedCalculatedAttributeIds);
  }
}
