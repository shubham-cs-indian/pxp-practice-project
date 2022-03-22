package com.cs.config.strategy.plugin.usecase.configdetails.instance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForInstanceTree extends AbstractOrientPlugin {
  
  public AbstractGetConfigDetailsForInstanceTree(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected void managePermissionDetailsForInstanceTree(String userId,
      Map<String, Object> mapToReturn) throws Exception
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
      
      if (allowedEntities.isEmpty()) {
        allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
        allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
      }
      
      if (!allowedEntities.contains(CommonConstants.FILE_INSTANCE_MODULE_ENTITY)) {
        allowedEntities.add(CommonConstants.FILE_INSTANCE_MODULE_ENTITY);
      }
      
      fillIdsHavingReadPermission(taxonomyIdsHavingRP, klassIdsHavingRP, roleNode);
      fillKlassIdsHavingCreatePermission(klassIdsHavingRP, klassIdsHavingCP, roleNode);

    }
    List<String> majorTaxonomyIds = GlobalPermissionUtils.getRootMajorTaxonomyIds();
    ConfigDetailsUtils.fillLinkedVariantPropertyCodes(mapToReturn);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.ALLOWED_ENTITIES, allowedEntities);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.KLASS_IDS_HAVING_RP, klassIdsHavingRP);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.KLASS_IDS_HAVING_CP, klassIdsHavingCP);
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.TAXONOMY_IDS_HAVING_RP,
        taxonomyIdsHavingRP);
    mapToReturn.put(IConfigDetailsForGetNewInstanceTreeModel.MAJOR_TAXONOMY_IDS, majorTaxonomyIds);
  }
  
  private void fillIdsHavingReadPermission(Set<String> taxonomyIdsHavingRP,
      Set<String> klassIdsHavingRP, Vertex roleNode) throws Exception
  {
    klassIdsHavingRP.addAll(GlobalPermissionUtils.getKlassIdsHavingReadPermission(roleNode));
    taxonomyIdsHavingRP.addAll(GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(roleNode));
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
}
