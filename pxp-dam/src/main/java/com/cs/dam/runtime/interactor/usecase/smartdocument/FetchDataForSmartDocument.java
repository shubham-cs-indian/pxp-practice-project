package com.cs.dam.runtime.interactor.usecase.smartdocument;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.strategy.usecase.smartdocument.fetchdata.IGetRelationshipInstancesForSmartDocument;
import com.cs.core.config.strategy.usecase.smartdocument.fetchdata.IGetVariantInstancesForSmartDocument;
import com.cs.core.runtime.interactor.model.smartdocument.GetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetEntityForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentKlassInstanceDataModel;
import com.cs.dam.runtime.smartdocument.IFetchDataForSmartDocumentService;

@Service("fetchDataForSmartDocument")
@SuppressWarnings("unchecked")
public class FetchDataForSmartDocument implements IFetchDataForSmartDocumentService {
  
  /*@Autowired
  protected IGetInstanceForSmartDocument              getInstanceForSmartDocument;*/
  
  @Autowired
  protected IGetVariantInstancesForSmartDocument      getVariantInstancesForSmartDocument;
  
  @Autowired
  protected IGetRelationshipInstancesForSmartDocument getRelationshipInstancesForSmartDocument;
  
  /*@Autowired
  protected IGetReferenceInstancesForSmartDocument    getReferenceInstancesForSmartDocument;*/
  
  @Override
  public List<ISmartDocumentKlassInstanceDataModel> getRelationships(String instanceId,
      String relationshipId, String presetId, Integer from, Integer size) throws Exception
  {
    IGetEntityForSmartDocumentRequestModel getEntityForSmartDocumentRequestModel = getRequestModel(
        instanceId, relationshipId, presetId, from, size);
    return (List<ISmartDocumentKlassInstanceDataModel>) getRelationshipInstancesForSmartDocument
        .execute(getEntityForSmartDocumentRequestModel)
        .getList();
  }
  
  /*@Override
  public List<ISmartDocumentKlassInstanceDataModel> getReferences(String instanceId,
      String referenceId, String presetId, Integer from, Integer size) throws Exception
  {
    IGetEntityForSmartDocumentRequestModel getEntityForSmartDocumentRequestModel = getRequestModel(
        instanceId, referenceId, presetId, from, size);
    return (List<ISmartDocumentKlassInstanceDataModel>) getReferenceInstancesForSmartDocument
        .execute(getEntityForSmartDocumentRequestModel)
        .getList();
  }*/
  
  @Override
  public List<ISmartDocumentKlassInstanceDataModel> getEmbeddedVariants(String instanceId,
      String embeddedClassId, String presetId, Integer from, Integer size) throws Exception
  {
    IGetEntityForSmartDocumentRequestModel getEntityForSmartDocumentRequestModel = getRequestModel(
        instanceId, embeddedClassId, presetId, from, size);
    return (List<ISmartDocumentKlassInstanceDataModel>) getVariantInstancesForSmartDocument
        .execute(getEntityForSmartDocumentRequestModel)
        .getList();
  }
  
  @Override
  public List<ISmartDocumentKlassInstanceDataModel> getUnits(String instanceId, String unitClassId,
      String presetId, Integer from, Integer size) throws Exception
  {
    IGetEntityForSmartDocumentRequestModel getEntityForSmartDocumentRequestModel = getRequestModel(
        instanceId, unitClassId, presetId, from, size);
    return (List<ISmartDocumentKlassInstanceDataModel>) getVariantInstancesForSmartDocument
        .execute(getEntityForSmartDocumentRequestModel)
        .getList();
  }
  
  /*@Override
  public ISmartDocumentKlassInstanceDataModel getInstanceById(String instanceId, String presetId)
      throws Exception
  {
    IGetEntityForSmartDocumentRequestModel getEntityForSmartDocumentRequestModel = new GetEntityForSmartDocumentRequestModel();
    getEntityForSmartDocumentRequestModel.setInstanceId(instanceId);
    getEntityForSmartDocumentRequestModel.setPresetId(presetId);
    return getInstanceForSmartDocument.execute(getEntityForSmartDocumentRequestModel);
  }*/
  
  /*private String evaluateUnitAttribute(ISmartDocumentAttributeInstanceDataModel attribute,
      Boolean isConversionRequired, String unitTo) throws Exception
  {
    
    String value = attribute.getValue();
    if (!value.isEmpty() && isUnitAttribute(attribute.getAttributeType())) {
      String attributeType = attribute.getAttributeType();
      String baseUnit = unitConversionUtil.getAttributeTypeBaseUnit(attributeType);
      if (isConversionRequired) {
        Double converetedValue = unitConversionUtil.convert(baseUnit, unitTo,
            Double.parseDouble(value), attributeType);
        return converetedValue.toString() + " " + unitTo;
      }
      return value + " " + baseUnit;
    }
    else {
      return value;
    }
  }*/
  
  /*@Override
  public String getFormattedValueForUnitAttribute(ISmartDocumentAttributeInstanceDataModel attribute) throws Exception{
    String defaultUnit = attribute.getDefaultUnit();
    return evaluateUnitAttribute(attribute, true, defaultUnit);
  }*/
  
  /*@Override
  public String getDefaultValueForUnitAttribute(ISmartDocumentAttributeInstanceDataModel attribute) throws Exception{
    return evaluateUnitAttribute(attribute, false, null);
  }*/
  
  @Override
  public Boolean isUnitAttribute(String attributeType)
  {
    if (CommonConstants.UNIT_ATTRIBUTE_TYPES.contains(attributeType)) {
      return true;
    }
    return false;
  }
  
  private IGetEntityForSmartDocumentRequestModel getRequestModel(String instanceId,
      String entityId, String presetId, Integer from, Integer size)
  {
    IGetEntityForSmartDocumentRequestModel getEntityForSmartDocumentRequestModel = new GetEntityForSmartDocumentRequestModel();
    getEntityForSmartDocumentRequestModel.setInstanceId(instanceId);
    getEntityForSmartDocumentRequestModel.setEntityId(entityId);
    getEntityForSmartDocumentRequestModel.setPresetId(presetId);
    getEntityForSmartDocumentRequestModel.setFrom(from);
    getEntityForSmartDocumentRequestModel.setSize(size);
    return getEntityForSmartDocumentRequestModel;
  }
}