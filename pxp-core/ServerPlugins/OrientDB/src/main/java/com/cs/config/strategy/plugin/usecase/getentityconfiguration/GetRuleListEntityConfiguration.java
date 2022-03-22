package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;



public class GetRuleListEntityConfiguration extends AbstractOrientPlugin {
  
  public GetRuleListEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRuleListEntityConfiguration/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> Ids = (List<String>) requestMap.get(IGetEntityConfigurationRequestModel.IDS);
    Map<String, Object> responseMap = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Map<String, Object>> usageSummary = new ArrayList<Map<String, Object>>();
    for (String id : Ids) {
      Map<String, Object> details = getRuleListEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
    
  }
  
  protected Map<String, Object> getRuleListEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex ruleListNode = (Vertex) UtilClass.getVertexByIndexedId(id,
        VertexLabelConstants.RULE_LIST);
    Map<String, Object> usageSummary = GetEntityConfigurationUtils.getUsageSummary(ruleListNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    String nodeId = ruleListNode.getId()
        .toString();
    
    String query = GetEntityConfigurationUtils.getThreeLevelQuery(nodeId, Direction.IN,
        Direction.IN, Direction.IN, RelationshipLabelConstants.HAS_RULE_LIST,
        RelationshipLabelConstants.RULE_LINK, RelationshipLabelConstants.ATTRIBUTE_DATA_RULE);
    GetEntityConfigurationUtils.elaborateRules(
        GetEntityConfigurationUtils.getVerticesByQuery(query), referenceData, usedBy, usageSummary);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
  
}
