package com.cs.runtime.strategy.plugin.usecase.allowedtypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetAllTaxonomiesForAllowedTypes extends AbstractOrientPlugin {
  
  public GetAllTaxonomiesForAllowedTypes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllTaxonomiesForAllowedTypes/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IAllowedTypesRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IAllowedTypesRequestModel.SIZE)
        .toString());
    String searchText = (String) requestMap.get(IAllowedTypesRequestModel.SEARCH_TEXT);
    String selectionType = (String) requestMap.get(IAllowedTypesRequestModel.SELECTION_TYPE);
    String idsToExcludeTaxonomy = EntityUtil
        .quoteIt((List<String>) requestMap.get(IAllowedTypesRequestModel.IDS_TO_EXCLUDE));
    
    String userId = (String) requestMap.get(IAllowedTypesRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    
    Map<String, Integer> isPermissionFromRoleOrOrganization = new HashMap<String, Integer>();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
    
    List<Object> rids = new ArrayList<>();
    List<String> listOfTopPermissionNodes = new ArrayList<String>();
    fillRecordIdsForAllowedTaxonomies(selectionType, roleNode, rids,
        isPermissionFromRoleOrOrganization, listOfTopPermissionNodes, idsToExcludeTaxonomy);
    String fieldToSearchAndSort = EntityUtil
        .getDataLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, fieldToSearchAndSort);
    
    StringBuilder excludeIdsQuery = new StringBuilder(" cid not in  " + idsToExcludeTaxonomy);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(excludeIdsQuery, searchQuery);
    Iterable<Vertex> taxonomies = new ArrayList<Vertex>();
    long totalCount = 0;
    if(!rids.isEmpty()) {
      
      String query = "select from(traverse in('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')" + " from " + rids.toString()
          + " strategy BREADTH_FIRST) " + conditionQuery + "order by " + fieldToSearchAndSort
          + " asc skip " + from + " limit " + size;
      
      taxonomies = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
     
      String countQuery =  "select count(*) from(traverse in('"
          + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')" + " from " + rids.toString()
          + " strategy BREADTH_FIRST) " + conditionQuery;
      
      totalCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    }
    
    Map<String, Object> configDetails = new HashMap<String, Object>();
    List<Map<String, Object>> allowedTypes = new ArrayList<>();
    for (Vertex taxonomy : taxonomies) {
      Map<String, Object> propertyMap = UtilClass.getMapFromVertex(
          Arrays.asList(ITaxonomy.LABEL, ITaxonomy.CODE, ITaxonomy.ICON, ITaxonomy.TAXONOMY_TYPE),
          taxonomy);
      
      propertyMap.put(IKlass.TYPE, taxonomy.getProperty(IAllowedTypesRequestModel.BASE_TYPE));
      TaxonomyUtil.fillParentIdAndConfigDetails(propertyMap, configDetails, taxonomy,
          isPermissionFromRoleOrOrganization, listOfTopPermissionNodes);
      
      allowedTypes.add(propertyMap);
    }
    
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(ITaxonomyHierarchyModel.LIST, allowedTypes);
    returnMap.put(ITaxonomyHierarchyModel.CONFIG_DETAILS, configDetails);
    returnMap.put(ITaxonomyHierarchyModel.COUNT, totalCount);
    return returnMap;
  }
  
  protected void fillRecordIdsForAllowedTaxonomies(String selectionType, Vertex roleNode,
      List<Object> rids, Map<String, Integer> isPermissionFromRoleOrOrganization,
      List<String> listOfTopPermissionNodes, String idsToExclude) throws Exception
  {
    List<Vertex> allowedTaxonomies = new ArrayList<>();
    Iterator<Vertex> targetTaxonomies = roleNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES)
        .iterator();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 1);
    
    if (!targetTaxonomies.hasNext()) {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      targetTaxonomies = organizationNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES)
          .iterator();
      isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 2);
      
    }
    if (!targetTaxonomies.hasNext()) {
      if (selectionType.equals(CommonConstants.TAXONOMY)) {
        allowedTaxonomies.addAll(TaxonomyUtil.getAllRootLevelTaxonomyNodes());
      }
      else {
        allowedTaxonomies.addAll(TaxonomyUtil.getAllRootLevelMajorTaxonomyNodes(idsToExclude));
      }
      isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
    }
    if (selectionType.equals(CommonConstants.TAXONOMY) && targetTaxonomies.hasNext()) {
      allowedTaxonomies.addAll(TaxonomyUtil.getAllRootLevelMinorTaxonomyNodes());
    }
    while (targetTaxonomies.hasNext()) {
      allowedTaxonomies.add(targetTaxonomies.next());
    }
    removeKlassesNotHavingCreatePermission(roleNode, allowedTaxonomies);
    rids.clear();
    for (Vertex klassNode : allowedTaxonomies) {
      rids.add(klassNode.getId());
      listOfTopPermissionNodes.add(UtilClass.getCodeNew(klassNode));
    }
    
  }
  
  protected void removeKlassesNotHavingCreatePermission(Vertex role, List<Vertex> allowedKlasses)
  {
    String query = "select from (select expand (OUT( '"
        + RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "')) from "
        + role.getId() + ") where " + CommonConstants.ORIENTDB_CLASS_PROPERTY + " = "
        + EntityUtil.quoteIt(VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex canCreatePermission : resultIterable) {
      Iterator<Vertex> klassVertices = canCreatePermission
          .getVertices(Direction.IN,
              RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION)
          .iterator();
      Vertex klassVertex = klassVertices.next();
      allowedKlasses.remove(klassVertex);
    }
  }
}