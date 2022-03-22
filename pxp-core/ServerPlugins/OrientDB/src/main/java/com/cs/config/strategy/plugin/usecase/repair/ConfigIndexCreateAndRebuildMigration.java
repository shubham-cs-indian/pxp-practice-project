package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.index.OIndex;
import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class ConfigIndexCreateAndRebuildMigration extends AbstractOrientMigration {
  
  public ConfigIndexCreateAndRebuildMigration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ConfigIndexCreateAndRebuildMigration/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<String> propertiesList = new ArrayList<String>();
    try {
      propertiesList.add(IAttribute.IS_FILTERABLE);
      propertiesList.add(IAttribute.IS_TRANSLATABLE);
      propertiesList.add(IAttribute.IS_SORTABLE);
      propertiesList.add(IAttribute.IS_SEARCHABLE);
      
      rebuildVertexIndex(graph, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, propertiesList);
      
      propertiesList.clear();
      propertiesList.add(ITag.IS_FILTERABLE);
      
      rebuildVertexIndex(graph, VertexLabelConstants.ENTITY_TAG, propertiesList);
    }
    catch (Exception exception) {
      System.out.println(exception);
    }
    
    return null;
  }
  
  private void rebuildVertexIndex(OrientGraph graph, String vertexLabel, List<String> propertiesList)
  {
    OrientVertexType vertexType = graph.getVertexType(vertexLabel);
    if (vertexType != null) {
      
      for (String property : propertiesList) {
        
        // Create the Property if not already existed.
        String indexName = vertexLabel + "." + property;
        
        if (vertexType.getProperty(property) == null) {
          vertexType.createProperty(property, OType.BOOLEAN);
        }
        
        // Get Or Create the Index
        OIndex<?> index = vertexType.getClassIndex(indexName);
        if (index == null) {
          vertexType.createIndex(indexName, OClass.INDEX_TYPE.NOTUNIQUE, property);
          index = vertexType.getClassIndex(indexName);
        }
        
        // Rebuild the Index.
        if (index.isAutomatic()) {
          index.rebuild();
        }
      }
    }
  }
}
