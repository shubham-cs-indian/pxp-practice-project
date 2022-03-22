package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetRequestModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class GetConfigDetailsForInstanceTreeGet extends AbstractGetConfigDetailsForInstanceTree {
  
  public GetConfigDetailsForInstanceTreeGet(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForInstanceTreeGet/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graphDB = UtilClass.getGraph();
    String userId = (String) requestMap.get(IConfigDetailsForInstanceTreeGetRequestModel.USER_ID);
    Boolean isCalendarView = (Boolean) requestMap
        .get(IConfigDetailsForInstanceTreeGetRequestModel.IS_CALENDAR_VIEW);
    List<String> idsToReturn = new ArrayList<>();
    Map<String, Object> mapToReturn = new HashMap<>();
    Vertex roleNode = RoleUtils.getRoleFromUser(userId);
    if (isCalendarView) {
      // Get all roles & pass to GlobalPermissionUtil to get task class Ids that
      Set<Vertex> roles = new HashSet<Vertex>();
      Iterable<Vertex> roleVertices = graphDB
          .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROLE);
      for (Vertex roleVertex : roleVertices) {
        roles.add(roleVertex);
      }
      fillTaskPermissionRelatedDetails(mapToReturn, roleNode, roles);
    }
    
    Map<String, Object> xRayConfigDetails = MultiClassificationUtils.getXRayConfigDetails(
        (List<String>) requestMap
            .get(IConfigDetailsForInstanceTreeGetRequestModel.X_RAY_ATTRIBUTES),
        (List<String>) requestMap.get(IConfigDetailsForInstanceTreeGetRequestModel.X_RAY_TAGS));
    
    Set<String> dimensionalTagIds = getDimensionalTagIds();
    
    managePermissionDetailsForInstanceTree(userId, mapToReturn);
    ConfigDetailsUtils.fillSide2LinkedVariantKrIds(mapToReturn);
    String kpiId = (String) requestMap.get(IConfigDetailsForInstanceTreeGetRequestModel.KPI_ID);
    fillKPIInformation(kpiId, mapToReturn);
    
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.MASTER_KLASS_IDS, idsToReturn);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.DIMENSIONAL_TAG_IDS, dimensionalTagIds);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.X_RAY_CONFIG_DETAILS, xRayConfigDetails);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.ROLE_ID_OF_CURRENT_USER,
        UtilClass.getCodeNew(roleNode));
    return mapToReturn;
  }
  
  private void fillKPIInformation(String kpiId, Map<String, Object> mapToReturn) throws Exception
  {
    if (kpiId == null || kpiId.isEmpty()) {
      return;
    }
    Vertex kpiNode = UtilClass.getVertexById(kpiId, VertexLabelConstants.GOVERNANCE_RULE_KPI);
    
    List<String> klassIds = new ArrayList<String>();
    Iterable<Vertex> klassVertices = kpiNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.LINK_KLASS);
    for (Vertex klassNode : klassVertices) {
      String klassId = UtilClass.getCodeNew(klassNode);
      klassIds.add(klassId);
    }
    
    List<String> taxonomyIds = new ArrayList<String>();
    Iterable<Vertex> taxonomyVertices = kpiNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.LINK_TAXONOMY);
    for (Vertex taxonomyNode : taxonomyVertices) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyNode);
      taxonomyIds.add(taxonomyId);
    }
    
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.KLASS_IDS_FOR_KPI, klassIds);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TAXONOMY_IDS_FOR_KPI, taxonomyIds);
  }
  
  private void managePermissionDetails(String userId, Map<String, Object> mapToReturn)
      throws Exception
  {
    Vertex userNode = null;
    try {
      userNode = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    }
    catch (NotFoundException e) {
      throw new UserNotFoundException();
    }
    
    Set<String> allowedEntities = new HashSet<>();
    Set<String> klassIdsHavingRP = new HashSet<>();
    Set<String> klassIdsHavingCP = new HashSet<>();
    Set<String> taxonomyIdsHavingRP = new HashSet<>();
    Iterable<Vertex> roleNodes = userNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_USER_IN);
    for (Vertex roleNode : roleNodes) {
      
      List<String> roleEntities = (List<String>) (roleNode.getProperty(IRole.ENTITIES));
      allowedEntities.addAll(roleEntities);
      
      List<String> endPoints = (List<String>) roleNode.getProperty(IRole.ENDPOINTS);
      
      if (allowedEntities.isEmpty()) {
        allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
        allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
      }
      // Sending FileInstance module every time irrespective of user
      // configuration for permissions
      // inside roles.
      else if (!allowedEntities.contains(CommonConstants.FILE_INSTANCE_MODULE_ENTITY)) {
        allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
      }
      
      fillIdsHavingReadPermission(taxonomyIdsHavingRP, klassIdsHavingRP, roleNode);
      fillKlassIdsHavingCreatePermission(klassIdsHavingRP, klassIdsHavingCP, roleNode);
    }
    
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.ALLOWED_ENTITIES, allowedEntities);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.KLASS_IDS_HAVING_RP, klassIdsHavingRP);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.KLASS_IDS_HAVING_CP, klassIdsHavingCP);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TAXONOMY_IDS_HAVING_RP,
        taxonomyIdsHavingRP);
  }
  
  private void fillKlassIdsHavingCreatePermission(Set<String> klassIdsHavingRP,
      Set<String> klassIdsHavingCP, Vertex roleNode) throws Exception
  {
    String roleId = UtilClass.getCodeNew(roleNode);
    List<String> klassIds = new ArrayList<>(klassIdsHavingRP);
    if (klassIds.isEmpty()) {
      String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where "
          + IKlass.IS_NATURE + " is not null and " + IKlass.IS_NATURE + " = true and "
          + IKlass.IS_DEFAULT_CHILD + " = true";
      
      Iterable<Vertex> vertices = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : vertices) {
        klassIds.add(UtilClass.getCodeNew(klassNode));
      }
    }
    for (String klassId : klassIds) {
      Map<String, Object> klassAndTaxonomyPermission = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(klassId, roleId);
      Boolean canCreate = (Boolean) klassAndTaxonomyPermission.get(IGlobalPermission.CAN_CREATE);
      if (canCreate) {
        klassIdsHavingCP.add(klassId);
      }
    }
  }
  
  private void fillIdsHavingReadPermission(Set<String> taxonomyIdsHavingRP,
      Set<String> klassIdsHavingRP, Vertex roleNode) throws Exception
  {
    klassIdsHavingRP.addAll(GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode));
    taxonomyIdsHavingRP.addAll(GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(roleNode));
  }
  
  private Set<String> getDimensionalTagIds()
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> i = graph
        .command(new OCommandSQL(
            "select code from tag where " + ITag.IS_ROOT + " = true and shouldDisplay=true"))
        .execute();
    Set<String> dimensionalTagIds = new HashSet<>();
    for (Vertex tagNode : i) {
      dimensionalTagIds.add(tagNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    return dimensionalTagIds;
  }
  
  protected void fillTaskPermissionRelatedDetails(Map<String, Object> mapToReturn,
      Vertex roleContainingLoginUser, Set<Vertex> roles) throws Exception
  {
    Map<String, Object> taskIdsForRoleHavingReadPermission = new HashMap<>();
    Set<String> taskIdsHavingReadPermission = new HashSet<>();
    Set<String> personalTaskIds = new HashSet<>();
    
    Iterable<Vertex> taskVertices = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex vertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(vertex);
      if ((vertex.getProperty(ITask.TYPE)).equals(CommonConstants.TASK_TYPE_PERSONAL)) {
        personalTaskIds.add(taskId);
      }
      for (Vertex role : roles) {
        String roleId = UtilClass.getCodeNew(role);
        Map<String, Object> globalPermissions = GlobalPermissionUtils
            .getKlassAndTaxonomyPermission(taskId, roleId);
        List<String> taskIdForRoleHavingReadPermission = (List<String>) taskIdsForRoleHavingReadPermission
            .get(roleId);
        if (taskIdForRoleHavingReadPermission == null) {
          taskIdForRoleHavingReadPermission = new ArrayList<>();
          taskIdsForRoleHavingReadPermission.put(roleId, taskIdForRoleHavingReadPermission);
        }
        if ((Boolean) globalPermissions.get(IGlobalPermission.CAN_READ)) {
          taskIdForRoleHavingReadPermission.add(taskId);
          if (roleContainingLoginUser.equals(role)) {
            taskIdsHavingReadPermission.add(taskId);
          }
        }
      }
    }
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION,
        taskIdsForRoleHavingReadPermission);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TASK_IDS_HAVING_READ_PERMISSION,
        taskIdsHavingReadPermission);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.PERSONAL_TASK_IDS, personalTaskIds);
  }
}
