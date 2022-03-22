package com.cs.core.config.interactor.model.governancerule;

import java.util.List;

import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;

public interface ICreateKeyPerformanceIndexModel extends IConfigModel {
  
  public static final String ID                     = "id";
  public static final String LABEL                  = "label";
  public static final String FREQUENCY              = "frequency";
  public static final String GOVERNANCE_RULE_BLOCKS = "governanceRuleBlocks";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getFrequency();
  
  public void setFrequency(String frequency);
  
  public List<IGovernanceRuleBlock> getGovernanceRuleBlocks();
  
  public void setGovernanceRuleBlocks(List<IGovernanceRuleBlock> governanceRuleBlocks);
}
