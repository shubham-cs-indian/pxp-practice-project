package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.textasset.util.TextAssetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetTextAssetsByIds extends AbstractOrientPlugin {
  
  public GetTextAssetsByIds(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    List<String> klassIds = new ArrayList<String>();
    klassIds = (List<String>) requestMap.get("ids");
    
    UtilClass.setReferencedKlassIdStructureMapping(new HashMap<String, Object>());
    List<Map<String, Object>> attributesList = new ArrayList<>();
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
      if (klassNode != null) {
        attributesList.add(TextAssetUtils.getTextAssetEntityMap(klassNode, null));
      }
    }
    Map<String, Object> response = new HashMap<>();
    response.put("list", attributesList);
    return response;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetTextAssetsByIds/*" };
  }
}
