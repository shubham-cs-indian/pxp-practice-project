package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class DeleteReturnModel   implements IDeleteReturnModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    success;
  protected IExceptionModel failure;
  protected List<String>    deletedUserNames;
  protected List<IAuditLogModel> auditLogInfo;

  @Override
  public List<String> getSuccess()
  {
    if (success == null) {
      success = new ArrayList<String>();
    }
    return success;
  }
  
  @Override
  public void setSuccess(List<String> ids)
  {
    
    this.success = ids;
  }
  
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

  @Override
  public void setDeletedUserNames(List<String> deletedUserNames)
  {
    this.deletedUserNames = deletedUserNames;
  }

  @Override
  public List<String> getDeletedUserNames()
  {
    if(deletedUserNames == null) {
      deletedUserNames = new ArrayList<String>();
    }
    return deletedUserNames;
  }
  
}

