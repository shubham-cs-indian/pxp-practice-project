package com.cs.config.strategy.plugin.usecase.user;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetUsers extends AbstractOrientPlugin {
  
  public GetUsers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<Map<String, Object>> usersList = new ArrayList<>();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER
            + " order by lastname asc, firstname.toLowerCase() asc"))
        .execute();
    for (Vertex userNode : resultIterable) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.putAll(UtilClass.getMapFromNode(userNode));
      UserUtils.getPreferredLanguages(map, userNode);
      usersList.add(map);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put(IListModel.LIST, usersList);
    UtilClass.getGraph()
        .commit();
    
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUsers/*" };
  }
}
