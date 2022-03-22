package com.cs.config.strategy.plugin.usecase.relationship.util;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.relationship.IConfigDetailsForRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.interactor.model.tabs.ITabModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetRelationshipUtils {
  
  private static final List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.ID,
      IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.ICON,
      IConfigEntityInformationModel.LABEL, IConfigEntityInformationModel.TYPE);
  
  public static void fillConfigDetails(Vertex vertex, Map<String, Object> returnMap)
      throws Exception
  {
    Map<String, Object> entityMap = (Map<String, Object>) returnMap
        .get(IGetRelationshipModel.ENTITY);
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedKlasses = new HashMap<>();
    Map<String, Object> referencedContexts = new HashMap<>();
    Map<String, Object> referencedTabs = new HashMap<>();
    
    Map<String, Object> side1Map = (Map<String, Object>) entityMap.get(IRelationship.SIDE1);
    Map<String, Object> side2Map = (Map<String, Object>) entityMap.get(IRelationship.SIDE2);
    fillConfigDetailsForSide(referencedAttributes, referencedTags, referencedKlasses,
        referencedContexts, side1Map);
    fillConfigDetailsForSide(referencedAttributes, referencedTags, referencedKlasses,
        referencedContexts, side2Map);
    
    Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(vertex,
        Arrays.asList(ITabModel.LABEL, ITabModel.CODE));
    referencedTabs.put((String) referencedTab.get(ITabModel.ID), referencedTab);
    entityMap.put(IRelationship.TAB_ID, referencedTab.get(ITabModel.ID));
    
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForRelationshipModel.REFERENCED_ATTRIBUTES,
        referencedAttributes);
    configDetails.put(IConfigDetailsForRelationshipModel.REFERENCED_TAGS, referencedTags);
    configDetails.put(IConfigDetailsForRelationshipModel.REFERENCED_KLASSES, referencedKlasses);
    configDetails.put(IConfigDetailsForRelationshipModel.REFERENCED_CONTEXTS, referencedContexts);
    configDetails.put(IConfigDetailsForRelationshipModel.REFERENCED_TABS, referencedTabs);
    
    returnMap.put(IGetRelationshipModel.CONFIG_DETAILS, configDetails);
  }
  
  public static void fillConfigDetailsForSide(Map<String, Object> referencedAttributes,
      Map<String, Object> referencedTags, Map<String, Object> referencedKlasses,
      Map<String, Object> referencedContexts, Map<String, Object> sideMap) throws Exception
  {
    List<Map<String, Object>> attributes = (List<Map<String, Object>>) sideMap
        .get(IRelationshipSide.ATTRIBUTES);
    List<Map<String, Object>> allAttributes = new ArrayList<>();
    allAttributes.addAll(attributes);
    for (Map<String, Object> attribute : attributes) {
      String attributeId = (String) attribute.get(IReferencedRelationshipProperty.ID);
      if (!referencedAttributes.containsKey(attributeId)) {
        referencedAttributes.put(attributeId, getReferencedAttribute(attributeId));
      }
    }
    List<Map<String, Object>> tags = (List<Map<String, Object>>) sideMap
        .get(IRelationshipSide.TAGS);
    List<Map<String, Object>> allTags = new ArrayList<>();
    allTags.addAll(tags);
    for (Map<String, Object> tag : tags) {
      String tagId = (String) tag.get(IReferencedRelationshipProperty.ID);
      if (!referencedTags.containsKey(tagId)) {
        referencedTags.put(tagId, getReferencedTag(tagId));
      }
    }
    
    String klassId = (String) sideMap.get(IRelationshipSide.KLASS_ID);
    Map<String, Object> klassMap = new HashMap<>();
    Vertex klassVertex = UtilClass.getVertexById(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
    klassMap.putAll(mapFromVertex);
    referencedKlasses.put(klassId, klassMap);
    referencedKlasses.put(klassId, klassMap);
    
    String contextId = (String) sideMap.get(IRelationshipSide.CONTEXT_ID);
    if (contextId != null) {
      Map<String, Object> contextMap = new HashMap<>();
      Vertex contextVertex = UtilClass.getVertexById(contextId,
          VertexLabelConstants.VARIANT_CONTEXT);
      
      contextMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IVariantContextModel.LABEL,
              IVariantContextModel.TYPE, IVariantContextModel.ICON, IVariantContext.CODE),
          contextVertex);
      referencedContexts.put(contextId, contextMap);
    }
  }
  
  // Do not make this function public
  private static Map<String, Object> getReferencedAttribute(String attributeId) throws Exception
  {
    Vertex attributeNode = null;
    try {
      attributeNode = UtilClass.getVertexById(attributeId,
          VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    }
    catch (NotFoundException e) {
      throw new AttributeNotFoundException();
    }
    return UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
        IAttribute.LABEL, IAttribute.TYPE, IAttribute.CODE), attributeNode);
  }
  
  // Do not make this function public
  private static Map<String, Object> getReferencedTag(String tagId) throws Exception
  {
    Vertex tagNode;
    try {
      tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
    }
    catch (NotFoundException e) {
      throw new TagNotFoundException();
    }
    return UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, ITag.LABEL, ITag.TYPE, ITag.CODE),
        tagNode);
  }
  
  public static void prepareRelationshipSidesTranslations(Map<String, Object> relationshipMap)
  {
    Map<String, Object> side1 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    prepareSideMapTranslation(side1);
    
    Map<String, Object> side2 = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    prepareSideMapTranslation(side2);
  }
  
  public static void prepareSideMapTranslation(Map<String, Object> side)
  {
    String userLanguage = UtilClass.getLanguage()
        .getUiLanguage();
    
    Set<String> languageKeys = new HashSet<String>();
    Map<String, Object> sideLangKeyMap = new HashMap<String, Object>();
    
    for (String key : side.keySet()) {
      if (!key.contains(Seperators.FIELD_LANG_SEPERATOR)) {
        continue;
      }
      Integer _Index = key.indexOf(Seperators.FIELD_LANG_SEPERATOR);
      String keyWithoutLangSuffix = key.substring(0, _Index);
      languageKeys.add(keyWithoutLangSuffix);
      sideLangKeyMap.put(key, side.get(key));
    }
    
    side.keySet()
        .removeAll(sideLangKeyMap.keySet());
    
    for (String key : languageKeys) {
      
      if (userLanguage == null) { // default handling
        continue;
      }
      
      String value = (String) sideLangKeyMap.get(key + Seperators.FIELD_LANG_SEPERATOR + userLanguage);
      // Default Label handling
      if (value == null || value.isEmpty()) {
        value = (String) side.get(CommonConstants.DEFAULT_LABEL);
      }
      side.put(key, value);
    }
    side.remove(CommonConstants.DEFAULT_LABEL);
  }
}
