package com.cs.core.runtime.interactor.model.statistics;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IUniquenessRuleEvaluationPropagationModel extends IModel {
  
  public static final String KPI_Id             = "kpiId";
  public static final String INSTANCE_TO_ADD    = "instanceToAdd";
  public static final String INSTANCE_TO_REMOVE = "instanceToRemove";
  
  public String getKpiId();
  
  public void setKpiId(String kpiId);
  
  // Key for Map is ruleId
  public Map<String, IIdAndTypeModel> getInstanceToAdd();
  
  public void setInstanceToAdd(Map<String, IIdAndTypeModel> instanceIdToAdd);
  
  // Key for Map is ruleId
  public Map<String, IIdAndTypeModel> getInstanceToRemove();
  
  public void setInstanceToRemove(Map<String, IIdAndTypeModel> instanceIdToRemove);
}
