package com.cs.core.config.interactor.model.smartdocument.template;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetSmartDocumentTemplateWithPresetModel extends SmartDocumentTemplateModel
    implements IGetSmartDocumentTemplateWithPresetModel {
  
  private static final long         serialVersionUID     = 1L;
  protected List<IIdLabelTypeModel> smartDocumentPresets = new ArrayList<>();
  protected List<IAuditLogModel> auditLogInfo;
  
  @Override
  public List<IIdLabelTypeModel> getSmartDocumentPresets()
  {
    return smartDocumentPresets;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setSmartDocumentPresets(List<IIdLabelTypeModel> smartDocumentPresets)
  {
    this.smartDocumentPresets = smartDocumentPresets;
  }
  
  @Override
  public List<IAuditLogModel> getAuditLogInfo()
  {
    if (auditLogInfo == null) {
      auditLogInfo = new ArrayList<>();
    }
    return auditLogInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = AuditLogModel.class)
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo)
  {
    this.auditLogInfo = auditLogInfo;
  }
  
}
