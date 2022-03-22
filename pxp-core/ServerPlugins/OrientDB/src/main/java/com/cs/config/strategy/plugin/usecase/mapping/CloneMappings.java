package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.entity.mapping.IOutBoundMapping;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.mapping.IBulkSaveMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigCloneEntityInformationModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IGetAllMappingsResponseModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

/** @author Subham.Shaw */
@SuppressWarnings("unchecked")
public class CloneMappings extends AbstractOrientPlugin {
  
  protected List<String> fieldsToExclude = Arrays
      .asList(IConfigCloneEntityInformationModel.ORIGINAL_ENTITY_ID, IConfigCloneEntityInformationModel.PHYSICAL_CATALOG_IDS);
  
  public CloneMappings(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CloneMappings/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> mappingsMap = new HashMap<>();
    List<Map<String, Object>> mappingsList = new ArrayList<>();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.PROPERTY_MAPPING, CommonConstants.CODE_PROPERTY);
    IExceptionModel failure = new ExceptionModel();
    
    for (Map<String, Object> mapping : (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST)) {
      String srcMappingId = (String) mapping
          .get(IConfigCloneEntityInformationModel.ORIGINAL_ENTITY_ID);
      try {
        Vertex srcMappingNode = UtilClass.getVertexById(srcMappingId,
            VertexLabelConstants.PROPERTY_MAPPING);
        
        Vertex destMappingNode = UtilClass.createNode(mapping, vertexType, fieldsToExclude);
        destMappingNode.setProperty(IMapping.MAPPING_TYPE, srcMappingNode.getProperty(IMapping.MAPPING_TYPE));
        
        if (srcMappingNode.getProperty(IMappingModel.MAPPING_TYPE).equals("inboundmapping")) {
            prepareLinkEntity(srcMappingNode, destMappingNode);
          }
          else {
            prepareOutboundLinkEntity(srcMappingNode, destMappingNode);
          }
        
        mappingsList
            .add(
                UtilClass.getMapFromVertex(
                    Arrays.asList(CommonConstants.CODE_PROPERTY,
                        IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.LABEL, IMapping.MAPPING_TYPE),
                    destMappingNode));
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, srcMappingId, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, srcMappingId, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    mappingsMap.put(IListModel.LIST, mappingsList);
    mappingsMap.put(IGetAllMappingsResponseModel.COUNT, mappingsList.size());
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IBulkSaveMappingsResponseModel.SUCCESS, mappingsMap);
    returnMap.put(IBulkSaveMappingsResponseModel.FAILURE, failure);
    return returnMap;
  }
  
