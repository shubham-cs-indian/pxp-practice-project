package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class Orient_Migration_for_IsRoot_Field_In_Tag extends AbstractOrientPlugin {
  
  public Orient_Migration_for_IsRoot_Field_In_Tag(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_for_IsRoot_Field_In_Tag/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    UtilClass.getDatabase().commit();
    
    OrientVertexType tagVertexType = UtilClass.getGraph().getVertexType(VertexLabelConstants.ENTITY_TAG);
    tagVertexType.createProperty("isRoot", OType.BOOLEAN);
    tagVertexType.createIndex(VertexLabelConstants.ENTITY_TAG+".isRoot", OClass.INDEX_TYPE.NOTUNIQUE, "isRoot");
   
    String query = "select from " + VertexLabelConstants.ENTITY_TAG + " where outE('Child_Of').size() = 0";
    Iterable<Vertex> tagNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    for (Vertex tag : tagNodes) {
      tag.setProperty("isRoot", true);
      Iterable<Vertex> childVertex = tag.getVertices(Direction.IN, "Child_Of");
      for (Vertex childNode : childVertex) {
        childNode.setProperty("isRoot", false);
      }
    }
    UtilClass.getGraph().commit();
    return null;
  }
}
