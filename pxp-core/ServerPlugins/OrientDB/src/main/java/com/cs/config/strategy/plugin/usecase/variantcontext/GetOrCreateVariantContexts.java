package com.cs.config.strategy.plugin.usecase.variantcontext;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.validationontype.InvalidContextTypeException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IAddedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagValuesModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetOrCreateVariantContexts extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToExclude = Arrays.asList(
      ISaveVariantContextModel.ADDED_TAGS, ISaveVariantContextModel.MODIFIED_TAGS,
      ISaveVariantContextModel.DELETED_TAGS, ISaveVariantContextModel.ADDED_SECTIONS,
      ISaveVariantContextModel.MODIFIED_SECTIONS, ISaveVariantContextModel.DELETED_SECTIONS,
      ISaveVariantContextModel.ADDED_ELEMENTS, ISaveVariantContextModel.MODIFIED_ELEMENTS,
      ISaveVariantContextModel.DELETED_ELEMENTS, ISaveVariantContextModel.ADDED_STATUS_TAGS,
      ISaveVariantContextModel.DELETED_STATUS_TAGS, ISaveVariantContextModel.ADDED_SUB_CONTEXTS,
      ISaveVariantContextModel.DELETED_SUB_CONTEXTS, ISaveVariantContextModel.ADDED_ENTITIES,
      ISaveVariantContextModel.DELETED_ENTITIES, ISaveVariantContextModel.ADDED_UNIQUE_SELECTIONS,
      ISaveVariantContextModel.DELETED_UNIQUE_SELECTIONS);
  
  public GetOrCreateVariantContexts(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<HashMap<String, Object>> variantContextList = new ArrayList<>();
    variantContextList = (List<HashMap<String, Object>>) requestMap.get(IListModel.LIST);
    
    for (HashMap<String, Object> variantContextMap : variantContextList) {
      String variantContextId = (String) variantContextMap.get(ISaveVariantContextModel.ID);
      try {
        UtilClass.getVertexById(variantContextId, VertexLabelConstants.VARIANT_CONTEXT);
      }
      catch (NotFoundException e) {
        createVariantContext(UtilClass.getGraph(), variantContextMap);
      }
    }
    UtilClass.getGraph()
        .commit();
    return requestMap;
  }
  
  private void createVariantContext(OrientGraph graph, HashMap<String, Object> variantContextMap)
      throws Exception
  {
    
    try {
      UtilClass.validateOnType(Constants.CONTEXTS_TYPES_LIST,
          (String) variantContextMap.get(ICreateVariantContextModel.TYPE), true);
    }
    catch (InvalidTypeException e) {
      throw new InvalidContextTypeException(e);
    }
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
    Vertex variantContextNode = UtilClass.createNode(variantContextMap, vertexType,
        fieldsToExclude);
    saveContext(variantContextNode, variantContextMap);
    UtilClass.saveNode(variantContextMap, variantContextNode, fieldsToExclude);
    
    // tab
    Map<String, Object> tabMap = (Map<String, Object>) variantContextMap
        .get(ICreateVariantContextModel.TAB);
    TabUtils.linkAddedOrDefaultTab(variantContextNode, tabMap, CommonConstants.CONTEXT);
  }
  
  private static void saveContext(Vertex contextNode, Map<String, Object> contextMap)
      throws Exception
  {
    
    List<Map<String, Object>> addedTags = (List<Map<String, Object>>) contextMap
        .get(ISaveVariantContextModel.ADDED_TAGS);
    Map<String, Object> addedTagsMap = new HashMap<>();
    for (Map<String, Object> addedTag : addedTags) {
      addedTagsMap.put((String) addedTag.get(IAddedVariantContextTagsModel.TAG_ID),
          addedTag.get(IAddedVariantContextTagsModel.TAG_VALUE_IDS));
    }
    
    handleAddedTags(contextNode, addedTagsMap);
  }
  
  private static void handleAddedTags(Vertex contextNode, Map<String, Object> addedTags)
      throws Exception
  {
    for (String tagId : addedTags.keySet()) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.VARIANT_CONTEXT_TAG, CommonConstants.CODE_PROPERTY);
      
      Vertex tagNode = null;
      try {
        tagNode = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
      Vertex contextTagNode = UtilClass.createNode(new HashMap<String, Object>(), vertexType,
          new ArrayList<>());
      Edge contextTagNodeEdge = contextNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG,
          contextTagNode);
      contextTagNodeEdge.setProperty(IVariantContextTagModel.TAG_ID, UtilClass.getCodeNew(tagNode));
      
      contextTagNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY, tagNode);
      List<String> tagValueIds = (List<String>) addedTags.get(tagId);
      if (tagValueIds == null) {
        continue;
      }
      for (String tagValueId : tagValueIds) {
        try {
          Vertex tagValueNode = UtilClass.getVertexByIndexedId(tagValueId,
              VertexLabelConstants.ENTITY_TAG);
          Edge contextTagValueNodeEdge = contextTagNode
              .addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE, tagValueNode);
          contextTagValueNodeEdge.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID,
              UtilClass.getCodeNew(tagValueNode));
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
      }
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateVariantContexts/*" };
  }
}
