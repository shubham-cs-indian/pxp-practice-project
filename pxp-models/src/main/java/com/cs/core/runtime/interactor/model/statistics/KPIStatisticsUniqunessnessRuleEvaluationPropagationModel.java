package com.cs.core.runtime.interactor.model.statistics;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class KPIStatisticsUniqunessnessRuleEvaluationPropagationModel
    implements IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel {
  
  private static final long                                      serialVersionUID = 1L;
  private String                                                 contentId;
  private String                                                 basetype;
  private Map<String, IUniquenessRuleEvaluationPropagationModel> uniquenessRuleEvaluationPropagationModel;
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel#getContentId()
   */
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel#setContentId(java.lang.String)
   */
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel#getBasetype()
   */
  @Override
  public String getBasetype()
  {
    return basetype;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel#setBasetype(java.lang.String)
   */
  @Override
  public void setBasetype(String basetype)
  {
    this.basetype = basetype;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel#getUniquenessRuleEvalutionPropagationModel()
   */
  public Map<String, IUniquenessRuleEvaluationPropagationModel> getUniquenessRuleEvaluationPropagationModel()
  {
    if (uniquenessRuleEvaluationPropagationModel == null) {
      uniquenessRuleEvaluationPropagationModel = new HashMap<>();
    }
    return uniquenessRuleEvaluationPropagationModel;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel#setUniquenessRuleEvalutionPropagationModel(java.util.Map)
   */
  @Override
  @JsonDeserialize(contentAs = UniquenessRuleEvaluationPropagationModel.class)
  public void setUniquenessRuleEvaluationPropagationModel(
      Map<String, IUniquenessRuleEvaluationPropagationModel> uniquenessRuleEvaluationPropagationModel)
  {
    this.uniquenessRuleEvaluationPropagationModel = uniquenessRuleEvaluationPropagationModel;
  }
}
