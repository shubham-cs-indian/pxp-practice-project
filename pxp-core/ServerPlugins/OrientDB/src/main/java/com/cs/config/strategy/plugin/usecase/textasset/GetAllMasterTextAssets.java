package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetAllMasterTextAssets extends AbstractOrientPlugin {
  
  public GetAllMasterTextAssets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Iterator<Vertex> klassNodesIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET)
        .iterator();
    
    List<Map<String, Object>> klassList = new ArrayList<>();
    
    while (klassNodesIterator.hasNext()) {
      Vertex targetNode = klassNodesIterator.next();
      klassList.add(TargetUtils.getTargetEntityMap(targetNode, null));
    }
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", klassList);
    
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllMasterTextAssets/*" };
  }
}
