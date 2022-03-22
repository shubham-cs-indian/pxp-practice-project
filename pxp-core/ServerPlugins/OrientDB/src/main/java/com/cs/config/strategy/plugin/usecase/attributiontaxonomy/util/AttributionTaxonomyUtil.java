package com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagAndTagValuesIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagLevelEntity;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.ParentKlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.ICreateMasterTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyConfigDetailsModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class AttributionTaxonomyUtil {
  
  public static final List<String>    fieldsToFetch                                  = Arrays
      .asList(IMasterTaxonomy.TAXONOMY_TYPE, ITaxonomy.CODE, IMasterTaxonomy.CHILD_COUNT,
          IMasterTaxonomy.LABEL, IMasterTaxonomy.ICON, IMasterTaxonomy.VERSION_ID,
          IMasterTaxonomy.VERSION_TIMESTAMP, IMasterTaxonomy.LAST_MODIFIED_BY);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY       = Arrays
      .asList(IMasterTaxonomy.LABEL, CommonConstants.CODE_PROPERTY);
  
  protected static List<String>       FIELDS_TO_FETCH_FOR_REFERENCED_CONTEXT_KLASSES = Arrays
      .asList(CommonConstants.CODE_PROPERTY, IKlassInformationModel.LABEL,
          IKlassInformationModel.TYPE, IKlassInformationModel.NATURE_TYPE,
          IKlassInformationModel.CODE, IKlassInformationModel.ICON);
  
  public static Map<String, Object> getAttributionTaxonomy(Vertex klassTaxonomyVertex, String levelTaxonomy) throws Exception
  {
    Map<String, Object> mapToReturn = UtilClass.getMapFromNode(klassTaxonomyVertex);
    
    List<Map<String, Object>> children = TaxonomyUtil
        .getImmediateChildrenKlassTaxonomy(klassTaxonomyVertex);
    mapToReturn.put(IGetAttributionTaxonomyModel.CHILDREN, children);
    
    List<String> klasses = TaxonomyUtil.getKlasses(klassTaxonomyVertex);
    mapToReturn.put(IGetAttributionTaxonomyModel.APPLIED_KLASSES, klasses);
    KlassUtils.addDataRules(klassTaxonomyVertex, mapToReturn);
    KlassUtils.addKlassTasks(klassTaxonomyVertex, mapToReturn);
    List<Map<String, Object>> tagLevels = new ArrayList<>();
    mapToReturn.put(IGetAttributionTaxonomyModel.TAG_LEVELS, tagLevels);
    fillTagLevels(klassTaxonomyVertex, mapToReturn, tagLevels, levelTaxonomy);
    
    KlassUtils.addSectionsToKlassEntityMap(klassTaxonomyVertex, mapToReturn, false);
    
    fillParentAndMasterTagId(klassTaxonomyVertex, mapToReturn);
    
    return mapToReturn;
  }
  
  private static void fillParentAndMasterTagId(Vertex klassTaxonomyVertex,
      Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> parentMap = new HashMap<>();
    
    Iterable<Vertex> parentVertices = klassTaxonomyVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex vertex : parentVertices) {
      String parentId = (String) vertex.getProperty(CommonConstants.CODE_PROPERTY);
      parentMap.put(ITreeEntity.ID, parentId);
      
      Vertex masterTagParentNode = TagUtils.getParentTag(klassTaxonomyVertex);
      String masterTagId = (String) masterTagParentNode
          .getProperty(CommonConstants.CODE_PROPERTY);
      mapToReturn.put(IGetAttributionTaxonomyModel.PARENT, parentMap);
      mapToReturn.put(ITaxonomy.LINKED_MASTER_TAG_ID, masterTagId);
    }
  }
  
  public static void fillTagLevels(Vertex taxonomyNode, Map<String, Object> mapToReturn,
      List<Map<String, Object>> tagLevels, String levelTaxonomy)
      throws Exception
  {
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetAttributionTaxonomyModel.REFERENCED_TAGS);
    if (referencedTags == null) {
      referencedTags = new HashMap<>();
      mapToReturn.put(IGetAttributionTaxonomyModel.REFERENCED_TAGS, referencedTags);
    }
    List<String> tagLevelSequence = taxonomyNode.getProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE);
    if (tagLevelSequence == null) {
      return;
    }
    for (String levelId : tagLevelSequence) {
      Vertex levelNode = UtilClass.getVertexById(levelId, levelTaxonomy);
      Map<String, Object> tagMap = getTagMap(taxonomyNode, levelNode, referencedTags);
      
      Map<String, Object> tagLevelMap = new HashMap<String, Object>();
      tagLevelMap.put(ITagLevelEntity.TAG, tagMap);
      tagLevelMap.put(ITagLevelEntity.ID, levelId);
      tagLevels.add(tagLevelMap);
    }
  }
  
  private static Map<String, Object> getTagMap(Vertex taxonomyNode, Vertex levelNode,
      Map<String, Object> referencedTags) throws Exception
  {
    Vertex tagNode = levelNode
        .getVertices(Direction.IN, RelationshipLabelConstants.LEVEL_TAGGROUP_OF)
        .iterator()
        .next();
    String tagId = UtilClass.getCodeNew(tagNode);
    List<String> tagValueIds = new ArrayList<String>();
    /*String query = "select from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY
        + " Where in('Child_Of').code contains '" + taxonomyId + "' and in('Child_Of').code contains '"
        + tagId + "'";
    Iterable<Vertex> childTagNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();*/
    Iterable<Vertex> childNodes = tagNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : childNodes) {
      Vertex vertex = getParentTaxonomy(childNode);
      if (taxonomyNode.equals(vertex)) {
        tagValueIds.add(UtilClass.getCodeNew(childNode));
      }
    }
    Map<String, Object> tagMap = new HashMap<String, Object>();
    tagMap.put(ITagAndTagValuesIds.ID, tagId);
    tagMap.put(ITagAndTagValuesIds.TAG_VALUE_IDS, tagValueIds);
    
    if (!referencedTags.containsKey(tagId)) {
      Map<String, Object> referencedTagMap = TagUtils.getTagMap(tagNode, false);
      referencedTags.put(tagId, referencedTagMap);
    }
    return tagMap;
  }
  
  public static void fillAttributionTaxonomyData(String id, Map<String, Object> mapToReturn,
      Vertex klassTaxonomyVertex, Boolean shouldGetKP, Boolean shouldFillReferencedTaxonomies) throws Exception
  {
    Map<String, Object> taxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetMasterTaxonomyWithoutKPModel.ENTITY);
    Map<String, Object> configDetails = new HashMap<>();
    mapToReturn.put(IGetMasterTaxonomyWithoutKPModel.CONFIG_DETAILS, configDetails);
    
    // TODO: defaultFilterTags
    /*    List<String> appliedKlasses = (List<String>) taxonomyMap
        .get(IGetAttributionTaxonomyModel.APPLIED_KLASSES);
    if (appliedKlasses == null) {
      appliedKlasses = new ArrayList<>();
    }
    Set<String> klassIdsSet = new HashSet<>();
    for (String klassId : appliedKlasses) {
      Vertex klassVertex = UtilClass.getVertexById(klassId, vertexLabel);
      klassIdsSet.addAll(TaxonomyUtil.getOwnAndAllChildrenKlassIdsSet(klassVertex));
    }
    appliedKlasses = new ArrayList<>();
    appliedKlasses.addAll(klassIdsSet);*/
    
    if (!id.equals("-1")) {
      // TODO :: when there will be permissions in taxonomy, below temp fix can
      // be removed..
      taxonomyMap.remove(IKlass.PERMISSIONS);
      
      configDetails.put(IGetMasterTaxonomyConfigDetailsModel.REFERENCED_TAGS,
          taxonomyMap.remove(IGetAttributionTaxonomyModel.REFERENCED_TAGS));
      configDetails.put(IGetMasterTaxonomyConfigDetailsModel.REFERENCED_ATTRIBUTES,
          new HashMap<>());
      
      fillContextKlassesDetials(taxonomyMap, configDetails, klassTaxonomyVertex);
      TaxonomyUtil.fillReferencedAttributeContextDetails(taxonomyMap, configDetails);
      TaxonomyUtil.fillReferencedTaskDetails(taxonomyMap, configDetails);
      TaxonomyUtil.fillReferencedDataRuleDetails(taxonomyMap, configDetails);
      
      if(shouldFillReferencedTaxonomies) {
        TaxonomyUtil.fillReferencedTaxonomies(taxonomyMap, configDetails);
      }
    }
  }
  
  /**
   * @author Lokesh
   * @param returnKlassMap
   * @param taxonomyNode
   * @throws Exception
   */
  public static void fillContextKlassesDetials(Map<String, Object> taxonomyMap,
      Map<String, Object> configDetails, Vertex taxonomyNode) throws Exception
  {
    Map<String, Object> referencedKlasses = (Map<String, Object>) configDetails
        .get(IGetAttributionTaxonomyModel.REFERENCED_KLASSES);
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
      configDetails.put(IGetAttributionTaxonomyModel.REFERENCED_KLASSES, referencedKlasses);
    }
    
    Iterable<Vertex> contextKlasses = taxonomyNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    List<String> embeddedKlassIds = (List<String>) taxonomyMap
        .get(IGetAttributionTaxonomyModel.EMBEDDED_KLASS_IDS);
    if (embeddedKlassIds == null) {
      embeddedKlassIds = new ArrayList<String>();
      taxonomyMap.put(IGetAttributionTaxonomyModel.EMBEDDED_KLASS_IDS, embeddedKlassIds);
    }
    List<String> contextKlassIds = new ArrayList<>();
    for (Vertex contextKlassNode : contextKlasses) {
      String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
      contextKlassIds.add(contextKlassId);
      String natureType = contextKlassNode.getProperty(IKlass.NATURE_TYPE);
      if (natureType.equals(CommonConstants.EMBEDDED_KLASS_TYPE)) {
        embeddedKlassIds.add(contextKlassId);
        Map<String, Object> referencedKlassMap = UtilClass
            .getMapFromVertex(FIELDS_TO_FETCH_FOR_REFERENCED_CONTEXT_KLASSES, contextKlassNode);
        referencedKlasses.put(contextKlassId, referencedKlassMap);
      }
    }
    KlassUtils.fillReferencedPropagableContextKlasses(taxonomyNode, referencedKlasses,
        configDetails, contextKlassIds);
  }
  
  public static Vertex createAttributionTaxonomyNode(Map<String, Object> taxonomyMap,
      String taxonomyVertexType) throws Exception
  {
    String taxonomyId = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.TAXONOMY_ID);
    String taxonomyLabel = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.LABEL);
    String parentTagId = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.PARENT_TAG_ID);
    String taxonomyType = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.TAXONOMY_TYPE);
    String code = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.CODE);
    String baseType = (String) taxonomyMap.get(ICreateMasterTaxonomyModel.BASE_TYPE);
    Boolean isRootMajorTaxonomy  = (Boolean) taxonomyMap.get(ITaxonomy.IS_ROOT);
    
    Long classifierIID = Long.parseLong(
    		String.valueOf(taxonomyMap.get(ICreateMasterTaxonomyModel.CLASSIFIER_IID)));
        
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(taxonomyVertexType,
        CommonConstants.CODE_PROPERTY);
    if (taxonomyId == null || taxonomyId.equals("")) {
      taxonomyId = UtilClass.getUniqueSequenceId(vertexType);
    }
    Map<String, Object> map = new HashMap<>();
    map.put(IMasterTaxonomy.ID, taxonomyId);
    map.put(IMasterTaxonomy.LABEL, taxonomyLabel);
    map.put(IMasterTaxonomy.IS_DIMENSIONAL, false);
    map.put(IMasterTaxonomy.IS_FOR_RELEVANCE, false);
    map.put(IMasterTaxonomy.IS_MULTI_SELECT, false);
    map.put(IMasterTaxonomy.IS_MANDATORY, false);
    map.put(IMasterTaxonomy.IS_STANDARD, false);
    map.put(IMasterTaxonomy.TAXONOMY_TYPE, taxonomyType);
    map.put(IMasterTaxonomy.TAG_LEVEL_SEQUENCE, new ArrayList<String>());
    map.put(IMasterTaxonomy.IS_TAXONOMY, true);
    map.put(IMasterTaxonomy.BASE_TYPE, baseType);
    map.put(ITaxonomy.CODE, code);
    map.put(IMasterTaxonomy.CLASSIFIER_IID, classifierIID);
    map.put(ITaxonomy.IS_ROOT, isRootMajorTaxonomy);
    /*if(taxonomyVertexType.equals(VertexLabelConstants.LANGUAGE_TAXONOMY)) {
      map.put(ILanguageTaxonomy.IS_LANGUAGE_TAXONOMY, true);
    }
    else {
      map.put(IMasterTaxonomy.IS_ATTRIBUTION_TAXONOMY, true);
    }*/
    // tagMap.put(ITag.TAG_TYPE, SystemLevelIds.MASTER_TAG_TYPE_ID);
    
    Vertex taxonomyNode = UtilClass.createNode(map, vertexType, new ArrayList<String>());
    
    if (parentTagId != null && !parentTagId.equals("")) {
      taxonomyNode.setProperty(IMasterTaxonomy.IS_TAG, true);
      Vertex parentTagNode = UtilClass.getVertexById(parentTagId, VertexLabelConstants.ENTITY_TAG);
      taxonomyNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentTagNode);
      List<String> sequenceList = parentTagNode.getProperty(ITag.TAG_VALUES_SEQUENCE);
      sequenceList.add(taxonomyId);
      parentTagNode.setProperty(ITag.TAG_VALUES_SEQUENCE, sequenceList);
    }
    
    return taxonomyNode;
  }
  
  public static Vertex createTagGroupNode(String tagId, String label, String code, String tagType, Object propertyIID)
      throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENTITY_TAG,
        CommonConstants.CODE_PROPERTY);
    if (StringUtils.isEmpty(code)) {
      code = UtilClass.getUniqueSequenceId(vertexType);
    }
    tagId = code;
    Map<String, Object> tagMap = new HashMap<>();
    tagMap.put(ITag.ID, tagId);
    tagMap.put(ITag.LABEL, label);
    tagMap.put(ITag.IS_DIMENSIONAL, false);
    tagMap.put(ITag.IS_FOR_RELEVANCE, false);
    tagMap.put(ITag.IS_MULTI_SELECT, true);
    tagMap.put(ITag.IS_MANDATORY, false);
    tagMap.put(ITag.IS_STANDARD, false);
    tagMap.put(ITag.IS_VERSIONABLE, true);
    tagMap.put(ITag.IS_GRID_EDITABLE, false);
    tagMap.put(ITag.TYPE, CommonConstants.TAG_TYPE);
    tagMap.put(ITag.IS_FILTERABLE, false);
    tagMap.put(ITag.TAG_TYPE, tagType);
    tagMap.put(ITag.TAG_VALUES_SEQUENCE, new ArrayList<String>());
    tagMap.put(ITag.CODE, code);
    tagMap.put(ITag.IS_ROOT, true);
    tagMap.put(ITag.PROPERTY_IID, propertyIID);
    Vertex tagNode = UtilClass.createNode(tagMap, vertexType, new ArrayList<String>());
    return tagNode;
  }
  
  /*  public static List<Map<String, Object>> getParentReferencedTaxonomyKlasses(String id,
      String klassVertexType) throws Exception
  {
    if (id.equals("-1")) {
      return new ArrayList<>();
    }
    List<Map<String, Object>> referencedKlasses = new ArrayList<>();
    Vertex taxonomyVertex = UtilClass.getVertexById(id, VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    Iterable<Vertex> parentVertices = taxonomyVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    if (!parentVertices.iterator()
        .hasNext()) {
      referencedKlasses.addAll(TaxonomyUtil.getDefaultParentReferencedTaxonomyKlasses());
    }
    for (Vertex parentVertex : parentVertices) {
    Vertex parentVertex = getParentTaxonomy(taxonomyVertex);
    if (parentVertex == null) {
      referencedKlasses.addAll(TaxonomyUtil.getDefaultParentReferencedTaxonomyKlasses());
      return referencedKlasses;
    }
    Map<String, Object> klassTaxonomy = TaxonomyUtil.getKlassTaxonomy(parentVertex);
    List<String> appliedKlasses = (List<String>) klassTaxonomy
        .get(IArticleTaxonomy.APPLIED_KLASSES);
    if (appliedKlasses.size() == 0) {
      return getParentReferencedTaxonomyKlasses(
          parentVertex.getProperty(CommonConstants.CODE_PROPERTY), klassVertexType);
    }
    for (String appliedKlass : appliedKlasses) {
      Vertex klass = UtilClass.getVertexById(appliedKlass, klassVertexType);
      Map<String, Object> klassMap = TaxonomyUtil.getReferencedKlasses(klass);
      referencedKlasses.add(klassMap);
    }
    return referencedKlasses;
  }*/
  
  public static List<Map<String, Object>> getTaxonomiesList()
  {
    List<Map<String, Object>> children = new ArrayList<>();
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ATTRIBUTION_TAXONOMY
            + " where outE('Child_Of').size() = 0 and taxonomyType = '"
            + CommonConstants.MAJOR_TAXONOMY + "' order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    
    for (Vertex childKlassTaxonomy : vertices) {
      Map<String, Object> childTaxonomy = UtilClass
          .getMapFromVertex(FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY, childKlassTaxonomy);
      children.add(childTaxonomy);
      getChildrenTaxonomiesList(childKlassTaxonomy, children);
    }
    
    return children;
  }
  
  public static void getChildrenTaxonomiesList(Vertex childKlassTaxonomy,
      List<Map<String, Object>> children)
  {
    Iterable<Vertex> childNodes = childKlassTaxonomy.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : childNodes) {
      Map<String, Object> childTaxonomy = UtilClass
          .getMapFromVertex(FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY, childNode);
      children.add(childTaxonomy);
      getChildrenTaxonomiesList(childNode, children);
    }
  }
  
  public static Vertex getParentTaxonomy(Vertex tagValue)
      throws MultipleLinkFoundException
  {    
    Iterable<Vertex> parentVertices = tagValue.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    Integer count = 0;
    for (Vertex parent : parentVertices) {
      count++;
      Boolean isTaxonomy = parent.getProperty(IMasterTaxonomy.IS_TAXONOMY);
      isTaxonomy = isTaxonomy == null ? false : isTaxonomy;
      if (isTaxonomy) {
        return parent;
      }
    }
    
    if (count > 1) {
      throw new MultipleLinkFoundException();
    }
    return null;
  }
  
  /**
   * @author Lokesh
   * @param taxonomyVertex
   * @return
   * @throws Exception
   */
  public static Vertex getRootParentTaxonomy(Vertex taxonomyVertex) throws Exception
  {
    String query = "select from(traverse out('Child_Of') from " + taxonomyVertex.getId()
        + " strategy BREADTH_FIRST) where outE('Child_Of').size()=0 And " + ITag.TYPE + " is null";
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = resultIterable.iterator();
    if (!iterator.hasNext()) {
      throw new ParentKlassTaxonomyNotFoundException();
    }
    Vertex parentNode = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    return parentNode;
  }
}
