package com.cs.config.strategy.plugin.usecase.role.abstrct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.datarule.util.DataRuleUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.OnboardingRoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.exception.role.RoleNotFoundException;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public abstract class AbstractSaveRole extends AbstractOrientPlugin {
  
  public AbstractSaveRole(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public Map<String, Object> saveRole(HashMap<String, Object> roleMap, String useCase, String label)
      throws Exception
  {
    String roleId;
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setSectionPermissionIdMap(new HashMap<>());
    List<Map<String, Object>> auditLogInfoList = new ArrayList<>();
    List<String>  deletedUserNames = new ArrayList<String>();
    
    roleId = (String) roleMap.get("id");
    List<String> addedUsers = (List<String>) roleMap.remove("addedUsers");
    List<String> deletedUsers = (List<String>) roleMap.remove("deletedUsers");
    
    List<String> addedEndpointIds = (List<String>) roleMap.remove(IRoleSaveModel.ADDED_ENDPOINTS);
    List<String> deletedEndpointIds = (List<String>) roleMap.remove(IRoleSaveModel.DELETED_ENDPOINTS);
    
    List<String> addedKPIs = (List<String>) roleMap.remove(IRoleSaveModel.ADDED_KPIS);
    List<String> deletedKPIs = (List<String>) roleMap.remove(IRoleSaveModel.DELETED_KPIS);
    
    List<String> addedSystemIds = (List<String>) roleMap.remove(IRoleSaveModel.ADDED_SYSTEM_IDS);
    List<String> deletedSystemIds = (List<String>) roleMap.remove(IRoleSaveModel.DELETED_SYSTEM_IDS);
    
    Boolean newIsMultiSelect = (Boolean) roleMap.get(IRole.IS_MULTISELECT);
    
    Vertex roleNode = null;
    try {
      roleNode = UtilClass.getVertexById(roleId, label);
    }
    catch (NotFoundException e) {
      throw new RoleNotFoundException();
    }
    Boolean oldIsMultiSelect = (Boolean) roleNode.getProperty(IRole.IS_MULTISELECT);
    
    if (!newIsMultiSelect && oldIsMultiSelect) {
      DataRuleUtils.deleteVerticesWithInDirection(roleNode,
          RelationshipLabelConstants.NORMALIZATION_ENTITY_LINK);
      DataRuleUtils.deleteIntermediateVerticesWithInDirection(roleNode,
          RelationshipLabelConstants.ROLE_DATA_RULE_LINK);
    }
    
    setEndpointList(roleMap, roleNode, addedEndpointIds, deletedEndpointIds);
    
    manageTargetKlasses(roleMap, roleNode);
    manageTargetTaxonomies(roleMap, roleNode);
    
    UtilClass.saveNode(roleMap, roleNode, new ArrayList<>());
    String vertexLabel = VertexLabelConstants.ENTITY_TYPE_USER;
    if (label.equals(VertexLabelConstants.ENTITY_TYPE_ROLE)) {
      vertexLabel = VertexLabelConstants.ONBOARDING_USER;
    }
    RoleUtils.manageAddedUsers(addedUsers, roleNode, vertexLabel);
    RoleUtils.manageDeletedUsers(deletedUsers, roleNode, deletedUserNames);
    
    if (addedEndpointIds != null && deletedEndpointIds != null) {
      OnboardingRoleUtils.manageAddedEndpoints(addedEndpointIds, roleNode);
      RoleUtils.manageDeletedEndpoints(deletedEndpointIds, roleNode);
    }
    
    if (addedKPIs != null && !addedKPIs.isEmpty()) {
      RoleUtils.manageAddedKPIs(addedKPIs, roleNode);
    }
    if (deletedKPIs != null && !deletedKPIs.isEmpty()) {
      RoleUtils.manageDeletedKPIs(deletedKPIs, roleNode);
    }
    if (addedSystemIds != null && !addedSystemIds.isEmpty()) {
      RoleUtils.manageAddedSystemIds(addedSystemIds, roleNode);
    }
    if (deletedSystemIds != null && !deletedSystemIds.isEmpty()) {
      RoleUtils.manageDeletedSystemIds(deletedSystemIds, roleNode);
    }
    
    graph.commit();
    
    Map<String, Object> roleMapToReturn = new HashMap<String, Object>();
    Map<String, Object> roleMapToSend = RoleUtils.getRoleEntityMap(roleNode);
    
    roleMapToSend = RoleUtils.getRoleEntityMap(roleNode);
    RoleUtils.addTargetKlasses(roleMapToSend, roleNode, roleMapToReturn);
    RoleUtils.addTargetTaxonomies(roleMapToSend, roleNode, roleMapToReturn);
    RoleUtils.addReferencedEndPoints(roleMapToSend, roleNode, roleMapToReturn);
    RoleUtils.addReferencedKPIs(roleMapToSend, roleNode, roleMapToReturn);
    RoleUtils.addReferencedSystems(roleMapToSend, roleNode, roleMapToReturn);
    
    AuditLogUtils.fillAuditLoginfo(auditLogInfoList, roleNode, Entities.ROLES, Elements.UNDEFINED);
    
    roleMapToReturn.put(CommonConstants.ROLE, roleMapToSend);
    roleMapToReturn.put(IGetRoleStrategyModel.DELETED_USER_NAMES, deletedUserNames);
    roleMapToReturn.put("articles",
        KlassGetUtils.getKlassesList("article", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("collections",
        KlassGetUtils.getKlassesList("collection", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("sets",
        KlassGetUtils.getKlassesList("set", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("assets",
        KlassGetUtils.getKlassesList("asset_asset", VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("collectionAssets",
        KlassGetUtils.getKlassesList("collection_asset", VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("markets",
        KlassGetUtils.getKlassesList("market", VertexLabelConstants.ENTITY_TYPE_TARGET));
    roleMapToReturn.put("collectionTargets",
        KlassGetUtils.getKlassesList("collection_target", VertexLabelConstants.ENTITY_TYPE_TARGET));
    roleMapToReturn.put(ICreateOrSaveRoleResponseModel.AUDIT_LOG_INFO, auditLogInfoList);
    
    return roleMapToReturn;
  }
  
  private void manageTargetTaxonomies(HashMap<String, Object> roleMap, Vertex roleNode)
      throws Exception
  {
    List<String> addedTargetTaxonmies = (List<String>) roleMap
        .remove(IRoleSaveModel.ADDED_TARGET_TAXONOMIES);
    List<String> deletedTargetTaxonomies = (List<String>) roleMap
        .remove(IRoleSaveModel.DELETED_TARGET_TAXONOMIES);
    RoleUtils.manageAddedTargetTaxonomies(addedTargetTaxonmies, roleNode);
    RoleUtils.manageDeletedTargetTaxonomies(deletedTargetTaxonomies, roleNode);
  }
  
  private void manageTargetKlasses(HashMap<String, Object> roleMap, Vertex roleNode)
      throws Exception
  {
    List<String> addedTargetKlasses = (List<String>) roleMap
        .remove(IRoleSaveModel.ADDED_TARGET_KLASSES);
    List<String> deletedTargetKlasses = (List<String>) roleMap
        .remove(IRoleSaveModel.DELETED_TARGET_KLASSES);
    RoleUtils.manageAddedTargetKlasses(addedTargetKlasses, roleNode);
    RoleUtils.manageDeletedTargetKlasses(deletedTargetKlasses, roleNode);
  }
  
  private void setEndpointList(HashMap<String, Object> roleMap, Vertex roleNode,
      List<String> addedEndpointIds, List<String> deletedEndpointIds)
  {
    List<String> endpoints = (List<String>) roleMap.get(IRole.ENDPOINTS);
    endpoints.removeAll(deletedEndpointIds);
    endpoints.addAll(addedEndpointIds);
    roleNode.setProperty(IRole.ENDPOINTS, endpoints);
  }
}
