package com.cs.config.strategy.plugin.usecase.mapping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.model.mapping.IMappingHelperModel;
import com.cs.config.strategy.plugin.model.mapping.MappingHelperModel;
import com.cs.config.strategy.plugin.usecase.mapping.util.OutboundMappingUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoResponseModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.ITagInfoModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class GetPropertyGroupInfo extends AbstractOrientPlugin {
  
  public static final List<String> ATTRIBUTE_FIELDS_TO_FETCH = Arrays.asList(
      CommonConstants.CID_PROPERTY, IAttribute.LABEL, IAttribute.ICON, IAttribute.CODE,
      IAttribute.TYPE);
  public static final List<String> TAG_FIELDS_TO_FETCH       = Arrays.asList(
      CommonConstants.CID_PROPERTY, IAttribute.LABEL, IAttribute.ICON, IAttribute.CODE,
      IAttribute.TYPE, ITag.TAG_TYPE);
  
  public GetPropertyGroupInfo(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetPropertyGroupInfo/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    String propertycollectionId = (String) requestMap
        .get(IGetPropertyGroupInfoRequestModel.PROPERTY_COLLECTION_ID);
    String mappingId = (String) requestMap.get(IGetPropertyGroupInfoRequestModel.MAPPING_ID);
    Vertex propertycollectionNode = UtilClass.getVertexById(propertycollectionId,
        VertexLabelConstants.PROPERTY_COLLECTION);
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<String> attrIds = new ArrayList<>();
    List<String> tagIds = new ArrayList<>();
    getPropertyCollection(propertycollectionNode, returnMap, attrIds, tagIds);
    
    Map<String, Object> attributeMappings = new HashMap<>();
    Map<String, Object> tagMappings = new HashMap<>();
    IMappingHelperModel mappingHelperModel = new MappingHelperModel();
    Vertex mappingNode = UtilClass.getVertexById(mappingId, VertexLabelConstants.PROPERTY_MAPPING);
    Iterable<Vertex> pciNodes = mappingNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_PROPERTY_COLLECTION_CONFIG_RULE);
    for (Vertex pciNode : pciNodes) {
      if (!attrIds.isEmpty()) {
        attributeMappings.putAll(OutboundMappingUtils.getAttributeMapping(mappingNode,
            RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE,
            mappingHelperModel.getConfigDetails()
                .getAttributes(),
            pciNode, attrIds));
      }
      if (!tagIds.isEmpty()) {
        tagMappings.putAll(OutboundMappingUtils.getTagMapping(mappingNode,
            RelationshipLabelConstants.HAS_TAG_CONFIG_RULE, mappingHelperModel.getConfigDetails()
                .getTags(),
            pciNode, tagIds));
      }
    }
    returnMap.put(IMappingModel.ATTRIBUTE_MAPPINGS, new ArrayList<>(attributeMappings.values()));
    returnMap.put(IMappingModel.TAG_MAPPINGS, new ArrayList<>(tagMappings.values()));
    returnMap.put(IMappingModel.CONFIG_DETAILS, mappingHelperModel.getConfigDetails());
    
    return returnMap;
  }
  
  public static Map<String, Object> getPropertyCollection(Vertex propertyCollectionNode,
      Map<String, Object> returnMap, List<String> attrIds, List<String> tagIds)
  {
    Iterator<Edge> iterator = propertyCollectionNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
        .iterator();
    List<Map<String, Object>> attributeList = new ArrayList<>();
    List<Map<String, Object>> tagList = new ArrayList<>();
    while (iterator.hasNext()) {
      Edge entityTo = iterator.next();
      String entityType = entityTo.getProperty(CommonConstants.TYPE_PROPERTY);
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      switch (entityType) {
        case CommonConstants.ATTRIBUTE_PROPERTY:
          attrIds.add(UtilClass.getCId(entityNode));
          attributeList.add(UtilClass.getMapFromVertex(ATTRIBUTE_FIELDS_TO_FETCH, entityNode));
          break;
        case CommonConstants.TAG_PROPERTY:
          tagIds.add(UtilClass.getCId(entityNode));
          tagList.add(getTagListByVertex(entityNode));
          break;
      }
    }
    
    returnMap.put(IGetPropertyGroupInfoResponseModel.ATTRIBUTE_LIST, attributeList);
    returnMap.put(IGetPropertyGroupInfoResponseModel.TAG_LIST, tagList);
    return returnMap;
  }
  
  public static Map<String, Object> getContextTags(Vertex contextNode, Map<String, Object> returnMap, List<String> tagIds)
  {
    Iterator<Vertex> iterator = contextNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG).iterator();
    List<Map<String, Object>> attributeList = new ArrayList<>();
    List<Map<String, Object>> tagList = new ArrayList<>();
    while (iterator.hasNext()) {
      Iterator<Vertex> contextTagNodeIterator = iterator.next()
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY).iterator();
      
      while (contextTagNodeIterator.hasNext()) {
        Vertex contextTagNode = contextTagNodeIterator.next();
        tagIds.add(UtilClass.getCId(contextTagNode));
        tagList.add(getTagListByVertex(contextTagNode));
        
      }
    }
    
    returnMap.put(IGetPropertyGroupInfoResponseModel.ATTRIBUTE_LIST, attributeList);
    returnMap.put(IGetPropertyGroupInfoResponseModel.TAG_LIST, tagList);
    return returnMap;
  }
  
  private static Map<String, Object> getTagListByVertex(Vertex propertyVertex)
  {
    Map<String, Object> tagGroup = getPropertyNode(propertyVertex);
    String tagType = (String) tagGroup.remove(ITag.TAG_TYPE);
    if (tagType != null && tagType.equals(CommonConstants.BOOLEAN_TAG_TYPE_ID)) {
      return tagGroup;
    }
    List<Map<String, Object>> childTags = new ArrayList<Map<String, Object>>();
    Iterable<Edge> vertices = propertyVertex.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Edge edge : vertices) {
      Vertex childTagVertex = edge.getVertex(Direction.OUT);
      Map<String, Object> childTag = getPropertyNode(childTagVertex);
      childTag.remove(ITag.TAG_TYPE);
      childTags.add(childTag);
    }
    tagGroup.put(ITagInfoModel.CHILD_TAG, childTags);
    return tagGroup;
  }
  
  private static Map<String, Object> getPropertyNode(Vertex propertyVertex)
  {
    return UtilClass.getMapFromVertex(TAG_FIELDS_TO_FETCH, propertyVertex);
  }
}
