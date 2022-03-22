package com.cs.config.strategy.plugin.usecase.asset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.globalpermissions.IGlobalPermissionModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkDownloadConfigInformationResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IIdLabelCodeDownloadPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class GetAllRenditionKlassesWithPermission extends AbstractOrientPlugin {
  
  public GetAllRenditionKlassesWithPermission(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllRenditionKlassesWithPermission/*" };
  }
  
  protected static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      CommonConstants.LABEL_PROPERTY, IKlass.IS_NATURE);
  
  @SuppressWarnings({ "unchecked" })
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> response = new HashMap<>();
    Map<String, Object> returnMapForVariantAssets = new HashMap<>();
    Map<String, Object> returnMapForMasterAssets = new HashMap<>();
    Map<String, Object> masterAssetTivKlassMap = new HashMap<>();
    
    List<String> klassTypes = (List<String>) requestMap.get(IIdsListWithUserModel.IDS);
    String userId = (String) requestMap.get(IIdsListWithUserModel.USER_ID);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Set<String> klassIdsHavingReadPermission = GlobalPermissionUtils.getKlassIdsHavingReadPermission(userInRole);
    
    Iterable<Vertex> masterVerticesIterable = UtilClass.getVerticesByIds(klassTypes, VertexLabelConstants.ENTITY_TYPE_ASSET);
    for (Vertex masterVertex : masterVerticesIterable) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(fieldsToFetch, masterVertex);
      if ((boolean) entityMap.get(IKlass.IS_NATURE)) {
        String code = (String) entityMap.get(CommonConstants.CODE_PROPERTY);
        returnMapForMasterAssets.put(code, createResponseMap(entityMap, klassIdsHavingReadPermission, userId));
        List<String> tivKlasses = getTIVInformation(code, klassIdsHavingReadPermission, userId, returnMapForVariantAssets);
        masterAssetTivKlassMap.put(code, tivKlasses);
      }
    }
    
    Vertex assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET, VertexLabelConstants.ENTITY_TYPE_ASSET);
    
    response.put(IBulkDownloadConfigInformationResponseModel.MASTER_ASSET_KLASS_INFORMATION, returnMapForMasterAssets);
    response.put(IBulkDownloadConfigInformationResponseModel.TIV_ASSET_KLASS_INFORMATION, returnMapForVariantAssets);
    response.put(IBulkDownloadConfigInformationResponseModel.MASTER_ASSET_TIV_KLASS_MAP, masterAssetTivKlassMap);
    response.put(IBulkDownloadConfigInformationResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME,
        assetVertex.getProperty(IAsset.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME));
    return response;
  }
  
  private List<String> getTIVInformation(String code, Set<String> klassIdsHavingReadPermission,
      String userId, Map<String, Object> returnMapForVariantAssets) throws Exception
  {
    List<String> tivKlasses = new ArrayList<>();
    String query = "SELECT FROM (SELECT expand(" + Direction.OUT + "('"+ RelationshipLabelConstants.HAS_CONTEXT_KLASS 
        + "')) FROM " + VertexLabelConstants.ENTITY_TYPE_ASSET + " WHERE " + CommonConstants.CODE_PROPERTY
        + " = '" + code + "' ) WHERE " + IKlass.NATURE_TYPE + " = '" + CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE + "'";
    
    Iterable<Vertex> technicalVariantVerticesIterable = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    
    for (Vertex techVertex : technicalVariantVerticesIterable) {
      Map<String, Object> entityMap = UtilClass.getMapFromVertex(fieldsToFetch, techVertex);
      String tivKlassCode = (String) entityMap.get(CommonConstants.CODE_PROPERTY);
      tivKlasses.add(tivKlassCode);
      if (returnMapForVariantAssets.get(tivKlassCode) == null) {
        Map<String, Object> responseMap = createResponseMap(entityMap, klassIdsHavingReadPermission, userId);
        returnMapForVariantAssets.put(tivKlassCode, responseMap);
      }
    }
    return tivKlasses;
  }

  protected Map<String, Object> createResponseMap(Map<String, Object> entityMap,
      Set<String> klassIdsHavingReadPermission, String userId) throws Exception
  {
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    String roleId = UtilClass.getCodeNew(userInRole);
    Map<String, Object> responseMap = new HashMap<>();
    String code = (String) entityMap.get(CommonConstants.CODE_PROPERTY);
    
    responseMap.put(IIdLabelCodeDownloadPermissionModel.ID, code);
    responseMap.put(IIdLabelCodeDownloadPermissionModel.CODE, code);
    responseMap.put(IIdLabelCodeDownloadPermissionModel.LABEL, entityMap.get(CommonConstants.LABEL_PROPERTY));
    
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
