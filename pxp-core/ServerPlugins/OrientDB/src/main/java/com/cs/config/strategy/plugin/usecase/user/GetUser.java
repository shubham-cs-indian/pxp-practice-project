package com.cs.config.strategy.plugin.usecase.user;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ILanguage;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class GetUser extends AbstractOrientPlugin {
  
  public GetUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = null;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    userId = (String) requestMap.get("id");
    try {
      Vertex userNode = UtilClass.getVertexByIndexedId(userId, VertexLabelConstants.ENTITY_TYPE_USER);
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      UserUtils.getPreferredLanguages(returnMap, userNode);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    return returnMap;
  }

  
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetUser/*" };
  }
}
