package com.cs.config.strategy.plugin.usecase.permission;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.assetinstance.ITypesAndCoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.IUserIDAndTypesAndCoverFlowModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCoverFlowForNatureKlassWithDownloadPermission extends AbstractOrientPlugin {
  
  public GetCoverFlowForNatureKlassWithDownloadPermission(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetCoverFlowForNatureKlassWithDownloadPermission/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> coverFlowModelReturnList = new ArrayList<>();
    List<Map<String, Object>> typesAndCoverFlowModelList = (List<Map<String, Object>>) requestMap
        .get(IUserIDAndTypesAndCoverFlowModel.TYPES_AND_COVERFLOW_MODEL_LIST);
    String loginUserId = (String) requestMap.get(IUserIDAndTypesAndCoverFlowModel.USER_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(loginUserId);
    String roleId = UtilClass.getCodeNew(userInRole);
    
    for (Map<String, Object> request : typesAndCoverFlowModelList) {
      List<String> klassIds = (List<String>) request.get(ITypesAndCoverFlowModel.TYPES);
      Map<String, Object> coverFlowModel = (Map<String, Object>) request
          .get(ITypesAndCoverFlowModel.COVER_FLOW_MODEL);
      Vertex natureKlassNode = getNatureKlassNode(klassIds);
      if (natureKlassNode == null) {
        throw new KlassNotFoundException();
      }
      String natureKlassIds = UtilClass.getCodeNew(natureKlassNode);
      Map<String, Object> permissionMap = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(natureKlassIds, roleId);
      if ((Boolean) permissionMap.get(IGlobalPermissionModel.CAN_DOWNLOAD)) {
        coverFlowModelReturnList.add(coverFlowModel);
      }
    }
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IListModel.LIST, coverFlowModelReturnList);
    return returnMap;
  }
  
  private Vertex getNatureKlassNode(List<String> klassIds) throws Exception
  {
    for (String klassId : klassIds) {
      Vertex klassNode = UtilClass.getVertexById(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Boolean isNature = klassNode.getProperty(IKlass.IS_NATURE);
      if (isNature) {
        return klassNode;
      }
    }
    return null;
  }
}
