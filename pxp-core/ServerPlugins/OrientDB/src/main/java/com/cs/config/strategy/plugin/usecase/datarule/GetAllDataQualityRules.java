package com.cs.config.strategy.plugin.usecase.datarule;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetAllDataQualityRules extends AbstractOrientPlugin {
  
  public GetAllDataQualityRules(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    List<String> ids = (List<String>) request.get(CommonConstants.IDS_PROPERTY);
    List<Map<String, Object>> returnMapList = new ArrayList<Map<String, Object>>();
    
    for (String id : ids) {
      Vertex dataRuleNode = UtilClass.getVertexById(id, VertexLabelConstants.DATA_RULE);
      returnMapList.add(GetDataRuleUtils.getDataRuleFromNode(dataRuleNode));
    }
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put("list", returnMapList);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllDataQualityRules/*" };
  }
}
