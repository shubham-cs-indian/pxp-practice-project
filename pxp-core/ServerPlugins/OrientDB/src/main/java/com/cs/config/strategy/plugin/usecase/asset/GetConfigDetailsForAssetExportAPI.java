package com.cs.config.strategy.plugin.usecase.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.asset.IAssetExportAPIRequestModel;
import com.cs.core.config.interactor.model.asset.IAssetExportAPIResponseModel;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigDetailsForAssetExportAPI extends AbstractOrientPlugin {
  
  public GetConfigDetailsForAssetExportAPI(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForAssetExportAPI/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String endpointCode = (String) requestMap.get(IAssetExportAPIRequestModel.ENDPOINT_CODE);
    String organizationCode = (String) requestMap.get(IAssetExportAPIRequestModel.ORAGANIZATION_CODE);
    String languageCode = (String) requestMap.get(IAssetExportAPIRequestModel.LANGUAGE_CODE);
  
    Map<String, Object> returnMap = new HashMap<>();
    Vertex assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET, VertexLabelConstants.ENTITY_TYPE_ASSET);
    returnMap.put(IDAMConfigurationDetailsResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME,
        assetVertex.getProperty(IDAMConfigurationDetailsResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME));
    
    if (!endpointCode.isEmpty()) {
      try {
        Vertex endpointNode = UtilClass.getVertexByIndexedId(endpointCode, VertexLabelConstants.ENDPOINT);
        Map<String, Object> endpointMap = UtilClass.getMapFromVertex(new ArrayList<>(), endpointNode);
        returnMap.put(IGetEndpointForGridModel.ENDPOINT, endpointMap);
      }
      catch (NotFoundException e) {
        returnMap.put(IAssetExportAPIResponseModel.IS_END_POINT_CODE_VALID, false);
      }
    }
   
    if (organizationCode != null && !organizationCode.isEmpty()) {
      try {
        UtilClass.getVertexById(organizationCode, VertexLabelConstants.ORGANIZATION);
      }
      catch (NotFoundException e) {
        returnMap.put(IAssetExportAPIResponseModel.IS_ORGANIZATION_CODE_VALID, false);
      }
    }
    
    if (languageCode != null && !languageCode.isEmpty()) {
      try {
        UtilClass.getVertexById(languageCode, VertexLabelConstants.LANGUAGE);
      }
      catch (NotFoundException e) {
        returnMap.put(IAssetExportAPIResponseModel.IS_LANGUAGE_CODE_VALID, false);
      }
    }
    
    return returnMap;
  }
}
