package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class Orient_Migration_Script_24_11_2017 extends AbstractOrientPlugin {
  
  /*
  Fix related to removing validator property from attributes other than HTML and Image..
  Date: 24-11-2016
   */
  
  public Orient_Migration_Script_24_11_2017(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> response = new HashMap<>();
    Iterable<Vertex> iterable = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, true);
    for (Vertex attributeNode : iterable) {
      String type = attributeNode.getProperty(CommonConstants.TYPE_PROPERTY);
      manageOtherAttributesThanHTMLAndImage(type, attributeNode);
    }
    UtilClass.getGraph()
        .commit();
    response.put("status", "SUCCESS");
    return response;
  }
  
  private void manageOtherAttributesThanHTMLAndImage(String oldType, Vertex attributeNode)
  {
    if (!oldType.equals(CommonConstants.HTML_TYPE_ATTRIBUTE)
        && !oldType.equals(CommonConstants.IMAGE_TYPE_ATTRIBUTE)) {
      attributeNode.removeProperty(CommonConstants.VALIDATOR_PROPERTY);
      attributeNode.removeProperty("characterLimit");
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_24_11_2017/*" };
  }
}
