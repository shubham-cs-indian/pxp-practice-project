package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.klass.IDeletedContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel;

import java.util.List;
import java.util.Map;

public interface IDeleteInstancesResponseModel extends IDeleteKlassInstanceResponseModel {
  
  public static final String DELETED_RELATIONSHIP_INSTANCE_IDS        = "deletedRelationshipInstanceIds";
  public static final String DELETED_NATURE_RELATIONSHIP_INSTANCE_IDS = "deletedNatureRelationshipInstanceIds";
  public static final String KLASS_INSTANCES                          = "klassInstances";
  public static final String DELETE_REQUEST                           = "deleteRequest";
  public static final String DELETED_EVENT_INSTANCES                  = "deletedEventInstances";
  public static final String KLASS_INSTANCES_TO_UPDATE                = "klassInstancesToUpdate";
  public static final String DELETED_VARIANT_INSTANCE_IDS             = "deletedVariantInstanceIds";
  public static final String SOURCE_IDS_TO_DELETE                     = "sourceIdsToDelete";
  public static final String EVALUATE_CONTENTS_INFO                   = "evaluateContentsInfo";
  
  public List<String> getDeletedRelationshipInstanceIds();
  
  public void setDeletedRelationshipInstanceIds(List<String> deletedRelationshipInstanceIds);
  
  public List<String> getDeletedNatureRelationshipInstanceIds();
  
  public void setDeletedNatureRelationshipInstanceIds(
      List<String> deletedNatureRelationshipInstanceIds);
  
  public List<Map<String, Object>> getKlassInstances();
  
  public void setKlassInstances(List<Map<String, Object>> klassInstances);
  
  public List<IDeleteKlassInstanceModel> getDeleteRequest();
  
  public void setDeleteRequest(List<IDeleteKlassInstanceModel> deleteRequest);
  
  public List<String> getDeletedEventInstances();
  
  public void setDeletedEventInstances(List<String> deletedEventInstances);
  
  public List<IDeletedContentTypeIdsInfoModel> getKlassInstancesToUpdate();
  
  public void setKlassInstancesToUpdate(
      List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate);
  
  public List<String> getDeletedVariantInstanceIds();
  
  public void setDeletedVariantInstanceIds(List<String> deletedVariantInstanceIds);
  
  public List<String> getSourceIdsToDelete();
  
  public void setSourceIdsToDelete(List<String> sourceIdsToDelete);
  
  public List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> getEvaluateContentsInfo();
  
  public void setEvaluateContentsInfo(
      List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> evaluateContentsInfo);
}
