package com.cs.core.bgprocess.idto;

import java.util.List;
import java.util.Set;

import com.cs.core.rdbms.coupling.idto.IModifiedCoupedPropertyDTO;

public interface IKlassTaxonomySaveDTO extends IInitializeBGProcessDTO {
  
  public static final String CLASSIFIER_CODES                                    = "classifierCodes";
  public static final String UPDATED_MANDATORY_PROPERTY_IIDS                     = "updatedMandatoryPropertyIIDs";
  public static final String PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER        = "propertyIIDsToEvaluateProductIdentifier";
  public static final String PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER          = "propertyIIDsToRemoveProductIdentifier";
  public static final String SAVED_KLASS_TAXONOMY_CLASSIFIER_IID                 = "savedKlassTaxonomyClassifierIID";
  public static final String REMOVED_EMBEDDED_CLASSIFIER_IIDS                    = "removedEmbeddedClassifierIIDs";
  public static final String DELETED_NATURE_RELATIONSHIP_PROPERTY_IIDS           = "deletedNatureRelationshipPropertyIIDs";
  public static final String MODIFIED_RELATIONSHIP_IIDS_FOR_TAXONOMY_INHERITANCE = "ModifiedRelationshipIIDsForTaxonomyInheritance";
  public static final String MODIFIED_COUPLED_PROPERTY_DTOS                      = "modifiedCoupledPropertyDTOs";
  public static final String CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS   = "changedClassifiersForAttributeContexts";
  public static final String REMOVE_ATTRIBUTE_VARIANT_CONTEXTS_DTO        = "removeAttributeVariantContextsDTO";
  public static final String ADDED_CALCULATED_ATTRIBUTE_IDS               = "addedCalculatedAttributeIds";
  
  public Set<String> getClassifierCodes();
  
  public void setClassifierCodes(Set<String> classifierCodes);
  
  List<Long> getUpdatedMandatoryPropertyIIDs();
  
  public void setUpdatedMandatoryPropertyIIDs(List<Long> updatedMandatoryPropertyIIDs);
  
  List<Long> getPropertyIIDsToEvaluateProductIdentifier();
  
  public void setPropertyIIDsToEvaluateProductIdentifier(List<Long> propertyIIDsToEvaluateProductIdentifier);
  
  List<Long> getPropertyIIDsToRemoveProductIdentifier();
  
  public void setPropertyIIDsToRemoveProductIdentifier(List<Long> propertyIIDsToRemoveProductIdentifier);
  
  public Long getSavedKlassTaxonomyClassifierIID();
  
  public void setSavedKlassTaxonomyClassifierIID(Long savedKlassTaxonomyClassifierIID);
  
  List<Long> getRemovedEmbeddedClassifierIIDs();
  
  public void setRemovedEmbeddedClassifierIIDs(List<Long> removedEmbeddedClassifierIIDs);
  
  List<Long> getDeletedNatureRelationshipPropertyIIDs();
  
  public void setDeletedNatureRelationshipPropertyIIDs(List<Long> deletedNatureRelationshipPropertyIIDs);
  
  List<Long> getModifiedRelationshipIIDsForTaxonomyInheritance();
  
  public void setModifiedRelationshipIIDsForTaxonomyInheritance(List<Long> modifiedRelationshipIIDsForTaxonomyInheritance);
  
  public void setModifiedCoupledPropertyDTOs(Set<IModifiedCoupedPropertyDTO> modifiedCoupledPropertyDTOs);
  
  public Set<IModifiedCoupedPropertyDTO> getModifiedCoupledPropertyDTOs();
  

  public List<IRemoveAttributeVariantContextsDTO> getRemoveAttributeVariantContextsDTO();
  public void setRemoveAttributeVariantContextsDTO(List<IRemoveAttributeVariantContextsDTO> removeAttributeVariantContextsDTO);
  
  public List<Long> getChangedClassifiersForAttributeContexts();
  public void setChangedClassifiersForAttributeContexts(List<Long> changedClassifiersForAttributeContexts);
  
  public List<String> getAddedCalculatedAttributeIds();
  
  public void setAddedCalculatedAttributeIds(List<String> addedCalculatedAttributeIds);
}
