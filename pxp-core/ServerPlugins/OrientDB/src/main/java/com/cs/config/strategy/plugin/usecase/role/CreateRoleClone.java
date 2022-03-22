package com.cs.config.strategy.plugin.usecase.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.role.ICreateRoleCloneModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.SystemLabels;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

public class CreateRoleClone extends AbstractOrientPlugin {

  public CreateRoleClone(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] {"POST|CreateRoleClone/*"};
  }

  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> request) throws Exception
  {
    Map<String, Object> requestMap = (Map<String, Object>) request.get("roleClone");
    Boolean isExactClone = (Boolean) requestMap.get(ICreateRoleCloneModel.IS_EXACT_CLONE);
    
    Vertex cloneVertex = isExactClone ? executeExactClone(requestMap)
                                      : executeCustomClone(requestMap);
    return prepareResponseMap(cloneVertex);
  }

  private Vertex executeExactClone(Map<String, Object> requestMap)
      throws Exception
  {
    String roleId = (String) requestMap.get(ICreateRoleCloneModel.ROLE_ID);
    String code = (String) requestMap.get(ICreateRoleCloneModel.CODE);
    String label = (String) requestMap.get(ICreateRoleCloneModel.LABEL);
    Boolean cloneEnableDashboard = (Boolean)requestMap.get(ICreateRoleCloneModel.CLONE_ENABLE_DASHBOARD);
    
    Vertex sourceVertex = UtilClass.getVertexByIndexedId(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    Vertex cloneVertex = UtilClass.createDuplicateNode(sourceVertex, Arrays.asList(ICreateRoleCloneModel.LABEL));
    setProperties(label, code, cloneVertex, cloneEnableDashboard);
    
    connectIncomingVertices(sourceVertex, cloneVertex);
    connectOutgoingVertices(sourceVertex, cloneVertex, requestMap);
   
    return cloneVertex;
  }
  
  private Vertex executeCustomClone(Map<String, Object> requestMap)
      throws Exception
  {
    String roleId = (String) requestMap.get(ICreateRoleCloneModel.ROLE_ID);
    String code = (String) requestMap.get(ICreateRoleCloneModel.CODE);
    String label = (String) requestMap.get(ICreateRoleCloneModel.LABEL);
    
    List<String> propertiesToExclude = new ArrayList<>();
    propertiesToExclude.add(ICreateRoleCloneModel.LABEL);
    
    Boolean clonePhysicalCatalogs = (Boolean)requestMap.get(ICreateRoleCloneModel.CLONE_PHYSICAL_CATALOGS);
    Boolean cloneEntities = (Boolean)requestMap.get(ICreateRoleCloneModel.CLONE_ENTITIES);
    Boolean cloneEnableDashboard = (Boolean)requestMap.get(ICreateRoleCloneModel.CLONE_ENABLE_DASHBOARD);
   
    if(!clonePhysicalCatalogs) {
      propertiesToExclude.add(IRole.PHYSICAL_CATALOGS);
    }
    
    if(!cloneEntities) {
      propertiesToExclude.add(IRole.ENTITIES);
    }
    
    if(!cloneEnableDashboard) {
      propertiesToExclude.add(IRole.IS_DASHBOARD_ENABLE); 
    }
    
    Vertex sourceVertex = UtilClass.getVertexByIndexedId(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    Vertex cloneVertex = UtilClass.createDuplicateNode(sourceVertex, propertiesToExclude);
   
    setProperties(label, code, cloneVertex, cloneEnableDashboard);

    connectIncomingVertices(sourceVertex, cloneVertex);
    connectOutgoingVertices(sourceVertex, cloneVertex, requestMap);
    return cloneVertex;
  }
  
  private void connectPermissionVertices(Vertex sourceVertex, Vertex cloneVertex)
  {
    List<String> edgesToExclude = Arrays.asList(RelationshipLabelConstants.HAS_KPI_ROLE,
        RelationshipLabelConstants.HAS_TARGET_TAXONOMIES,
        RelationshipLabelConstants.HAS_TARGET_KLASSES);
    
    Iterable<Edge> outgoingEdgesOfSource = sourceVertex.getEdges(Direction.OUT);
    
    for(Edge outgoingEdge : outgoingEdgesOfSource) {
      
      if(edgesToExclude.contains(outgoingEdge.getLabel())){
        continue;
      }
      
      Vertex sourcePermissionVertex = outgoingEdge.getVertex(Direction.IN);
      Vertex clonePermissionVertex = UtilClass.createDuplicateNode(sourcePermissionVertex);
      clonePermissionVertex.setProperty(ICreateRoleCloneModel.ROLE_ID, cloneVertex.getProperty(ICreateRoleCloneModel.CODE));
      
      Iterable<Edge> permissionIncomingEdges = sourcePermissionVertex.getEdges(Direction.IN);
      
      for(Edge permissionIncomingEdge : permissionIncomingEdges) {
        
        if(permissionIncomingEdge.getLabel().equals(outgoingEdge.getLabel())) {
          cloneVertex.addEdge(permissionIncomingEdge.getLabel(), clonePermissionVertex);
          continue;
        }
        
        Vertex vertex = permissionIncomingEdge.getVertex(Direction.OUT);
        vertex.addEdge(permissionIncomingEdge.getLabel(), clonePermissionVertex);
      }
      
      Iterable<Edge> permissionOutgoingEdges = sourcePermissionVertex.getEdges(Direction.OUT);
      
      connectVertex(clonePermissionVertex, permissionOutgoingEdges);
    }
  }

  private void connectOutgoingVertices(Vertex sourceVertex, Vertex cloneVertex, Map<String, Object> requestMap)
  {
    Boolean cloneKPI = (Boolean) requestMap.get(ICreateRoleCloneModel.CLONE_KPI);
    Boolean cloneTargetKlasses = (Boolean) requestMap.get(ICreateRoleCloneModel.CLONE_TARGET_CLASSES);
    Boolean cloneTargetTaxonomies = (Boolean) requestMap.get(ICreateRoleCloneModel.CLONE_TAXONOMIES);
    
    if(cloneKPI) {
      Iterable<Edge> kpiEdges = sourceVertex.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_KPI_ROLE);
      connectVertex(cloneVertex, kpiEdges);
    }
    
    if(cloneTargetTaxonomies) {
      Iterable<Edge> targetTaxonomyEdges = sourceVertex.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
      connectVertex(cloneVertex, targetTaxonomyEdges);
    }
    
    if(cloneTargetKlasses) {
      Iterable<Edge> targetKlassEdges = sourceVertex.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TARGET_KLASSES);
      connectVertex(cloneVertex, targetKlassEdges);
    }
    
    if(!(boolean) sourceVertex.getProperty(IRole.IS_READ_ONLY))
        connectPermissionVertices(sourceVertex, cloneVertex);
  }
  
  private void setProperties(String label, String code, Vertex cloneVertex, Boolean cloneEnableDashboard)
  {
    String userLanguage = UtilClass.getLanguage().getUiLanguage();
    String key = ICreateRoleCloneModel.LABEL + Seperators.FIELD_LANG_SEPERATOR + userLanguage;

    cloneVertex.setProperty(key, label);
    
    if(!StringUtils.isEmpty(code)) {
      cloneVertex.setProperty(ICreateRoleCloneModel.CODE, code);
    }
    
    if(!cloneEnableDashboard) {
      cloneVertex.setProperty(IRole.IS_DASHBOARD_ENABLE, true);
    }
    
    cloneVertex.setProperty(IRole.IS_STANDARD, false);
  }

  private void connectVertex(Vertex cloneVertex, Iterable<Edge> edges)
  {
    for(Edge edge : edges) {
      Vertex vertex = edge.getVertex(Direction.IN);
      cloneVertex.addEdge(edge.getLabel(), vertex);
    }
  }

  private void connectIncomingVertices(Vertex sourceVertex, Vertex cloneVertex)
  {
    Iterable<Edge> incomingEdgesOfSource = sourceVertex.getEdges(Direction.IN);
    
    for(Edge incomingEdge : incomingEdgesOfSource) {
      String edgeName = incomingEdge.getLabel();
      
      if(edgeName.equals(RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN)) {
        continue;
      }
      
      Vertex incomingVertex = incomingEdge.getVertex(Direction.OUT);
      incomingVertex.addEdge(edgeName, cloneVertex);
    }
  }
  
  private Map<String, Object> prepareResponseMap(Vertex cloneVertex) throws Exception
  {
    Map<String, Object> roleMapToReturn = new HashMap<>();
    
    HashMap<String, Object> roleMap = RoleUtils.getRoleEntityMap(cloneVertex);
    
    RoleUtils.addTargetKlasses(roleMap, cloneVertex, roleMapToReturn);
    RoleUtils.addTargetTaxonomies(roleMap, cloneVertex, roleMapToReturn);
    RoleUtils.addReferencedEndPoints(roleMap, cloneVertex, roleMapToReturn);
    RoleUtils.addReferencedKPIs(roleMap, cloneVertex, roleMapToReturn);
    RoleUtils.addReferencedSystems(roleMap, cloneVertex, roleMapToReturn);
    
    roleMapToReturn.put(CommonConstants.ROLE, roleMap);
    roleMapToReturn.put("articles",
        KlassGetUtils.getKlassesListForRoles(SystemLabels.STANDARD_ARTICLE_KLASS_LABEL, VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("collections",
        KlassGetUtils.getKlassesListForRoles("collection", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("sets",
        KlassGetUtils.getKlassesListForRoles("set", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("assets", KlassGetUtils.getKlassesListForRoles("asset_asset",
        VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("collectionAssets", KlassGetUtils.getKlassesListForRoles("collection_asset",
        VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("markets",
        KlassGetUtils.getKlassesListForRoles(SystemLabels.STANDARD_TARGET_KLASS_MARKET_LABEL, VertexLabelConstants.ENTITY_TYPE_TARGET));
    roleMapToReturn.put("collectionTargets", KlassGetUtils
        .getKlassesListForRoles("collection_target", VertexLabelConstants.ENTITY_TYPE_TARGET));
    
    AuditLogUtils.fillAuditLoginfo(roleMapToReturn, cloneVertex, Entities.ROLES, Elements.UNDEFINED);
    
    return roleMapToReturn;
  }

  
}
