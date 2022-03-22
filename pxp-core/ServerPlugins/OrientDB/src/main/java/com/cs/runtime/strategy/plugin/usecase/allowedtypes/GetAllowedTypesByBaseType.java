package com.cs.runtime.strategy.plugin.usecase.allowedtypes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetAllowedTypesByBaseType extends AbstractOrientPlugin {
  
  static List<String> FILEDS_TO_FATCH_FOR_KLASS = Arrays.asList(IKlass.LABEL, IKlass.CODE,
      IKlass.ICON, IKlass.TYPE);

  static List<String> FILEDS_TO_FATCH_FOR_TAXONOMY = Arrays.asList(IKlass.LABEL, IKlass.CODE,
      IKlass.ICON, IKlass.TYPE, ITaxonomy.TAXONOMY_TYPE);
  
  public GetAllowedTypesByBaseType(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String baseType = (String) requestMap.get(IAllowedTypesRequestModel.BASE_TYPE);
    String selectionType = (String) requestMap.get(IAllowedTypesRequestModel.SELECTION_TYPE);
    Long from = Long.valueOf(requestMap.get(IAllowedTypesRequestModel.FROM).toString());
    Long size = Long.valueOf(requestMap.get(IAllowedTypesRequestModel.SIZE).toString());
    String searchText = (String) requestMap.get(IAllowedTypesRequestModel.SEARCH_TEXT);
    String idsToExclude = EntityUtil.quoteIt((List<String>) requestMap.get(IAllowedTypesRequestModel.IDS_TO_EXCLUDE));
    List<String> entities = (List<String>) requestMap.get(IAllowedTypesRequestModel.ALLOWED_ENTITIES);


    List<Map<String,Object>> allowedTypes = new ArrayList<>();

    Map<String,Object> configDetails = new HashMap<String, Object>();
    
    List<String> listOfTopPermissionNodes = new ArrayList<String>();

    String parentId = (String)requestMap.get(IAllowedTypesRequestModel.ID);
    
    String userId = (String) requestMap.get(IAllowedTypesRequestModel.USER_ID);
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    
    Map<String, Integer> isPermissionFromRoleOrOrganization = new HashMap<String, Integer>();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);    
    // 0 none, 1 -role, 2- organization
    
    List<Object> rids = new ArrayList<>();
    String fieldToSearchAndSort = null;
    if (selectionType.equals(CommonConstants.PRIMARY_TYPES) || selectionType.equals(CommonConstants.SECONDARY_TYPES)) {
      fillRecordIdsForAllowedKlasses(selectionType, baseType, parentId, roleNode, rids, idsToExclude, entities);
      fieldToSearchAndSort = EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    }
    
    else if (selectionType.equals(CommonConstants.MAJOR_TAXONOMY) || selectionType.equals(CommonConstants.TAXONOMY)) {

      fillRecordIdsForAllowedTaxonomies(selectionType, roleNode, rids, isPermissionFromRoleOrOrganization, listOfTopPermissionNodes, idsToExclude);
      fieldToSearchAndSort = EntityUtil.getDataLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    }
      
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, fieldToSearchAndSort);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);

    
    Long totalCount = 0l;
    if(!rids.isEmpty()) {
      String query = "select from " + rids + conditionQuery
          + " order by " + fieldToSearchAndSort +  " asc skip " + from + " limit " + size;
      
      String countQuery = "select count(*) from" + rids + conditionQuery;
      totalCount = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
      
      Iterable<Vertex> resultIterable = UtilClass.getGraph().command(new OCommandSQL(query))
          .execute();
      for (Vertex klass : resultIterable) {
        Map<String, Object> propertyMap;
        if (selectionType.equals(CommonConstants.MAJOR_TAXONOMY) || selectionType.equals(CommonConstants.TAXONOMY)) {
          propertyMap = UtilClass.getMapFromVertex(FILEDS_TO_FATCH_FOR_TAXONOMY, klass);
          propertyMap.put(ITaxonomy.BASE_TYPE, klass.getProperty(IAllowedTypesRequestModel.BASE_TYPE));
          propertyMap.put(ITaxonomy.TYPE, klass.getProperty(IAllowedTypesRequestModel.BASE_TYPE));
          propertyMap.put(IConfigTaxonomyHierarchyInformationModel.TAXONOMY_TYPE, klass.getProperty(ITaxonomy.TAXONOMY_TYPE));
          TaxonomyUtil.fillParentIdAndConfigDetails(propertyMap, configDetails, klass, isPermissionFromRoleOrOrganization, listOfTopPermissionNodes);
        }
        else {
          propertyMap = UtilClass.getMapFromVertex(FILEDS_TO_FATCH_FOR_TAXONOMY, klass);
        }
        allowedTypes.add(propertyMap);
      }
    }
    
    Map<String,Object> returnMap = new HashMap<>();
    returnMap.put(ITaxonomyHierarchyModel.LIST, allowedTypes);
    returnMap.put(ITaxonomyHierarchyModel.CONFIG_DETAILS, configDetails);
    returnMap.put(ITaxonomyHierarchyModel.COUNT, totalCount);
    
    return returnMap;
  }


  protected void removeKlassesNotHavingCreatePermission(Vertex role,List<Vertex> allowedKlasses) {
    String query = "select from (select expand (OUT( '" +RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "')) from " + role.getId()
    + ") where " + CommonConstants.ORIENTDB_CLASS_PROPERTY + " = " + EntityUtil.quoteIt(VertexLabelConstants.GLOBAL_CAN_CREATE_PERMISSIONS) ;
    
    Iterable<Vertex> resultIterable = UtilClass.getGraph().command(new OCommandSQL(query))
        .execute();
    for (Vertex canCreatePermission : resultIterable) {
      Iterator<Vertex> klassVertices= canCreatePermission.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION).iterator();
      Vertex klassVertex = klassVertices.next();
      allowedKlasses.remove(klassVertex);
    }
  }
  
  protected void fillRecordIdsForAllowedTaxonomies(String selectionType, Vertex roleNode, List<Object> rids, Map<String, Integer> isPermissionFromRoleOrOrganization, List<String> listOfTopPermissionNodes,String idsToExclude) throws Exception {
  List<Vertex> allowedTaxonomies = new ArrayList<>();
    Iterator<Vertex> targetTaxonomies = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES).iterator();
    isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 1);
    
    if (!targetTaxonomies.hasNext()) {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      targetTaxonomies = organizationNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES).iterator();
      isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 2);
      
    }
    if (!targetTaxonomies.hasNext()) {
      if(selectionType.equals(CommonConstants.TAXONOMY)) {
        allowedTaxonomies.addAll(TaxonomyUtil.getAllRootLevelTaxonomyNodes());
      }
      else {
        allowedTaxonomies.addAll(TaxonomyUtil.getAllRootLevelMajorTaxonomyNodes(idsToExclude));
      }
      isPermissionFromRoleOrOrganization.put(CommonConstants.PERMISSION_PROPERTY, 0);
    }
    if(selectionType.equals(CommonConstants.TAXONOMY) && targetTaxonomies.hasNext()) {
      allowedTaxonomies.addAll(TaxonomyUtil.getAllRootLevelMinorTaxonomyNodes());
    }
    while (targetTaxonomies.hasNext()) {
      Vertex targetTaxonomy = targetTaxonomies.next(); 
      allowedTaxonomies.add(targetTaxonomy);
      listOfTopPermissionNodes.add(UtilClass.getCode(targetTaxonomy));
    }
    removeKlassesNotHavingCreatePermission(roleNode, allowedTaxonomies);
    rids.clear();
    for (Vertex klassNode : allowedTaxonomies) {
      rids.add(klassNode.getId());
    }
  }
  
  protected void fillRecordIdsForAllowedKlasses(String selectionType, String baseType,String parentId,Vertex roleNode, List<Object> rids,String idsToExclude,
      List<String> entities) throws Exception {
    List<Vertex> allowedKlasses = new ArrayList<>();
    String klassType = EntityUtil.getKlassType(baseType);
    String klassTypes = null;
    if (klassType != null) {
      klassTypes = EntityUtil.quoteIt(klassType);
    }
    else {
      List <String> klassTypesFromEntities = EntityUtil.getKlassTypes(entities);
      klassTypes = EntityUtil.quoteIt(klassTypesFromEntities);
    }
    if (selectionType.equals(CommonConstants.PRIMARY_TYPES)) { 
      allowedKlasses = KlassUtils.getAllNatureKlassNodes(klassType, idsToExclude);
    }
    else if (selectionType.equals(CommonConstants.SECONDARY_TYPES)) {
      allowedKlasses = KlassUtils.getAllNonNatureKlassNodes(klassTypes, idsToExclude);
    }
    removeKlassesNotHavingCreatePermission(roleNode, allowedKlasses);
    List<Vertex> klassesHavingReadPermissions = new ArrayList<>();
    Iterator<Vertex> targetKlasses = roleNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_KLASSES).iterator();
    if (!targetKlasses.hasNext()) {
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      targetKlasses = organizationNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_AVAILABLE_KLASSES).iterator();
    }
    while (targetKlasses.hasNext()) {
      klassesHavingReadPermissions.add(targetKlasses.next());
    }
    if (!klassesHavingReadPermissions.isEmpty()) {
      allowedKlasses.retainAll(klassesHavingReadPermissions);
    }
    
    for (Vertex klassNode : allowedKlasses) {
      String natureType = klassNode.getProperty(IKlass.NATURE_TYPE);
      if (CommonConstants.CONTEXTUAL_KLASSES.contains(natureType) || UtilClass.getCode(klassNode).equals(parentId)) {
        continue;
      }
      rids.add(klassNode.getId());
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllowedTypesByBaseType/*" };
  }
}