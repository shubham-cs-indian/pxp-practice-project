package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel extends IModel {
  
  public static final String CONTENT_ID                                   = "contentId";
  public static final String BASETYPE                                     = "basetype";
  public static final String UNIQUENESS_RULE_EVALUATION_PROPAGATION_MODEL = "uniquenessRuleEvaluationPropagationModel";
  
  String getContentId();
  
  void setContentId(String contentId);
  
  String getBasetype();
  
  void setBasetype(String basetype);
  
  Map<String, IUniquenessRuleEvaluationPropagationModel> getUniquenessRuleEvaluationPropagationModel();
  
  void setUniquenessRuleEvaluationPropagationModel(
      Map<String, IUniquenessRuleEvaluationPropagationModel> uniquenessRuleEvalutionPropagationModel);
}
