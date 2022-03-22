package com.cs.config.strategy.plugin.usecase.endpoint;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.exception.validationontype.InvalidEnpointTypeException;
import com.cs.core.config.interactor.model.endpoint.IConfigDetailsForGridEndpointsModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.variants.EmptyMandatoryFieldsException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings({ "unchecked" })
public class CreateEndpoint extends AbstractOrientPlugin {
  
  protected static List<String> propertiesToExclude = Arrays.asList(IEndpointModel.SYSTEM_ID, IEndpointModel.PROCESSES,
      IEndpointModel.JMS_PROCESSES, IEndpointModel.AUTHORIZATION_MAPPING, IEndpointModel.MAPPINGS);
  
  public CreateEndpoint(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> profileMap = new HashMap<String, Object>();
    HashMap<String, Object> endpointMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    profileMap = (HashMap<String, Object>) map.get(CommonConstants.ENDPOINT);
    String endpointType = (String) profileMap.get(IEndpointModel.ENDPOINT_TYPE);
    try {
      UtilClass.validateOnType(Constants.ENDPOINT_TYPE_LIST, endpointType, false);
    }
    catch (InvalidTypeException e) {
      throw new InvalidEnpointTypeException(e);
    }
    UtilClass.checkDuplicateName((String) profileMap.get(CommonConstants.LABEL_PROPERTY), VertexLabelConstants.ENDPOINT);  
    
    
    String systemId = (String) profileMap.get(IEndpointModel.SYSTEM_ID);
    if (systemId == null || systemId.equals("")) 
    {
      throw new EmptyMandatoryFieldsException("systemId input field empty");
    }
    
    EndpointUtils.validatePhysicalCatalog(profileMap);
    
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    Vertex profileNode = UtilClass.createNode(profileMap, vertexType, propertiesToExclude);
    
    Vertex systemVertex = UtilClass.getVertexById(systemId, VertexLabelConstants.SYSTEM);
    profileNode.addEdge(RelationshipLabelConstants.HAS_SYSTEM, systemVertex);
    
    String defaultDashboardTab = null;
    if(endpointType.equals(CommonConstants.ONBOARDING_ENDPOINT)) {
       defaultDashboardTab = SystemLevelIds.ONBOARD_TAB;
    }
    else if(endpointType.equals(CommonConstants.OFFBOARDING_ENDPOINT)) {
      defaultDashboardTab = SystemLevelIds.PUBLISH_TAB;
    }
    TabUtils.addDefaultDashboardTab(profileNode, defaultDashboardTab);

    graph.commit();
    endpointMap.putAll(UtilClass.getMapFromNode(profileNode));
    endpointMap.put(IEndpointModel.SYSTEM_ID, systemId);
    endpointMap.put(IEndpoint.DASHBOARD_TAB_ID,
        defaultDashboardTab);
    Map<String, Object> referencedDashboardTabs = new HashMap<>();
    TabUtils.fillReferencedDashboardTabs(
        Arrays.asList(defaultDashboardTab), referencedDashboardTabs);
    Map<String, Object> referencedSystems = new HashMap<>();
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_SYSTEMS, referencedSystems);
    if (systemId != null && !systemId.equals("")) {
      EndpointUtils.fillReferencedSystems(Arrays.asList(systemId), referencedSystems);
    }
    returnMap.put(IGetEndpointForGridModel.ENDPOINT, endpointMap);
    returnMap.put(IGetEndpointForGridModel.CONFIG_DETAILS, configDetails);
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_DASHBOARD_TABS,
        referencedDashboardTabs);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateEndpoint/*" };
  }
}
