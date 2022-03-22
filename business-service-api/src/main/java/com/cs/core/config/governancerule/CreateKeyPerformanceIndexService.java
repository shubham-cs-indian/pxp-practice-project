package com.cs.core.config.governancerule;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.governancerule.GovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.entity.governancerule.IThreshold;
import com.cs.core.config.interactor.entity.governancerule.Threshold;
import com.cs.core.config.interactor.model.governancerule.ICreateKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.strategy.usecase.governancerule.ICreateKeyPerformanceIndexStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreateKeyPerformanceIndexService extends AbstractCreateConfigService<ICreateKeyPerformanceIndexModel, IGetKeyPerformanceIndexModel>
    implements ICreateKeyPerformanceIndexService {
  
  @Autowired
  protected ICreateKeyPerformanceIndexStrategy createKeyPerformanceIndexStrategy;
  
  public IGetKeyPerformanceIndexModel executeInternal(ICreateKeyPerformanceIndexModel model)
      throws Exception
  {
    String kpiId = model.getId();
    if (kpiId == null || kpiId.equals("")) {
      kpiId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.KPI.getPrefix());
      model.setId(kpiId);
    }
    
    if(model.getCode() == null || model.getCode().equals("")) {
      String code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.KPI.getPrefix());
      model.setCode(code);
    }
    
    IThreshold defaultThreshold = new Threshold();
    defaultThreshold.setLower(0);
    defaultThreshold.setUpper(100);
    List<IGovernanceRuleBlock> governanceRuleBlocks = new ArrayList<>();
    for (String governanceRuleBlockType : CommonConstants.GOVERNANCE_RULE_BLOCK_TYPES) {
      IGovernanceRuleBlock ruleBlock = new GovernanceRuleBlock();
      ruleBlock.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RULE.getPrefix()));
      ruleBlock.setType(governanceRuleBlockType);
      ruleBlock.setThreshold(defaultThreshold);
      ruleBlock.setUnit("percentage");
      governanceRuleBlocks.add(ruleBlock);
    }
    model.setGovernanceRuleBlocks(governanceRuleBlocks);
    
    return createKeyPerformanceIndexStrategy.execute(model);
  }
}
