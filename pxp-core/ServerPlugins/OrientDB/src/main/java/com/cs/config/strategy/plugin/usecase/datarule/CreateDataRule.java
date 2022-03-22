package com.cs.config.strategy.plugin.usecase.datarule;

import com.cs.config.strategy.plugin.usecase.datarule.util.CreateDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.validationontype.InvalidDataRuleTypeException;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.exception.InvalidTypeException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.HashMap;
import java.util.Map;

public class CreateDataRule extends AbstractOrientPlugin {
  
  public CreateDataRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    
    HashMap<String, Object> map = new HashMap<String, Object>();
    OrientGraph graph = UtilClass.getGraph();
    map = (HashMap<String, Object>) request.get(CommonConstants.RULE_PROPERTY);
    
    try {
      UtilClass.validateOnType(Constants.Rule_TYPE_LIST, (String) request.get(IDataRule.TYPE),
          true);
    }
    catch (InvalidTypeException e) {
      throw new InvalidDataRuleTypeException(e);
    }
    
    Map<String, Object> returnMap = CreateDataRuleUtils.createDataRuleNode(map);
    GetDataRuleUtils.fillConfigDetails(returnMap);
    graph.commit();
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateDataRule/*" };
  }
}
