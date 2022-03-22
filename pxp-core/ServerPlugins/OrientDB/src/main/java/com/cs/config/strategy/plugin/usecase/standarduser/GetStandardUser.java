package com.cs.config.strategy.plugin.usecase.standarduser;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetStandardUser extends AbstractOrientPlugin {
  
  public GetStandardUser(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = null;
    String userId = "";
    userId = (String) requestMap.get("id");
    Vertex userNode = UtilClass.getVertexByIndexedId(userId,
        VertexLabelConstants.ENTITY_STANDARD_USER);
    if (userNode != null) {
      returnMap = new HashMap<String, Object>();
      returnMap.putAll(UtilClass.getMapFromNode(userNode));
      UserUtils.getPreferredLanguages(returnMap, userNode);
    }
    
    // UtilClass.getGraph().commit();
    // if(returnMap != null){
    // result=ObjectMapperUtil.writeValueAsString(returnMap);
    // }
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetStandardUser/*" };
  }
}
