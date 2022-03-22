package com.cs.config.strategy.plugin.usecase.base.taxonomy.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomyarticle.ParentArticleTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTagModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTaxonomyLevelModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyConfigDetailsModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.interactor.model.taxonomy.IFilterDataModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ISortDataModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class TaxonomyUtil {
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA = Arrays.asList(
      IAttribute.LABEL, IAttribute.TYPE, IUnitAttribute.DEFAULT_UNIT, INumberAttribute.PRECISION,
      CommonConstants.CODE_PROPERTY);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA       = Arrays
      .asList(ITag.LABEL, ITag.TAG_TYPE, CommonConstants.CODE_PROPERTY);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_SORT_DATA             = Arrays
      .asList(IAttribute.TYPE, IAttribute.LABEL, CommonConstants.CODE_PROPERTY);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_REFERENCED_KLASSES    = Arrays
      .asList(CommonConstants.CODE_PROPERTY, IKlass.LABEL);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY  = Arrays
      .asList(ITaxonomy.LABEL, CommonConstants.CODE_PROPERTY, CommonConstants.ICON_PROPERTY);
  
  public static final List<String>    FIELDS_TO_FETCH_FOR_DEFAULT_FILTERS       = Arrays.asList(
      CommonConstants.CODE_PROPERTY, ITag.COLOR, ITag.IS_MULTI_SELECT, ITag.DEFAULT_VALUE,
      ITag.TAG_TYPE, ITag.TAG_VALUES, ITag.IS_DIMENSIONAL, ITag.SHOULD_DISPLAY,
      ITag.IS_FOR_RELEVANCE, ITag.KLASS, ITag.TAG_VALUES_SEQUENCE, ITag.DESCRIPTION, ITag.TOOLTIP,
      ITag.IS_MANDATORY, ITag.IS_STANDARD, ITag.PLACEHOLDER, ITag.LABEL, ITag.ICON, ITag.TYPE,
      ITag.VERSION_ID, ITag.VERSION_TIMESTAMP, ITag.LAST_MODIFIED_BY);
  
  @Deprecated
  /*Because configDetails should be filled everytime..*/
  public static Map<String, Object> getKlassTaxonomy(Vertex klassTaxonomy) throws Exception
  {
    Map<String, Object> mapToReturn = UtilClass.getMapFromVertex(new ArrayList<String>(),
        klassTaxonomy);
    
    List<Map<String, Object>> children = getImmediateChildrenKlassTaxonomy(klassTaxonomy);
    mapToReturn.put(IGetAttributionTaxonomyModel.CHILDREN, children);
    
    List<String> klasses = getKlasses(klassTaxonomy);
    mapToReturn.put(IGetAttributionTaxonomyModel.APPLIED_KLASSES, klasses);
    mapToReturn.put(ITaxonomy.LINKED_MASTER_TAG_ID, getLinkedMasterTagId(klassTaxonomy));
    mapToReturn.put(IGetAttributionTaxonomyModel.LINKED_LEVELS, getLinkedLevel(klassTaxonomy, mapToReturn, new HashMap<>()));
    
    return mapToReturn;
  }
  
  public static Map<String, String> getLinkedLevel(Vertex klassTaxonomy,
      Map<String, Object> mapToReturn, Map<String, Object> configDetails) throws Exception
  {
    Map<String, String> levelMap = new HashMap<>();
    Map<String, Object> referencedTags = new HashMap<>();
    configDetails.put(IGetAttributionTaxonomyModel.REFERENCED_TAGS, referencedTags);
    Iterable<Edge> taxonomyWithMasterTagEdges = klassTaxonomy.getEdges(Direction.OUT,
        RelationshipLabelConstants.TAXONOMY_LEVEL);
    if (taxonomyWithMasterTagEdges != null) {
      for (Edge taxonomyWithMasterTagEdge : taxonomyWithMasterTagEdges) {
        String level = taxonomyWithMasterTagEdge.getProperty(CommonConstants.TAXONOMY_LEVEL);
        Vertex masterTagVertex = taxonomyWithMasterTagEdge.getVertex(Direction.IN);
        String masterTagId = masterTagVertex.getProperty(CommonConstants.CODE_PROPERTY);
        if (!referencedTags.containsKey(masterTagId)) {
          Map<String, Object> referencedTagMap = TagUtils.getTagMap(masterTagVertex, true);
          referencedTags.put(masterTagId, referencedTagMap);
        }
        levelMap.put(level, masterTagId);
      }
    }
    return levelMap;
  }
  
  public static String getLinkedMasterTagId(Vertex klassTaxonomy)
  {
    Iterator<Edge> edgeBetweemTaxonomyAndMasterTag = klassTaxonomy
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_MASTER_TAG)
        .iterator();
    if (edgeBetweemTaxonomyAndMasterTag.hasNext()) {
      Edge linkEdge = edgeBetweemTaxonomyAndMasterTag.next();
      Vertex masterTagVertex = linkEdge.getVertex(Direction.IN);
      return masterTagVertex.getProperty(CommonConstants.CODE_PROPERTY);
    }
    return null;
  }
  
  public static List<String> getKlasses(Vertex klassTaxonomy)
  {
    Iterable<Vertex> klassVertices = klassTaxonomy.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_TAXONOMY_LINK);
    List<String> klasses = new ArrayList<>();
    for (Vertex klass : klassVertices) {
      klasses.add(klass.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return klasses;
  }
  
  public static List<Map<String, Object>> getImmediateChildrenKlassTaxonomy(Vertex klassTaxonomy)
  {
    List<Map<String, Object>> children = new ArrayList<>();
    String rid = klassTaxonomy.getId()
        .toString();
    String query = "select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
        + "')) from " + rid + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> childrenVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Vertex childKlassTaxonomy : childrenVertices) {
      Map<String, Object> childKlassTaxonomyMap = UtilClass
          .getMapFromVertex(FIELDS_TO_FETCH_FOR_CHILD_KLASS_TAXONOMY, childKlassTaxonomy);
      String linkedMasterTagId = getLinkedMasterTagId(childKlassTaxonomy);
      childKlassTaxonomyMap.put(ITaxonomy.LINKED_MASTER_TAG_ID, linkedMasterTagId);
      children.add(childKlassTaxonomyMap);
    }
    return children;
  }
  
  public static Map<String, Object> getSortData(Collection<String> attributeIds) throws Exception
  {
    List<Map<String, Object>> attributes = new ArrayList<>();
    Iterable<Vertex> attributeVertices = UtilClass.getVerticesByIdsInSortedOrder(attributeIds,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY),
        CommonConstants.SORTORDER_ASC);
    for (Vertex attribute : attributeVertices) {
      Map<String, Object> attributeMap = UtilClass.getMapFromVertex(FIELDS_TO_FETCH_FOR_SORT_DATA,
          attribute);
      attributes.add(attributeMap);
    }
    Map<String, Object> sortData = new HashMap<>();
    sortData.put(ISortDataModel.ATTRIBUTES, attributes);
    return sortData;
  }
  
  public static Map<String, Object> getFilterData(Collection<String> tagIds,
      Collection<String> attributeIds) throws Exception
  {
    List<Map<String, Object>> tags = new ArrayList<>();
    Iterable<Vertex> tagVertices = UtilClass.getVerticesByIdsInSortedOrder(tagIds,
        VertexLabelConstants.ENTITY_TAG,
        EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY),
        CommonConstants.SORTORDER_ASC);
    for (Vertex tag : tagVertices) {
      Map<String, Object> tagMap = getFilterTagDetails(tag, FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA);
      tags.add(tagMap);
    }
    
    List<Map<String, Object>> attributes = new ArrayList<>();
    Iterable<Vertex> attributeVertices = UtilClass.getVerticesByIdsInSortedOrder(attributeIds,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
        EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY),
        CommonConstants.SORTORDER_ASC);
    
    for (Vertex attribute : attributeVertices) {
      Map<String, Object> attributeMap = getAttributesDetails(attribute,
          FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA);
      attributes.add(attributeMap);
    }
    
    Map<String, Object> filterData = new HashMap<>();
    filterData.put(IFilterDataModel.TAGS, tags);
    filterData.put(IFilterDataModel.ATTRIBUTES, attributes);
    return filterData;
  }
  
  private static Map<String, Object> getAttributesDetails(Vertex attribute,
      List<String> fieldsToFetch)
  {
    Map<String, Object> attributeMap = UtilClass.getMapFromVertex(fieldsToFetch, attribute);
    attributeMap.put(IConfigEntityTreeInformationModel.CHILDREN, new ArrayList<>());
    return attributeMap;
  }
  
  public static Map<String, Object> getFilterTagDetails(Vertex tag, List<String> fieldsToFetch)
  {
    Map<String, Object> tagMap = UtilClass.getMapFromVertex(fieldsToFetch, tag);
    tagMap.put(IConfigEntityTreeInformationModel.TYPE, tagMap.get(ITag.TAG_TYPE));
    tagMap.remove(ITag.TAG_TYPE);
    Iterable<Vertex> childTags = tag.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    List<Map<String, Object>> children = new ArrayList<>();
    for (Vertex childTag : childTags) {
      Map<String, Object> childTagMap = getFilterTagDetails(childTag, fieldsToFetch);
      if (tagMap.get(ITag.TYPE) != null && tagMap.get(ITag.TYPE)
          .equals(CommonConstants.BOOLEAN_TAG_TYPE_ID)) {
        childTagMap.put(ITag.LABEL, tagMap.get(ITag.LABEL));
      }
      children.add(childTagMap);
    }
    tagMap.put(IConfigEntityTreeInformationModel.CHILDREN, children);
    return tagMap;
  }
  
  public static List<Map<String, Object>> getDefaultFilterTags(
      Collection<String> tagIdsForDefaultFilter) throws Exception
  {
    List<Map<String, Object>> tags = new ArrayList<>();
    Iterable<Vertex> tagVertices = UtilClass.getVerticesByIds(tagIdsForDefaultFilter,
        VertexLabelConstants.ENTITY_TAG);
    for (Vertex tag : tagVertices) {
      Map<String, Object> tagMap = getTagDetails(tag, FIELDS_TO_FETCH_FOR_DEFAULT_FILTERS);
      tagMap.put(ITag.TAG_VALUES, TagUtils.getTagValues(tag));
      tags.add(tagMap);
    }
    return tags;
  }
  
  private static Map<String, Object> getTagDetails(Vertex tag, List<String> fieldsToFetch)
  {
    Map<String, Object> tagMap = UtilClass.getMapFromVertex(fieldsToFetch, tag);
    Iterable<Vertex> childTags = tag.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    List<Map<String, Object>> children = new ArrayList<>();
    for (Vertex childTag : childTags) {
      Map<String, Object> childTagMap = getTagDetails(childTag, fieldsToFetch);
      if (childTagMap.get(ITag.TYPE) == null || childTagMap.get(ITag.TYPE)
          .equals("")) {
        childTagMap.put(ITag.TYPE, CommonConstants.TAG_TYPE);
      }
      children.add(childTagMap);
    }
    tagMap.put(IConfigEntityTreeInformationModel.CHILDREN, children);
    return tagMap;
  }
  
  public static Map<String, Object> getReferencedKlasses(Vertex klass)
  {
    Map<String, Object> klassMap = UtilClass
        .getMapFromVertex(FIELDS_TO_FETCH_FOR_REFERENCED_KLASSES, klass);
    List<Map<String, Object>> childrenList = new ArrayList<>();
    Iterable<Vertex> children = klass.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex child : children) {
      Map<String, Object> childMap = getReferencedKlasses(child);
      childrenList.add(childMap);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, childrenList);
    return klassMap;
  }
  
  public static void deleteTaxonomyNodesAttachedToSectionElement(String sectionElementId,
      Boolean isKlassDeleted) throws Exception
  {
    Vertex sectionElement = UtilClass.getVertexById(sectionElementId,
        VertexLabelConstants.ENTITY_KLASS_PROPERTY);
    
    Iterable<Vertex> filterableTags = sectionElement.getVertices(Direction.IN,
        RelationshipLabelConstants.FILTERABLE_TAG_SECTION_ELEMENT_LINK);
    Iterable<Vertex> defaultFilterTags = sectionElement.getVertices(Direction.IN,
        RelationshipLabelConstants.DEFAULT_FILTER_SECTION_ELEMENT_LINK);
    Iterable<Vertex> sortableAttributes = sectionElement.getVertices(Direction.IN,
        RelationshipLabelConstants.SORTABLE_ATTRIBUTE_SECTION_ELEMENT_LINK);
    
    if (isKlassDeleted.equals(true)) {
      deleteVerticesConnectedToSingleSectionElement(filterableTags,
          RelationshipLabelConstants.FILTERABLE_TAG_SECTION_ELEMENT_LINK);
      deleteVerticesConnectedToSingleSectionElement(sortableAttributes,
          RelationshipLabelConstants.SORTABLE_ATTRIBUTE_SECTION_ELEMENT_LINK);
      deleteVerticesConnectedToSingleSectionElement(defaultFilterTags,
          RelationshipLabelConstants.DEFAULT_FILTER_SECTION_ELEMENT_LINK);
    }
    else {
      UtilClass.deleteVertices(filterableTags);
      UtilClass.deleteVertices(sortableAttributes);
      UtilClass.deleteVertices(defaultFilterTags);
    }
  }
  
  @SuppressWarnings("unused")
  public static void deleteVerticesConnectedToSingleSectionElement(Iterable<Vertex> Vertices,
      String edgeName)
  {
    for (Vertex vertex : Vertices) {
      int vertexCount = 0;
      Iterable<Vertex> linkedVertices = vertex.getVertices(Direction.OUT, edgeName);
      for (Vertex linkedVertice : linkedVertices) {
        vertexCount++;
      }
      if (vertexCount < 2) {
        vertex.remove();
      }
    }
  }
  
  public static Set<String> getOwnAndAllChildrenKlassIdsSet(Vertex klassVertex)
  {
    Set<String> allIdsSet = new HashSet<>();
    Iterable<Vertex> childrenVertices = klassVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childVertex : childrenVertices) {
      allIdsSet.addAll(getOwnAndAllChildrenKlassIdsSet(childVertex));
    }
    allIdsSet.add(klassVertex.getProperty(CommonConstants.CODE_PROPERTY));
    return allIdsSet;
  }
  
  public static Set<Vertex> getOwnAndAllChildrenKlassNodes(Vertex klassVertex)
  {
    Set<Vertex> allNodes = new HashSet<>();
    Iterable<Vertex> childrenVertices = klassVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childVertex : childrenVertices) {
      allNodes.addAll(getOwnAndAllChildrenKlassNodes(childVertex));
    }
    allNodes.add(klassVertex);
    
    return allNodes;
  }
  
  public static Set<String> getOwnAndAllChildrenKlassIdsSet(String parentKlassId, String label)
  {
    Set<String> allIdsSet = new HashSet<>();
    Iterable<Vertex> childrenVertices = getOwnAndChildOfVertices(parentKlassId, label);
    for (Vertex childVertex : childrenVertices) {
      allIdsSet.add(UtilClass.getCodeNew(childVertex));
    }
    
    return allIdsSet;
  }
  
  public static Iterable<Vertex> getOwnAndChildOfVertices(String parentKlassId, String label)
  {
    String query = "SELECT FROM (TRAVERSE in(\""
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "\") FROM (SELECT FROM " + label
        + " where code=\"" + parentKlassId + "\"))";
    Iterable<Vertex> childrenVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return childrenVertices;
  }
  
  public static List<Map<String, Object>> getDefaultParentReferencedTaxonomyKlasses()
      throws Exception
  {
    List<Map<String, Object>> referencedKlasses = new ArrayList<>();
    
    Iterable<Vertex> iterableKlasses = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
            + " where outE('Child_Of').size() = 0 order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    for (Vertex klass : iterableKlasses) {
      Map<String, Object> klassMap = TaxonomyUtil.getReferencedKlasses(klass);
      referencedKlasses.add(klassMap);
    }
    return referencedKlasses;
  }
  
  public static void getTaxonomyData(String id, Map<String, Object> mapToReturn,
      Vertex klassTaxonomy, String vertexLabel) throws Exception
  {
    // TODO: defaultFilterTags
    List<String> appliedKlasses = (List<String>) mapToReturn.get(ITaxonomy.APPLIED_KLASSES);
    if (appliedKlasses == null) {
      appliedKlasses = new ArrayList<>();
    }
    Set<String> klassIdsSet = new HashSet<>();
    for (String klassId : appliedKlasses) {
      Vertex klassVertex = UtilClass.getVertexById(klassId, vertexLabel);
      klassIdsSet.addAll(TaxonomyUtil.getOwnAndAllChildrenKlassIdsSet(klassVertex));
    }
    appliedKlasses = new ArrayList<>();
    appliedKlasses.addAll(klassIdsSet);
    
    // add section details to taxonomy map...
    if (!id.equals("-1")) {
      KlassUtils.addSectionsToKlassEntityMap(klassTaxonomy, mapToReturn);
      mapToReturn.remove(IKlass.PERMISSIONS);
    }
    // List<Map<String, Object>> referencedKlasses =
    // TaxonomyUtil.getParentReferencedTaxonomyKlasses(id, vertexLabel);
    // mapToReturn.put(IGetArticleTaxonomyModel.REFERENCED_KLASSES,
    // referencedKlasses);
    // TaxonomyUtil.fillReferencedAttributeContextDetails(mapToReturn);
  }
  
  public static void fillReferencedAttributeContextDetails(Map<String, Object> taxonomyMap,
      Map<String, Object> configDetails) throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.LABEL,
        IConfigEntityInformationModel.TYPE);
    Map<String, Object> referencedContextDetails = new HashMap<>();
    configDetails.put(IGetAttributionTaxonomyModel.REFERENCED_CONTEXTS, referencedContextDetails);
    List<Map<String, Object>> klassSections = (List<Map<String, Object>>) taxonomyMap
        .get(IGetAttributionTaxonomyModel.SECTIONS);
    if (klassSections == null) {
      return;
    }
    for (Map<String, Object> section : klassSections) {
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
            || referencedContextDetails.containsKey(attributeVariantContextId)) {
          continue;
        }
        Vertex attributeVariantContextNode = UtilClass.getVertexById(attributeVariantContextId,
            VertexLabelConstants.VARIANT_CONTEXT);
        referencedContextDetails.put(attributeVariantContextId,
            UtilClass.getMapFromVertex(fieldsToFetch, attributeVariantContextNode));
      }
    }
  }
  
  public static List<Vertex> getAllRootLevelTaxonomyNodes()
  {
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
            + " where outE('Child_Of').size() = 0 order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    Iterator<Vertex> parentTaxonomyIterator = vertices.iterator();
    List<Vertex> parentTaxonomyList = StreamSupport
        .stream(Spliterators.spliteratorUnknownSize(parentTaxonomyIterator, Spliterator.ORDERED),
            false)
        .collect(Collectors.<Vertex> toList());
    return parentTaxonomyList;
  }
  
  public static List<Vertex> getAllRootLevelMajorTaxonomyNodes(String idsToExclude)
  {
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
            + " where outE('Child_Of').size() = 0 and code not in " + idsToExclude + " and "
            + ITaxonomy.TAXONOMY_TYPE + " = " + EntityUtil.quoteIt(CommonConstants.MAJOR_TAXONOMY)
            + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
            + " asc"))
        .execute();
    Iterator<Vertex> parentTaxonomyIterator = vertices.iterator();
    List<Vertex> parentTaxonomyList = StreamSupport
        .stream(Spliterators.spliteratorUnknownSize(parentTaxonomyIterator, Spliterator.ORDERED),
            false)
        .collect(Collectors.<Vertex> toList());
    return parentTaxonomyList;
  }
  
  /**
   * @param parentTaxonomyId
   * @param leafTaxonomyIds
   * @return returns a tree structure (with id & label of each node) with parent
   *         taxonomy at root and all intermediate taxonomies till given leaf
   *         taxonomy node ids
   * @throws Exception
   * @author Kshitij
   */
  public static Map<String, Object> getTaxonomyTreeByRootTaxonomyIdAndLeafIds(
      String parentTaxonomyId, Collection<String> leafTaxonomyIds) throws Exception
  {
    Map<String, Object> masterTaxonomyNodes = new HashMap<>();
    for (String leafTaxonomyId : leafTaxonomyIds) {
      Vertex vertex;
      try {
        vertex = UtilClass.getVertexById(leafTaxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      catch (NotFoundException e) {
        continue;
      }
      generateTaxonomyTree(vertex, masterTaxonomyNodes);
    }
    
    Map<String, Object> parentTaxonomyMap = null;
    
    if (parentTaxonomyId.equals(CommonConstants.TAXONOMY_HIERARCHY_ROOT)) {
      List<String> rootTaxonomyIds = new ArrayList<>();
      Iterable<Vertex> rootTaxonomyVertices = UtilClass.getGraph()
          .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      for (Vertex rootTaxonomyVertex : rootTaxonomyVertices) {
        String rootTaxonomyId = UtilClass.getCodeNew(rootTaxonomyVertex);
        rootTaxonomyIds.add(rootTaxonomyId);
      }
      
      if (!rootTaxonomyIds.isEmpty()) {
        parentTaxonomyMap = new HashMap<>();
        parentTaxonomyMap.put(ITaxonomy.ID, CommonConstants.TAXONOMY_HIERARCHY_ROOT);
        List<Object> children = new ArrayList<>();
        for (String rootTaxonomyId : rootTaxonomyIds) {
          Map<String, Object> child = (Map<String, Object>) masterTaxonomyNodes.get(rootTaxonomyId);
          if (child == null) {
            continue;
          }
          children.add(child);
        }
        parentTaxonomyMap.put(ITaxonomy.CHILDREN, children);
      }
      
    }
    else {
      parentTaxonomyMap = (Map<String, Object>) masterTaxonomyNodes.get(parentTaxonomyId);
    }
    
    if (parentTaxonomyMap == null) {
      Vertex parentTaxonomyVertex;
      try {
        parentTaxonomyVertex = UtilClass.getVertexById(parentTaxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      }
      catch (NotFoundException e) {
        throw new ParentArticleTaxonomyNotFoundException();
      }
      parentTaxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL), parentTaxonomyVertex);
      parentTaxonomyMap.put(ITaxonomy.CHILDREN, new ArrayList<>());
    }
    return parentTaxonomyMap;
  }
  
  private static void generateTaxonomyTree(Vertex taxonomyVertex,
      Map<String, Object> masterTaxonomyNodes) throws MultipleLinkFoundException
  {
    String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
    if (masterTaxonomyNodes.containsKey(taxonomyId)) {
      return;
    }
    Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL), taxonomyVertex);
    taxonomyMap.put(ITaxonomy.CHILDREN, new ArrayList<>());
    masterTaxonomyNodes.put(taxonomyId, taxonomyMap);
    
    Vertex parentTaxonomyVertex = AttributionTaxonomyUtil.getParentTaxonomy(taxonomyVertex);
    if (parentTaxonomyVertex == null) {
      return;
    }
    String parentTaxonomyId = UtilClass.getCodeNew(parentTaxonomyVertex);
    Map<String, Object> parentTaxonomyMap = (Map<String, Object>) masterTaxonomyNodes
        .get(parentTaxonomyId);
    if (parentTaxonomyMap == null) {
      generateTaxonomyTree(parentTaxonomyVertex, masterTaxonomyNodes);
    }
    parentTaxonomyMap = (Map<String, Object>) masterTaxonomyNodes.get(parentTaxonomyId);
    ((List<Map<String, Object>>) parentTaxonomyMap.get(ITaxonomy.CHILDREN)).add(taxonomyMap);
  }
  
  /**
   * @param klassId
   * @return a tree structure of entire klass taxonomy acc. to given klass id
   *         with -1 at root
   * @throws Exception
   * @author Kshitij
   */
  public static Map<String, Object> getKlassTaxonomyByKlassId(String klassId) throws Exception
  {
    Map<String, Object> klassMap = new HashMap<>();
    Vertex klassNode = null;
    try {
      klassNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    
    List<String> fieldsToFetchForKlass = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IConfigEntityTreeInformationModel.LABEL, IConfigEntityTreeInformationModel.TYPE);
    klassMap = UtilClass.getMapFromVertex(fieldsToFetchForKlass, klassNode);
    fillKlassesTree(klassNode, klassMap, fieldsToFetchForKlass);
    
    List<Map<String, Object>> children = new ArrayList<>();
    children.add(klassMap);
    
    Map<String, Object> klassTaxonomyMap = new HashMap<>();
    klassTaxonomyMap.put(CommonConstants.ID_PROPERTY, "-1");
    klassTaxonomyMap.put(ICategoryInformationModel.CHILDREN, children);
    
    return klassTaxonomyMap;
  }
  
  private static void fillKlassesTree(Vertex klassNode, Map<String, Object> klassMap,
      List<String> fieldsToFetchForKlass)
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    Iterable<Vertex> ChildNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : ChildNodes) {
      Map<String, Object> childMap = UtilClass.getMapFromVertex(fieldsToFetchForKlass, childNode);
      fillKlassesTree(childNode, childMap, fieldsToFetchForKlass);
      klassesList.add(childMap);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, klassesList);
  }
  
  /**
   * Thsi function returns all AllChildrenTaxonomyIds if selectionType matches
   * its taxonomyType. else return null;
   *
   * @author Lokesh
   * @param taxonomyVertex
   * @return
   * @throws Exception
   */
  public static List<String> getAllChildTaxonomyIds(Vertex taxonomyVertex) throws Exception
  {
    List<String> childrenTaxonomies = new ArrayList<>();
    String query = "select from(traverse in('Child_Of') from " + taxonomyVertex.getId()
        + " strategy BREADTH_FIRST) where " + ITag.TYPE + " is null";
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : resultIterable) {
      childrenTaxonomies.add(UtilClass.getCodeNew(vertex));
    }
    childrenTaxonomies.remove(UtilClass.getCodeNew(taxonomyVertex));
    
    return childrenTaxonomies;
  }
  
  public static Vertex getRootParentVertexAndFillAllParentTaxonomyIds(String taxonomyId,
      List<String> parentTaxonomies) throws Exception
  {
    Vertex taxonomyVertex = UtilClass.getVertexById(taxonomyId,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    String query = "select from(traverse out('Child_Of') from " + taxonomyVertex.getId()
        + " strategy BREADTH_FIRST) where " + ITag.TYPE + " is null";
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Vertex rootTaxonomy = null;
    for (Vertex vertex : resultIterable) {
      parentTaxonomies.add(UtilClass.getCodeNew(vertex));
      if (vertex.getProperty(ITaxonomy.TAXONOMY_TYPE) != null) {
        rootTaxonomy = vertex;
      }
    }
    parentTaxonomies.remove(taxonomyId);
    return rootTaxonomy;
  }
  
  public static void fillReferencedTaskDetails(Map<String, Object> getKlassMap,
      Map<String, Object> configDetails) throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IConfigEntityInformationModel.LABEL, IConfigEntityInformationModel.TYPE);
    
    List<String> taskIds = (List<String>) getKlassMap.get(IKlass.TASKS);
    Map<String, Object> referencedTaskDetails = new HashMap<>();
    for (String taskId : taskIds) {
      Vertex taskNode = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
      referencedTaskDetails.put(taskId, UtilClass.getMapFromVertex(fieldsToFetch, taskNode));
    }
    configDetails.put(IGetKlassWithGlobalPermissionModel.REFERENCED_TASKS, referencedTaskDetails);
  }
  
  public static void fillReferencedDataRuleDetails(Map<String, Object> getKlassMap,
      Map<String, Object> configDetails) throws Exception
  {
    List<String> dataRuleIds = (List<String>) getKlassMap.get(IKlass.DATA_RULES);
    Map<String, String> referencedDataRuleDetails = new HashMap<>();
    for (String dataRuleId : dataRuleIds) {
      Vertex eventNode = UtilClass.getVertexById(dataRuleId, VertexLabelConstants.DATA_RULE);
      referencedDataRuleDetails.put(dataRuleId,
          (String) UtilClass.getValueByLanguage(eventNode, CommonConstants.LABEL_PROPERTY));
    }
    configDetails.put(IGetKlassWithGlobalPermissionModel.REFERENCED_DATARULES,
        referencedDataRuleDetails);
  }
  
  public static String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        IReferencedArticleTaxonomyModel.CODE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.ID, UtilClass.getCodeNew(taxonomyVertex));
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CODE,
        (String) taxonomyVertex.getProperty(ITaxonomy.CODE));
    taxonomyMap.put(IReferencedArticleTaxonomyModel.LABEL,
        UtilClass.getValueByLanguage(taxonomyVertex, CommonConstants.LABEL_PROPERTY));
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CLASSIFIER_IID, taxonomyVertex.getProperty(ITaxonomy.CLASSIFIER_IID));
    UtilClass.fetchIconInfo(taxonomyVertex, taxonomyMap);
    return MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap,
        taxonomyVertex);
  }
  
  public static void fillParentIdAndConfigDetails(Map<String, Object> propertyMap,
      Map<String, Object> configDetails, Vertex leafTaxonomyNode,
      Map<String, Integer> isPermissionFromRoleOrOrganization,
      List<String> listOfTopPermissionNodes) throws Exception
  {
    Vertex parentVertex = null;
    Iterator<Vertex> parentVertices = leafTaxonomyNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    
    while (parentVertices.hasNext()) {
      Vertex nextParentVertex = parentVertices.next();
      if (nextParentVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
          .equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)
          || nextParentVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
              .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
        parentVertex = nextParentVertex;
      }
    }
    if (parentVertex == null) {
      propertyMap.put(CommonConstants.PARENT_ID_PROPERTY, -1);
    } else {
    	propertyMap.put(CommonConstants.PARENT_ID_PROPERTY, parentVertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Vertex topPermissionNode = leafTaxonomyNode;
    List<String> idsToDelete = new ArrayList<String>();
    
    topPermissionNode = fillConfigDetailsRecursive(configDetails, topPermissionNode, idsToDelete,
        leafTaxonomyNode, isPermissionFromRoleOrOrganization, listOfTopPermissionNodes);
    
    if (isPermissionFromRoleOrOrganization.get(CommonConstants.PERMISSION_PROPERTY) != 0) {
      if (propertyMap.get(CommonConstants.ID_PROPERTY)
          .equals(UtilClass.getCodeNew(topPermissionNode))) {
        propertyMap.put(CommonConstants.PARENT_ID_PROPERTY, -1);
      }
      
      Map<String, Object> topPermissionTaxonomyMap = (Map<String, Object>) configDetails
          .get(UtilClass.getCodeNew(topPermissionNode));
      topPermissionTaxonomyMap.put(CommonConstants.PARENT_ID_PROPERTY, -1);
      
      configDetails.keySet()
          .removeAll(idsToDelete);
    }
  }
  
  public static Vertex fillConfigDetailsRecursive(Map<String, Object> configDetails,
      Vertex topPermissionNode, List<String> idsToDelete, Vertex taxonmyNode,
      Map<String, Integer> isPermissionFromRoleOrOrganization,
      List<String> listOfTopPermissionNodes) throws Exception
  {
    if (taxonmyNode == null) {
      return topPermissionNode;
    }
    
    Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, IKlass.LABEL, IKlass.ICON), taxonmyNode);
    
    Vertex parentVertex = null;
    Iterator<Vertex> parentVertices = taxonmyNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
        .iterator();
    
    while (parentVertices.hasNext()) {
      Vertex nextParentVertex = parentVertices.next();
      if (nextParentVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
          .equals(VertexLabelConstants.ROOT_KLASS_TAXONOMY)
          || nextParentVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY)
              .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
        parentVertex = nextParentVertex;
        taxonomyMap.put(CommonConstants.PARENT_ID_PROPERTY,
            parentVertex.getProperty(CommonConstants.CODE_PROPERTY));
      }
    }
    
    if (parentVertex == null) {
      taxonomyMap.put(CommonConstants.PARENT_ID_PROPERTY, -1);
    }
    
    String taxonomyId = taxonmyNode.getProperty(CommonConstants.CODE_PROPERTY);
    configDetails.put(taxonomyId, taxonomyMap);
    
    if (isPermissionFromRoleOrOrganization.get(CommonConstants.PERMISSION_PROPERTY) != 0) {
      idsToDelete.add(taxonomyId);
      
      if (listOfTopPermissionNodes.contains(taxonomyId)) {
        topPermissionNode = taxonmyNode;
        idsToDelete.clear();
      }
    }
    
    return fillConfigDetailsRecursive(configDetails, topPermissionNode, idsToDelete, parentVertex,
        isPermissionFromRoleOrOrganization, listOfTopPermissionNodes);
  }
  
  public static List<Vertex> getAllRootLevelMinorTaxonomyNodes()
  {
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
            + " where outE('Child_Of').size() = 0 and  " + ITaxonomy.TAXONOMY_TYPE + " = " + EntityUtil.quoteIt(CommonConstants.MINOR_TAXONOMY) + 
            " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    Iterator<Vertex> parentTaxonomyIterator = vertices.iterator();
    List<Vertex> parentTaxonomyList = StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(parentTaxonomyIterator,
                Spliterator.ORDERED), false).collect(
        Collectors.<Vertex> toList());
    return parentTaxonomyList;
  }
  
  public static void fillReferencedTaxonomies(Map<String, Object> taxonomyMap, Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> referencedTaxonomies = new HashMap<String, Object>();
    Map<String, Integer> isPermissionFromRoleOrOrganization = new HashMap<String, Integer>();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
    
    List<Map<String, Object>> listOfTaxonomyMap = new ArrayList<Map<String,Object>>();
    listOfTaxonomyMap.add(taxonomyMap);
    listOfTaxonomyMap.addAll((List<Map<String, Object>>) taxonomyMap.get(IGetAttributionTaxonomyModel.CHILDREN));
    
    for (Map<String, Object> childTaxonomyMap : listOfTaxonomyMap) {
      Vertex childVertex = UtilClass.getVertexById((String) childTaxonomyMap.get(ITaxonomy.ID), VertexLabelConstants.ATTRIBUTION_TAXONOMY);
      TaxonomyUtil.fillParentIdAndConfigDetails(new HashMap<String, Object>(), referencedTaxonomies,
          childVertex, isPermissionFromRoleOrOrganization, new ArrayList<String>());
    }
    configDetails.put(IGetMasterTaxonomyConfigDetailsModel.REFERENCED_TAXONOMIES, referencedTaxonomies); 
  }
  
  public static boolean isImmediateChildPresent(Vertex taxonomyVertex)
  {
    Iterator<Vertex> vertices = getImmediateChildVertices(taxonomyVertex).iterator();
    return vertices.hasNext();
  }
  
  private static Iterable<Vertex> getImmediateChildVertices(Vertex klassTaxonomy)
  {
    String rid = klassTaxonomy.getId().toString();
    String query = "select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')) from " + rid + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> childrenVertices = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    return childrenVertices;
  }

  public static void manageAddedLevel(Vertex taxonomyNode, Map<String, Object> addedLevel, String taxonomyLevelType, String tagType)
      throws Exception
  {
    if (addedLevel == null || addedLevel.isEmpty()) {
      return;
    }
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(taxonomyLevelType,
        CommonConstants.CODE_PROPERTY);
    String addedLevelId = (String) addedLevel.get(IAddedTaxonomyLevelModel.ID);
    if (addedLevelId == null || addedLevelId.equals("")) {
      addedLevelId = UtilClass.getUniqueSequenceId(vertexType);
    }

    Map<String, Object> levelMap = new HashMap<String, Object>();
    levelMap.put(CommonConstants.CODE_PROPERTY, addedLevelId);
    Vertex levelNode = UtilClass.createNode(levelMap, vertexType, new ArrayList<String>());

    Map<String, Object> addedTag = (Map<String, Object>) addedLevel
        .get(IAddedTaxonomyLevelModel.ADDED_TAG);

    String tagId = (String) addedTag.get(IAddedTagModel.ID);
    Boolean isNewlyCreated = (Boolean) addedTag.get(IAddedTagModel.IS_NEWLY_CREATED);
    Vertex tagGroupNode;
    if ((isNewlyCreated == null || !isNewlyCreated) && tagId != null) {
      tagGroupNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
    }
    else {
      String label = (String) addedTag.get(IAddedTagModel.LABEL);
      String code = (String) addedTag.get(IAddedTagModel.CODE);
      Object propertyIID = addedTag.get(IAddedTagModel.PROPERTY_IID);
      tagGroupNode = AttributionTaxonomyUtil.createTagGroupNode(tagId, label, code, tagType, propertyIID);
    }
    tagGroupNode.addEdge(RelationshipLabelConstants.LEVEL_TAGGROUP_OF, levelNode);

    taxonomyNode.addEdge(RelationshipLabelConstants.HAS_TAXONOMY_LEVEL, levelNode);
    List<String> tagLevelSequence = taxonomyNode.getProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE);
    tagLevelSequence.add(addedLevelId);
    taxonomyNode.setProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE, tagLevelSequence);
  }

  public static void manageDeletedLevelNode(Vertex taxonomyNode, String deletedLevel, String taxonomyLevelType, String vertexType) throws Exception
  {
    List<String> tagLevelSequence = taxonomyNode.getProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE);
    if (deletedLevel == null || deletedLevel.equals("")
        || !tagLevelSequence.contains(deletedLevel)) {
      return;
    }

    List<String> levelNodeIdsToDelete = new ArrayList<String>();
    Integer indexOfDeletedLevel = tagLevelSequence.indexOf(deletedLevel);

    // add all the taxonomies which are link to the deleted level to
    // attributionTaxonomiesToDelete
    Vertex deletedlevelNode = UtilClass.getVertexById(deletedLevel, taxonomyLevelType);
    Iterable<Vertex> tagGroups = deletedlevelNode.getVertices(Direction.IN,
        RelationshipLabelConstants.LEVEL_TAGGROUP_OF);
    Set<String> attributionTaxonomiesToDelete = new HashSet<>();
    for (Vertex tagGroup : tagGroups) {
      Iterable<Vertex> tagValues = tagGroup.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      for (Vertex tagValue : tagValues) {
        // check if tag value is linked to a taxonomy or not(tag value can have
        // two parent taxonomy,
        // tag group)
        Vertex parentTaxonomy = AttributionTaxonomyUtil.getParentTaxonomy(tagValue);
        if (parentTaxonomy != null) {
          attributionTaxonomiesToDelete.add(UtilClass.getCodeNew(tagValue));
        }
      }
    }

    // remove taxonomy level taxonomies
    for (String attributionTaxonomyToDelete : attributionTaxonomiesToDelete) {
      DeleteTaxonomyUtil.deleteKlassTaxonomies(attributionTaxonomyToDelete, vertexType, new ArrayList<>());
    }

    // remove level node
    for (int i = indexOfDeletedLevel; i < tagLevelSequence.size(); i++) {
      String levelId = tagLevelSequence.get(i);
      levelNodeIdsToDelete.add(levelId);
      Vertex levelNode = UtilClass.getVertexById(levelId, taxonomyLevelType);
      levelNode.remove();
    }
    tagLevelSequence.removeAll(levelNodeIdsToDelete);
    taxonomyNode.setProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE, tagLevelSequence);
  }

  public static void manageParent(Map<String, Object> taxonomyMap, Vertex taxonomyNode, String linkedTagCode, String parentCode,
      String taxonomyVertexLabel) throws Exception
  {
    Map<String, Object> parentInfo = new HashMap<>();
    Map<String,Edge> parentEdges = getParentEdges(taxonomyNode);

    if (linkedTagCode != null && !linkedTagCode.equals("") && !linkedTagCode.equals("-1")) {
      Vertex parentTagNode = UtilClass.getVertexById(linkedTagCode, VertexLabelConstants.ENTITY_TAG);
      String code = parentTagNode.getProperty(CommonConstants.CODE_PROPERTY);
      if (VertexLabelConstants.ATTRIBUTION_TAXONOMY.equals(taxonomyVertexLabel)) {
        taxonomyNode.setProperty(IMasterTaxonomy.IS_TAG, true);
        if (!parentEdges.containsKey(code)) {
          taxonomyNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentTagNode);
        }
      }
      List<String> sequenceList = parentTagNode.getProperty(ITag.TAG_VALUES_SEQUENCE);
      String taxonomyCode = (String) taxonomyMap.get(CommonConstants.CODE_PROPERTY);
      if (sequenceList == null) {
        sequenceList = new ArrayList<String>();
      }
      sequenceList.add(taxonomyCode);
      parentTagNode.setProperty(ITag.TAG_VALUES_SEQUENCE, sequenceList);
    }

    Vertex parent = null;
    if (!parentCode.isEmpty() && !parentCode.equals("-1")) {
      parentInfo.put(CommonConstants.ID_PROPERTY, parentCode);
      taxonomyMap.put(CommonConstants.PARENT_PROPERTY, parentInfo);
      parent = UtilClass.getVertexByCode(parentCode, taxonomyVertexLabel);
      String childOf = RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF;

      if(!parentEdges.containsKey(parentCode)){
        taxonomyNode.addEdge(childOf, parent);
        taxonomyNode.setProperty(ITaxonomy.TAXONOMY_TYPE, parent.getProperty(ITaxonomy.TAXONOMY_TYPE));
        Integer childCount = parent.getProperty(IMasterTaxonomy.CHILD_COUNT);
        if (childCount == null)
          childCount = 0;
        parent.setProperty(IMasterTaxonomy.CHILD_COUNT, ++childCount);
        KlassUtils.inheritParentKlassData(taxonomyNode, parent);
      }
      parentEdges.remove(linkedTagCode);
      parentEdges.remove(parentCode);
      parentEdges.forEach((code, edge) -> {
        edge.remove();
      });
    }
    UtilClass.getGraph().commit();
  }

  public static Map<String, Edge> getParentEdges(Vertex entityNode)
  {
    Map<String, Edge> parentEdges = new HashMap<>();
    Iterable<Edge> edges = entityNode.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for(Edge edge : edges){
      Vertex tag = edge.getVertex(Direction.IN);
      String code = tag.getProperty(CommonConstants.CODE_PROPERTY);
      parentEdges.put(code, edge);
    }
    return parentEdges;
  }
}
