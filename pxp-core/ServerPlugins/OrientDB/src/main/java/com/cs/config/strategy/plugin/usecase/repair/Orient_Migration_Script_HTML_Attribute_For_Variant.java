package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_HTML_Attribute_For_Variant extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_HTML_Attribute_For_Variant(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
    // TODO Auto-generated constructor stub
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_HTML_Attribute_For_Variant/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    // TODO Auto-generated method stub
    List<String> htmlAttributes = new ArrayList<>();
    
    String query = "select code from " + VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE + " where "
        + CommonConstants.TYPE_PROPERTY + " in \"" + CommonConstants.HTML_TYPE_ATTRIBUTE + "\"";
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : vertices) {
      htmlAttributes.add(vertex.getProperty("code"));
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IIdsListParameterModel.IDS, htmlAttributes);
    return returnMap;
  }
}
