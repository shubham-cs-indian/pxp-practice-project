package com.cs.config.strategy.plugin.usecase.base;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesResponseModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetTagTagValues extends AbstractOrientPlugin {
  
  public GetTagTagValues(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTagTagValues/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    Map<String, Object> tagTagValuesMap = new HashMap<String, Object>();
    List<String> tagCodesList = (List<String>) requestMap.get(IGetTagTagValuesRequestModel.LIST);
    StringBuilder tagCodes = preProcessMap(tagCodesList);
    String query = "Select from " + VertexLabelConstants.ENTITY_TAG + " where "
        + CommonConstants.CODE_PROPERTY + " IN " + tagCodes;
    Iterable<Vertex> iterable = executeQuery(query);
    
    for (Vertex vertex : iterable) {
      ArrayList<String> tagValues = new ArrayList<String>();
      Iterable<Vertex> vertices = vertex.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      UtilClass.getListOfIds(vertices, tagValues);
      tagTagValuesMap.put(vertex.getProperty(CommonConstants.CODE_PROPERTY)
          .toString(), tagValues);
    }
    responseMap.put(IGetTagTagValuesResponseModel.MAP, tagTagValuesMap);
    
    return responseMap;
  }
  
  private Iterable<Vertex> executeQuery(String query)
  {
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return iterable;
  }
  
  private StringBuilder preProcessMap(List<String> list)
  {
    StringBuilder listString = new StringBuilder();
    listString.append("[");
    for (int i = 0; i < list.size(); i++) {
      listString.append("\"");
      listString.append(list.get(i));
      listString.append("\"");
      if (i < list.size() - 1) {
        listString.append(",");
      }
    }
    listString.append("]");
    return listString;
  }
}
