package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BulkCreateTaxonomyResponseModel extends ConfigResponseWithAuditLogModel implements IBulkCreateTaxonomyResponseModel {
	
	public BulkCreateTaxonomyResponseModel() {}
  
  private static final long                    serialVersionUID = 1L;
  protected IExceptionModel                    failure = new ExceptionModel();
  protected IBulkCreateTaxonomyListResponseModel success;
  protected List<IAuditLogModel>             auditLogInfo;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public Object getSuccess()
  {
    return success;
  }
  
  @JsonCreator
  @JsonDeserialize(as = BulkCreateTaxonomyListResponseModel.class)
  @Override
  public void setSuccess(IBulkCreateTaxonomyListResponseModel success)
  {
    this.success = success;
  }
}
