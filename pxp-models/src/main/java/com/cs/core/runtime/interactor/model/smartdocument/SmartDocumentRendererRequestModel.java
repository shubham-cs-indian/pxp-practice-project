package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.config.interactor.model.smartdocument.preset.ISmartDocumentPresetModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SmartDocumentRendererRequestModel implements ISmartDocumentRendererRequestModel {
  
  private static final long           serialVersionUID = 1L;
  protected ISmartDocumentInput       smartDocumentByteArrays;
  protected String                    htmlFromTemplateEngine;
  protected String                    rendererLicenceKey;
  protected ISmartDocumentPresetModel smartDocumentPreset;
  
  @Override
  public ISmartDocumentInput getSmartDocumentByteArrays()
  {
    return smartDocumentByteArrays;
  }
  
  @Override
  @JsonDeserialize(as = SmartDocumentInput.class)
  public void setSmartDocumentByteArrays(ISmartDocumentInput smartDocumentByteArrays)
  {
    this.smartDocumentByteArrays = smartDocumentByteArrays;
  }
  
  @Override
  public String getHtmlFromTemplateEngine()
  {
    return htmlFromTemplateEngine;
  }
  
  @Override
  public void setHtmlFromTemplateEngine(String htmlFromTemplateEngine)
  {
    this.htmlFromTemplateEngine = htmlFromTemplateEngine;
  }
  
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
  public ISmartDocumentPresetModel getSmartDocumentPreset()
  {
    return smartDocumentPreset;
  }
  
  @Override
  public void setSmartDocumentPreset(ISmartDocumentPresetModel smartDocumentPreset)
  {
    this.smartDocumentPreset = smartDocumentPreset;
  }
}
