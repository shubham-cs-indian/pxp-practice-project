package com.cs.config.strategy.plugin.usecase.repair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class Orient_Endpoint_Migration extends AbstractOrientPlugin {
  
  public Orient_Endpoint_Migration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Endpoint_Migration/*" };
    
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String[] outEdgesToRemove = new String[] { RelationshipLabelConstants.PROFILE_PROCESS_LINK,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_ENDPOINT,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_ENDPOINT,
        RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_ENDPOINT,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_ENDPOINT };
    
    String sqlQuery = "select * from " + VertexLabelConstants.ENDPOINT;
    Iterable<Vertex> endpointVertices = UtilClass.getGraph().command(new OCommandSQL(sqlQuery)).execute();
    Iterator<Vertex> iterator = endpointVertices.iterator();
    while (iterator.hasNext()) {
      Vertex endpointNode = iterator.next();     
      //endpointNode.removeProperty("cid");
      // Set physical catalogId to new endpoint from exportReadCatalog and
      // importWriteCatalog
      List<String> dataCatalogIds = endpointNode.getProperty("dataCatalogIds");
      if (dataCatalogIds != null && !dataCatalogIds.isEmpty()) {
        endpointNode.setProperty(IEndpoint.PHYSICAL_CATALOGS, dataCatalogIds);    
        // remove exportReadCatalog and importWriteCatalog
        endpointNode.removeProperty("dataCatalogIds");
      }  
      endpointNode.removeProperty("workSpace");
      removeEdges(outEdgesToRemove, endpointNode, Direction.OUT);
    }  
    Map<String, Object> returnMap = new HashMap<>();
    return returnMap;
  }
  
  private void removeEdges(String[] outEdges, Vertex endpointNode, Direction direction)
  {
    Iterator<Edge> edges = endpointNode.getEdges(Direction.OUT, outEdges)
        .iterator();
    while (edges.hasNext()) {
      Edge edge = edges.next();
      edge.remove();
      
    }
  }
}
