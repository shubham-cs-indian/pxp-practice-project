package com.cs.config.strategy.plugin.usecase.variantcontext.util;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ReferencedContextUtil {
  
  /**
   * @author Aayush
   * @param contextNode
   * @param referencedTags
   * @return
   * @throws Exception
   */
  public static List<Map<String, Object>> fillReferencedContextTags(Vertex contextNode,
      Map<String, Object> referencedContextMap, Map<String, Object> referencedTags) throws Exception
  {
    List<Map<String, Object>> contextTagsList = new ArrayList<Map<String, Object>>();
    Iterable<Vertex> contextTags = contextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    
    for (Vertex contextTag : contextTags) {
      Iterator<Vertex> tagNodes = contextTag
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY)
          .iterator();
      if (!tagNodes.hasNext()) {
        continue;
      }
      Vertex tagNode = tagNodes.next();
      fillReferencedContextTag(contextNode, contextTag, tagNode, referencedContextMap,
          referencedTags);
    }
    
    return contextTagsList;
  }
  
  /**
   * @author Aayush
   * @param contextNode
   * @param contextTag
   * @param tagNode
   * @param referencedContextMap
   * @throws Exception
   */
  public static void fillReferencedContextTag(Vertex contextNode, Vertex contextTag, Vertex tagNode,
      Map<String, Object> referencedContextMap, Map<String, Object> referencedTags) throws Exception
  {
    String tagId = UtilClass.getCodeNew(tagNode);
    Iterable<Vertex> contextTagValueNodes = contextTag.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE);
    List<String> selectedTagValueIds = new ArrayList<>();
    for (Vertex contextTagValueNode : contextTagValueNodes) {
      selectedTagValueIds.add(UtilClass.getCodeNew(contextTagValueNode));
    }
    
    Map<String, Object> contextTagMap = new HashMap<String, Object>();
    contextTagMap.put(IReferencedVariantContextTagsModel.TAG_ID, tagId);
    contextTagMap.put(IReferencedVariantContextTagsModel.TAG_VALUE_IDS, selectedTagValueIds);
    
    List<Map<String, Object>> contextTags = (List<Map<String, Object>>) referencedContextMap
        .get(IReferencedVariantContextModel.TAGS);
    if (contextTags == null) {
      contextTags = new ArrayList<>();
      referencedContextMap.put(IReferencedVariantContextModel.TAGS, contextTags);
    }
    contextTags.add(contextTagMap);
    
    if (!referencedTags.containsKey(tagId)) {
      referencedTags.put(tagId, TagUtils.getTagMap(tagNode, false));
    }
  }
}
