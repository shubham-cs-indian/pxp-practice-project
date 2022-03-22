package com.cs.config.strategy.plugin.usecase.configdetails.instancetree.abstrct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeIconModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IReferencedKlassOrTaxonomyModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForFilterAndSortData extends AbstractConfigDetailsForNewInstanceTree {

	public AbstractGetConfigDetailsForFilterAndSortData(final OServerCommandConfiguration iConfiguration) {
		super(iConfiguration);
	}

	public void execute(Map<String, Object> requestMap, Map<String, Object> response) throws Exception
	{
	  String userId = (String) requestMap.get(IConfigDetailsForFilterAndSortInfoRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    
    managePermission(roleNode, requestMap, response);
    
		fillFilterAndSortDataForSelectedModule(requestMap, response);

		fillReferencedProperties(requestMap, response);
	}

	protected void fillReferencedProperties(Map<String, Object> requestMap, Map<String, Object> responseMap) throws Exception
	{
		fillReferencedAttributes(requestMap, responseMap);
		fillReferencedTags(requestMap, responseMap);
		fillReferencedTaxonomies(requestMap, responseMap);
    fillReferencedKlasses(requestMap, responseMap);
	}

  private void fillReferencedAttributes(Map<String, Object> requestMap, Map<String, Object> responseMap) throws Exception
  {
    List<String> attributeIds = (List<String>) requestMap.get(IConfigDetailsForFilterAndSortInfoRequestModel.ATTRIBUTE_IDS);
		Map<String, Object> referencedAttributes = new HashMap<>();
		for (String attributeId : attributeIds) {
			Vertex attributeNode = null;
			try {
				attributeNode = UtilClass.getVertexByIndexedId(attributeId, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
			} catch (NotFoundException e) {
				throw new AttributeNotFoundException();
			}
			Map<String, Object> referencedAttribute = AttributeUtils.getAttributeMap(attributeNode);
			referencedAttributes.put(attributeId, referencedAttribute);
		}
		responseMap.put(IGetNewFilterAndSortDataResponseModel.REFERENCED_ATTRIBUTES, referencedAttributes);
  }

  private void fillReferencedTags(Map<String, Object> requestMap, Map<String, Object> responseMap) throws Exception
  {
    List<String> tagIds = (List<String>) requestMap.get(IConfigDetailsForFilterAndSortInfoRequestModel.TAG_IDS);
		Map<String, Object> referencedTags = new HashMap<>();
		for (String tagId : tagIds) {
			Vertex tagNode;
			try {
				tagNode = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
			} catch (NotFoundException e) {
				throw new TagNotFoundException();
			}
			Map<String, Object> referencedTag = TagUtils.getTagMap(tagNode, true);
			referencedTags.put(tagId, referencedTag);
		}
		responseMap.put(IGetNewFilterAndSortDataResponseModel.REFERENCED_TAGS, referencedTags);
  }

  private void fillReferencedTaxonomies(Map<String, Object> requestMap, Map<String, Object> responseMap) throws Exception
  {
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForFilterAndSortInfoRequestModel.TAXONOMY_IDS);
    /*String clickedTaxonomyId = (String) requestMap
        .get(IConfigDetailsForFilterAndSortInfoRequestModel.CLICKED_TAXONOMY_ID);*/
    /*if (clickedTaxonomyId != null && !clickedTaxonomyId.isEmpty()) {
      taxonomyIds.add(clickedTaxonomyId);
    }*/
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    /**
     * We don't have clicked taxonomy id (rootTaxonomyId) in case of bookmark so
     * need to find root taxonomy
     *//*
       if (!taxonomyIds.isEmpty()
        && (Boolean) requestMap.get(IConfigDetailsForFilterAndSortInfoRequestModel.IS_BOOKMARK)) {
       fillRootTaxonomy(referencedTaxonomies, taxonomyIds.get(0));
       }*/
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyNode = UtilClass.getVertexByIndexedId(taxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(IIdLabelCodeModel.ID, IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE,
              IIdLabelCodeIconModel.ICON), taxonomyNode);
      
      fillParentTaxonomyDetails(referencedTaxonomies, taxonomyMap, taxonomyNode);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
    }
    responseMap.put(IGetNewFilterAndSortDataResponseModel.REFERENCED_TAXONOMIES,
        referencedTaxonomies);
  }
  
  /*private void fillRootTaxonomy(Map<String, Object> referencedTaxonomies, String taxonomyId)
      throws NotFoundException
  {
    List<String> TaxonomyTypes = Arrays.asList(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    String query = "SELECT FROM (TRAVERSE OUT('Child_Of') FROM (select from "
        + VertexLabelConstants.ROOT_KLASS_TAXONOMY + " where code = "
        + EntityUtil.quoteIt(taxonomyId) + ")) WHERE @class in " + EntityUtil.quoteIt(TaxonomyTypes)
        + "  AND OUT('Child_Of').size() = 0";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex vertex = iterator.next();
    Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
        Arrays.asList(IIdLabelCodeModel.ID, IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE),
        vertex);
    taxonomyMap.put(IReferencedKlassOrTaxonomyModel.PARENT_ID, null);
    String nodeId = (String) taxonomyMap.get(IIdLabelCodeModel.ID);
    referencedTaxonomies.put(nodeId, taxonomyMap);
  }
  */
  private void fillReferencedKlasses(Map<String, Object> requestMap,
      Map<String, Object> responseMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IGetNewFilterAndSortDataRequestModel.SELECTED_TYPES);
    Map<String, Object> referencedKlasses = new HashMap<>();
    /*Boolean isBookmark = (Boolean) requestMap
        .get(IConfigDetailsForFilterAndSortInfoRequestModel.IS_BOOKMARK);*/
    /*if (isBookmark && !klassIds.isEmpty()) {
      Map<String, Object> klassMap = getRootClassNode();
      referencedKlasses.put("-1", klassMap);
      
      *//**
          * Iterate on class tree and fill only parent data if all children are
          * selected
          *//*
            List<Map<String, Object>> klassTaxonomyList = new ArrayList<>();
            List<String> moduleEntities = (List<String>) requestMap.get(IModuleEntitiesWithUserIdModel.ALLOWED_ENTITIES);
            List<String> parentKlassIds = EntityUtil.getStandardKlassIds(moduleEntities);
            Iterable<Vertex> vertices = UtilClass.getVerticesByIndexedIds(parentKlassIds, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
            for (Vertex klassNode : vertices) {
            fillKlasses(klassTaxonomyList, klassNode);
            }
            fillReferencedSelectedParentKlasses(referencedKlasses, klassTaxonomyList, klassIds, "-1");
            }
            else if (!klassIds.isEmpty()){
            Map<String, Object> klassMap = getRootClassNode();
            referencedKlasses.put("-1", klassMap);*/
    
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(Arrays.asList(IIdLabelCodeModel.ID,
          IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE, IIdLabelCodeIconModel.ICON), klassNode);
      
      Iterable<Vertex> parentVertices = klassNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      Iterator<Vertex> parentKlassesIterator = parentVertices.iterator();
      String parentId = parentKlassesIterator.hasNext() ? parentKlassesIterator.next()
          .getProperty(IIdLabelCodeModel.CODE) : "-1";
      klassMap.put(IReferencedKlassOrTaxonomyModel.PARENT_ID, parentId);
      if (parentKlassesIterator.hasNext()) {
        throw new MultipleVertexFoundException();
      }
      
      referencedKlasses.put(klassId, klassMap);
      // }
    }
    
    responseMap.put(IGetNewFilterAndSortDataResponseModel.REFERENCED_KLASSES, referencedKlasses);
    
  }

  /*private Map<String, Object> getRootClassNode()
  {
    Map<String, Object> klassMap = new HashMap<String, Object>();
    klassMap.put(IIdLabelCodeModel.ID, "-1");
    klassMap.put(IIdLabelCodeModel.LABEL, "Class Taxonomy");
    klassMap.put(IIdLabelCodeModel.CODE, "-1");
    klassMap.put(IReferencedKlassOrTaxonomyModel.PARENT_ID, null);
    return klassMap;
  }*/
  
  /* private void fillReferencedSelectedParentKlasses(Map<String, Object> referencedKlasses, List<Map<String, Object>> klassTaxonomyList,
      List<String> klassIds, String parentId) {
    
    for (Map<String, Object> klassTaxonomy : klassTaxonomyList) {
      String klassTaxonomyId = (String)klassTaxonomy.get(IIdLabelCodeModel.ID);
      if (klassIds.contains(klassTaxonomyId)) {
        klassTaxonomy.put(IReferencedKlassOrTaxonomyModel.PARENT_ID, parentId);
        referencedKlasses.put(klassTaxonomyId, klassTaxonomy);
      }
      else {
        List<Map<String, Object>> children = (List<Map<String, Object>>) klassTaxonomy.get(IConfigEntityTreeInformationModel.CHILDREN);
        fillReferencedSelectedParentKlasses(referencedKlasses, children, klassIds, klassTaxonomyId);
      }
    }
  }*/
  
  /*private void fillKlasses(List<Map<String, Object>> klassesList, Vertex klassNode) throws Exception
  {
    Map<String, Object> klassMap = UtilClass.getMapFromVertex(Arrays.asList(IIdLabelCodeModel.ID,  IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE), klassNode);
    klassesList.add(klassMap);
    fillChildrenData(klassNode, klassMap);
  }*/
  
  /*private void fillChildrenData(Vertex klassNode, Map<String, Object> klassMap) throws Exception
  {
    List<Map<String, Object>> klassesList = new ArrayList<>();
    OrientGraph graph = UtilClass.getGraph();
    
    Iterable<Vertex> ChildNodes = graph.command(
        new OCommandSQL("select expand(in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
            + "')) from " + klassNode.getId() + " order by "
            + EntityUtil.getLanguageConvertedField(IIdLabelCodeModel.LABEL) + " asc"))
        .execute();
    for (Vertex childNode : ChildNodes) {
      fillKlasses( klassesList, childNode);
    }
    klassMap.put(IConfigEntityTreeInformationModel.CHILDREN, klassesList);
  }*/


	protected void fillFilterAndSortDataForSelectedModule(Map<String, Object> requestMap,
	    Map<String, Object> response) throws Exception
	{
	  List<String> entityTypes = (List<String>) response
	      .get(IGetNewFilterAndSortDataResponseModel.ALLOWED_ENTITIES);
	  Map<String, Object> paginatedsortInfo = (Map<String, Object>) requestMap
	      .get(IConfigDetailsForFilterAndSortInfoRequestModel.PAGINATED_SORT_INFO);

	  List<Map<String, Object>> sortData = new ArrayList<>();
    List<Map<String, Object>> filterData = new ArrayList<>();
    
    fillSortData(entityTypes, sortData, paginatedsortInfo);

	  fillFilterData(entityTypes, filterData);

		Iterable<Vertex> translatableAttributesVertices = getSortableFilterableTranslableSerachableData(IAttribute.IS_TRANSLATABLE,
		    VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, entityTypes, new ArrayList<>(), null, null);
		List<String> translatableAttributes = UtilClass.getCIds(translatableAttributesVertices);

		Iterable<Vertex> serachableAttributesVertices = getSortableFilterableTranslableSerachableData(IAttribute.IS_SEARCHABLE,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, entityTypes, new ArrayList<>(), null, null);
    List<String> serachableAttributes = UtilClass.getCIds(serachableAttributesVertices);

    response.put(IGetNewFilterAndSortDataResponseModel.SORT_DATA, sortData);
    response.put(IGetNewFilterAndSortDataResponseModel.FILTER_DATA, filterData);
    response.put(IGetNewFilterAndSortDataResponseModel.TRANSLATABLE_ATTRIBUTE_IDS, translatableAttributes);
    response.put(IGetNewFilterAndSortDataResponseModel.SEARCHABLE_ATTRIBUTE_IDS,
        serachableAttributes);
  }
  
  private void fillParentTaxonomyDetails(Map<String, Object> referencedTaxonomies,
      Map<String, Object> taxonomyMap, Vertex taxonomyNode)
  {
    Iterable<Vertex> parentVertices = taxonomyNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex vertex : parentVertices) {
      String parentType = (String) vertex.getProperty("@class");
      if (parentType.equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
        String parentId = (String) vertex.getProperty(IIdLabelCodeModel.CODE);
        taxonomyMap.put(IReferencedKlassOrTaxonomyModel.PARENT_ID, parentId);
        
        if (referencedTaxonomies.get(parentId) == null) {
          Map<String, Object> parentMap = UtilClass.getMapFromVertex(
              Arrays.asList(IIdLabelCodeModel.ID, IIdLabelCodeModel.LABEL, IIdLabelCodeModel.CODE),
              vertex);
          referencedTaxonomies.put(parentId, parentMap);
          fillParentTaxonomyDetails(referencedTaxonomies, parentMap, vertex);
        }
      }
    }
  }
}
