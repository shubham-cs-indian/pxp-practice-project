package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.offboarding.IOffBoardingTransferRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetOffboardingEndpointForUser extends AbstractOrientPlugin {
  
  public GetOffboardingEndpointForUser(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, String> userEndpointMap = new HashMap<>();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER
            + " order by lastname asc, firstname.toLowerCase() asc"))
        .execute();
    for (Vertex userNode : resultIterable) {
      Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
      for (Vertex roleNode : roleNodes) {
        Iterable<Vertex> iteratorNode = roleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_ENDPOINT);
        for (Vertex endpoint : iteratorNode) {
          String endpointType = endpoint.getProperty(IEndpoint.ENDPOINT_TYPE);
          if (endpointType != null && endpointType.equals(CommonConstants.OFFBOARDING_ENDPOINT)) {
            /*userEndpointMap.put(UtilClass.getCodeNew(userNode),
                endpoint.getProperty(IEndpoint.ENDPOINT_INDEX));*/
          }
        }
      }
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IOffBoardingTransferRequestModel.USER_ENDPOINT_MAP, userEndpointMap);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOffboardingEndpointForUser/*" };
  }
}
