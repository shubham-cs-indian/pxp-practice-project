package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IdAndTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class UniquenessRuleEvaluationPropagationModel
    implements IUniquenessRuleEvaluationPropagationModel {
  
  private static final long            serialVersionUID = 1L;
  private String                       kpiId;
  private Map<String, IIdAndTypeModel> instanceToAdd;
  private Map<String, IIdAndTypeModel> instanceToRemove;
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IUniquenessRuleEvaluationPropagationModel#getKpiId()
   */
  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IUniquenessRuleEvaluationPropagationModel#setKpiId(java.lang.String)
   */
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IUniquenessRuleEvaluationPropagationModel#getInstanceIdToAdd()
   */
  @Override
  public Map<String, IIdAndTypeModel> getInstanceToAdd()
  {
    if (instanceToAdd == null) {
      instanceToAdd = new HashMap<>();
    }
    return instanceToAdd;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IUniquenessRuleEvaluationPropagationModel#setInstanceIdToAdd(com.cs.base.interactor.model.IIdAndTypeModel)
   */
  @Override
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  public void setInstanceToAdd(Map<String, IIdAndTypeModel> instanceIdToAdd)
  {
    this.instanceToAdd = instanceIdToAdd;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IUniquenessRuleEvaluationPropagationModel#getInstanceIdToRemove()
   */
  @Override
  public Map<String, IIdAndTypeModel> getInstanceToRemove()
  {
    if (instanceToRemove == null) {
      instanceToRemove = new HashMap<>();
    }
    return instanceToRemove;
  }
  
  /* (non-Javadoc)
   * @see com.cs.runtime.interactor.model.statistics.IUniquenessRuleEvaluationPropagationModel#setInstanceIdToRemove(com.cs.base.interactor.model.IIdAndTypeModel)
   */
  @Override
  @JsonDeserialize(contentAs = IdAndTypeModel.class)
  public void setInstanceToRemove(Map<String, IIdAndTypeModel> instanceIdToRemove)
  {
    this.instanceToRemove = instanceIdToRemove;
  }
}
