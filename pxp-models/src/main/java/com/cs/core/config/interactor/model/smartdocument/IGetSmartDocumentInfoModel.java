package com.cs.core.config.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetSmartDocumentInfoModel extends IModel {
  
  public static final String RENDERER_LICENCE_KEY  = "rendererLicenceKey";
  public static final String ZIP_TEMPLATE_ID       = "zipTemplateId";
  public static final String SMART_DOCUMENT_PRESET = "smartDocumentPreset";
  
  public String getRendererLicenceKey();
  
  public void setRendererLicenceKey(String rendererLicenceKey);
  
  public String getZipTemplateId();
  
  public void setZipTemplateId(String zipTemplateId);
  
  public IGetSmartDocumentPresetModel getSmartDocumentPreset();
  
  public void setSmartDocumentPreset(IGetSmartDocumentPresetModel smartDocumentPreset);
}
