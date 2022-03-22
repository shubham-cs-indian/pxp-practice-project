
package com.cs.imprt.config.strategy.plugin.usecase.process;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.constants.application.OnboardingConstants;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdRequestModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetEndpointIdsOfCustomTypeProcess extends AbstractOrientPlugin {
  
  public GetEndpointIdsOfCustomTypeProcess(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> processDefinitionInfo = new HashMap<String, Object>();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<String> processIds = (List<String>) requestMap
        .get(IGetEndpointsAndOrganisationIdRequestModel.PROCESS_IDS);
    String userID = (String) requestMap.get(IGetEndpointsAndOrganisationIdRequestModel.USER_ID);
    
    for (String processId : processIds) {
      Vertex processNode = UtilClass.getVertexById(processId, VertexLabelConstants.PROCESS_EVENT);
      Iterable<Vertex> vertices = processNode.getVertices(Direction.IN,
          RelationshipLabelConstants.PROFILE_PROCESS_LINK);
      for (Vertex endpoint : vertices) {
        if (endpoint.getProperty(IEndpointModel.ENDPOINT_TYPE)
            .equals(OnboardingConstants.ONBOARDING_ENDPOINT_TYPE)) {
          Map<String, Object> processDefinitionInfoMap = new HashMap<String, Object>();
          processDefinitionInfoMap.put(ProcessConstants.PROCESS_ID, processId);
          processDefinitionInfoMap.put(ProcessConstants.ENDPOINT_ID,
              UtilClass.getCodeNew(endpoint));
          processDefinitionInfo.put(processNode.getProperty(IProcessEvent.PROCESS_DEFINITION_ID),
              processDefinitionInfoMap);
        }
      }
    }
    String organization = null;
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select expand(in('Organization_Role_Link')) from "
            + "(select expand(out('User_In')) from User where code='" + userID + "')"))
        .execute();
    
    if (resultIterable.iterator()
        .hasNext()) {
      Vertex orgVertex = resultIterable.iterator()
          .next();
      organization = (String) orgVertex.getProperty("code");
    }
    if (organization == null)
      organization = "-1";
    returnMap.put(IGetEndpointsAndOrganisationIdResponseModel.PROCESS_DEFINITION_INFO,
        processDefinitionInfo);
    returnMap.put(IGetEndpointsAndOrganisationIdResponseModel.ORGANISATION_ID, organization);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEndpointIdsOfCustomTypeProcess/*" };
  }
}
