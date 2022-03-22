package com.cs.config.strategy.plugin.usecase.target;

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

public class GetAllMasterTargets extends AbstractOrientPlugin {
  
  public GetAllMasterTargets(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Iterator<Vertex> targetNodesIterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TARGET)
        .iterator();
    
    List<Map<String, Object>> targetsList = new ArrayList<>();
    
    while (targetNodesIterator.hasNext()) {
      
      Vertex targetNode = targetNodesIterator.next();
      targetsList.add(TargetUtils.getTargetEntityMap(targetNode, null));
    }
    
    Map<String, Object> listModel = new HashMap<>();
    listModel.put("list", targetsList);
    
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllMasterTargets/*" };
  }
}
