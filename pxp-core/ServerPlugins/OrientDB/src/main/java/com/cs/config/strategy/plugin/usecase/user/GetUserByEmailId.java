package com.cs.config.strategy.plugin.usecase.user;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.exception.user.UserNameFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GetUserByEmailId extends AbstractOrientPlugin {
  
  public GetUserByEmailId(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String emailId = null;
    Map<String, Object> returnMap = new HashMap<>();
    emailId = (String) requestMap.get(IUser.EMAIL);
    emailId = emailId.trim();
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_USER + " where "
            + IUser.EMAIL + " = '" + emailId + "'"))
        .execute();
    Iterator<Vertex> iterator = resultIterable.iterator();
    Vertex userNode = iterator.next();
    returnMap.putAll(UtilClass.getMapFromNode(userNode));
    //UserUtils.getPreferredLanguages(returnMap, userNode);
    while (iterator.hasNext()) {
      throw new UserNameFoundException();
    }
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUserByEmailId/*" };
  }
}
