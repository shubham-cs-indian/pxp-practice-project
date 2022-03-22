package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IRules;
import com.cs.core.config.interactor.entity.governancerule.IThreshold;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveGovernanceRuleModel extends IModel {
  
  public static final String ID                   = "id";
  public static final String LABEL                = "label";
  public static final String THRESHOLD            = "threshold";
  public static final String UNIT                 = "unit";
  public static final String TASK                 = "task";
  public static final String KPI_ID               = "kpiId";
  public static final String ADDED_RULE_GROUPS    = "addedRuleGroups";
  public static final String DELETED_RULE_GROUPS  = "deletedRuleGroups";
  public static final String MODIFIED_RULE_GROUPS = "modifiedRuleGroups";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<IRules> getAddedRuleGroups();
  
  public void setAddedRuleGroups(List<IRules> addedRuleGroups);
  
  public List<String> getDeletedRuleGroups();
  
  public void setDeletedRuleGroups(List<String> deletedRuleGroups);
  
  public List<IModifiedRuleGroupModel> getModifiedRuleGroups();
  
  public void setModifiedRuleGroups(List<IModifiedRuleGroupModel> modifiedRuleGroups);
  
  public IThreshold getThreshold();
  
  public void setThreshold(IThreshold threshold);
  
  public String getUnit();
  
  public void setUnit(String unit);
  
  public String getKpiId();
  
  public void setKpiId(String kpiId);
  
  public String getTask();
  
  public void setTask(String task);
}
