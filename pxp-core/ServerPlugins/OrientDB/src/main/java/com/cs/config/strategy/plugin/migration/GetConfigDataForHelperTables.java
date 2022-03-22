package com.cs.config.strategy.plugin.migration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;


public class GetConfigDataForHelperTables extends AbstractOrientPlugin {
  
  private static final String CLASSIFIER_IID = "classifierIID";
  private static final String REFERENCE        = "Reference";
  private static final String ELEMENT_ID      = "elementId";
  private static final String SIDE2_ELEMENT_ID = "side2ElementId";
  private static final String SIDE1_ELEMENT_ID = "side1ElementId";
  private static final String CODE            = "code";
  private static final String TYPE            = "type";
  private static final String USERNAME        = "userName";
  private static final String LIST            = "list";
  private static final String VERTEX_TYPE     = "vertexType";
  private static final String FROM            = "from";
  private static final String SIZE            = "size";
  private static final String IID             = "iid";
  private static final String CID             = "cid";
  private static final String IS_NATURE       = "isNature";
  
  public GetConfigDataForHelperTables(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDataForHelperTables/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String vertexType = (String) requestMap.get(VERTEX_TYPE);
    int from = (int) requestMap.get(FROM);
    int size = (int) requestMap.get(SIZE);
    
    Map<String, Object> returnMap = new HashMap<>();
    
    switch (vertexType) {
      case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
      case VertexLabelConstants.VARIANT_CONTEXT:
      case VertexLabelConstants.ENTITY_TAG:
      case VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE:
      case VertexLabelConstants.ENTITY_TYPE_TASK:
      case VertexLabelConstants.ENTITY_TYPE_ROLE:
      case VertexLabelConstants.ENDPOINT:
      case VertexLabelConstants.ORGANIZATION:
        getConfigData(from, size, returnMap, vertexType);
        break;
      case VertexLabelConstants.ROOT_KLASS_TAXONOMY:
      case VertexLabelConstants.ATTRIBUTION_TAXONOMY:
        getTaxonomy(from, size, returnMap, vertexType);
        break;
      case VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP:
      case VertexLabelConstants.NATURE_RELATIONSHIP:
      case REFERENCE:
        getRelationships(from, size, returnMap, vertexType);
        break;
      case VertexLabelConstants.ENTITY_TYPE_USER:
        getUsers(from, size, returnMap);
        break;
      default:
        break;
    }
    
    return returnMap;
  }

  /***
   * Get data from vertex @param vertexType and fill data in @param returnMap
   * @param from
   * @param size
   * @param returnMap
   * @param vertexType
   */
  private void getConfigData(int from, int size, Map<String, Object> returnMap, String vertexType)
  {
    List<Object> list = new ArrayList<>();
    
    String query = "select from " + vertexType + " where (isTaxonomy is null) skip " + from + " limit " + size;
    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL(query)).execute();
    
    for (Vertex vertex : searchResults) {
      Map<String, Object> configDataMap = new HashMap<>();
      configDataMap.put(CODE, vertex.getProperty(CommonConstants.CODE_PROPERTY));
      configDataMap.put(TYPE, vertex.getProperty(CommonConstants.TYPE_PROPERTY));
      configDataMap.put(CID, vertex.getProperty(CID));
      
      if (VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS.equals(vertexType)) {
        configDataMap.put(IID, vertex.getProperty(CLASSIFIER_IID));
        configDataMap.put(IS_NATURE, vertex.getProperty(IS_NATURE));
      }
      else {
        Object propertyIId = vertex.getProperty(CommonConstants.PROPERTY_IID);
        configDataMap.put(IID, propertyIId == null ? 0 : propertyIId);
      }
      list.add(configDataMap);
    }
    returnMap.put(LIST, list);
  }
  
  /***
   * Get taxonomy data from vertex @param vertexType and fill data in @param returnMap
   * @param from
   * @param size
   * @param returnMap
   * @param vertexType
   */
  private void getTaxonomy(int from, int size, Map<String, Object> returnMap, String vertexType)
  {
    List<Object> list = new ArrayList<>();
    
    String query = "select  from " + vertexType + " where (isTaxonomy is null or isTaxonomy = true) skip " + from + " limit " + size;
    Iterable<Vertex> searchResults = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex taxonomyVertex : searchResults) {
      Map<String, Object> taxonomy = new HashMap<>();
      taxonomy.put(CODE, taxonomyVertex.getProperty(CommonConstants.CODE_PROPERTY));
      taxonomy.put(TYPE, taxonomyVertex.getProperty(CommonConstants.BASE_TYPE));
      taxonomy.put(CID, taxonomyVertex.getProperty(CID));
      taxonomy.put(IID, taxonomyVertex.getProperty(CLASSIFIER_IID));
      list.add(taxonomy);
    }
    returnMap.put(LIST, list);
  }
  
  /**
   * Get relationship data from vertex @param vertexType and fill data in @param returnMap
   * @param from
   * @param size
   * @param returnMap
   * @param vertexType
   */
  private void getRelationships(int from, int size, Map<String, Object> returnMap, String vertexType)
  {
    List<Object> list = new ArrayList<>();
    String query = "Select from " + vertexType + " skip " + from + " limit " + size;
    Iterable<Vertex> searchResults = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex relationshipVertex : searchResults) {
      Map<String, Object> relationship = new HashMap<>();
      relationship.put(CID, relationshipVertex.getProperty(CID));
      Map<String, Object> side1 = relationshipVertex.getProperty("side1");
      relationship.put(SIDE1_ELEMENT_ID, side1.get(ELEMENT_ID));
      Map<String, Object> side2 = relationshipVertex.getProperty("side2");
      relationship.put(SIDE2_ELEMENT_ID, side2.get(ELEMENT_ID));
      relationship.put(CODE, relationshipVertex.getProperty(CommonConstants.CODE_PROPERTY));
      relationship.put(IID, relationshipVertex.getProperty(CommonConstants.PROPERTY_IID));
      list.add(relationship);
    }
    returnMap.put(LIST, list);
  }
  
  /**
   * Get user data from User vertex and fill data in @param returnMap
   * @param from
   * @param size
   * @param returnMap
   */
  private void getUsers(int from, int size, Map<String, Object> returnMap)
  {
    List<Object> list = new ArrayList<>();
    
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_USER + " skip " + from + " limit " + size;
    Iterable<Vertex> searchResults = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex userVertex : searchResults) {
      Map<String, Object> user = new HashMap<>();
      user.put(USERNAME, userVertex.getProperty(CommonConstants.USER_NAME_PROPERTY));
      user.put(CODE, userVertex.getProperty(CommonConstants.CODE_PROPERTY));
      user.put(IID, userVertex.getProperty("userIID"));
      list.add(user);
    }
    returnMap.put(LIST, list);
  }
  
}
