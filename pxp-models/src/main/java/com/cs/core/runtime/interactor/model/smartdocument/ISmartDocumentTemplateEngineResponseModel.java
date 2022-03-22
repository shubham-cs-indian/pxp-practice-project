package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISmartDocumentTemplateEngineResponseModel extends IModel {
  
  public static final String HTML_FROM_TEMPLATE_ENGINE = "htmlFromTemplateEngine";
  
  public String getHtmlFromTemplateEngine();
  
  public void setHtmlFromTemplateEngine(String htmlFromTemplateEngine);
}
