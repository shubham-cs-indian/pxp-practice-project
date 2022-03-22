package com.cs.config.strategy.plugin.usecase.asset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetShareDialogInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IIdLabelCodeDownloadPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

/***
 * This plug-in will return the config information for link sharing dialog.
 * 
 * @author vannya.kalani
 *
 */
public class GetAssetShareDialogInformation extends AbstractOrientPlugin {
  
  public GetAssetShareDialogInformation(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAssetShareDialogInformation/*" };
  }
  
  protected static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      CommonConstants.LABEL_PROPERTY, IKlass.IS_NATURE);
  
  @SuppressWarnings({ "unchecked" })
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> response = new HashMap<>();
    Set<Map<String, Object>> returnSetForVariantAssets = new HashSet<>();
    Set<Map<String, Object>> returnSetForMasterAssets = new HashSet<>();
    
    List<String> klassTypes = (List<String>) requestMap.get(IIdsListWithUserModel.IDS);
    List<String> natureKlassTypes = new ArrayList<>();
    String userId = (String) requestMap.get(IIdsListWithUserModel.USER_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    
    Iterable<Vertex> masterVerticesIterable = UtilClass.getVerticesByIds(klassTypes, VertexLabelConstants.ENTITY_TYPE_ASSET);
    Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils.getKlassIdsHavingReadPermission(userInRole);
    
    for (Vertex masterVertex : masterVerticesIterable) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(fieldsToFetch, masterVertex);
      if ((boolean) entityMap.get(IKlass.IS_NATURE)) {
        String code = (String) entityMap.get(CommonConstants.CODE_PROPERTY);
        natureKlassTypes.add(code);
        returnSetForMasterAssets.add(createResponseMap(entityMap, klassIdsHavingReadPermission, userId));
      }
    }
    
    //get tivs classifier codes according to nature classifier codes
    String klassTypeString = EntityUtil.quoteIt(natureKlassTypes);
    String query = "SELECT FROM (SELECT expand(" + Direction.OUT + "('" + RelationshipLabelConstants.HAS_CONTEXT_KLASS 
        + "')) FROM " + VertexLabelConstants.ENTITY_TYPE_ASSET + " WHERE " + CommonConstants.CODE_PROPERTY + " IN " 
        + klassTypeString + " ) WHERE " + IKlass.NATURE_TYPE + " = '" + CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE + "'";
    
    Iterable<Vertex> technicalVariantVerticesIterable = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex techVertex : technicalVariantVerticesIterable) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(fieldsToFetch, techVertex);
      returnSetForVariantAssets.add(createResponseMap(entityMap, klassIdsHavingReadPermission, userId));
    }
    
    response.put(IAssetShareDialogInformationModel.TECHNICAL_VARIANT_TYPE_IDS_LIST, new ArrayList<>(returnSetForVariantAssets));
    response.put(IAssetShareDialogInformationModel.MASTER_ASSET_TYPE_IDS_LIST, new ArrayList<>(returnSetForMasterAssets));
    return response;
  }
  
  protected Map<String, Object> createResponseMap(Map<String, Object> entityMap,
      Set<String> klassIdsHavingReadPermission, String userId) throws Exception
  {
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    String roleId = UtilClass.getCodeNew(userInRole);
    Map<String, Object> responseMap = new HashMap<>();
    String code = (String) entityMap.get(CommonConstants.CODE_PROPERTY);
    
    responseMap.put(IIdLabelCodeDownloadPermissionModel.ID, code);
    responseMap.put(IIdLabelCodeDownloadPermissionModel.LABEL, entityMap.get(CommonConstants.LABEL_PROPERTY));
    responseMap.put(IIdLabelCodeDownloadPermissionModel.CODE, code);
    
    if (klassIdsHavingReadPermission.isEmpty() || klassIdsHavingReadPermission.contains(code)) {
      Map<String, Object> permissionMap = GlobalPermissionUtils.getKlassAndTaxonomyPermission(code, roleId);
      responseMap.put(IIdLabelCodeDownloadPermissionModel.CAN_DOWNLOAD,
          (Boolean) permissionMap.get(IGlobalPermissionModel.CAN_DOWNLOAD));
    }
    else {
      responseMap.put(IIdLabelCodeDownloadPermissionModel.CAN_DOWNLOAD, false);
    }
    return responseMap;
  }
}
