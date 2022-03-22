package com.cs.config.strategy.plugin.usecase.tag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.validationontype.InvalidTagTypeException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class GetOrCreateTags extends AbstractOrientPlugin {
  
  private static final List<String> FIELDS_TO_EXCLUDE = Arrays.asList(ITagModel.DEFAULT_VALUE,
      ITagModel.TAG_VALUES);
  
  public GetOrCreateTags(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> tagsList = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    
    for (Map<String, Object> tagMap : tagsList) {
      String tagId = (String) tagMap.get(ITagModel.ID);
      try {
        UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        createTag(UtilClass.getGraph(), tagMap);
      }
    }
    UtilClass.getGraph()
        .commit();
    return requestMap;
  }
  
  @SuppressWarnings("unchecked")
  private void createTag(OrientGraph graph, Map<String, Object> tagMap) throws Exception
  {
    String vertexLabel = VertexLabelConstants.ENTITY_TAG;
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TAG,
        CommonConstants.CODE_PROPERTY);
    try {
      UtilClass.validateOnType(IStandardConfig.TagType.AllTagTypes,
          (String) tagMap.get(ITag.TAG_TYPE), false);
    }
    catch (InvalidTypeException e) {
      HashMap<String, Object> parentTagMap = (HashMap<String, Object>) tagMap.get("parent");
      if (parentTagMap == null) {
        throw new InvalidTagTypeException(e);
      }
    }
    Vertex tagNode = UtilClass.createNode(tagMap, vertexType, FIELDS_TO_EXCLUDE);
    TagUtils.createAndlinkTagAndTagType(tagMap, tagNode);
    Map<String, Object> parentTagMap = (Map<String, Object>) tagMap.get("parent");
    Vertex parentTagNode = null;
    if (parentTagMap != null) {
      String parentId = (String) parentTagMap.get(CommonConstants.ID_PROPERTY);
      try {
        parentTagNode = UtilClass.getVertexByIndexedId(parentId, vertexLabel);
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
      else if (tagType.equals(SystemLevelIds.LANGUAGE_TAG_TYPE_ID)) {
        vertexLabel = VertexLabelConstants.LANGUAGE;
        tagMap.put(IMasterTaxonomy.IS_TAG, true);
        tagMap.remove(IMasterTaxonomy.TYPE);
      }
    }
    
    /*  OrientVertexType vertexType = UtilClass.getOrCreateVertexType(vertexLabel,
        CommonConstants.CODE_PROPERTY);
    Vertex tagNode = UtilClass.createNode(tagMap, vertexType, FIELDS_TO_EXCLUDE);
    TagUtils.createAndlinkTagAndTagType(tagMap, tagNode);*/
    
    if (parentTagNode != null) {
      TagUtils.linkTagNodeToNormalizationAndRuleNodes(tagNode, parentTagNode);
      tagNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentTagNode);
      
      // for adding the childValue id to the tagGroup sequence
      List<String> addSequenceTagChildValue = parentTagNode
          .getProperty(ITagModel.TAG_VALUES_SEQUENCE);
      addSequenceTagChildValue.add((String) tagMap.get(CommonConstants.CODE_PROPERTY));
      // major change to remove "parent" property from tag
      tagNode.removeProperty("parent");
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateTags/*" };
  }
}
