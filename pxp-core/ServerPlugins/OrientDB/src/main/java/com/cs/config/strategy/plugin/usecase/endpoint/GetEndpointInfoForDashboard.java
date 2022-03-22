package com.cs.config.strategy.plugin.usecase.endpoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetEndpointInfoForDashboard extends AbstractOrientPlugin {
  
  public GetEndpointInfoForDashboard(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEndpointInfoForDashboard/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String endpointId = (String) requestMap.get(IDataIntegrationRequestModel.ENDPOINT_ID);
    Vertex endpointNode = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
    
    Map<String, Object> endpointMap = new HashMap<>();
    endpointMap.put(IIdLabelTypeModel.ID,
        endpointNode.getProperty(CommonConstants.CODE_PROPERTY));
    endpointMap.put(IIdLabelTypeModel.CODE,
        endpointNode.getProperty(CommonConstants.CODE_PROPERTY));
    endpointMap.put(IIdLabelTypeModel.TYPE, endpointNode.getProperty(IEndpointModel.ENDPOINT_TYPE));
    endpointMap.put(IIdLabelTypeModel.LABEL,
        UtilClass.getValueByLanguage(endpointNode, IEndpointModel.LABEL));
    List<Map<String, Object>> endpointsList = new ArrayList<>();
    endpointsList.add(endpointMap);
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGetEndointsInfoModel.ENDPOINTS, endpointsList);
    return returnMap;
  }
}
