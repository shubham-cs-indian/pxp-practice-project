package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IDeletedContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel;

import java.util.List;

public interface IBulkDeleteInstanceVariantsResponseModel extends IBulkDeleteReturnModel {
  
  String KLASS_INSTANCES_TO_UPDATE    = "klassInstancesToUpdate";
  String VERSION_ID                   = "versionId";
  String DELETED_VARIANT_INSTANCE_IDS = "deletedVariantInstanceIds";
  String EVALUATE_CONTENTS_INFO       = "evaluateContentsInfo";
  String ROOT_KLASS_INSTANCE_ID       = "rootKlassInstanceId";
  
  public List<IDeletedContentTypeIdsInfoModel> getKlassInstancesToUpdate();
  
  public void setKlassInstancesToUpdate(
      List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate);
  
  public Long getVersionId();
  
  public void setVersionId(Long versionId);
  
  public List<String> getDeletedVariantInstanceIds();
  
  public void setDeletedVariantInstanceIds(List<String> deletedVariantInstanceIds);
  
  public List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> getEvaluateContentsInfo();
  
  public void setEvaluateContentsInfo(
      List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> evaluateContentsInfo);
  
  public String getRootKlassInstanceId();
  
  public void setRootKlassInstanceId(String rootKlassInstanceId);
}
