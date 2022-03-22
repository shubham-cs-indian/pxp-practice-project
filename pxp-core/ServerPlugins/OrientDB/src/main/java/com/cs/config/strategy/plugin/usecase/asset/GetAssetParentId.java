package com.cs.config.strategy.plugin.usecase.asset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetAssetParentId extends AbstractOrientPlugin {
  
  public GetAssetParentId(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAssetParentId/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String parentId = SystemLevelIds.ASSET;
    Vertex assetNode = UtilClass.getVertexById((String) requestMap.get(IIdParameterModel.ID),
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    
    try {
      Iterable<Vertex> iterable = graph
          .command(new OCommandSQL("SELECT FROM (TRAVERSE out('Child_Of') FROM " + assetNode.getId()
              + ") WHERE code IN ['" + SystemLevelIds.IMAGE + "','" + SystemLevelIds.VIDEO + "','"
              + SystemLevelIds.DOCUMENT + "']"))
          .execute();
      
      if (iterable.iterator()
          .hasNext()) {
        Vertex parentNode = iterable.iterator()
            .next();
        Map<String, Object> parentMap = UtilClass
            .getMapFromVertex(Arrays.asList(CommonConstants.ID_PROPERTY), parentNode);
        parentId = (String) parentMap.get(CommonConstants.ID_PROPERTY);
      }
    }
    catch (Exception e) {
      throw new PluginException();
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IIdParameterModel.ID, parentId);
    return returnMap;
  }
}
