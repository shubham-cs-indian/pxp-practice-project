package com.cs.core.runtime.interactor.model.smartdocument;

public class SmartDocumentTemplateEngineResponseModel
    implements ISmartDocumentTemplateEngineResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected String          htmlFromTemplateEngine;
  
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
}
