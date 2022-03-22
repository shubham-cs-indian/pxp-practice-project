package com.cs.core.runtime.interactor.model.klassinstance;

import java.util.ArrayList;
import java.util.List;

import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.config.interactor.model.klass.DeletedContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IDeletedContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkDeleteInstanceVariantsResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel;
import com.cs.core.runtime.interactor.model.statistics.KPIStatisticsUniqunessnessRuleEvaluationPropagationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class BulkDeleteInstanceVariantsResponseModel
    implements IBulkDeleteInstanceVariantsResponseModel {
  
  private static final long                                                 serialVersionUID          = 1L;
  protected List<String>                                                    success;
  protected IExceptionModel                                                 failure;
  protected List<IDeletedContentTypeIdsInfoModel>                           klassInstancesToUpdate;
  protected Long                                                            versionId;
  protected List<String>                                                    deletedVariantInstanceIds = new ArrayList<>();
  protected List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> evaluateContentsInfo;
  protected String                                                          rootKlassInstanceId;
  protected List<IAuditLogModel>                                            auditLogInfo;
  
  @Override
  public List<String> getSuccess()
  {
    if (success == null) {
      success = new ArrayList<>();
    }
    return success;
  }
  
  @Override
  public void setSuccess(List<String> success)
  {
    this.success = success;
  }
  
  @Override
  public IExceptionModel getFailure()
  {
    if (failure == null) {
      failure = new ExceptionModel();
    }
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public List<IDeletedContentTypeIdsInfoModel> getKlassInstancesToUpdate()
  {
    if (klassInstancesToUpdate == null) {
      klassInstancesToUpdate = new ArrayList<>();
    }
    return klassInstancesToUpdate;
  }
  
  @Override
  @JsonDeserialize(contentAs = DeletedContentTypeIdsInfoModel.class)
  public void setKlassInstancesToUpdate(
      List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate)
  {
    this.klassInstancesToUpdate = klassInstancesToUpdate;
  }
  
  @Override
  public Long getVersionId()
  {
    return versionId;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    this.versionId = versionId;
  }
  
  @Override
  public List<String> getDeletedVariantInstanceIds()
  {
    return deletedVariantInstanceIds;
  }
  
  @Override
  public void setDeletedVariantInstanceIds(List<String> deletedVariantInstanceIds)
  {
    this.deletedVariantInstanceIds = deletedVariantInstanceIds;
  }
  
  @Override
  public List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> getEvaluateContentsInfo()
  {
    if (evaluateContentsInfo == null) {
      evaluateContentsInfo = new ArrayList<>();
    }
    return evaluateContentsInfo;
  }
  
  @JsonDeserialize(contentAs = KPIStatisticsUniqunessnessRuleEvaluationPropagationModel.class)
  @Override
  public void setEvaluateContentsInfo(
      List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> evaluateContentsInfo)
  {
    this.evaluateContentsInfo = evaluateContentsInfo;
  }
  
  @Override
  public String getRootKlassInstanceId()
  {
    return rootKlassInstanceId;
  }
  
  @Override
  public void setRootKlassInstanceId(String rootKlassInstanceId)
  {
    this.rootKlassInstanceId = rootKlassInstanceId;
  }

  @Override
  public List<IAuditLogModel> getAuditLogInfo()
  {
    return this.auditLogInfo;
  }

  @Override
  @JsonDeserialize(contentAs = AuditLogModel.class)
  public void setAuditLogInfo(List<IAuditLogModel> auditLogInfo)
  {
    this.auditLogInfo = auditLogInfo;
  }
}
