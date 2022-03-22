package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetLanguageEntityConfiguration extends AbstractOrientPlugin {
  
  public GetLanguageEntityConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLanguageEntityConfiguration/*" };
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
      Vertex attributeNode = UtilClass.getVertexByIndexedId(id,
          VertexLabelConstants.ENTITY_TYPE_LANGUAGE);
      Map<String, Object> details = getLanguageEntityUsageSummary(id, attributeNode);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
      
      /* if parent has no usage then search for their child usages */
      Map<String, Object> summary = (Map<String, Object>) details
          .get(IGetEntityConfigurationResponseModel.USAGE_SUMMARY);
      if (summary.get(IUsageSummary.TOTAL_COUNT)
          .equals(0)) {
        Iterable<Vertex> childNodes = GetEntityConfigurationUtils
            .getChildNodesFromParentNodeId(attributeNode.getId()
                .toString());
        for (Vertex childNode : childNodes) {
          String cid = UtilClass.getCodeNew(childNode);
          if (cid != id) {
            Map<String, Object> childDetails = getLanguageEntityUsageSummary(
                UtilClass.getCodeNew(childNode), childNode);
            Map<String, Object> childSummary = (Map<String, Object>) childDetails
                .get(IGetEntityConfigurationResponseModel.USAGE_SUMMARY);
            if (!childSummary.get(IUsageSummary.TOTAL_COUNT)
                .equals(0)) {
              /*GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(childDetails,
              referenceData, usageSummary);*/
              responseMap.put(IGetEntityConfigurationResponseModel.HAS_CHILD_DEPENDENCY, true);
              break;
            }
          }
        }
      }
    }
    
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of language type entity
   *
   * @author yogesh.mandaokar
   * @param id
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getLanguageEntityUsageSummary(String id, Vertex attributeNode)
      throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Map<String, Object> usageSummary = GetEntityConfigurationUtils
        .getIdLabelCodeFromVertex(attributeNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    /* Rule configuration */
    GetEntityConfigurationUtils
        .elaborateRules(
            GetEntityConfigurationUtils.getVerticesByGraphAPI(attributeNode, Direction.IN,
                RelationshipLabelConstants.RULE_LANGUAGE_LINK),
            referenceData, usedBy, usageSummary);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
