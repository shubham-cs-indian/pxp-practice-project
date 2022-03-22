package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.DeleteKlassInstanceModel;
import com.cs.core.config.interactor.model.klass.DeletedContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IDeletedContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel;
import com.cs.core.runtime.interactor.model.statistics.KPIStatisticsUniqunessnessRuleEvaluationPropagationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteInstancesResponseModel implements IDeleteInstancesResponseModel {
  
  private static final long                                                 serialVersionUID          = 1L;
  
  protected List<String>                                                    success;
  protected IExceptionModel                                                 failure;
  protected List<String>                                                    deletedRelationshipInstanceIds;
  protected List<String>                                                    deletedNatureRelationshipInstanceIds;
  protected List<Map<String, Object>>                                       klassInstances;
  protected List<IDeleteKlassInstanceModel>                                 deleteRequest;
  protected List<String>                                                    deletedEventInstances;
  protected List<IDeletedContentTypeIdsInfoModel>                           klassInstancesToUpdate;
  protected List<String>                                                    deletedVariantInstanceIds = new ArrayList<>();
  protected List<String>                                                    sourceIdsToDelete;
  protected List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> evaluateContentsInfo;
  
  @Override
  public List<String> getDeletedEventInstances()
  {
    if (deletedEventInstances == null) {
      deletedEventInstances = new ArrayList<>();
    }
    return deletedEventInstances;
  }
  
  @Override
  public void setDeletedEventInstances(List<String> deletedEventInstances)
  {
    this.deletedEventInstances = deletedEventInstances;
  }
  
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
  public List<String> getDeletedRelationshipInstanceIds()
  {
    return this.deletedRelationshipInstanceIds;
  }
  
  @Override
  public void setDeletedRelationshipInstanceIds(List<String> deletedRelationshipInstanceIds)
  {
    this.deletedRelationshipInstanceIds = deletedRelationshipInstanceIds;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipInstanceIds()
  {
    return deletedNatureRelationshipInstanceIds;
  }
  
  @Override
  public void setDeletedNatureRelationshipInstanceIds(
      List<String> deletedNatureRelationshipInstanceIds)
  {
    this.deletedNatureRelationshipInstanceIds = deletedNatureRelationshipInstanceIds;
  }
  
  @Override
  public List<Map<String, Object>> getKlassInstances()
  {
    return klassInstances;
  }
  
  @Override
  public void setKlassInstances(List<Map<String, Object>> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public List<IDeleteKlassInstanceModel> getDeleteRequest()
  {
    return deleteRequest;
  }
  
  @Override
  @JsonDeserialize(contentAs = DeleteKlassInstanceModel.class)
  public void setDeleteRequest(List<IDeleteKlassInstanceModel> deleteRequest)
  {
    this.deleteRequest = deleteRequest;
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
  public List<String> getSourceIdsToDelete()
  {
    if (sourceIdsToDelete == null) {
      sourceIdsToDelete = new ArrayList<>();
    }
    return sourceIdsToDelete;
  }
  
  @Override
  public void setSourceIdsToDelete(List<String> sourceIdsToDelete)
  {
    this.sourceIdsToDelete = sourceIdsToDelete;
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
}
