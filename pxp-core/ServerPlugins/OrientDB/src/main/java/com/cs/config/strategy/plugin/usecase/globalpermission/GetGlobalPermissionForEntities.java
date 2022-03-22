package com.cs.config.strategy.plugin.usecase.globalpermission;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.globalpermissions.IGetGlobalPermissionForEntitiesModel;
import com.cs.core.config.interactor.model.globalpermissions.IIdLabelWithGlobalPermissionModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetGlobalPermissionForEntities extends AbstractOrientPlugin {
  
  public GetGlobalPermissionForEntities(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetGlobalPermissionForEntities/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    
    OrientGraph graph = UtilClass.getGraph();
    String roleId = (String) requestMap.get("roleId");
    Map<String, Object> mapToReturn = getMapToReturn();
    
    Vertex roleNode = UtilClass.getVertexById(roleId, VertexLabelConstants.ENTITY_TYPE_ROLE);
    Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
    
    Iterable<Vertex> targetKlasses = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_KLASSES);
    Boolean isTargetKlassesExists = false;
    
    for (Vertex klassNode : targetKlasses) {
      fillKlassPermissions(mapToReturn, roleId, klassNode);
      isTargetKlassesExists = true;
    }
    
    if (!isTargetKlassesExists) {
      targetKlasses = organizationNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_AVAILABLE_KLASSES);
      for (Vertex klassNode : targetKlasses) {
        fillKlassPermissions(mapToReturn, roleId, klassNode);
        isTargetKlassesExists = true;
      }
    }
    
    if (!isTargetKlassesExists) {
      String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where "
          + CommonConstants.ORIENTDB_CLASS_PROPERTY + " not in "
          + EntityUtil.quoteIt(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
      Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex klassNode : iterable) {
        fillKlassPermissions(mapToReturn, roleId, klassNode);
      }
    }
    
    Iterable<Vertex> targetTaxonomies = roleNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TARGET_TAXONOMIES);
    
    for (Vertex targetTaxonomy : targetTaxonomies) {
      fillTaxonomyPermissions(mapToReturn, roleId, targetTaxonomy);
    }
    
    if (((List<Map<String, Object>>) mapToReturn
        .get(IGetGlobalPermissionForEntitiesModel.TAXONOMIES)).isEmpty()) {
      targetTaxonomies = organizationNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_AVAILABLE_TAXONOMIES);
      
      for (Vertex targetTaxonomy : targetTaxonomies) {
        fillTaxonomyPermissions(mapToReturn, roleId, targetTaxonomy);
      }
    }
    if (((List<Map<String, Object>>) mapToReturn
        .get(IGetGlobalPermissionForEntitiesModel.TAXONOMIES)).isEmpty()) {
      Iterable<Vertex> taxonomyIterable = graph
          .command(new OCommandSQL("select from " + VertexLabelConstants.ROOT_KLASS_TAXONOMY
              + " where outE('Child_Of').size() = 0 order by "
              + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
          .execute();
      for (Vertex taxonomyNode : taxonomyIterable) {
        fillTaxonomyPermissions(mapToReturn, roleId, taxonomyNode);
      }
    }
    
    Iterable<Vertex> taskIterable = graph
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_TASK))
        .execute();
    for (Vertex taskNode : taskIterable) {
      fillTaskPermissions(mapToReturn, roleId, taskNode);
    }
    
    return mapToReturn;
  }
  
  private Map<String, Object> getMapToReturn()
  {
    Map<String, Object> returnMap = new HashMap<>();
    
    returnMap.put(IGetGlobalPermissionForEntitiesModel.ARTICLES, new ArrayList<>());
    returnMap.put(IGetGlobalPermissionForEntitiesModel.ASSETS, new ArrayList<>());
    returnMap.put(IGetGlobalPermissionForEntitiesModel.MARKETS, new ArrayList<>());
    returnMap.put(IGetGlobalPermissionForEntitiesModel.SUPPLIERS, new ArrayList<>());
    returnMap.put(IGetGlobalPermissionForEntitiesModel.TEXT_ASSETS, new ArrayList<>());
    returnMap.put(IGetGlobalPermissionForEntitiesModel.TAXONOMIES, new ArrayList<>());
    returnMap.put(IGetGlobalPermissionForEntitiesModel.TASKS, new ArrayList<>());
    
    return returnMap;
  }
  
  private void fillKlassPermissions(Map<String, Object> mapToReturn, String roleId,
      Vertex klassNode) throws Exception
  {
    String klass = klassNode.getProperty("@class");
    String id = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String label = (String) UtilClass.getValueByLanguage(klassNode, CommonConstants.LABEL_PROPERTY);
    Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
    
    Map<String, Object> entityPermissionMap = new HashMap<>();
    
    Map<String, Object> globalPermission = GlobalPermissionUtils.getKlassAndTaxonomyPermission(id,
        roleId);
    entityPermissionMap.put(IIdLabelWithGlobalPermissionModel.ID, id);
    entityPermissionMap.put(IIdLabelWithGlobalPermissionModel.LABEL, label);
    entityPermissionMap.put(IIdLabelWithGlobalPermissionModel.IS_NATURE, isNature);
    entityPermissionMap.put(IIdLabelWithGlobalPermissionModel.GLOBAL_PERMISSION, globalPermission);
    
    switch (klass) {
      case "Klass":
        ((List<Map<String, Object>>) mapToReturn.get(IGetGlobalPermissionForEntitiesModel.ARTICLES))
            .add(entityPermissionMap);
        break;
      
      case "Asset":
        ((List<Map<String, Object>>) mapToReturn.get(IGetGlobalPermissionForEntitiesModel.ASSETS))
            .add(entityPermissionMap);
        break;
      
      case "Market":
        ((List<Map<String, Object>>) mapToReturn.get(IGetGlobalPermissionForEntitiesModel.MARKETS))
            .add(entityPermissionMap);
        break;
      
      case "SupplierKlass":
        ((List<Map<String, Object>>) mapToReturn
            .get(IGetGlobalPermissionForEntitiesModel.SUPPLIERS)).add(entityPermissionMap);
        break;
      
      case "Target":
        String type = klassNode.getProperty(CommonConstants.TYPE_PROPERTY);
        if (type.equals(Constants.MARKET_KLASS_TYPE)) {
          ((List<Map<String, Object>>) mapToReturn
              .get(IGetGlobalPermissionForEntitiesModel.MARKETS)).add(entityPermissionMap);
        }
        break;
      
      case "TextAsset":
        ((List<Map<String, Object>>) mapToReturn
            .get(IGetGlobalPermissionForEntitiesModel.TEXT_ASSETS)).add(entityPermissionMap);
        ;
        break;
    }
  }
  
  private void fillTaxonomyPermissions(Map<String, Object> mapToReturn, String roleId,
      Vertex taxonomyNode) throws Exception
  {
    Map<String, Object> taxonomyPermissionMap = new HashMap<>();
    String taxonomyId = UtilClass.getCodeNew(taxonomyNode);
    Map<String, Object> globalPermission = GlobalPermissionUtils
        .getKlassAndTaxonomyPermission(taxonomyId, roleId);
    taxonomyPermissionMap.put(IIdLabelWithGlobalPermissionModel.ID, taxonomyId);
    String label = (String) UtilClass.getValueByLanguage(taxonomyNode,
        CommonConstants.LABEL_PROPERTY);
    taxonomyPermissionMap.put(IIdLabelWithGlobalPermissionModel.LABEL, label);
    taxonomyPermissionMap.put(IIdLabelWithGlobalPermissionModel.GLOBAL_PERMISSION,
        globalPermission);
    ((List<Map<String, Object>>) mapToReturn.get(IGetGlobalPermissionForEntitiesModel.TAXONOMIES))
        .add(taxonomyPermissionMap);
  }
  
  private void fillTaskPermissions(Map<String, Object> mapToReturn, String roleId, Vertex taskNode)
      throws Exception
  {
    Map<String, Object> taskPermissionMap = new HashMap<>();
    String taskId = UtilClass.getCodeNew(taskNode);
    Map<String, Object> globalPermission = GlobalPermissionUtils
        .getKlassAndTaxonomyPermission(taskId, roleId);
    taskPermissionMap.put(IIdLabelWithGlobalPermissionModel.ID, taskId);
    String label = (String) UtilClass.getValueByLanguage(taskNode, CommonConstants.LABEL_PROPERTY);
    taskPermissionMap.put(IIdLabelWithGlobalPermissionModel.LABEL, label);
    taskPermissionMap.put(IIdLabelWithGlobalPermissionModel.GLOBAL_PERMISSION, globalPermission);
    ((List<Map<String, Object>>) mapToReturn.get(IGetGlobalPermissionForEntitiesModel.TASKS))
        .add(taskPermissionMap);
  }
}
