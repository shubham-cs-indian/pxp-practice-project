package com.cs.core.config.interactor.model.smartdocument.template;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;

public interface IGetSmartDocumentTemplateWithPresetModel extends ISmartDocumentTemplateModel, IConfigResponseWithAuditLogModel {
  
  public static final String SMART_DOCUMENT_PRESETS = "smartDocumentPresets";
  
  public List<IIdLabelTypeModel> getSmartDocumentPresets();
  
  public void setSmartDocumentPresets(List<IIdLabelTypeModel> smartDocumentPresets);
}
