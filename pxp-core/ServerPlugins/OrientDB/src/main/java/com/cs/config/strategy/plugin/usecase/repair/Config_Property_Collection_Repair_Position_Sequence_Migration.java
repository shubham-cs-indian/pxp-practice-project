package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class Config_Property_Collection_Repair_Position_Sequence_Migration
    extends AbstractOrientPlugin {
  
  public Config_Property_Collection_Repair_Position_Sequence_Migration(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Config_Property_Collection_Repair_Position_Sequence_Migration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    // Find Count of all Property Collection Nodes.
    int limit = 500;
    String countQuery = "select count(*) from " + VertexLabelConstants.PROPERTY_COLLECTION;
    int batches = ((EntityUtil.executeCountQueryToGetTotalCount(countQuery).intValue())/limit) + 1;
    
    // Get the Property Collection Nodes.
    for (int count = 0; count < batches; count++) {
      String query = "select from " + VertexLabelConstants.PROPERTY_COLLECTION + " skip "
          + count * limit + " limit " + limit;
          Iterable<Vertex> propertyCollectionNodes = UtilClass.getGraph()
              .command(new OCommandSQL(query))
              .execute();
      for (Vertex propertyCollectionNode : propertyCollectionNodes) {
        int rows = propertyCollectionNode.getProperty(ISection.ROWS);
        int columns = propertyCollectionNode.getProperty(ISection.COLUMNS);
        
        // Creating and initializing list with (column * row) elements
        ArrayList <String> propertySequenceIds = new ArrayList<String>();
        propertySequenceIds.addAll(Arrays.asList(new String[rows * columns]));
        
        Iterable<Edge> edges = propertyCollectionNode.getEdges(Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
        for (Edge edge : edges) {
          Map<String, Object> positionMap = edge.getProperty(CommonConstants.POSITION_PROPERTY);
          int x = (int) positionMap.get(IPosition.X);
          int y = (int) positionMap.get(IPosition.Y);
          int listPosition = (columns * x) + y;
          // added try catch as discussed with rohit raina need to fix this
          try {
            // Remove "position" from the Edge.
            edge.removeProperty(CommonConstants.POSITION_PROPERTY);
            propertySequenceIds.add(listPosition, edge.getVertex(Direction.OUT).getProperty(CommonConstants.CODE_PROPERTY));
          }
          catch (Exception e) {
            
          }
            
          }
        // Remove any null values from list
        propertySequenceIds.removeAll(Collections.singletonList(null)); 
        
        // Add "propertySequenceIds" to the Node.
        propertyCollectionNode.setProperty("propertySequenceIds", propertySequenceIds);
      }
    }
    UtilClass.getGraph().commit();
    return null;
  }
}
