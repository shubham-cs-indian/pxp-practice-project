package com.cs.runtime.strategy.plugin.usecase.allowedtypes;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomyarticle.ArticleTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GetNonRootTaxonomiesForAllowedTypes extends AbstractOrientPlugin {
  
  public GetNonRootTaxonomiesForAllowedTypes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetNonRootTaxonomiesForAllowedTypes/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IAllowedTypesRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IAllowedTypesRequestModel.SIZE)
        .toString());
    String searchColumn = EntityUtil
        .getLanguageConvertedField(requestMap.get(IAllowedTypesRequestModel.SEARCH_COLUMN)
            .toString());
    String searchText = requestMap.get(IAllowedTypesRequestModel.SEARCH_TEXT)
        .toString();
    String userId = (String) requestMap.get(IAllowedTypesRequestModel.USER_ID);
    
    String rootTaxonomyId = requestMap.get(IAllowedTypesRequestModel.ID)
        .toString();
    List<String> selectedTaxonomyIds = (List<String>) requestMap
        .get(IAllowedTypesRequestModel.IDS_TO_EXCLUDE);
    selectedTaxonomyIds.add(rootTaxonomyId);
    String idsToExcludeTaxonomy = EntityUtil.quoteIt(selectedTaxonomyIds);
    
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    String roleId = UtilClass.getCodeNew(roleNode);
    Map<String, Integer> isPermissionFromRoleOrOrganization = new HashMap<String, Integer>();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
    
    List<Map<String, Object>> allowedTypes = new ArrayList<>();
    Map<String, Object> configDetails = new HashMap<String, Object>();
    
    List<String> listOfTopPermissionNodes = new ArrayList<String>();
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(ITaxonomyHierarchyModel.LIST, allowedTypes);
    returnMap.put(ITaxonomyHierarchyModel.CONFIG_DETAILS, configDetails);
    
    Vertex requestTaxonomyNode;
    try {
      requestTaxonomyNode = UtilClass.getVertexByIndexedId(rootTaxonomyId,
          VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    }
    catch (NotFoundException e) {
      throw new ArticleTaxonomyNotFoundException();
    }
    String requestTaxonomyRId = requestTaxonomyNode.getId()
        .toString();
    
    List<String> childTaxonomyRIdHavingPermission = new ArrayList<>();
    Vertex taxonomyNodeHavingPermission = prepareTaxonomyPermissionForQuery(roleNode, roleId,
        requestTaxonomyNode, childTaxonomyRIdHavingPermission, isPermissionFromRoleOrOrganization,
        listOfTopPermissionNodes);
    
    if (taxonomyNodeHavingPermission == null && childTaxonomyRIdHavingPermission.isEmpty()) {
      // This means no taxonomy in heierarchy of requestTaxonomy has create
      // permission
      return returnMap;
    }
    String searchQuery = "";
    if (!searchColumn.isEmpty() && !searchText.isEmpty()) {
      searchQuery = " and " + searchColumn + " like '%" + searchText + "%'";
    }
    String query;
    String countQuery;
    Long totalCount = 0l;
    if (taxonomyNodeHavingPermission != null) {
      // if requestTaxonomy or its parent has permission all of its children has
      // permission
      query = "select from(traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
          + "')" + " from " + requestTaxonomyRId + " strategy BREADTH_FIRST) where code not in  "
          + idsToExcludeTaxonomy + searchQuery + "order by "
          + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc skip "
          + from + " limit " + size;
      
      countQuery = "select count(*) from(traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
          + "')" + " from " + requestTaxonomyRId + " strategy BREADTH_FIRST) where code not in  "
          + idsToExcludeTaxonomy + searchQuery;
    }
    else {
      // requestTaxonomy has no permission hence get hierarchy of taxonomy
      // children having crete
      // permission
      query = "select from(traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
          + "')" + " from " + childTaxonomyRIdHavingPermission.toString()
          + " strategy BREADTH_FIRST) where code not in " + idsToExcludeTaxonomy + searchQuery
          + "order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
          + " asc skip " + from + " limit " + size;
      
      countQuery = "select count(*) from(traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
          + "')" + " from " + childTaxonomyRIdHavingPermission.toString()
          + " strategy BREADTH_FIRST) where code not in " + idsToExcludeTaxonomy + searchQuery;
    }
    
    Iterable<Vertex> taxonomies = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex taxonomy : taxonomies) {
      Map<String, Object> propertyMap = UtilClass
          .getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
              ITaxonomy.CODE, ITaxonomy.ICON), taxonomy);
      
      propertyMap.put(IKlass.TYPE, taxonomy.getProperty(IAllowedTypesRequestModel.BASE_TYPE));
      TaxonomyUtil.fillParentIdAndConfigDetails(propertyMap, configDetails, taxonomy,
          isPermissionFromRoleOrOrganization, listOfTopPermissionNodes);
      
      allowedTypes.add(propertyMap);
    }
    
    totalCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put(ITaxonomyHierarchyModel.COUNT, totalCount);
    return returnMap;
  }
  
  /**
   * @author Lokesh
   * @param roleNode
   * @param roleId
   * @param requestedTaxonomyNode
   * @param requestedTaxonomyRId
   * @param childTaxonomyRIdHavingPermission
   * @param isPermissionFromRoleOrOrganization
   * @param listOfTopPermissionNodes
   * @return taxonomyNodeHavingPermission
   * @throws Exception
   */
  private Vertex prepareTaxonomyPermissionForQuery(Vertex roleNode, String roleId,
      Vertex requestedTaxonomyNode, List<String> childTaxonomyRIdHavingPermission,
      Map<String, Integer> isPermissionFromRoleOrOrganization,
      List<String> listOfTopPermissionNodes) throws Exception
  {
    String requestedTaxonomyRId = requestedTaxonomyNode.getId()
        .toString();
    Vertex rootParentTaxonomy = AttributionTaxonomyUtil
        .getRootParentTaxonomy(requestedTaxonomyNode);
    if (rootParentTaxonomy.getProperty(ITaxonomy.TAXONOMY_TYPE)
        .equals(CommonConstants.MINOR_TAXONOMY)) {
      // if taxonomy is minor taxonomy then return rootParentTaxonomy as it
      // always has permission
      return rootParentTaxonomy;
    }
    
    Vertex taxonomyNodeHavingPermission = null;
    
    Iterator<Edge> edges = roleNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES)
        .iterator();
    if (edges.hasNext()) {
      // if role contains target klasses then check taxonomies according to role
      taxonomyNodeHavingPermission = getTaxonomyWithPermission(roleId, roleId, requestedTaxonomyRId,
          childTaxonomyRIdHavingPermission, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES,
          listOfTopPermissionNodes);
      isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 1);
    }
    else {
      // if role don't contains target klasses then check taxonomies according
      // to organization
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      
      edges = organizationNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES)
          .iterator();
      if (edges.hasNext()) {
        taxonomyNodeHavingPermission = getTaxonomyWithPermission(roleId,
            UtilClass.getCodeNew(organizationNode), requestedTaxonomyRId,
            childTaxonomyRIdHavingPermission, RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES,
            listOfTopPermissionNodes);
        isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 2);
      }
      else {
        // if role and organization both don't contains any target klasses then
        // simply check of root
        // taxonomy of requested taxonomy
        taxonomyNodeHavingPermission = rootParentTaxonomy;
        
        listOfTopPermissionNodes.add(UtilClass.getCodeNew(rootParentTaxonomy));
        
        Vertex canCreatePermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(
            roleId, UtilClass.getCodeNew(taxonomyNodeHavingPermission),
            VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
        if (canCreatePermissionVertex != null) {
          // has no create permission
          taxonomyNodeHavingPermission = null;
        }
        isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
      }
    }
    return taxonomyNodeHavingPermission;
  }
  
  /**
   * @param roleId
   * @param code
   *          (code can be roleId or organizationId)
   * @param rootTaxonomyRId
   * @param childTaxonomyRIdHavingPermission
   * @param hasTargetTaxonomies
   * @param listOfTopPermissionNodes
   * @return
   * @throws MultipleVertexFoundException
   */
  private Vertex getTaxonomyWithPermission(String roleId, String code, String rootTaxonomyRId,
      List<String> childTaxonomyRIdHavingPermission, String hasTargetTaxonomies,
      List<String> listOfTopPermissionNodes) throws MultipleVertexFoundException
  {
    // check if requested taxonomy or its parent has read permission
    Vertex taxonomyNodeHavingPermission = null;
    String query = "select from(traverse out('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "')" + " from " + rootTaxonomyRId
        + " strategy BREADTH_FIRST) where in('" + hasTargetTaxonomies + "').code contains '" + code
        + "'";
    Iterable<Vertex> taxonomies = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = taxonomies.iterator();
    if (iterator.hasNext()) {
      taxonomyNodeHavingPermission = iterator.next();
      
      listOfTopPermissionNodes.add(UtilClass.getCodeNew(taxonomyNodeHavingPermission));
      
      while (iterator.hasNext()) {
        listOfTopPermissionNodes.add(UtilClass.getCodeNew(iterator.next()));
      }
      
      // check if requested taxonomy or its parent has create permission
      Vertex canCreatePermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(
          roleId, UtilClass.getCodeNew(taxonomyNodeHavingPermission),
          VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
      if (canCreatePermissionVertex != null) {
        // has no create permission
        taxonomyNodeHavingPermission = null; // check for permission of
                                             // listOfTopPermissionNodes as
                                             // child
      }
    }
    else {
      // if requested taxonomy or its parent has no read permission then check
      // its children having
      // permission
      query = "select from(traverse in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF
          + "')" + " from " + rootTaxonomyRId + " strategy BREADTH_FIRST) where in('"
          + hasTargetTaxonomies + "').code contains '" + code + "'";
      taxonomies = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex vertex : taxonomies) {
        Vertex canCreatePermissionVertex = GlobalPermissionUtils.getGlobalPermissionVertexIfExist(
            roleId, UtilClass.getCodeNew(vertex),
            VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS);
        if (canCreatePermissionVertex == null) {
          childTaxonomyRIdHavingPermission.add(vertex.getId()
              .toString());
          
          listOfTopPermissionNodes.add(UtilClass.getCodeNew(vertex)); // check
                                                                      // necessity
        }
      }
    }
    return taxonomyNodeHavingPermission;
  }
}
