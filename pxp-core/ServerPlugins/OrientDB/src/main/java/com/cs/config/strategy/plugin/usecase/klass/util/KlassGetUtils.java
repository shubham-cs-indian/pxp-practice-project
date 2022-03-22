package com.cs.config.strategy.plugin.usecase.klass.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoConfigDetailsModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedUniqueSelectorModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class KlassGetUtils {
  
  public static final List<String> fieldsToFetchForKlass = Arrays.asList(
      CommonConstants.CODE_PROPERTY, IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON,
      IKlass.LABEL, IKlass.TYPE, IKlass.NATURE_TYPE, IKlass.IS_NATURE, IKlass.PREVIEW_IMAGE,
      IKlass.CODE);
  
  public static List<Map<String, Object>> getKlassesList(String id, String nodeLabel,
      String[] keyValues, List<String> allowedTypes) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> klassesList = new ArrayList<>();
    try {
      Vertex rootNode = UtilClass.getVertexByIndexedId(id, nodeLabel);
      String rid = (String) rootNode.getId()
          .toString();
      String query = "select from(traverse in('Child_Of') from " + rid
          + " strategy BREADTH_FIRST) ";
      if (keyValues != null && keyValues.length > 1) {
        String key = null;
        String value = null;
        query += "where ";
        for (int keyValueIndex = 0; keyValueIndex < keyValues.length; keyValueIndex++) {
          if (keyValueIndex % 2 == 0) {
            key = keyValues[keyValueIndex];
          }
          else {
            value = keyValues[keyValueIndex];
          }
          if (key != null && value != null) {
            query += key + "=\"" + value + "\" and ";
            key = null;
            value = null;
          }
        }
        query = query.substring(0, query.length() - 4);
      }
      
      if (allowedTypes != null) {
        query += " and code in " + EntityUtil.quoteIt(allowedTypes);
      }
      query += " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
          + " asc";
      Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : resultIterable) {
        klassesList.add(UtilClass.getMapFromNode(klassNode));
      }
      
    }
    catch (Exception e) {
    }
    return klassesList;
  }
  
  public static List<Map<String, Object>> getNonAbstractKlassesList(String id, String nodeLabel,
      String[] keyValues, List<Map<String, Object>> klassesList) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.getVertexByIndexedId(id, nodeLabel);
    try {
      Vertex rootNode = UtilClass.getVertexByIndexedId(id, nodeLabel);
      // if (iterator.hasNext()) {
      // rootNode = iterator.next();
      
      String rid = (String) rootNode.getId()
          .toString();
      String query = "select from(traverse in('Child_Of') from " + rid
          + " strategy BREADTH_FIRST) where ";
      if (keyValues != null && keyValues.length > 1) {
        String key = null;
        String value = null;
        // query+= "where ";
        for (int keyValueIndex = 0; keyValueIndex < keyValues.length; keyValueIndex++) {
          if (keyValueIndex % 2 == 0) {
            key = keyValues[keyValueIndex];
          }
          else {
            value = keyValues[keyValueIndex];
          }
          if (key != null && value != null) {
            query += key + "=\"" + value + "\" and ";
            key = null;
            value = null;
          }
        }
      }
      
      query += " (isAbstract = \"false\" or isAbstract is null) order by "
          + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
      
      Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : resultIterable) {
        klassesList.add(UtilClass.getMapFromNode(klassNode));
      }
      
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    return klassesList;
  }
  
  public static List<Map<String, Object>> getKlassesList(String id, String nodeLabel)
      throws Exception
  {
    return getKlassesList(id, nodeLabel, null, null);
  }
  
  public static List<Map<String, Object>> getKlassesListForRoles(String id, String nodeLabel)
      throws Exception
  {
    List<Map<String, Object>> klassesList = new ArrayList<Map<String, Object>>();
    try {
      klassesList = getKlassesList(id, nodeLabel, null, null);
    }
    catch (Exception e) {
    }
    return klassesList;
  }
  
  public static List<Map<String, Object>> getKlassesListForSide(String id, String nodeLabel,
      String[] keyValues, List<String> allowedTypes) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Map<String, Object>> klassesList = new ArrayList<>();
    try {
      Vertex rootNode = UtilClass.getVertexByIndexedId(id, nodeLabel);
      ;
      
      String rid = (String) rootNode.getId()
          .toString();
      String query = "select from(traverse out('Child_Of') from " + rid
          + " strategy BREADTH_FIRST) ";
      if (keyValues != null && keyValues.length > 1) {
        String key = null;
        String value = null;
        query += "where ";
        for (int keyValueIndex = 0; keyValueIndex < keyValues.length; keyValueIndex++) {
          if (keyValueIndex % 2 == 0) {
            key = keyValues[keyValueIndex];
          }
          else {
            value = keyValues[keyValueIndex];
          }
          if (key != null && value != null) {
            query += key + "=\"" + value + "\" and ";
            key = null;
            value = null;
          }
        }
        query = query.substring(0, query.length() - 4);
      }
      
      if (allowedTypes != null) {
        query += " and code in " + EntityUtil.quoteIt(allowedTypes);
      }
      query += " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
          + " asc";
      Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : resultIterable) {
        klassesList.add(UtilClass.getMapFromNode(klassNode));
      }
      
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    return klassesList;
  }
  
  public static void fillTechnicalImageVariantWithAutoCreateEnabled(Vertex klassNode,
      List<Map<String, Object>> contextsWithAutoCreateEnabled) throws Exception
  {
    Iterable<Vertex> contextKlassIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex contextKlassNode : contextKlassIterable) {
      Iterator<Vertex> contextIterator = contextKlassNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator();
      if (!contextIterator.hasNext()) {
        continue;
      }
      Vertex variantContextNode = contextIterator.next();
      Map<String, Object> contextWithAutoCreateEnable = getContextMapWithAutoCreateEnabled(
          variantContextNode);
      if (contextWithAutoCreateEnable != null) {
        contextsWithAutoCreateEnabled.add(contextWithAutoCreateEnable);
      }
    }
  }
  
  /**
   * @Description : For this current contextNode, if its auto create, then fetch
   *              context data like defaultTimeRange, contextualTags and unique
   *              selectors..
   *
   * @author Ajit
   * @param variantContextNode
   * @return
   * @throws Exception
   */
  public static Map<String, Object> getContextMapWithAutoCreateEnabled(Vertex variantContextNode)
      throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IVariantContext.TYPE, IVariantContext.IS_AUTO_CREATE, IVariantContext.DEFAULT_TIME_RANGE);
    Map<String, Object> variantContextMap = UtilClass.getMapFromVertex(fieldsToFetch,
        variantContextNode);
    Boolean isAutoCreate = (Boolean) variantContextMap.get(IVariantContext.IS_AUTO_CREATE);
    if (isAutoCreate != null && !isAutoCreate) {
      return null;
    }
    
    Map<String, Object> tagValueMap = new HashMap<>();
    List<Map<String, Object>> contexualTags = VariantContextUtils
        .getContextTags(variantContextNode);
    Map<String, Object> contextWithAutoCreateEnable = new HashMap<String, Object>();
    contextWithAutoCreateEnable.put(ITechnicalImageVariantWithAutoCreateEnableModel.ID,
        variantContextMap.get(IVariantContext.ID));
    contextWithAutoCreateEnable.put(ITechnicalImageVariantWithAutoCreateEnableModel.CONTEXTUAL_TAGS,
        contexualTags);
    contextWithAutoCreateEnable.put(ITechnicalImageVariantWithAutoCreateEnableModel.TYPE,
        variantContextMap.get(IVariantContext.TYPE));
    contextWithAutoCreateEnable.put(
        ITechnicalImageVariantWithAutoCreateEnableModel.DEFAULT_TIME_RANGE,
        variantContextMap.get(IVariantContext.DEFAULT_TIME_RANGE));
    contextWithAutoCreateEnable.put(ITechnicalImageVariantWithAutoCreateEnableModel.TAG_VALUE_MAP,
        tagValueMap);
    
    // unique selector
    List<Map<String, Object>> uniqueSelections = getUniqueSelections(variantContextNode,
        tagValueMap);
    if (uniqueSelections.size() == 0) {
      return null;
    }
    /*
    for (Map<String, Object> uniqueSelection : uniqueSelections) {
      List<Map<String,Object>> tags = (List<Map<String, Object>>) uniqueSelection.get(IReferencedUniqueSelectorModel.TAGS);
      for (Map<String, Object> tag : tags) {
        List<String>  tagValueIds = (List<String>) tag.get(IReferencedVariantContextTagsModel.TAG_VALUE_IDS);
        for (String tagValueId : tagValueIds) {
          try{
            Vertex tagValueNode = UtilClass.getVertexById(tagValueId, VertexLabelConstants.ENTITY_TAG);
            tagValueIdToLabelMap.put(tagValueId, tagValueNode.getProperty(CommonConstants.LABEL_PROPERTY));
          }
          catch(NotFoundException e){
            throw new TagValueNotFoundException();
          }
        }
      }
    }*/
    contextWithAutoCreateEnable.put(ITechnicalImageVariantWithAutoCreateEnableModel.UNIQUE_SELECTOR,
        uniqueSelections);
    
    // fill attribute ids
    if (variantContextMap.get(IVariantContext.TYPE)
        .equals(CommonConstants.IMAGE_VARIANT)) {
      List<String> attributeIds = new ArrayList<String>();
      contextWithAutoCreateEnable.put(ITechnicalImageVariantWithAutoCreateEnableModel.ATTRIBUTE_IDS,
          attributeIds);
      Iterable<Vertex> vertices = variantContextNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Vertex klassPropertyNode : vertices) {
        String type = klassPropertyNode.getProperty(CommonConstants.TYPE);
        if (!type.equals(CommonConstants.ATTRIBUTE)) {
          continue;
        }
        Iterator<Vertex> iterator = klassPropertyNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        if (!iterator.hasNext()) {
          throw new NotFoundException();
        }
        Vertex attributeNode = iterator.next();
        attributeIds.add(UtilClass.getCodeNew(attributeNode));
      }
    }
    return contextWithAutoCreateEnable;
  }
  
  /**
   * Description : fetch unique selectors from context and fills it in return
   * map.
   *
   * @author Ajit
   * @param contextNode
   * @param returnContextMap
   */
  public static List<Map<String, Object>> getUniqueSelections(Vertex contextNode,
      Map<String, Object> tagValueMap)
  {
    Iterable<Vertex> uniqueSelectors = contextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_UNIQUE_SELECTOR);
    List<Map<String, Object>> uniqueSelections = new ArrayList<>();
    
    for (Vertex uniqueSelectorNode : uniqueSelectors) {
      Iterable<Vertex> uniqueTagProperties = uniqueSelectorNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_UNIQUE_TAG_PROPERTY);
      Map<String, Object> uniqueSelectionMap = new HashMap<>();
      List<Map<String, Object>> selectionValues = new ArrayList<>();
      uniqueSelectionMap.put(IReferencedUniqueSelectorModel.TAGS, selectionValues);
      uniqueSelections.add(uniqueSelectionMap);
      for (Vertex uniqueTagPropertyNode : uniqueTagProperties) {
        
        // populate tag
        Iterable<Vertex> uniqueTags = uniqueTagPropertyNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_UNIQUE_TAG);
        Map<String, Object> tagMap = new HashMap<>();
        selectionValues.add(tagMap);
        List<String> tagValueIds = new ArrayList<>();
        tagMap.put(IReferencedVariantContextTagsModel.TAG_VALUE_IDS, tagValueIds);
        for (Vertex tagNode : uniqueTags) {
          String tagId = UtilClass.getCodeNew(tagNode);
          tagMap.put(IReferencedVariantContextTagsModel.TAG_ID, tagId);
        }
        
        // populate tag values
        Iterable<Vertex> uniqueTagValues = uniqueTagPropertyNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_UNIQUE_TAG_VALUE);
        for (Vertex tagValueNode : uniqueTagValues) {
          String tagValueId = UtilClass.getCodeNew(tagValueNode);
          HashMap<String, Object> mapFromNode = UtilClass.getMapFromNode(tagValueNode);
          mapFromNode.putIfAbsent(ITag.TYPE, CommonConstants.TAG_TYPE);
          tagValueMap.put(tagValueId, mapFromNode);
          tagValueIds.add(tagValueId);
          
          Iterator<Vertex> parentIterator = tagValueNode
              .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
              .iterator();
          if (!parentIterator.hasNext()) {
            continue;
          }
          Vertex parentTag = parentIterator.next();
          if (CommonConstants.BOOLEAN_TAG_TYPE_ID.equals(parentTag.getProperty(ITag.TAG_TYPE))) {
            HashMap<String, Object> childTagMap = (HashMap<String, Object>) tagValueMap
                .get(tagValueId);
            childTagMap.put(ITag.LABEL, UtilClass.getValueByLanguage(parentTag, ITag.LABEL));
          }
        }
      }
    }
    
    return uniqueSelections;
  }
  
  /**
   * @throws Exception
   *           @Krish @Description - This method fills klass info in a map from
   *           given klass ids
   */
  public static void fillKlassInfoForKlassIds(List<String> klassIds,
      Map<String, Object> klassInfoMap, Boolean shouldGetNonNature, Boolean shouldFetchChild)
      throws Exception
  {
    
    for (String klassId : klassIds) {
      try {
        Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
        isNature = (isNature == null) ? false : isNature;
        if (shouldGetNonNature || isNature) {
          Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetchForKlass,
              klassNode);
          klassInfoMap.put(klassId, klassMap);
        }
        if (shouldFetchChild) {
          fillChildKlassesInfo(klassNode, klassInfoMap, shouldGetNonNature);
        }
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
    }
  }
  
  /**
   * @Krish @Description - This method fills klass info of children in a map for
   *        the given Root Node
   */
  public static void fillChildKlassesInfo(Vertex rootNode, Map<String, Object> klassInfoMap,
      Boolean shouldGetNonNature)
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = (String) rootNode.getId()
        .toString();
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex klassNode : resultIterable) {
      String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
      Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
      isNature = (isNature == null) ? false : isNature;
      if (shouldGetNonNature || isNature) {
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetchForKlass, klassNode);
        klassInfoMap.put(klassId, klassMap);
      }
    }
  }
  
  public static Map<String, Object> getKlassEntityReferencesMap(Vertex klassNode,
      boolean shouldGetKP) throws Exception
  {
    HashMap<String, Object> klassReturnMap = new HashMap<>();
    Map<String, Object> configDetails = new HashMap<>();
    Map<String, Object> klassEntityMap = KlassUtils.getKlassMap(klassNode, shouldGetKP);
    klassReturnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, klassEntityMap);
    klassReturnMap.put(IGetKlassEntityWithoutKPModel.CONFIG_DETAILS, configDetails);
    return klassReturnMap;
  }
  
  public static void fillReferencedConfigDetails(Map<String, Object> returnMap, Vertex klassNode)
      throws Exception
  {
    Map<String, Object> configDetails = (Map<String, Object>) returnMap
        .get(IGetKlassEntityWithoutKPModel.CONFIG_DETAILS);
    if (configDetails == null) {
      configDetails = new HashMap<>();
      returnMap.put(IGetKlassEntityWithoutKPModel.CONFIG_DETAILS, configDetails);
    }
    KlassUtils.fillReferencedAttributesAndTagsDetails(configDetails,
        (Map<String, Object>) returnMap.get(IGetKlassEntityWithoutKPModel.ENTITY));
    KlassUtils.fillReferencedContextDetails(returnMap, configDetails);
    KlassUtils.fillReferencedTaskDetails(returnMap, configDetails);
    KlassUtils.fillReferencedDataRuleDetails(returnMap, configDetails);
    KlassUtils.fillContextKlassDetails(returnMap, klassNode);
    KlassUtils.fillTabDetailsAssociatedWithNatureRelationship(returnMap, configDetails);
  }
  
  public static void fillReferencedConfigDetailsForSection(Map<String, Object> returnMap)
      throws Exception
  {
    List<Map<String, Object>> sections = (List<Map<String, Object>>) returnMap
        .get(IGetSectionInfoModel.LIST);
    Map<String, Object> configDetails = new HashMap<>();
    returnMap.put(IGetSectionInfoModel.CONFIGDETAILS, configDetails);
    
    Map<String, Object> referencedContexts = new HashMap<>();
    configDetails.put(IGetSectionInfoConfigDetailsModel.REFERENCED_CONTEXTS, referencedContexts);
    
    for (Map<String, Object> section : sections) {
      List<Map<String, Object>> elements = (List<Map<String, Object>>) section
          .get(ISection.ELEMENTS);
      for (Map<String, Object> element : elements) {
        if (!element.get(ISectionElement.TYPE)
            .equals(CommonConstants.ATTRIBUTE)) {
          continue;
        }
        String attributeVariantContextId = (String) element
            .get(ISectionElement.ATTRIBUTE_VARIANT_CONTEXT);
        if (attributeVariantContextId == null
            || referencedContexts.containsKey(attributeVariantContextId)) {
          continue;
        }
        Vertex attributeVariantContextNode = UtilClass.getVertexById(attributeVariantContextId,
            VertexLabelConstants.VARIANT_CONTEXT);
        Map<String, Object> mapFromVertex = UtilClass.getMapFromVertex(
            Arrays.asList(CommonConstants.CODE_PROPERTY, IVariantContext.TYPE,
                IVariantContext.LABEL, IVariantContext.ICON, IVariantContext.CODE),
            attributeVariantContextNode);
        referencedContexts.put(attributeVariantContextId, mapFromVertex);
      }
    }
  }
}
