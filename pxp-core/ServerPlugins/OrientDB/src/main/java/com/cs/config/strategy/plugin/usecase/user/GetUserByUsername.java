package com.cs.config.strategy.plugin.usecase.user;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.user.UserNameFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.Iterator;
import java.util.Map;

public class GetUserByUsername extends AbstractOrientPlugin {
  
  public GetUserByUsername(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userName = null;
    Map<String, Object> returnMap = null;
    userName = (String) requestMap.get("username");
    userName = userName.trim();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER + " where "
            + CommonConstants.USER_NAME_PROPERTY + " = '" + userName + "'"))
        .execute();
    Iterator<Vertex> iterator = resultIterable.iterator();
    
    while (iterator.hasNext()) {
      throw new UserNameFoundException();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUserByUsername/*" };
  }
}
