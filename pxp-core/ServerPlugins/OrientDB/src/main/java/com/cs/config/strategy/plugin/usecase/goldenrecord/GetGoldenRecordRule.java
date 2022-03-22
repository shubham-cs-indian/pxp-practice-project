package com.cs.config.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.exception.goldenrecord.GoldenRecordRuleNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetGoldenRecordRule extends AbstractOrientPlugin {
  
  public GetGoldenRecordRule(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String ruleId = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    Vertex goldenRecordRuleNode = null;
    try {
      goldenRecordRuleNode = UtilClass.getVertexById(ruleId,
          VertexLabelConstants.GOLDEN_RECORD_RULE);
    }
    catch (NotFoundException e) {
      throw new GoldenRecordRuleNotFoundException();
    }
    
    returnMap = GoldenRecordRuleUtil.getGoldenRecordRuleFromNode(goldenRecordRuleNode);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGoldenRecordRule/*" };
  }
}
