package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.GetSmartDocumentPresetModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetSmartDocumentInfoModel implements IGetSmartDocumentInfoModel {
  
  private static final long              serialVersionUID = 1L;
  protected String                       rendererLicenceKey;
  protected String                       zipTemplateId;
  protected IGetSmartDocumentPresetModel smartDocumentPreset;
  
  @Override
  public String getRendererLicenceKey()
  {
    return rendererLicenceKey;
  }
  
  @Override
  public void setRendererLicenceKey(String rendererLicenceKey)
  {
    this.rendererLicenceKey = rendererLicenceKey;
  }
  
  @Override
  public String getZipTemplateId()
  {
    return zipTemplateId;
  }
  
  @Override
  public void setZipTemplateId(String zipTemplateId)
  {
    this.zipTemplateId = zipTemplateId;
  }
  
  @Override
  public IGetSmartDocumentPresetModel getSmartDocumentPreset()
  {
    return smartDocumentPreset;
  }
  
  @Override
  @JsonDeserialize(as = GetSmartDocumentPresetModel.class)
  public void setSmartDocumentPreset(IGetSmartDocumentPresetModel smartDocumentPreset)
  {
    this.smartDocumentPreset = smartDocumentPreset;
  }
}
