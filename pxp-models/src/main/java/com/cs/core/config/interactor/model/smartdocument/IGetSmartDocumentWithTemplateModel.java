package com.cs.core.config.interactor.model.smartdocument;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

public interface IGetSmartDocumentWithTemplateModel extends ISmartDocumentModel, IConfigResponseWithAuditLogModel {
  
  public static final String SMART_DOCUMENT_TEMPLATES = "smartDocumentTemplates";
  
  public List<IIdLabelTypeModel> getSmartDocumentTemplates();
  
  public void setSmartDocumentTemplates(List<IIdLabelTypeModel> smartDocumentTemplates);
}
