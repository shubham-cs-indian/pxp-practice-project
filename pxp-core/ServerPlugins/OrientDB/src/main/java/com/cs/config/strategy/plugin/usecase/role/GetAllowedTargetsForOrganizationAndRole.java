package com.cs.config.strategy.plugin.usecase.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.IteratorUtils;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.klass.IGetMajorTaxonomiesResponseModel;
import com.cs.core.config.interactor.model.role.IGetAllowedTargetsForRoleRequestModel;
import com.cs.core.config.interactor.model.user.IGetAllowedUsersRequestModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassTaxonomyTreeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetAllowedTargetsForOrganizationAndRole extends AbstractOrientPlugin {
  
  protected final List<String> fieldsToFetch = Arrays.asList(IIdLabelCodeModel.LABEL,	
			IIdLabelCodeModel.CODE, CommonConstants.ICON_PROPERTY, CommonConstants.TYPE_PROPERTY, ITaxonomy.BASE_TYPE);
  
  public GetAllowedTargetsForOrganizationAndRole(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedTargetsForOrganizationAndRole/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    List<Map<String, Object>> returnList = new ArrayList<>();
    Long from = Long.valueOf(requestMap.get(IGetAllowedUsersRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IGetAllowedUsersRequestModel.SIZE)
        .toString());
    String selectionType = (String) requestMap
        .get(IGetAllowedTargetsForRoleRequestModel.SELECTION_TYPE);
    String searchText = (String) requestMap.get(IGetAllowedTargetsForRoleRequestModel.SEARCH_TEXT);
    String organizationId = (String) requestMap
        .get(IGetAllowedTargetsForRoleRequestModel.ORGANIZATION_ID);
    
    Map<String, Object> configDetails = new HashMap<String, Object>();
    Map<String, Integer> isPermissionFromRoleOrOrganization = new HashMap<String, Integer>();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
    // 0 none, 1 -role, 2- organization
    
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN) != null
        ? requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
            .toString()
        : CommonConstants.LABEL_PROPERTY;
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    
    if (selectionType.equals(CommonConstants.TAXONOMIES)) {
      Boolean isTaxonomyVertiecsSelected = false;
      String id = (String) requestMap.get(IGetAllowedTargetsForRoleRequestModel.ID);
      if (id.equals("-1")) {
        List<Vertex> taxonomyVertices = new ArrayList<Vertex>();
        if (organizationId != null && !organizationId.equals("")) {
          Vertex organizationNode = UtilClass.getVertexByIndexedId(organizationId,
              VertexLabelConstants.ORGANIZATION);
          // if any search text is entered then check for if any taxonomy is
          // present
          Iterator<Vertex> taxonomyVertiecsSelected = organizationNode
              .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES)
              .iterator();
          isTaxonomyVertiecsSelected = taxonomyVertiecsSelected.hasNext();
          if (isTaxonomyVertiecsSelected) {
            StringBuilder linkCondition = new StringBuilder();
            linkCondition.append(" in('" + RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES
                + "').code contains " + EntityUtil.quoteIt(organizationId));
            
            StringBuilder conditionQuery = EntityUtil.getConditionQuery(linkCondition, searchQuery);
            
            String query = "select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
                + conditionQuery + " order by "
                + EntityUtil.getLanguageConvertedField(IKlassTaxonomyTreeModel.LABEL) + " asc skip "
                + from + " limit " + size;
            
            OrientGraph graph = UtilClass.getGraph();
            Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
                .execute();
            
            Iterator<Vertex> taxonomyVerticesIterator = searchResults.iterator();
            taxonomyVertices = IteratorUtils.toList(taxonomyVerticesIterator);
          }
        }
        
        if ((organizationId == null || organizationId.equals(""))
            || (taxonomyVertices.isEmpty() && !isTaxonomyVertiecsSelected)) {
          taxonomyVertices = getAllRootLevelMajorTaxonomyNodes(searchText, from, size, searchQuery);
        }
        returnList = getListAndConfigDetailsOfTaxonomy(taxonomyVertices, configDetails,	
				isPermissionFromRoleOrOrganization);;
      }
      else {
    	Vertex parentTaxonomy = null;	
		try {	
			parentTaxonomy = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ROOT_KLASS_TAXONOMY);	
		} catch (NotFoundException e) {	
			throw new KlassTaxonomyNotFoundException(e);	
		}	
		String rid = parentTaxonomy.getId().toString();
        StringBuilder linkCondition = new StringBuilder();
        linkCondition.append(" code != '" + id + "' ");
        StringBuilder conditionQuery = EntityUtil.getConditionQuery(linkCondition, searchQuery);
        
        String query = "select from (traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF	
				+ "') from " + rid + ") " + conditionQuery + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
            + " asc skip " + from + " limit " + size;
        
        OrientGraph graph = UtilClass.getGraph();
        Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
            .execute();
        Iterator<Vertex> taxonomyVerticesIterator = searchResults.iterator();
        List<Vertex> taxonomyVertices = IteratorUtils.toList(taxonomyVerticesIterator);
        returnList = getListAndConfigDetailsOfTaxonomy(taxonomyVertices, configDetails,	
				isPermissionFromRoleOrOrganization);
      }
    }
    else if (selectionType.equals(CommonConstants.ENTITY_KLASS)) {
      Boolean isTargetKlassSelected = false;
      List<Vertex> klassVertices = new ArrayList<Vertex>();
      if (organizationId != null && !organizationId.equals("")) {
        Vertex organizationNode = UtilClass.getVertexById(organizationId,
            VertexLabelConstants.ORGANIZATION);
        Iterator<Vertex> klassVertiecs = organizationNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_AVAILABLE_KLASSES)
            .iterator();
        isTargetKlassSelected = klassVertiecs.hasNext();
        
        if (isTargetKlassSelected) {
          StringBuilder linkCondition = new StringBuilder();
          linkCondition.append(" in('" + RelationshipLabelConstants.HAS_AVAILABLE_KLASSES
              + "').code contains '" + organizationId + "'");
          StringBuilder conditionQuery = EntityUtil.getConditionQuery(linkCondition, searchQuery);
          
          String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
              + conditionQuery + " order by " + EntityUtil.getLanguageConvertedField(IKlass.LABEL)
              + " asc skip " + from + " limit " + size;
          
          OrientGraph graph = UtilClass.getGraph();
          Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
              .execute();
          Iterator<Vertex> klassVerticesIterator = searchResults.iterator();
          klassVertices = IteratorUtils.toList(klassVerticesIterator);
        }
      }
      if ((organizationId == null || organizationId.equals(""))
          || (klassVertices.isEmpty() && !isTargetKlassSelected)) {
        klassVertices = getAllKlasses(searchText, from, size, searchQuery);
      }
      returnList = getIdLabelOfNode(klassVertices);
    }
    returnMap.put(IGetMajorTaxonomiesResponseModel.LIST, returnList);	
	returnMap.put(IGetMajorTaxonomiesResponseModel.CONFIG_DETAILS, configDetails);
    return returnMap;
  }
  
  private List<Map<String, Object>> getListAndConfigDetailsOfTaxonomy(List<Vertex> taxonomyVertices,	
			Map<String, Object> configDetails, Map<String, Integer> isPermissionFromRoleOrOrganization)	
			throws Exception {	
		List<Map<String, Object>> targetList = new ArrayList<>();	
		for (Vertex taxonomy : taxonomyVertices) {	
			Map<String, Object> targetMap = UtilClass.getMapFromVertex(fieldsToFetch, taxonomy);	
			TaxonomyUtil.fillParentIdAndConfigDetails(targetMap, configDetails, taxonomy,	
					isPermissionFromRoleOrOrganization, new ArrayList<String>());	
			targetList.add(targetMap);	
		}	
		return targetList;	
	}
  
  private List<Vertex> getAllRootLevelMajorTaxonomyNodes(String searchText, Long from, Long size,
      StringBuilder searchQuery)
  {
    StringBuilder linkCondition = new StringBuilder();
    linkCondition.append(" outE('Child_Of').size() = 0 ");
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(linkCondition, searchQuery);
    
    String query = "select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY + conditionQuery
        + " AND " + ITaxonomy.TAXONOMY_TYPE + "= '" + CommonConstants.MAJOR_TAXONOMY + "'"
        + " order by " + EntityUtil.getLanguageConvertedField(ITaxonomyInformationModel.LABEL)
        + " asc skip " + from + " limit " + size;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> taxonomyIterator = vertices.iterator();
    List<Vertex> taxonomyVertices = IteratorUtils.toList(taxonomyIterator);
    return taxonomyVertices;
  }
  
  private List<Map<String, Object>> getIdLabelOfNode(List<Vertex> targetVertices)
  {
    List<Map<String, Object>> targetList = new ArrayList<>();
    for (Vertex target : targetVertices) {
      Map<String, Object> targetMap = UtilClass.getMapFromVertex(fieldsToFetch, target);
      targetList.add(targetMap);
    }
    return targetList;
  }
  
  private List<Vertex> getAllKlasses(String searchText, Long from, Long size,
      StringBuilder searchQuery)
  {
    StringBuilder linkCondition = new StringBuilder();
    List<String> taxonomyVertexTypes = Arrays.asList(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    linkCondition.append(CommonConstants.ORIENTDB_CLASS_PROPERTY + " not in "
        + EntityUtil.quoteIt(taxonomyVertexTypes));
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(linkCondition, searchQuery);
    
    String labelOfUserLanguage = EntityUtil
        .getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + conditionQuery
        + " order by " + labelOfUserLanguage + " asc skip " + from + " limit " + size;
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> klassIterator = vertices.iterator();
    List<Vertex> klassVertices = IteratorUtils.toList(klassIterator);
    return klassVertices;
  }
}
