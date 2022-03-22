package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

public interface IBulkCreateTaxonomyResponseModel extends IBulkResponseModel, IConfigResponseWithAuditLogModel {
  
  public static final String AUDIT_LOG_INFO = "auditLogInfo";
  public void setSuccess(IBulkCreateTaxonomyListResponseModel success);
}
