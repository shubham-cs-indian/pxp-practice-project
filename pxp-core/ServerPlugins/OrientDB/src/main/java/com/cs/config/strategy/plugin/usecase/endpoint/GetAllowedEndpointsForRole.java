package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllowedEndpointsForRole extends AbstractOrientPlugin {
  
  private static List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IEndpointModel.ENDPOINT_TYPE, IEndpointModel.LABEL);
  
  public GetAllowedEndpointsForRole(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String organizationId = (String) requestMap.get(IIdParameterModel.ID);
    Vertex organizationVertex = UtilClass.getVertexById(organizationId,
        VertexLabelConstants.ORGANIZATION);
    
    Iterable<Vertex> organizationEndpoints = organizationVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AVAILABLE_ENDPOINTS);
    List<Map<String, Object>> endpointList = new ArrayList<>();
    for (Vertex organizationEndpoint : organizationEndpoints) {
      endpointList.add(UtilClass.getMapFromVertex(fieldsToFetch, organizationEndpoint));
    }
    if (endpointList.isEmpty()) {
      String query = "select from " + VertexLabelConstants.ENDPOINT + " order by "
          + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
      organizationEndpoints = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex organizationEndpoint : organizationEndpoints) {
        endpointList.add(UtilClass.getMapFromVertex(fieldsToFetch, organizationEndpoint));
      }
    }
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IListModel.LIST, endpointList);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedEndpointsForRole/*" };
  }
}
