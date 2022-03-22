package com.cs.config.strategy.plugin.usecase.downloadtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.dam.config.interactor.model.downloadtracker.IDownloadTrackerConfigurationResponseModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;


public class GetDownloadTrackerConfiguration extends AbstractOrientPlugin {
  
  public GetDownloadTrackerConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDownloadTrackerConfiguration/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> responseList = new ArrayList<>();
    List<String> klassIds = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    for (String klassId : klassIds) {
      Vertex assetNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ASSET);
      Map<String, Object> assetMap = UtilClass.getMapFromVertex(
          Arrays.asList(IAssetModel.TRACK_DOWNLOADS, CommonConstants.CODE_PROPERTY), assetNode);
      Map<String, Object> responseMap = new HashMap<>();
      if (assetMap.get(IAssetModel.TRACK_DOWNLOADS) != null) {
        responseMap.put(IDownloadTrackerConfigurationResponseModel.IS_DOWNLOAD_TRACKER_ENABLED,
            assetMap.get(IAssetModel.TRACK_DOWNLOADS));
      }
      responseMap.put(IDownloadTrackerConfigurationResponseModel.KLASS_ID, klassId);
      responseMap.put(IDownloadTrackerConfigurationResponseModel.KLASS_CODE,
          assetMap.get(CommonConstants.CODE_PROPERTY));
      responseList.add(responseMap);
    }
    Map<String, Object> response = new HashMap<>();
    response.put(IListModel.LIST, responseList);
    return response;
  }
  
}
