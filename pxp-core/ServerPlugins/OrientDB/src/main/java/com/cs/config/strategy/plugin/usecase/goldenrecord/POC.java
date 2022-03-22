package com.cs.config.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.collate.OCaseInsensitiveCollate;
import com.orientechnologies.orient.core.metadata.schema.OClass.INDEX_TYPE;
import com.orientechnologies.orient.core.metadata.schema.OProperty;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType.OrientVertexProperty;
import java.util.Map;

public class POC extends AbstractOrientPlugin {
  
  public POC(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|POC/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String[] fieldsToIndex = { CommonConstants.CODE_PROPERTY, CommonConstants.CODE_PROPERTY };
    
    String propertyName = CommonConstants.LABEL_PROPERTY + Seperators.FIELD_LANG_SEPERATOR + "Amit";
    
    /* Vertex vertex = UtilClass.getVertexById(SystemLevelIds.PID_ATTRIBUTE, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    vertex.setProperty(propertyName, "pidIndia");
    Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(new ArrayList<>(), vertex);*/
    
    OrientVertexType attributeVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, fieldsToIndex);
    createVertexPropertyAndApplyFulLTextIndex(attributeVertexType, propertyName);
    
    System.out.println(">>>>>>>>>>>>Hey Done>>>>>>>>>>");
    
    UtilClass.getGraph()
        .commit();
    
    return null;
  }
  
  public static void createVertexPropertyAndApplyFulLTextIndex(OrientVertexType vertexType,
      String propertyName)
  {
    OProperty property = vertexType.getProperty(propertyName);
    
    createPropertyAndApplyFullTextIndex(vertexType, propertyName);
  }
  
  private static void createPropertyAndApplyFullTextIndex(OrientVertexType vertexType,
      String propertyName)
  {
    UtilClass.getDatabase()
        .commit();
    OrientVertexProperty vertexProperty = vertexType.createProperty(propertyName, OType.STRING);
    vertexProperty.setCollate(new OCaseInsensitiveCollate());
    vertexProperty.createIndex(INDEX_TYPE.FULLTEXT);
  }
}
