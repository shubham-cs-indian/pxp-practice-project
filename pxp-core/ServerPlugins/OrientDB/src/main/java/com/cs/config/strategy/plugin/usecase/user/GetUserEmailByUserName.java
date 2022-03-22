package com.cs.config.strategy.plugin.usecase.user;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.exception.user.UserNameFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetUserEmailByUserName extends AbstractOrientPlugin{

  public GetUserEmailByUserName(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
     return new String[] { "POST|GetUserEmailByUserName/*" };

  }

  @Override
    protected Object execute(Map<String, Object> requestMap) throws Exception
    {
    String username = null;
    Map<String, Object> returnMap = new HashMap<>();
    username = (String) requestMap.get("userName");
    username = username.trim();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(
            new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER + " where " + IUser.USER_NAME + " = '" + username + "'"))
        .execute();
    Iterator<Vertex> iterator = resultIterable.iterator();
    Vertex userNode = iterator.next();
    returnMap.putAll(UtilClass.getMapFromNode(userNode));
    UserUtils.getPreferredLanguages(returnMap, userNode);
    return returnMap;
    
  }
  
}
