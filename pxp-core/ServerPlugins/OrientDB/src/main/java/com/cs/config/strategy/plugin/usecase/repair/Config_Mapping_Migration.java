package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.propertycollection.IPosition;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class Config_Mapping_Migration extends AbstractOrientPlugin {
  
  public Config_Mapping_Migration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Config_Mapping_Migration/*" };
    
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    // collect orphan tags
    createOrphanPCforOrphanTags(graph); 
    
    String query = "select * from " + VertexLabelConstants.PROPERTY_MAPPING + " where "
        + "mappingType = " + "'inboundmapping'";
    
    Iterable<Vertex> mappingVertices = graph.command(new OCommandSQL(query)).execute();  
    
    for (Vertex mappingNode : mappingVertices) {    
         
      Iterator<Vertex> attributeConfigRuleIterator = mappingNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE)
          .iterator();
      Iterator<Vertex> tagConfigRuleIterator = mappingNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE)
          .iterator();
      
      Set<String> pcCodes = new HashSet<String>();
      Set<String> attributeCodes = new HashSet<String>();
      Set<String> tagsCodes = new HashSet<String>();
      Map<String, Object> properyMap = new HashMap<String, Object>();
 
      // selected attributes
      Map<String, String> attributeConfigRuleMap = getPCBySelectedProperties(attributeConfigRuleIterator, pcCodes, attributeCodes);
      // selected tags
      Map<String, String> tagConfigRuleMap = getPCBySelectedProperties(tagConfigRuleIterator, pcCodes, tagsCodes);
      
           properyMap.put(CommonConstants.ATTRIBUTE_PROPERTY, attributeConfigRuleMap) ;
           properyMap.put(CommonConstants.TAG_PROPERTY, tagConfigRuleMap) ;
           
      updateInboundMapping(mappingNode,  RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, pcCodes, properyMap);
      
      Iterable<Edge> edges = mappingNode.getEdges(Direction.OUT,
          new String[] { RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, RelationshipLabelConstants.HAS_TAG_CONFIG_RULE });
      Iterator<Edge> propertyConfigRuleEdges = edges.iterator();
      while (propertyConfigRuleEdges.hasNext()) {
        propertyConfigRuleEdges.next().remove();
      }
      graph.commit();
    }
    return null;
  }

  private void createOrphanPCforOrphanTags(OrientGraph graph) throws Exception
  {
    // collect orphan tags
    String orphanTagQuery = "select * from " + VertexLabelConstants.ENTITY_TAG
        + " where outE('Entity_To').size() = 0 "
        + "and isRoot = true "
        +"and type = 'com.cs.core.config.interactor.entity.tag.Tag'"
        + "and inE('Mapped_To_Entity').size() > 0 ";
    
    Iterable<Vertex> tagVertices = graph.command(new OCommandSQL(orphanTagQuery))
        .execute();
    
    if (tagVertices.iterator().hasNext()) {
      
      // create orphan OrphanPropertyCollection
      Map<String, Object> map = new HashMap<String, Object>();
      map.put(CommonConstants.CODE_PROPERTY, "OrphanPropertyCollection");
      map.put(IPropertyCollection.IS_FOR_X_RAY, false);
      map.put(IPropertyCollection.IS_DEFAULT_FOR_X_RAY, false);
      map.put(IPropertyCollection.IS_STANDARD, false);
      map.put(ISection.ROWS, 10);
      map.put(ISection.COLUMNS, 6);
      map.put("label__de_DE_AT", "OrphanPropertyCollection");
      map.put("label__en_US_GB", "OrphanPropertyCollection");
      map.put("cid", "OrphanPropertyCollection");
     
    
      
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.PROPERTY_COLLECTION, CommonConstants.CODE_PROPERTY);
      Vertex propertyCollectionNode = UtilClass.createNode(map, vertexType,
          new ArrayList<String>());
      
      List<String> tagCodes = new ArrayList<String>();
      int xPosition = 0;
      int yPosition = 0;
      for (Vertex tagNode : tagVertices) {
        addTagsToOrphanPC(propertyCollectionNode, tagCodes, tagNode, xPosition, yPosition);
        if (yPosition == 5) {
          xPosition++;
          yPosition = 0;
        }
        else {
          yPosition++;
        }
      }
      
      propertyCollectionNode.setProperty(IPropertyCollection.TAG_IDS, tagCodes);
      propertyCollectionNode.setProperty(IPropertyCollection.ATTRIBUTE_IDS, new ArrayList<String>());
      manageTabOfPC(propertyCollectionNode);
    }
  }

  private void manageTabOfPC(Vertex propertyCollectionNode) throws Exception
  {
    
    Vertex tabNode = UtilClass.getVertexById(SystemLevelIds.PROPERTY_COLLECTION_TAB,
        VertexLabelConstants.TAB);
    
    List<String> entityList = tabNode.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
    entityList.add(propertyCollectionNode.getProperty(CommonConstants.CODE_PROPERTY));
    tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, entityList);
    
    propertyCollectionNode.addEdge(RelationshipLabelConstants.HAS_TAB, tabNode);
  }
  
  @SuppressWarnings("unchecked")
  private void updateInboundMapping(Vertex mappingNode, String propertyLink, Set<String> pcCodes,  Map<String, Object> properyMap)
      throws Exception
  {
    Map<String, String> attributeConfigRuleMap = (Map<String, String>) properyMap.get(CommonConstants.ATTRIBUTE_PROPERTY) ;
    
    Map<String, String> tagConfigRuleMap= (Map<String, String>) properyMap.get(CommonConstants.TAG_PROPERTY) ;
    
    for (String pcCode : pcCodes) {
      
      Vertex pcNode = UtilClass.getVertexByCode(pcCode, VertexLabelConstants.PROPERTY_COLLECTION);
          
      Set<String> checkedAttributeCodes = new HashSet<String>();
      Set<String> checkedTagsCodes = new HashSet<String>();
      
      List<String> attCodes = new ArrayList<String>();
      List<String> tagCodes = new ArrayList<String>();
      
      Iterator<Edge> iterator = pcNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
          .iterator();
      while (iterator.hasNext()) {
        Edge entityTo = iterator.next();
        String entityType = entityTo.getProperty(CommonConstants.TYPE_PROPERTY);
        Vertex entityNode = entityTo.getVertex(Direction.OUT);
        switch (entityType) {
          case CommonConstants.ATTRIBUTE_PROPERTY:
            attCodes.add(entityNode.getProperty(CommonConstants.CODE_PROPERTY));
            break;
          case CommonConstants.TAG_PROPERTY:
            tagCodes.add(entityNode.getProperty(CommonConstants.CODE_PROPERTY));
            break;
          
        }
      }
      
   
      checkedAttributeCodes.addAll(attCodes );
      checkedAttributeCodes.retainAll(attributeConfigRuleMap.keySet());
      
      checkedTagsCodes.addAll(tagCodes);
      checkedTagsCodes.retainAll(tagConfigRuleMap.keySet());

      // creating mapping config node
      OrientVertexType vertexType = UtilClass
          .getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE, CommonConstants.CODE_PROPERTY);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put(CommonConstants.CODE_PROPERTY, UUID.randomUUID()
          .toString());
      Vertex mappingConfigNode = UtilClass.createNode(map, vertexType, new ArrayList<String>());
      mappingConfigNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, pcNode);
      mappingNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE,
          mappingConfigNode);
      
      updateTags(tagConfigRuleMap, checkedTagsCodes, tagCodes,
          mappingConfigNode);
      
      updateAttributes(attributeConfigRuleMap, checkedAttributeCodes, attCodes,
          mappingConfigNode);
      }  
  }



  private void updateTags(Map<String, String> propertyConfigRuleMap,
      Set<String> checkedPropertyCodes, List<String> propertyCodess, Vertex mappingConfigNode
    ) throws Exception
  {
    for (String propertyCode : propertyCodess) {
      
      Vertex propertyNode = UtilClass.getVertexById(propertyCode, VertexLabelConstants.ENTITY_TAG);
      
      if (checkedPropertyCodes.contains(propertyCode)) {
        Vertex propertyConfigNode = UtilClass.getVertexByCode(
            propertyConfigRuleMap.get(propertyCode), VertexLabelConstants.CONFIG_RULE);
        
        Set<Vertex> allChildTag = new HashSet<Vertex>();
        Set<Vertex> selectedChildTag = new HashSet<Vertex>();
        
        Iterator<Vertex> childTagsIterator = propertyNode
            .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
            .iterator();
        while (childTagsIterator.hasNext()) {
          allChildTag.add(childTagsIterator.next());
        }
        
         Iterator<Vertex> columnMappingNodeIterator = propertyConfigNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_COLUMN_MAPPING)
            .iterator();
        
         while (columnMappingNodeIterator.hasNext()) {
           Vertex columnMappingNode = columnMappingNodeIterator.next();
          
        Iterator<Vertex> exisingValueNodeIterator = columnMappingNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_VALUE_MAPPING)
            .iterator();
        
        while (exisingValueNodeIterator.hasNext()) {
          Vertex valueNode = exisingValueNodeIterator.next();
          
          Iterator<Vertex> childTagIterator = valueNode
              .getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY)
              .iterator();
          while (childTagIterator.hasNext()) {
            selectedChildTag.add(childTagIterator.next());    
          }
        }
        allChildTag.removeAll(selectedChildTag);
        for( Vertex childTag :allChildTag) {
          createNewValue(columnMappingNode, childTag);
          }
      }
        mappingConfigNode.addEdge(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE,
            propertyConfigNode); 
      }
      else {
        OrientVertexType PropertyVertexType = UtilClass
            .getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE, CommonConstants.CODE_PROPERTY);
        
        Map<String, Object> propertyMap = new HashMap<String, Object>();
        propertyMap.put(CommonConstants.CODE_PROPERTY, UUID.randomUUID()
            .toString());
        propertyMap.put(CommonConstants.IS_IGNORED_PROPERTY, true);
        propertyMap.put(IConfigRuleAttributeMappingModel.IS_AUTOMAPPED, false);
        Vertex propertyConfigNode = UtilClass.createNode(propertyMap, PropertyVertexType,
            new ArrayList<String>());
        
        OrientVertexType colunmMappingVertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.COLUMN_MAPPING, CommonConstants.CODE_PROPERTY);
        
        Map<String, Object> columnMappingMap = new HashMap<String, Object>();
        columnMappingMap.put(CommonConstants.CODE_PROPERTY, UUID.randomUUID()
            .toString());
        columnMappingMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME, propertyCode);
        Vertex columnMappingNode = UtilClass.createNode(columnMappingMap, colunmMappingVertexType,
            new ArrayList<String>());
        
        Iterator<Vertex> childTagsIterator = propertyNode
            .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
            .iterator();
        while (childTagsIterator.hasNext()) {
          Vertex childTag = childTagsIterator.next();
        createNewValue(columnMappingNode, childTag);
        }
        
        propertyConfigNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, propertyNode);
        propertyConfigNode.addEdge(RelationshipLabelConstants.HAS_COLUMN_MAPPING,
            columnMappingNode);
        mappingConfigNode.addEdge(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, propertyConfigNode);
        
      }
    }
    
  }

  private void createNewValue(Vertex columnMappingNode, Vertex childTag) throws Exception
  {
    // creating value node
      OrientVertexType vertexType = UtilClass
          .getOrCreateVertexType(VertexLabelConstants.VALUE, CommonConstants.CODE_PROPERTY);
      Map<String, Object> map = new HashMap<String, Object>();
      map.put(CommonConstants.CODE_PROPERTY, UUID.randomUUID()
          .toString());
      map.put(IColumnValueTagValueMappingModel.COLUMN_NAME, childTag.getProperty(CommonConstants.CODE_PROPERTY));

      Vertex valueNode = UtilClass.createNode(map, vertexType, new ArrayList<String>());
      valueNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, childTag);
      columnMappingNode.addEdge(RelationshipLabelConstants.HAS_VALUE_MAPPING, valueNode);
  }

  private void updateAttributes(Map<String, String> propertyConfigRuleMap,
      Set<String> checkedPropertyCodes, List<String> propertyCodess, Vertex mappingConfigNode
     ) throws Exception
  {
    for (String propertyCode : propertyCodess) {
      
      Vertex propertyNode = UtilClass.getVertexById(propertyCode,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    
      
      if (checkedPropertyCodes.contains(propertyCode)) {
        Vertex propertyConfigRuleNode = UtilClass.getVertexByCode(
            propertyConfigRuleMap.get(propertyCode), VertexLabelConstants.CONFIG_RULE);
        
        mappingConfigNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, propertyConfigRuleNode);
      }
      else {
        OrientVertexType PropertyVertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.CONFIG_RULE, CommonConstants.CODE_PROPERTY);
        
        Map<String, Object> propertyMap = new HashMap<String, Object>();
        propertyMap.put(CommonConstants.CODE_PROPERTY, UUID.randomUUID()
            .toString());
        propertyMap.put(CommonConstants.IS_IGNORED_PROPERTY, true);
        Vertex propertyConfigNode = UtilClass.createNode(propertyMap, PropertyVertexType,
            new ArrayList<String>());
        
        OrientVertexType colunmMappingVertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.COLUMN_MAPPING, CommonConstants.CODE_PROPERTY);
        
        Map<String, Object> columnMappingMap = new HashMap<String, Object>();
        columnMappingMap.put(CommonConstants.CODE_PROPERTY, UUID.randomUUID()
            .toString());
        columnMappingMap.put(IColumnValueTagValueMappingModel.COLUMN_NAME, propertyCode);
        Vertex columnMappingNode = UtilClass.createNode(columnMappingMap, colunmMappingVertexType,
            new ArrayList<String>());
        
        propertyConfigNode.addEdge(RelationshipLabelConstants.MAPPED_TO_ENTITY, propertyNode);
        propertyConfigNode.addEdge(RelationshipLabelConstants.HAS_COLUMN_MAPPING,
            columnMappingNode);
        mappingConfigNode.addEdge(RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, propertyConfigNode);
        
      }      
    }
  }
  
  private Map<String, String> getPCBySelectedProperties(Iterator<Vertex> propertyConfigRuleIterator,
      Set<String> pcCodes, Set<String> propertyCodes) throws Exception
  {
    Map<String, String> propertyConfigRuleMap = new HashMap<String, String>();
    while (propertyConfigRuleIterator.hasNext()) {
      Vertex propertyConfigRuleNode = propertyConfigRuleIterator.next();
      Iterator<Vertex> propertyIterator = propertyConfigRuleNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY)
          .iterator();
      while (propertyIterator.hasNext()) {
        Vertex propertyNode = propertyIterator.next();
        propertyCodes.add(propertyNode.getProperty(CommonConstants.CODE_PROPERTY));
        propertyConfigRuleMap.put(propertyNode.getProperty(CommonConstants.CODE_PROPERTY),
            (propertyConfigRuleNode.getProperty(CommonConstants.CODE_PROPERTY)));
        Iterator<Vertex> pcIterator = propertyNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
            .iterator();
        while (pcIterator.hasNext()) {
          Vertex pcNode = pcIterator.next();
          if (!(boolean) pcNode.getProperty("isForXRay")) {
            pcCodes.add(pcNode.getProperty(CommonConstants.CODE_PROPERTY));
          }
        }
      }
    }
    return propertyConfigRuleMap;
  }
  
  public static void addTagsToOrphanPC(Vertex propertyCollectionNode, List<String> tagCodes,
      Vertex tag,  int xPosition, int yPosition) throws Exception
  {
    Edge entityTo = tag.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO,
        propertyCollectionNode);
    
    Map<String, Object> positionMap = new HashMap<String, Object>();
    positionMap.put(IPosition.X, xPosition);
    positionMap.put(IPosition.Y, yPosition);
    entityTo.setProperty(CommonConstants.POSITION_PROPERTY, positionMap);
    entityTo.setProperty(CommonConstants.TYPE_PROPERTY, CommonConstants.TAG_PROPERTY);
    
    tagCodes.add(tag.getProperty(CommonConstants.CODE_PROPERTY));
    
  }
}