  private void prepareLinkEntity(Vertex srcMappingNode, Vertex destMappingNode) throws Exception
  {
		linkOutBoundEntity(srcMappingNode, destMappingNode);
		linkEntity(srcMappingNode, destMappingNode, RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE,
				VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
		linkEntity(srcMappingNode, destMappingNode, RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE,
				VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    linkEntity(srcMappingNode, destMappingNode, RelationshipLabelConstants.HAS_RELATIONSHIP_CONFIG_RULE,
        VertexLabelConstants.ROOT_RELATIONSHIP);
  }
  
  private void prepareOutboundLinkEntity(Vertex srcMappingNode, Vertex destMappingNode)
	      throws Exception
	  {
	    destMappingNode.setProperty(IOutBoundMapping.IS_ALL_TAXONOMIES_SELECTED,
	        srcMappingNode.getProperty(IOutBoundMapping.IS_ALL_TAXONOMIES_SELECTED));
	    destMappingNode.setProperty(IOutBoundMapping.IS_ALL_CLASSES_SELECTED,
	        srcMappingNode.getProperty(IOutBoundMapping.IS_ALL_CLASSES_SELECTED));
	    destMappingNode.setProperty(IOutBoundMapping.IS_ALL_PROPERTY_COLLECTION_SELECTED,
	        srcMappingNode.getProperty(IOutBoundMapping.IS_ALL_PROPERTY_COLLECTION_SELECTED));
	    
	    linkOutBoundEntity(srcMappingNode, destMappingNode);
	    linkEntity(srcMappingNode, destMappingNode, RelationshipLabelConstants.HAS_CLASS_CONFIG_RULE, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
	    linkEntity(srcMappingNode, destMappingNode, RelationshipLabelConstants.HAS_TAXONOMY_CONFIG_RULE, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
	  }
  
  /**
   * This method shall link new mapping nodes to entites
   *
   * @param srcMappingNode
   *          - Source Mapping node
   * @param destMappingNode
   *          - Destination Mapping node
   * @param relationshipVertexLabel
   *          - Relationship label
   * @param entityType
   *          - Entity type
   * @throws Exception
   */
  private void linkEntity(Vertex srcMappingNode, Vertex destMappingNode,
      String relationshipVertexLabel, String entityType) throws Exception
  {
    
    OrientVertexType configRuleVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE, CommonConstants.CODE_PROPERTY);
    OrientVertexType columnVertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.COLUMN_MAPPING, CommonConstants.CODE_PROPERTY);
    OrientVertexType valueVertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.VALUE,
        CommonConstants.CODE_PROPERTY);
    
    /**
     * Fetching linked configRule node, creating new configRule node, copying
     * the properties and linking it to new mapping node
     */
    Iterable<Vertex> srcConfigRuleVertices = srcMappingNode.getVertices(Direction.OUT,
        relationshipVertexLabel);
    for (Vertex srcConfigRuleVertex : srcConfigRuleVertices) {
      Vertex destConfigRuleVertex = UtilClass.copyPropertiesFromNodeToNewNodeWithoutCode(
          srcConfigRuleVertex, configRuleVertexType,
          Arrays.asList(IConfigRuleAttributeMappingModel.IS_AUTOMAPPED,
              IConfigRuleAttributeMappingModel.IS_IGNORED));
      destMappingNode.addEdge(relationshipVertexLabel, destConfigRuleVertex);
      
      // Linking new configRule node to entity
      UtilClass.copyEdgeWithoutProperties(srcConfigRuleVertex, destConfigRuleVertex,
          RelationshipLabelConstants.MAPPED_TO_ENTITY, Direction.OUT);
      
      /**
       * Creating new column node, copying the properties and linking it to new
       * configRule node
       */
      Iterable<Vertex> srcColumnVertices = srcConfigRuleVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex srcColumnVertex : srcColumnVertices) {
        
        Vertex destColumnNode = UtilClass.copyPropertiesFromNodeToNewNodeWithoutCode(
            srcColumnVertex, columnVertexType,
            Arrays.asList(IColumnValueTagValueMappingModel.COLUMN_NAME));
        destConfigRuleVertex.addEdge(RelationshipLabelConstants.HAS_COLUMN_MAPPING, destColumnNode);
        
        // If the entity is a tag, then this will create a value node
        if (entityType.equals(VertexLabelConstants.ENTITY_TAG)) {
          Iterable<Vertex> srcLinkedValueVertices = srcColumnVertex.getVertices(Direction.OUT,
              RelationshipLabelConstants.HAS_VALUE_MAPPING);
          for (Vertex srcValueVertex : srcLinkedValueVertices) {
            Vertex destValueNode = UtilClass.copyPropertiesFromNodeToNewNodeWithoutCode(
                srcValueVertex, valueVertexType,
                Arrays.asList(IColumnValueTagValueMappingModel.COLUMN_NAME,
                    IConfigRuleAttributeMappingModel.IS_AUTOMAPPED,
                    IConfigRuleAttributeMappingModel.IS_IGNORED));
            destColumnNode.addEdge(RelationshipLabelConstants.HAS_VALUE_MAPPING, destValueNode);
            
            // Linking new value node to entity
            UtilClass.copyEdgeWithoutProperties(srcValueVertex, destValueNode,
                RelationshipLabelConstants.MAPPED_TO_ENTITY, Direction.OUT);
          }
        }
      }
    }
  }
  
