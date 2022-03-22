package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUserEntityConfigurationPlugin extends AbstractOrientPlugin {
  
  public GetUserEntityConfigurationPlugin(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUserEntityConfigurationPlugin/*" };
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
      Map<String, Object> details = getUserEntityUsageSummary(id);
      GetEntityConfigurationUtils.setReferenceDataAndUsageSummary(details, referenceData,
          usageSummary);
    }
    responseMap.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    responseMap.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return responseMap;
  }
  
  /**
   * Description: get usage summary of user type entity
   *
   * @author yogesh.mandaokar
   * @param id
   * @return
   * @throws Exception
   */
  protected Map<String, Object> getUserEntityUsageSummary(String id) throws Exception
  {
    Map<String, Object> details = new HashMap<String, Object>();
    Map<String, Object> referenceData = new HashMap<String, Object>();
    List<Object> usedBy = new ArrayList<Object>();
    Vertex attributeNode = UtilClass.getVertexByIndexedId(id,
        VertexLabelConstants.ENTITY_TYPE_USER);
    Map<String, Object> usageSummary = GetEntityConfigurationUtils
        .getIdLabelCodeFromVertex(attributeNode);
    
    /* set label to user */
    Map<String, Object> labelObject = UtilClass
        .getMapFromVertex(Arrays.asList(CommonConstants.USER_NAME_PROPERTY), attributeNode);
    usageSummary.put(IIdLabelCodeModel.LABEL, labelObject.get(CommonConstants.USER_NAME_PROPERTY));
    
    usageSummary.put(IUsageSummary.TOTAL_COUNT, 0);
    
    /* Role configuration */
    GetEntityConfigurationUtils.setByGraphAPI(attributeNode, Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.ROLE);
    
    usageSummary.put(IUsageSummary.USED_BY, usedBy);
    details.put(IGetEntityConfigurationResponseModel.REFERENCE_DATA, referenceData);
    details.put(IGetEntityConfigurationResponseModel.USAGE_SUMMARY, usageSummary);
    return details;
  }
}
