package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class GetKeyPerformanceIndex extends AbstractOrientPlugin {
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKeyPerformanceIndex/*" };
  }
  
  public GetKeyPerformanceIndex(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    String kpiId = (String) request.get(IIdParameterModel.ID);
    
    Vertex kpiNode = UtilClass.getVertexById(kpiId, VertexLabelConstants.GOVERNANCE_RULE_KPI);
    Map<String, Object> returnMap = GovernanceRuleUtil.getKPIFromNode(kpiNode);
    return returnMap;
  }
}