  /**
   * @param srcMappingNode
   * @param destMappingNode
   * @throws Exception
   */
  private void linkOutBoundEntity(Vertex srcMappingNode, Vertex destMappingNode) throws Exception
  {
    OrientVertexType       configRuleVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.CONFIG_RULE, CommonConstants.CID_PROPERTY);
    OrientVertexType       columnVertexType     = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.COLUMN_MAPPING, CommonConstants.CID_PROPERTY);
    OrientVertexType       valueVertexType      = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.VALUE, CommonConstants.CID_PROPERTY);
    
    Iterable<Vertex> srcPCINodes = srcMappingNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    for (Vertex srcPCINode : srcPCINodes) {
      Vertex destinationPCINode = createPCINode(srcPCINode, destMappingNode, configRuleVertexType);
      linkOutboundPropertyEntities(RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, configRuleVertexType,
          columnVertexType, valueVertexType, srcPCINode, destinationPCINode);
      linkOutboundPropertyEntities(RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, VertexLabelConstants.ENTITY_TAG, configRuleVertexType,
          columnVertexType, valueVertexType, srcPCINode, destinationPCINode);
    }
  }

  /**
   * @param relationshipVertexLabel
   * @param entityType
   * @param configRuleVertexType
   * @param columnVertexType
   * @param valueVertexType
   * @param srcPCINode
   * @param destinationPCINode
   * @throws Exception
   */
  private void linkOutboundPropertyEntities(String relationshipVertexLabel, String entityType,
      OrientVertexType configRuleVertexType, OrientVertexType columnVertexType,
      OrientVertexType valueVertexType, Vertex srcPCINode, Vertex destinationPCINode)
      throws Exception
  {
    Iterable<Vertex> srcConfigRuleVertices = srcPCINode.getVertices(Direction.OUT, relationshipVertexLabel);
    for (Vertex srcConfigRuleVertex : srcConfigRuleVertices) {
      Vertex destConfigRuleVertex = UtilClass.copyPropertiesFromNodeToNewNodeWithoutCID(
          srcConfigRuleVertex, configRuleVertexType, Arrays.asList(IConfigRuleAttributeMappingModel.IS_IGNORED));
      destinationPCINode.addEdge(relationshipVertexLabel, destConfigRuleVertex);
      
      //Linking new configRule node to entity
      UtilClass.copyEdgeWithoutProperties(srcConfigRuleVertex, destConfigRuleVertex,
          RelationshipLabelConstants.MAPPED_TO_ENTITY, Direction.OUT);
      
      /** Creating new column node, copying the properties and linking it to new
          configRule node */
      Iterable<Vertex> srcColumnVertices = srcConfigRuleVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex srcColumnVertex : srcColumnVertices) {
        
        Vertex destColumnNode = UtilClass.copyPropertiesFromNodeToNewNodeWithoutCID(
            srcColumnVertex, columnVertexType,
            Arrays.asList(IColumnValueTagValueMappingModel.COLUMN_NAME));
        destConfigRuleVertex.addEdge(RelationshipLabelConstants.HAS_COLUMN_MAPPING, destColumnNode);
        
        // If the entity is a tag, then this will create a value node
        if (entityType.equals(VertexLabelConstants.ENTITY_TAG)) {
          Iterable<Vertex> srcLinkedValueVertices = srcColumnVertex.getVertices(Direction.OUT,
              RelationshipLabelConstants.HAS_VALUE_MAPPING);
          for (Vertex srcValueVertex : srcLinkedValueVertices) {
            Vertex destValueNode = UtilClass.copyPropertiesFromNodeToNewNodeWithoutCID(
                srcValueVertex, valueVertexType,
                Arrays.asList(IColumnValueTagValueMappingModel.COLUMN_NAME,
                    IConfigRuleAttributeMappingModel.IS_IGNORED));
            destColumnNode.addEdge(RelationshipLabelConstants.HAS_VALUE_MAPPING, destValueNode);
            
          //Linking new value node to entity
            UtilClass.copyEdgeWithoutProperties(srcValueVertex, destValueNode,
                RelationshipLabelConstants.MAPPED_TO_ENTITY, Direction.OUT);
          }
        }
      }
    }
  }
  
  /**
   * @param srcMappingNode
   * @param destMappingNode
   * @param hasPropertyCollectionConfigRule
   * @param propertyCollection
   * @throws Exception 
   */
  private Vertex createPCINode(Vertex srcPCINode, Vertex destMappingNode,
      OrientVertexType configRuleVertexType) throws Exception
  {
     Vertex destinationPCINode = UtilClass.copyPropertiesFromNodeToNewNodeWithoutCID(
          srcPCINode, configRuleVertexType, new ArrayList<>());
      destMappingNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE, destinationPCINode);
      
     //Linking new configRule node to entity
     UtilClass.copyEdgeWithoutProperties(srcPCINode, destinationPCINode,
          RelationshipLabelConstants.MAPPED_TO_ENTITY, Direction.OUT);
     
     return destinationPCINode;
  }
  
  
}
