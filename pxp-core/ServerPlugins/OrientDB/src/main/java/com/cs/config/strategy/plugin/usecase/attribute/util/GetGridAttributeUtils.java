package com.cs.config.strategy.plugin.usecase.attribute.util;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.ICalculatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttribute;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.attribute.IConfigDetailsForGridAttributesModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.tinkerpop.blueprints.Vertex;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetGridAttributeUtils {
  
  public static Map<String, Object> getConfigDetails(List<Map<String, Object>> list)
      throws Exception
  {
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedContexts = new HashMap<>();
    Map<String, Object> referencedAttributes = getReferencedAttributesandFillReferencedTags(list,
        referencedTags, referencedContexts);
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForGridAttributesModel.REFERENCED_ATTRIBUTES,
        referencedAttributes);
    configDetails.put(IConfigDetailsForGridAttributesModel.REFERENCED_TAGS, referencedTags);
    configDetails.put(IConfigDetailsForGridAttributesModel.REFERENCED_CONTEXTS, referencedContexts);
    return configDetails;
  }
  
  private static Map<String, Object> getReferencedAttributesandFillReferencedTags(
      List<Map<String, Object>> list, Map<String, Object> referencedTags,
      Map<String, Object> referencedContexts) throws Exception
  {
    Map<String, Object> referencedAttributes = new HashMap<>();
    for (Map<String, Object> attribute : list) {
      String type = (String) attribute.get(IAttribute.TYPE);
      switch (type) {
        case CommonConstants.CALCULATED_ATTRIBUTE_TYPE:
          fillReferencedAttributes((List<Map<String, Object>>) attribute
              .get(ICalculatedAttribute.ATTRIBUTE_OPERATOR_LIST), referencedAttributes);
          break;
        case CommonConstants.CONCATENATED_ATTRIBUTE_TYPE:
          fillReferencedAttributes((List<Map<String, Object>>) attribute
              .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST), referencedAttributes);
          fillReferencedTags((List<Map<String, Object>>) attribute
              .get(IConcatenatedAttribute.ATTRIBUTE_CONCATENATED_LIST), referencedTags);
          break;
      }
    }
    return referencedAttributes;
  }
  
  public static void fillReferencedAttributes(List<Map<String, Object>> attributesList,
      Map<String, Object> referencedAttributes) throws Exception
  {
    for (Map<String, Object> attribute : attributesList) {
      String attributeId = (String) attribute.get(IAttributeOperator.ATTRIBUTE_ID);
      if (attributeId != null && !referencedAttributes.containsKey(attributeId)) {
        Vertex attributeNode = null;
        try {
          attributeNode = UtilClass.getVertexById(attributeId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        }
        catch (NotFoundException e) {
          throw new AttributeNotFoundException();
        }
        Map<String, Object> referencedAttribute = AttributeUtils.getAttributeMap(attributeNode);
        referencedAttributes.put(attributeId, referencedAttribute);
      }
    }
  }
  
  public static void fillReferencedTags(List<Map<String, Object>> attributesList,
      Map<String, Object> referencedTags) throws Exception
  {
    for (Map<String, Object> attribute : attributesList) {
      String tagId = (String) attribute.get(IConcatenatedTagOperator.TAG_ID);
      if (tagId != null && !referencedTags.containsKey(tagId)) {
        Vertex tagNode = null;
        try {
          tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
        Map<String, Object> referencedAttribute = TagUtils.getTagMap(tagNode, true);
        referencedTags.put(tagId, referencedAttribute);
      }
    }
  }
}
