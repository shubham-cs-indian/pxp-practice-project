package com.cs.runtime.strategy.plugin.usecase.klassinstance.clone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.IGetCloneWizardForRequestStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetKlassInstancePropertiesForClone extends AbstractOrientPlugin {
  
  public GetKlassInstancePropertiesForClone(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKlassInstancePropertiesForClone/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIdsToClone = (List<String>) requestMap
        .get(IGetCloneWizardForRequestStrategyModel.TYPEIDS_TO_CLONE);
    List<String> taxonomyIdsToClone = (List<String>) requestMap
        .get(IGetCloneWizardForRequestStrategyModel.TAXONOMY_IDS_TO_CLONE);
    List<String> klassIds = (List<String>) requestMap
        .get(IGetCloneWizardForRequestStrategyModel.TYPE_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IGetCloneWizardForRequestStrategyModel.TAXONOMY_IDS);
    Boolean isForLinkedVariant = (Boolean) requestMap
        .get(IGetCloneWizardForRequestStrategyModel.IS_FOR_LINKED_VARIANT);
    if (isForLinkedVariant) {
      String parentNatureKlassId = (String) requestMap
          .get(IGetCloneWizardForRequestStrategyModel.PARENT_NATURE_KLASS_ID);
      klassIds.remove(parentNatureKlassId);
    }
    
    Map<String, Object> mapToReturn = getMapToReturn();
    Map<String, Object> sortedKlasses = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.KLASSES);
    Map<String, Object> klasses = new HashMap<>();
    
    Set<Vertex> klassAndTaxonomyVertices = new HashSet<>();
    Set<String> propertiesToExclude = new HashSet<>();
    for (String klassId : klassIdsToClone) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      fillKlassDetails(klasses, klassNode);
      fillKlassProperties(mapToReturn, klassNode, propertiesToExclude);
      klassAndTaxonomyVertices.add(klassNode);
    }
    
    Map<String, Object> taxonomies = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.TAXONOMIES);
    for (String taxonomyId : taxonomyIdsToClone) {
      Vertex taxonomyNode = UtilClass.getVertexByIndexedId(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      fillKlassProperties(mapToReturn, taxonomyNode, propertiesToExclude);
      fillTaxonomies(taxonomies, taxonomyNode, taxonomyId);
      klassAndTaxonomyVertices.add(taxonomyNode);
    }
    
    fillPropertyCollection(mapToReturn, klassAndTaxonomyVertices, propertiesToExclude);
    
    for (String klassId : klassIds) {
      if (!klassIdsToClone.contains(klassId)) {
        Vertex klassNode = UtilClass.getVertexById(klassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        fillKlassDetails(klasses, klassNode);
      }
    }
    
    for (String taxonomyId : taxonomyIds) {
      if (!taxonomyIdsToClone.contains(taxonomyId)) {
        Vertex taxonomyNode = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        fillTaxonomies(taxonomies, taxonomyNode, taxonomyId);
      }
    }
    
    manageKlassDetails(klasses, sortedKlasses);
    return mapToReturn;
  }
  
  private void manageKlassDetails(Map<String, Object> klasses, Map<String, Object> sortedKlasses)
  {
    Map<String, Object> tempKlasses = new HashMap<>(klasses);
    for (String entity : klasses.keySet()) {
      Map<String, Object> entityMap = (Map<String, Object>) klasses.get(entity);
      if ((boolean) entityMap.get(IKlass.IS_NATURE)) {
        sortedKlasses.put(entity, entityMap);
        tempKlasses.remove(entity);
      }
    }
    sortedKlasses.putAll(tempKlasses);
  }
  
  private void fillKlassDetails(Map<String, Object> klasses, Vertex klassNode)
  {
    Map<String, Object> entityMap = new HashMap<String, Object>();
    String entityId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    entityMap.put(IReferencedKlassDetailStrategyModel.ID, entityId);
    entityMap.put(IReferencedKlassDetailStrategyModel.LABEL,
        UtilClass.getValueByLanguage(klassNode, IKlass.LABEL));
    entityMap.put(IReferencedKlassDetailStrategyModel.CODE, klassNode.getProperty(IKlass.CODE));
    entityMap.put(IReferencedKlassDetailStrategyModel.TYPE, klassNode.getProperty(IKlass.TYPE));
    entityMap.put(IReferencedKlassDetailStrategyModel.IS_NATURE,
        klassNode.getProperty(IKlass.IS_NATURE));
    UtilClass.fetchIconInfo(klassNode, entityMap);
    klasses.put(entityId, entityMap);
  }
  
  private void fillTaxonomies(Map<String, Object> taxonomies, Vertex taxonomyNode,
      String taxonomyId) throws Exception
  {
    Map<String, Object> propertyCollection = new HashMap<>();
    Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
            IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
            IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.BASETYPE),
        taxonomyNode);
    fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyNode);
    // fillPropertyCollection(propertyCollection, taxonomyNode);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS,
        new ArrayList<>(propertyCollection.keySet()));
    taxonomies.put(taxonomyId, taxonomyMap);
  }
  
  protected String fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.CODE, ITaxonomy.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
        ITaxonomy.BASE_TYPE);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where code <> '" + UtilClass.getCodeNew(taxonomyVertex) + "'";
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    return MultiClassificationUtils.fillParentsData(fieldsToFetch, taxonomiesParentMap,
        taxonomyVertex);
  }
  
  /**
   * fill properies of klass i.e attributes, tags, roles and relationships(one
   * to many & many to many)
   *
   * @author Lokesh
   * @param maptoReturn
   * @param klassNode
   * @throws NotFoundException
   */
  private void fillKlassProperties(Map<String, Object> maptoReturn, Vertex klassNode,
      Set<String> propertiesToExclude) throws Exception
  {
    Iterable<Vertex> kPIterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassPropertyNode : kPIterable) {
      Iterator<Vertex> iterable = klassPropertyNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      if (!iterable.hasNext()) {
        throw new NotFoundException();
      }
      Vertex entityNode = iterable.next();
      fillEntitiesToRespectivePropertyList(maptoReturn, klassPropertyNode, entityNode,
          propertiesToExclude);
    }
  }
  
  private void fillPropertyCollection(Map<String, Object> mapToReturn,
      Set<Vertex> klassAndTaxonomyNodes, Set<String> propertiesToExclude) throws NotFoundException
  {
    Map<String, Object> propertyCollections = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.PROPERTY_COLLECTIONS);
    Map<String, Object> attributes = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.ATTRIBUTES);
    Map<String, Object> tags = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.TAGS);
    
    attributes.keySet()
        .removeAll(propertiesToExclude);
    tags.keySet()
        .removeAll(propertiesToExclude);
    
    for (Vertex klassNode : klassAndTaxonomyNodes) {
      Iterable<Vertex> sectionVertices = klassNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
      for (Vertex sectionVertex : sectionVertices) {
        Iterator<Vertex> iterator = sectionVertex
            .getVertices(Direction.IN, RelationshipLabelConstants.PROPERTY_COLLECTION_OF)
            .iterator();
        if (!iterator.hasNext()) {
          throw new NotFoundException();
        }
        Vertex entityNode = iterator.next();
        fillPropertyCollectionList(propertyCollections, sectionVertices, entityNode, attributes,
            tags);
      }
    }
  }
  
  private void fillPropertyCollectionList(Map<String, Object> propertyCollections,
      Iterable<Vertex> sectionVertices, Vertex entityNode, Map<String, Object> attributes,
      Map<String, Object> tags)
  {
    String propertyCollectionId = UtilClass.getCodeNew(entityNode);
    if (!propertyCollections.containsKey(propertyCollectionId)) {
      Map<String, Object> referencedPropertyCollection = UtilClass
          .getMapFromVertex(new ArrayList<>(), entityNode);
      propertyCollections.put(propertyCollectionId, referencedPropertyCollection);
      List<Map<String, Object>> elementsList = new ArrayList<Map<String, Object>>();
      Set<String> entityIds = new HashSet<>();
      Iterable<Edge> entityToRelationships = entityNode.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
      for (Edge entityTo : entityToRelationships) {
        Vertex entityVertex = entityTo.getVertex(Direction.OUT);
        String vertexType = entityVertex.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
        String entityId = UtilClass.getCodeNew(entityVertex);
        if (!vertexType.equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)
            && !attributes.containsKey(entityId) && !tags.containsKey(entityId)) {
          continue;
        }
        Map<String, Object> propertyCollectionElement = new HashMap<String, Object>();
        propertyCollectionElement.put(IReferencedPropertyCollectionElementModel.ID, entityId);
        elementsList.add(propertyCollectionElement);
        entityIds.add(entityId);
      }
      
      List<String> propertySequenceIds = entityNode.getProperty(CommonConstants.PROPERTY_SEQUENCE_IDS);
      propertySequenceIds.retainAll(entityIds);
      PropertyCollectionUtils.sortPropertyCollectionList(elementsList, propertySequenceIds, CommonConstants.ID_PROPERTY);
      
      referencedPropertyCollection.put(IReferencedPropertyCollectionModel.ELEMENTS, elementsList);
    }
  }
  
  /**
   * fill properies of klass in proper map by type
   *
   * @author Lokesh
   * @param mapToReturn
   * @param klassNode
   * @throws NotFoundException
   */
  private void fillEntitiesToRespectivePropertyList(Map<String, Object> mapToReturn,
      Vertex klassPropertyNode, Vertex entityNode, Set<String> propertiesToExclude) throws Exception
  {
    String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
    Map<String, Object> entityMap = new HashMap<String, Object>();
    String entityId = entityNode.getProperty(CommonConstants.CODE_PROPERTY);
    entityMap.put(IConfigEntityInformationModel.ID, entityId);
    entityMap.put(IConfigEntityInformationModel.LABEL,
        UtilClass.getValueByLanguage(entityNode, CommonConstants.LABEL_PROPERTY));
    entityMap.put(IConfigEntityInformationModel.CODE,
        entityNode.getProperty(CommonConstants.CODE_PROPERTY));
    entityMap.put(IConfigEntityInformationModel.ICON,
        entityNode.getProperty(CommonConstants.ICON_PROPERTY));
    UtilClass.fetchIconInfo(entityNode, entityMap);
    
    Map<String, Object> attributes = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.ATTRIBUTES);
    Map<String, Object> tags = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.TAGS);
    Map<String, Object> relationships = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.RELATIONSHIPS);
    Map<String, Object> taxonomies = (Map<String, Object>) mapToReturn
        .get(IGetKlassInstancePropertiesForCloneModel.TAXONOMIES);
    
    Boolean isSkipped = (Boolean) klassPropertyNode.getProperty(ISectionAttribute.IS_SKIPPED);
    switch (entityType) {
      case CommonConstants.ATTRIBUTE:
        entityMap.put(IConfigEntityInformationModel.TYPE, CommonConstants.ATTRIBUTE);
        Iterator<Vertex> attributeContextsIterator = klassPropertyNode
            .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
            .iterator();
        Boolean isIdentifier = klassPropertyNode.getProperty(ISectionAttribute.IS_IDENTIFIER);
        if (isIdentifier != null && isIdentifier) {
          propertiesToExclude.add(entityId);
          break;
        }
        if (isSkipped != null && isSkipped) {
          break;
        }
        if (attributeContextsIterator.hasNext()) {
          propertiesToExclude.add(entityId);
          break;
        }
        if (!attributes.containsKey(entityId)) {
          attributes.put(entityId, entityMap);
        }
        break;
      case CommonConstants.TAG:
        entityMap.put(IConfigEntityInformationModel.TYPE, CommonConstants.TAG);
        if (isSkipped != null && isSkipped) {
          break;
        }
        if (!tags.containsKey(entityId)) {
          tags.put(entityId, entityMap);
        }
        break;
      case CommonConstants.RELATIONSHIP:
        entityMap.put(IConfigEntityInformationModel.TYPE, CommonConstants.RELATIONSHIP);
        // check if source(otherside) cardinality is one
        Map<String, Object> relationshipSide = klassPropertyNode
            .getProperty(ISectionRelationship.RELATIONSHIP_SIDE);
        String sourceCardinality = (String) relationshipSide
            .get(IKlassRelationshipSide.SOURCE_CARDINALITY);
        String cardinality = (String) relationshipSide.get(IKlassRelationshipSide.CARDINALITY);
        String propertyId = klassPropertyNode.getProperty(ISectionRelationship.PROPERTY_ID);
        if (!propertyId.equals(SystemLevelIds.STANDARD_ARTICLE_GOLDEN_ARTICLE_RELATIONSHIP_ID)
            && !(sourceCardinality.equals(CommonConstants.CARDINALITY_ONE)
                && cardinality.equals(CommonConstants.CARDINALITY_ONE))
            && !entityNode.getProperty("@class")
                .equals(VertexLabelConstants.NATURE_RELATIONSHIP)
            && !relationships.containsKey(entityId)) {
          relationships.put(entityId, entityMap);
        }
        break;
      
      case CommonConstants.TAXONOMY:
        String taxonomyId = klassPropertyNode.getProperty(ISectionRelationship.PROPERTY_ID);
        Vertex taxonomyNode = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        fillTaxonomies(taxonomies, taxonomyNode, taxonomyId);
        break;
    }
  }
  
  private Map<String, Object> getMapToReturn()
  {
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    
    mapToReturn.put(IGetKlassInstancePropertiesForCloneModel.ATTRIBUTES, new HashMap<>());
    mapToReturn.put(IGetKlassInstancePropertiesForCloneModel.TAGS, new HashMap<>());
    mapToReturn.put(IGetKlassInstancePropertiesForCloneModel.RELATIONSHIPS, new HashMap<>());
    mapToReturn.put(IGetKlassInstancePropertiesForCloneModel.PROPERTY_COLLECTIONS, new HashMap<>());
    mapToReturn.put(IGetKlassInstancePropertiesForCloneModel.KLASSES, new LinkedHashMap<>());
    mapToReturn.put(IGetKlassInstancePropertiesForCloneModel.TAXONOMIES, new HashMap<>());
    
    return mapToReturn;
  }
}
