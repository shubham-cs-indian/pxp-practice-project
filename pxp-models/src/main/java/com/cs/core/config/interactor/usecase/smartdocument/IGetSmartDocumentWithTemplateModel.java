package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

import java.util.List;

public interface IGetSmartDocumentWithTemplateModel extends ISmartDocumentModel {
  
  public static final String SMART_DOCUMENT_TEMPLATES = "smartDocumentTemplates";
  
  public List<IIdLabelTypeModel> getSmartDocumentTemplates();
  
  public void setSmartDocumentTemplates(List<IIdLabelTypeModel> smartDocumentTemplates);
}
