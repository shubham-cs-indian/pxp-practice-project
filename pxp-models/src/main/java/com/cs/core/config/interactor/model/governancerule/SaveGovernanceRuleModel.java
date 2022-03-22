package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.governancerule.IRules;
import com.cs.core.config.interactor.entity.governancerule.IThreshold;
import com.cs.core.config.interactor.entity.governancerule.Rules;
import com.cs.core.config.interactor.entity.governancerule.Threshold;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class SaveGovernanceRuleModel implements ISaveGovernanceRuleModel {
  
  /**
   *
   */
  private static final long               serialVersionUID = 1L;
  
  protected String                        id;
  protected String                        label;
  protected IThreshold                    threshold;
  protected String                        unit;
  protected String                        kpiId;
  protected String                        task;
  protected List<IRules>                  addedRuleGroups;
  protected List<String>                  deletedRuleGroups;
  protected List<IModifiedRuleGroupModel> modifiedRuleGroups;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public IThreshold getThreshold()
  {
    return threshold;
  }
  
  @JsonDeserialize(as = Threshold.class)
  @Override
  public void setThreshold(IThreshold threshold)
  {
    this.threshold = threshold;
  }
  
  @Override
  public String getUnit()
  {
    return unit;
  }
  
  @Override
  public void setUnit(String unit)
  {
    this.unit = unit;
  }
  
  @Override
  public String getKpiId()
  {
    return kpiId;
  }
  
  @Override
  public void setKpiId(String kpiId)
  {
    this.kpiId = kpiId;
  }
  
  @Override
  public String getTask()
  {
    return task;
  }
  
  @Override
  public void setTask(String task)
  {
    this.task = task;
  }
  
  @Override
  public List<IRules> getAddedRuleGroups()
  {
    return addedRuleGroups;
  }
  
  @JsonDeserialize(contentAs = Rules.class)
  @Override
  public void setAddedRuleGroups(List<IRules> addedRuleGroups)
  {
    this.addedRuleGroups = addedRuleGroups;
  }
  
  @Override
  public List<String> getDeletedRuleGroups()
  {
    return deletedRuleGroups;
  }
  
  @Override
  public void setDeletedRuleGroups(List<String> deletedRuleGroups)
  {
    this.deletedRuleGroups = deletedRuleGroups;
  }
  
  @Override
  public List<IModifiedRuleGroupModel> getModifiedRuleGroups()
  {
    return modifiedRuleGroups;
  }
  
  @JsonDeserialize(contentAs = ModifiedRuleGroupModel.class)
  @Override
  public void setModifiedRuleGroups(List<IModifiedRuleGroupModel> modifiedRuleGroups)
  {
    this.modifiedRuleGroups = modifiedRuleGroups;
  }
}
