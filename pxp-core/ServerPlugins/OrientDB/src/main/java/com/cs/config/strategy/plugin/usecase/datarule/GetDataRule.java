package com.cs.config.strategy.plugin.usecase.datarule;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Map;

public class GetDataRule extends AbstractOrientPlugin {
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDataRule/*" };
  }
  
  public GetDataRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    String id = (String) request.get(CommonConstants.ID_PROPERTY);
    Vertex dataRuleNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.DATA_RULE);
    Map<String, Object> returnMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleNode);
    GetDataRuleUtils.fillConfigDetails(returnMap);
    return returnMap;
  }
}
