package com.cs.config.strategy.plugin.usecase.repair;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class Orient_Migration_Script_Rendition_Tags extends AbstractOrientPlugin {
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Rendition_Tags/*" };
  }
  
  public Orient_Migration_Script_Rendition_Tags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex resolutionTagVertex = UtilClass.getVertexById(SystemLevelIds.RESOLUTION_TAG,
        VertexLabelConstants.ENTITY_TAG);
    
    Iterable<Edge> edges = resolutionTagVertex.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Edge edge : edges) {
      Vertex vertexForEdge = edge.getVertex(Direction.OUT);
      int resolution = getResolution((String) vertexForEdge.getProperty(IConfigEntity.CODE));
      vertexForEdge.setProperty(ITag.IMAGE_RESOLUTION, resolution);
    }
    UtilClass.getGraph()
        .commit();
    
    HashMap<String, Object> response = new HashMap<>();
    return response;
  }
  
  private int getResolution(String imgResolution)
  {
    int resolution = 0;
    switch (imgResolution) {
      case SystemLevelIds.RESOLUTION_300P:
        resolution = 300;
        break;
      case SystemLevelIds.RESOLUTION_150P:
        resolution = 150;
        break;
      case SystemLevelIds.RESOLUTION_72P:
        resolution = 72;
        break;
      default:
    }
    return resolution;
  }
}
