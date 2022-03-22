package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPartnerEntityConfigurationPlugin extends AbstractOrientPlugin {
  
  public GetPartnerEntityConfigurationPlugin(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPartnerEntityConfigurationPlugin/*" };
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
      Map<String, Object> details = getPartnerEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of partner type entity
   *
   * @author yogesh.mandaokar
   * @param id
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getPartnerEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex partnerNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ORGANIZATION);
    Map<String, Object> usageSummary = UtilClass.getMapFromVertex(
        Arrays.asList(IUsageSummary.CODE, IUsageSummary.LABEL, CommonConstants.CODE_PROPERTY),
        partnerNode);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    String nodeId = partnerNode.getId()
        .toString();
    
    /* Rule configuration */
    GetEntityConfigurationUtils.elaborateRules(
        GetEntityConfigurationUtils.getVerticesByGraphAPI(partnerNode, Direction.IN,
            RelationshipLabelConstants.ORGANISATION_RULE_LINK),
        referenceData, usedBy, usageSummary);
    
    /* KPI configuration */
    GetEntityConfigurationUtils.setByGraphAPI(partnerNode, Direction.IN,
        RelationshipLabelConstants.KPI_ORGANISATION_LINK, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KPI);
    
    /* Golden Record configuration */
    GetEntityConfigurationUtils.setByGraphAPI(partnerNode, Direction.IN,
        RelationshipLabelConstants.GOLDEN_RECORD_RULE_ORGANIZATION_LINK, referenceData, usedBy,
        usageSummary, EntityConfigurationConstants.GOLDEN_RECORD);
    
    /* Golden Record configuration merge*/
    GetEntityConfigurationUtils.setGoldenRecordMergeConfiguration(nodeId, referenceData, usedBy,
        usageSummary, VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP,
        EntityConfigurationConstants.GOLDEN_RECORD_MERGE,
        RelationshipLabelConstants.EFFECT_TYPE_ORGANIZATION_LINK);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
