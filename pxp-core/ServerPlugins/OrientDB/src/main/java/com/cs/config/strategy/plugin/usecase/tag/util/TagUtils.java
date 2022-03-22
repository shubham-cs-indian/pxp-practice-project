package com.cs.config.strategy.plugin.usecase.tag.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class TagUtils {
  
  public static final List<String> fieldsToFetch             = Arrays.asList(ITag.COLOR,
      ITag.IS_MULTI_SELECT, ITag.DEFAULT_VALUE, ITag.TAG_TYPE, ITag.TAG_VALUES, ITag.IS_DIMENSIONAL,
      ITag.SHOULD_DISPLAY, ITag.IS_FOR_RELEVANCE, ITag.KLASS, ITag.ALLOWED_TAGS,
      ITag.TAG_VALUES_SEQUENCE, ITag.DESCRIPTION, ITag.TOOLTIP, ITag.IS_MANDATORY, ITag.IS_STANDARD,
      ITag.PLACEHOLDER, ITag.LABEL, ITag.ICON, ITag.TYPE, ITag.VERSION_ID, ITag.VERSION_TIMESTAMP,
      ITag.LAST_MODIFIED_BY, CommonConstants.CODE_PROPERTY, ITag.CODE, ITag.AVAILABILITY,
      ITag.IS_FILTERABLE, ITag.IS_GRID_EDITABLE, ITag.IMAGE_EXTENSION, ITag.IMAGE_RESOLUTION,
      ITag.IS_VERSIONABLE, ITag.PROPERTY_IID, ITag.IS_ROOT, ITag.IS_DISABLED);
  
  public static final List<String> fieldsToFetchForTagValues = Arrays.asList(ITagValue.COLOR,
      ITagValue.LABEL, ITagValue.VERSION_ID, ITagValue.CODE, ITagValue.VERSION_TIMESTAMP,
      ITagValue.LAST_MODIFIED_BY);
  
  public static HashMap<String, Object> getTagMapWithSelectTagValues(Vertex tagNode,
      List<String> tagValueIds) throws Exception
  {
    HashMap<String, Object> tagMap = new HashMap<>();
    tagMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, tagNode));
    
    if (tagMap.get(ITag.TYPE) == null) {
      tagMap.put(ITag.TYPE, CommonConstants.TAG_TYPE);
    }
    
    Map<String, Object> defaultTagValue = new HashMap<>();
    defaultTagValue.put(CommonConstants.TYPE_PROPERTY, CommonConstants.TAG_TYPE);
    tagMap.put(CommonConstants.DEFAULT_VALUE_PROPERTY, defaultTagValue);
    
    Iterator<Edge> i = tagNode
        .getEdges(com.tinkerpop.blueprints.Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF)
        .iterator();
    Edge defaultValueRelationship = null;
    while (i.hasNext()) {
      defaultValueRelationship = i.next();
    }
    
    if (defaultValueRelationship != null) {
      Vertex defaultTagNode = defaultValueRelationship
          .getVertex(com.tinkerpop.blueprints.Direction.OUT);
      defaultTagValue.putAll(UtilClass.getMapFromVertex(fieldsToFetch, defaultTagNode));
      if (defaultTagValue.get(ITag.TYPE) == null) {
        defaultTagValue.put(ITag.TYPE, CommonConstants.TAG_TYPE);
      }
    }
    
    tagMap.put(CommonConstants.TAG_VALUES, getTagValues(tagNode));
    List<Map<String, Object>> children = new ArrayList<>();
    tagMap.put(CommonConstants.CHILDREN_PROPERTY, children);
    
    if (tagNode.getProperty(ITag.TAG_TYPE) == null) {
      return tagMap;
    }
    
    fillTagValueMapForSelectIds(tagValueIds, children);
    fillTagMapWithLinkedMasterTagId(tagNode, tagMap);
    return tagMap;
  }
  
  public static void fillTagValueMapForSelectIds(List<String> tagValueIds,
      List<Map<String, Object>> children)
  {
    int size = (tagValueIds != null) ? tagValueIds.size() : 0;
    int startIndex = 0;
    
    while (size > 40) {
      int endIndex = startIndex + 40;
      List<String> tagValueIdsSubList = tagValueIds.subList(startIndex, endIndex);
      fillTagValueMapForSelectIds(tagValueIdsSubList, children);
      startIndex = endIndex;
      size = size - 40;
    }
    
    if (size != 0) {
      OrientGraph graph = UtilClass.getGraph();
      List<String> tagValueIdsSubList = tagValueIds.subList(startIndex, startIndex + size);
      Iterable<Vertex> resultIterable = graph
          .command(new OCommandSQL(
              "select * from Tag where code in ['" + String.join("','", tagValueIdsSubList) + "']"))
          .execute();
      for (Vertex childTagNode : resultIterable) {
        HashMap<String, Object> childTagMap = new HashMap<>();
        childTagMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, childTagNode));
        fillTagMapWithLinkedMasterTagId(childTagNode, childTagMap);
        if (childTagMap.get(ITag.TYPE) == null) {
          childTagMap.put(ITag.TYPE, CommonConstants.TAG_TYPE);
        }
        children.add(childTagMap);
      }
    }
  }
  
  public static HashMap<String, Object> getTagMap(Vertex tagNode, boolean getCompleteTree)
      throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> tagMap = new HashMap<>();
    tagMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, tagNode));
    
    if (tagMap.get(ITag.TYPE) == null) {
      tagMap.put(ITag.TYPE, CommonConstants.TAG_TYPE);
    }
    
    Map<String, Object> defaultTagValue = new HashMap<>();
    defaultTagValue.put(CommonConstants.TYPE_PROPERTY, CommonConstants.TAG_TYPE);
    tagMap.put(CommonConstants.DEFAULT_VALUE_PROPERTY, defaultTagValue);
    
    Iterator<Edge> i = tagNode
        .getEdges(com.tinkerpop.blueprints.Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF)
        .iterator();
    Edge defaultValueRelationship = null;
    while (i.hasNext()) {
      defaultValueRelationship = i.next();
    }
    
    if (defaultValueRelationship != null) {
      
      Vertex defaultTagNode = defaultValueRelationship
          .getVertex(com.tinkerpop.blueprints.Direction.OUT);
      HashMap<String, Object> map = new HashMap<String, Object>();
      defaultTagValue.putAll(UtilClass.getMapFromVertex(fieldsToFetch, defaultTagNode));
      if (defaultTagValue.get(ITag.TYPE) == null) {
        defaultTagValue.put(ITag.TYPE, CommonConstants.TAG_TYPE);
      }
    }
    
    tagMap.put(CommonConstants.TAG_VALUES, getTagValues(tagNode));
    List<Map<String, Object>> children = new ArrayList<>();
    tagMap.put(CommonConstants.CHILDREN_PROPERTY, children);
    
    // fill parent if any
    tagMap.put(ITagModel.PARENT, getParentIfPresent(tagNode));
    
    fillTagMapWithLinkedMasterTagId(tagNode, tagMap);
    if (tagNode.getProperty(ITag.TAG_TYPE) == null) {
      return tagMap;
    }
    
    String rid = tagNode.getId()
        .toString();
    
    Iterable<Vertex> resultIterable = graph
        .command(new OCommandSQL("select expand(in ('Child_Of')) from " + rid + " order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    String tagType = (String) tagMap.get(ITag.TAG_TYPE);
    for (Vertex childTagNode : resultIterable) {
      HashMap<String, Object> map = new HashMap<>(
          UtilClass.getMapFromVertex(fieldsToFetch, childTagNode));
      fillTagMapWithLinkedMasterTagId(childTagNode, map);
      HashMap<String, Object> childTagMap = new HashMap<>(
          getCompleteTree ? getTagMap(childTagNode, getCompleteTree) : map);
      childTagMap.putIfAbsent(ITag.TYPE, CommonConstants.TAG_TYPE);
      if (tagType.equals(CommonConstants.BOOLEAN_TAG_TYPE_ID) && (childTagMap.get(ITag.LABEL) == null || childTagMap.get(ITag.LABEL) == "")) {
        childTagMap.put(ITag.LABEL, tagMap.get(ITag.LABEL));
      }
      children.add(childTagMap);
    }
    return tagMap;
  }
  
  /**
   * create and return parent Map(if any parent for tagNode is present tagNode
   * else return null)
   *
   * @author Lokesh
   * @param tagNode
   * @return
   */
  private static Map<String, Object> getParentIfPresent(Vertex tagNode) throws Exception
  {
    
    Vertex parentNode = getParentTag(tagNode);
    if (parentNode == null) {
      return null;
    }
    Map<String, Object> parentMap = new HashMap<String, Object>();
    parentMap.put(ITagModel.ID, UtilClass.getCodeNew(parentNode));
    return parentMap;
  }
  
  private static void fillTagMapWithLinkedMasterTagId(Vertex tagNode,
      HashMap<String, Object> tagMap)
  {
    Iterator<Vertex> masterTagIterator = tagNode
        .getVertices(Direction.IN, RelationshipLabelConstants.MASTER_TAG_OF)
        .iterator();
    if (masterTagIterator.hasNext()) {
      Vertex masterTagVertex = masterTagIterator.next();
      String linkedMasterTagId = UtilClass.getCodeNew(masterTagVertex);
      tagMap.put(ITaxonomy.LINKED_MASTER_TAG_ID, linkedMasterTagId);
    }
  }
  
  public static List<Map<String, Object>> getTagValues(Vertex tagNode)
  {
    
    List<Map<String, Object>> tagValues = new ArrayList<>();
    
    Iterator<Edge> i = tagNode
        .getEdges(com.tinkerpop.blueprints.Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_TYPE_OF)
        .iterator();
    Edge tagTypeRelationship = null;
    while (i.hasNext()) {
      tagTypeRelationship = i.next();
    }
    
    if (tagTypeRelationship != null) {
      Vertex tagTypeNode = tagTypeRelationship.getVertex(com.tinkerpop.blueprints.Direction.OUT);
      
      String tagTypeId = (String) tagTypeNode.getProperty(CommonConstants.CODE_PROPERTY);
      
      Vertex nodeToGetValueNode = null;
      
      Iterable<Edge> tagValuesRelationships = null;
      
      // If TagType is custom then take tag values from tag node
      
      if (tagTypeId.equals(CommonConstants.CUSTOM_TAG_TYPE_ID)) {
        
        tagValuesRelationships = tagNode.getEdges(Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF);
        nodeToGetValueNode = tagNode;
      }
      else {
        
        // If TagType is not custom then take tag values from tag type
        // node
        tagValuesRelationships = tagTypeNode.getEdges(Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF);
        nodeToGetValueNode = tagTypeNode;
      }
      
      for (Edge tagValueRelationship : tagValuesRelationships) {
        
        Vertex tagValueNode = tagValueRelationship.getVertex(Direction.OUT);
        HashMap<String, Object> tagValueMap = new HashMap<String, Object>();
        tagValueMap.putAll(UtilClass.getMapFromVertex(fieldsToFetchForTagValues, tagValueNode));
        tagValues.add(tagValueMap);
      }
    }
    return tagValues;
  }
  
  public static void createTagCustomTypeValues(Vertex tagNode,
      List<HashMap<String, Object>> tagValues, OrientGraph graph)
  {
    
    deleteCustomTypeValuesFromtag(tagNode, graph);
    
    for (HashMap<String, Object> tagValueMap : tagValues) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.TAG_VALUE,
          CommonConstants.CODE_PROPERTY);
      
      Vertex tagValueNode = UtilClass.createNode(tagValueMap, vertexType);
      tagValueNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF, tagNode);
    }
  }
  
  public static void deleteCustomTypeValuesFromtag(Vertex tagNode, OrientGraph graph)
  {
    
    Iterable<Edge> tagValueRelationships = tagNode.getEdges(com.tinkerpop.blueprints.Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_VALUE_OF);
    
    for (Edge tagValueRelationship : tagValueRelationships) {
      Vertex tagValueNodeToDelete = tagValueRelationship
          .getVertex(com.tinkerpop.blueprints.Direction.OUT);
      tagValueRelationship.remove();
      tagValueNodeToDelete.remove();
    }
  }
  
  public static void createAndlinkTagAndTagType(Map<String, Object> tagMap, Vertex tagNode)
  {
    
    String tagTypeId = (String) tagMap.get(CommonConstants.TAG_TYPE_PROPERTY);
    
    if (tagTypeId != null) {
      
      OrientGraph graph = UtilClass.getGraph();
      Iterator<Vertex> iterator = graph
          .getVertices("tag_type", new String[] { CommonConstants.CODE_PROPERTY },
              new Object[] { tagTypeId })
          .iterator();
      Vertex tagTypeNode = null;
      while (iterator.hasNext()) {
        tagTypeNode = iterator.next();
        
        tagTypeNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_TAG_TYPE_OF, tagNode);
        
        if (tagTypeId == CommonConstants.CUSTOM_TAG_TYPE_ID) {
          
          List<HashMap<String, Object>> customTagValues = (List<HashMap<String, Object>>) tagMap
              .get(CommonConstants.TAG_VALUES);
          createTagCustomTypeValues(tagNode, customTagValues, graph);
        }
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  public static Map<String, Object> createTag(Map<String, Object> tagMap, List<Map<String, Object>> auditLogInfoList)
      throws TagNotFoundException, Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    if (ValidationUtils.validateTagInfo(tagMap)) {
      HashMap<String, Object> parentTagMap = (HashMap<String, Object>) tagMap.get("parent");
      String vertexLabel = VertexLabelConstants.ENTITY_TAG;
      Vertex parentTagNode = null;
      if (parentTagMap != null) {
        String parentId = (String) parentTagMap.get(CommonConstants.ID_PROPERTY);
        try {
          parentTagNode = UtilClass.getVertexByIndexedId(parentId, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
        String tagType = parentTagNode.getProperty(ITag.TAG_TYPE);
        if (tagType.equals(SystemLevelIds.MASTER_TAG_TYPE_ID)) {
          vertexLabel = VertexLabelConstants.ATTRIBUTION_TAXONOMY;
          tagMap.put(IMasterTaxonomy.IS_TAG, true);
          tagMap.remove(IMasterTaxonomy.TYPE);
        }
      }
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(vertexLabel,
          CommonConstants.CODE_PROPERTY);
      Vertex tagNode = UtilClass.createNode(tagMap, vertexType, new ArrayList<>());
      HashMap<String, Object> defaultTagMap = (HashMap<String, Object>) tagMap.get("defaultValue");
      if (defaultTagMap != null) {
        String tagId = (String) defaultTagMap.get("id");
        if (tagId != null) {
          Vertex defaultTagValue = UtilClass.getVertexByIndexedId(tagId,
              VertexLabelConstants.ENTITY_TAG);
          defaultTagValue.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF,
              tagNode);
        }
      }
      // major change to remove "default value" property from tag
      tagNode.removeProperty("defaultValue");
      tagNode.removeProperty("tagValues");
      
      createAndlinkTagAndTagType(tagMap, tagNode);
      if (parentTagNode != null) {
        tagNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentTagNode);
        // major change to remove "parent" property from tag
        tagNode.removeProperty("parent");
      }
      AuditLogUtils.fillAuditLoginfo(auditLogInfoList, tagNode, Entities.PROPERTIES, Elements.TABS_MENU_ITEM_TILE);
      graph.commit();
      return getTagMap(tagNode, false);
    }
    
    return null;
  }
  
  public static void linkTagNodeToNormalizationAndRuleNodes(Vertex newTagNode, Vertex parentNode)
  {
    Iterable<Vertex> peerTagNodes = parentNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    Vertex peerTagNode = null;
    for (Vertex node : peerTagNodes) {
      peerTagNode = node;
      break;
    }
    if (peerTagNode == null) {
      return;
    }
    String entityId = newTagNode.getProperty(CommonConstants.CODE_PROPERTY);
    Iterable<Vertex> ruleNodes = peerTagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
    Iterable<Vertex> normalizations = peerTagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
    for (Vertex ruleNode : ruleNodes) {
      ruleNode.addEdge(RelationshipLabelConstants.RULE_TAG_VALUE_LINK, newTagNode);
    }
    for (Vertex normalization : normalizations) {
      normalization.addEdge(RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK, newTagNode);
    }
    Iterable<Edge> ruleTagValueEdges = newTagNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RULE_TAG_VALUE_LINK);
    Iterable<Edge> normalizationTagValueEdges = newTagNode.getEdges(Direction.IN,
        RelationshipLabelConstants.NORMALIZATION_TAG_VALUE_LINK);
    addPropertiesToEdges(ruleTagValueEdges, entityId);
    addPropertiesToEdges(normalizationTagValueEdges, entityId);
  }
  
  public static void addPropertiesToEdges(Iterable<Edge> edges, String entityId)
  {
    for (Edge edge : edges) {
      edge.setProperty(CommonConstants.TO_PROPERTY, 0);
      edge.setProperty(CommonConstants.FROM_PROPERTY, 0);
      edge.setProperty(CommonConstants.ENTITY_ID_PROPERTY, entityId);
    }
  }
  
  public static boolean hasParent(Vertex tagNode)
  {
    boolean flag = false;
    
    Iterator<Edge> parentEdges = tagNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    if (parentEdges.hasNext()) {
      flag = true;
    }
    
    return flag;
  }
  
  public static void createDefaultBooleanChild(Vertex parentNode) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TAG,
        CommonConstants.CODE_PROPERTY);
    
    Map<String, Object> booleanTagDefaultValue = new HashMap<String, Object>();
    String childTagId = UtilClass.getUniqueSequenceId(vertexType);
    booleanTagDefaultValue.put(IConfigModel.CODE, childTagId);
    booleanTagDefaultValue.put(ITagModel.LABEL, parentNode.getProperty(ITagModel.LABEL));
    booleanTagDefaultValue.put(ITagModel.TYPE, CommonConstants.TAG_TYPE);
    booleanTagDefaultValue.put(ITagModel.IS_STANDARD, false);
    
    Vertex tagNode = UtilClass.createNode(booleanTagDefaultValue, vertexType,
        new ArrayList<String>());
    tagNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentNode);
    
    List<String> addSequenceTagChildValue = new ArrayList<String>();
    addSequenceTagChildValue.add(childTagId);
    parentNode.setProperty(ITagModel.TAG_VALUES_SEQUENCE, addSequenceTagChildValue);
  }
  
  public static Vertex getParentTag(Vertex tagValue) throws MultipleLinkFoundException
  {
    Iterable<Vertex> parentVertices = tagValue.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    Integer count = 0;
    for (Vertex parent : parentVertices) {
      count++;
      String type = ((OrientVertex) parent).getType()
          .toString();
      if (type.equals(VertexLabelConstants.ENTITY_TAG)) {
        return parent;
      }
    }
    
    if (count > 1) {
      throw new MultipleLinkFoundException();
    }
    return null;
  }
  
  public static Boolean isTagNode(Vertex node)
  {
    String type = node.getProperty(ITag.TYPE);
    if (type != null && type.equals(CommonConstants.TAG_TYPE)) {
      return true;
    }
    return false;
  }
  
  public static Map<String, Object> fillReferenceTags(List<Map<String, Object>> childTags)
      throws Exception
  {
    List<String> linkedMasterTagIdsList = new ArrayList<>();
    Map<String, Object> referencedTags = new HashMap<>();
    
    for (Map<String, Object> childmap : childTags) {
      for (Map.Entry<String, Object> entry : childmap.entrySet()) {
        if ((entry.getKey()).equals(ITagModel.LINKED_MASTER_TAG_ID)) {
          if (entry.getValue() == null)
            continue;
          linkedMasterTagIdsList.add((String) entry.getValue());
        }
        if ((entry.getKey()).equals(CommonConstants.CHILDREN_PROPERTY)) {
          List<Map<String, Object>> value = (List<Map<String, Object>>) entry.getValue();
          for (Map<String, Object> children : value) {
            for (Map.Entry<String, Object> childrenEntry : children.entrySet()) {
              if ((childrenEntry.getKey()).equals(ITagModel.LINKED_MASTER_TAG_ID)) {
                if (childrenEntry.getValue() == null)
                  continue;
                linkedMasterTagIdsList.add((String) childrenEntry.getValue());
              }
            }
          }
        }
      }
    }
    
    for (String masterTagId : linkedMasterTagIdsList) {
      Vertex tagNode = UtilClass.getVertexById(masterTagId, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> referencedTag = new HashMap<>();
      String id = tagNode.getProperty(CommonConstants.CODE_PROPERTY);
      referencedTag.put(IConfigEntityInformationModel.ID, id);
      referencedTag.put(IConfigEntityInformationModel.LABEL,
          tagNode.getProperty(EntityUtil.getLanguageConvertedField(ITagModel.LABEL)));
      referencedTag.put(IConfigEntityInformationModel.TYPE,
          tagNode.getProperty(ITagModel.TAG_TYPE));
      referencedTag.put(IConfigEntityInformationModel.ICON, tagNode.getProperty(ITagModel.ICON));
      referencedTag.put(IConfigEntityInformationModel.CODE, tagNode.getProperty(ITag.CODE));
      referencedTags.put(id, referencedTag);
    }
    return referencedTags;
  }
}
