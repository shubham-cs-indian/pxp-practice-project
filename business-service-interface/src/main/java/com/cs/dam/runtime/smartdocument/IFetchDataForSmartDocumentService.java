package com.cs.dam.runtime.smartdocument;

import java.util.List;

import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;

public interface IFetchDataForSmartDocumentService {
  
  List<ISmartDocumentKlassInstanceDataModel> getRelationships(String instanceId,
      String relationshipId, String presetId, Integer from, Integer size) throws Exception;
  
  /*List<ISmartDocumentKlassInstanceDataModel> getReferences(String instanceId, String referenceId,
      String presetId, Integer from, Integer size) throws Exception;*/
  
  List<ISmartDocumentKlassInstanceDataModel> getEmbeddedVariants(String instanceId,
      String embeddedClassId, String presetId, Integer from, Integer size) throws Exception;
  
  List<ISmartDocumentKlassInstanceDataModel> getUnits(String instanceId, String unitClassId,
      String presetId, Integer from, Integer size) throws Exception;
  
  /*ISmartDocumentKlassInstanceDataModel getInstanceById(String instanceId, String presetId) throws Exception;
  
  String getFormattedValueForUnitAttribute(ISmartDocumentAttributeInstanceDataModel attribute) throws Exception;
  
  String getDefaultValueForUnitAttribute(ISmartDocumentAttributeInstanceDataModel attribute) throws Exception;*/
  
  Boolean isUnitAttribute(String attributeType);
}
