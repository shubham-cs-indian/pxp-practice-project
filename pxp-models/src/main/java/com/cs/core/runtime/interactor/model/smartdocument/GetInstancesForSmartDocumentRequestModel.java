package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;

import java.util.List;

public class GetInstancesForSmartDocumentRequestModel
    implements IGetInstancesForSmartDocumentRequestModel {
  
  private static final long              serialVersionUID = 1L;
  protected List<Long>                   klassInstanceIds;
  protected IGetSmartDocumentPresetModel smartDocumentPresetConfigData;
  protected String                       baseType;
  
  @Override
  public List<Long> getKlassInstanceIds()
  {
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<Long> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public IGetSmartDocumentPresetModel getSmartDocumentPresetConfigData()
  {
    return smartDocumentPresetConfigData;
  }
  
  @Override
  public void setSmartDocumentPresetConfigData(
      IGetSmartDocumentPresetModel smartDocumentPresetConfigData)
  {
    this.smartDocumentPresetConfigData = smartDocumentPresetConfigData;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
}
