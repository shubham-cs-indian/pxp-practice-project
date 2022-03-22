package com.cs.config.strategy.plugin.usecase.role.abstrct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.exception.organization.OrganizationNotFoundException;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public abstract class AbstractCreateRole extends AbstractOrientPlugin {
  
  public AbstractCreateRole(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public Map<String, Object> create(Map<String, Object> map, String getKey, String label)
      throws Exception
  {
    HashMap<String, Object> roleMap = new HashMap<String, Object>();
    
    HashMap<String, Object> role;
    role = (HashMap<String, Object>) map.get(getKey);
    
    OrientGraph graph = UtilClass.getGraph();
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(label,
        CommonConstants.CODE_PROPERTY);
    
    String organizationId = (String) role.remove(ICreateRoleModel.ORGANIZATION_ID);
    
    Vertex roleNode = UtilClass.createNode(role, vertexType,
        new ArrayList<>(Arrays.asList(IRole.ENDPOINTS)));
    Map<String, Object> roleMapToReturn = new HashMap<String, Object>();
    roleMap.putAll(UtilClass.getMapFromNode(roleNode));
    
    if (organizationId != null) {
      Vertex organization = null;
      try {
        organization = UtilClass.getVertexById(organizationId, VertexLabelConstants.ORGANIZATION);
      }
      catch (NotFoundException e) {
        throw new OrganizationNotFoundException();
      }
      organization.addEdge(RelationshipLabelConstants.ORGANIZATION_ROLE_LINK, roleNode);
    }
    
    roleMapToReturn.put(CommonConstants.ROLE, roleMap);
    roleMapToReturn.put("articles",
        KlassGetUtils.getKlassesListForRoles("article", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("collections",
        KlassGetUtils.getKlassesListForRoles("collection", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("sets",
        KlassGetUtils.getKlassesListForRoles("set", VertexLabelConstants.ENTITY_TYPE_KLASS));
    roleMapToReturn.put("assets", KlassGetUtils.getKlassesListForRoles("asset_asset",
        VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("collectionAssets", KlassGetUtils.getKlassesListForRoles("collection_asset",
        VertexLabelConstants.ENTITY_TYPE_ASSET));
    roleMapToReturn.put("markets",
        KlassGetUtils.getKlassesListForRoles("market", VertexLabelConstants.ENTITY_TYPE_TARGET));
    roleMapToReturn.put("collectionTargets", KlassGetUtils
        .getKlassesListForRoles("collection_target", VertexLabelConstants.ENTITY_TYPE_TARGET));
    graph.commit();
    AuditLogUtils.fillAuditLoginfo(roleMapToReturn, roleNode, Entities.ROLES, Elements.UNDEFINED);
    
    return roleMapToReturn;
  }
}
