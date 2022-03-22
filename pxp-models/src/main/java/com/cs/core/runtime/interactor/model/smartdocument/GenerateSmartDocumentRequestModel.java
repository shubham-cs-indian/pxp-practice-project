package com.cs.core.runtime.interactor.model.smartdocument;

import java.util.ArrayList;
import java.util.List;

public class GenerateSmartDocumentRequestModel implements IGenerateSmartDocumentRequestModel {
  
  private static final long serialVersionUID = 1L;
  protected List<Long>      klassInstanceIds = new ArrayList<>();
  protected String          smartDocumentPresetId;
  protected String          baseType;
  
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
  public String getSmartDocumentPresetId()
  {
    return smartDocumentPresetId;
  }
  
  @Override
  public void setSmartDocumentPresetId(String smartDocumentPresetId)
  {
    this.smartDocumentPresetId = smartDocumentPresetId;
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
