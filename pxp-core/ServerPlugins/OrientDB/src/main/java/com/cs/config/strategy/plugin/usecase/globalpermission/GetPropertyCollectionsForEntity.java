package com.cs.config.strategy.plugin.usecase.globalpermission;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermissionsForRole;
import com.cs.core.config.interactor.model.globalpermissions.IGetPropertyCollectionsForEntityModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.Map;

public class GetPropertyCollectionsForEntity extends AbstractOrientPlugin {
  
  public GetPropertyCollectionsForEntity(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> globalPermissionMap = (HashMap<String, Object>) map
        .get("globalPermissions");
    
    String roleId = (String) globalPermissionMap.get(IGetPropertyCollectionsForEntityModel.ID);
    /*      List<String> entityIds = (List<String>) globalPermissionMap
        .get(IGetPropertyCollectionsForEntityModel.ENTITY_IDS);
    String type = (String) globalPermissionMap.get(IGetPropertyCollectionsForEntityModel.TYPE);
    
    ResponseCarrier.successResponse(iResponse,
        GlobalPermissionUtils.getPropertyCollectionsPermissions(roleId, entityIds, type));*/
    
    String query = "SELECT FROM " + VertexLabelConstants.PROPERTY_COLLECTION + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> iterator = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    Map<String, Object> propertyCollectionIdPermissionMap = new HashMap<String, Object>();
    
    for (Vertex propertyCollectionNode : iterator) {
      String propertyCollectionId = UtilClass.getCodeNew(propertyCollectionNode);
      Map<String, Object> propertyCollectionPermission = GlobalPermissionUtils
          .getPropertyCollectionPermission(propertyCollectionId, roleId);
      propertyCollectionIdPermissionMap.put(propertyCollectionId, propertyCollectionPermission);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IGlobalPermissionsForRole.ID, roleId);
    returnMap.put(IGlobalPermissionsForRole.PROPERTY_COLLECTION_PERMISSIONS,
        propertyCollectionIdPermissionMap);
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    
    return new String[] { "POST|GetPropertyCollectionsForEntity/*" };
  }
}
