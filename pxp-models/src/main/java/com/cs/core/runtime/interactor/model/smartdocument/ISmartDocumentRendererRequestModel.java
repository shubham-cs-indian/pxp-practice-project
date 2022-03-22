package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISmartDocumentRendererRequestModel extends IModel {
  
  public static final String HTML_FROM_TEMPLATE_ENGINE  = "htmlFromTemplateEngine";
  public static final String SMART_DOCUMENT_BYTE_ARRAYS = "smartDocumentByteArrays";
  public static final String RENDERER_LICENCE_KEY       = "rendererLicenceKey";
  public static final String SMART_DOCUMENT_PRESET      = "smartDocumentPreset";
  
  public ISmartDocumentInput getSmartDocumentByteArrays();
  
  public void setSmartDocumentByteArrays(ISmartDocumentInput smartDocumentByteArrays);
  
  public String getHtmlFromTemplateEngine();
  
  public void setHtmlFromTemplateEngine(String htmlFromTemplateEngine);
  
  public String getRendererLicenceKey();
  
  public void setRendererLicenceKey(String rendererLicenceKey);
  
  public ISmartDocumentPresetModel getSmartDocumentPreset();
  
  public void setSmartDocumentPreset(ISmartDocumentPresetModel smartDocumentPreset);
}
