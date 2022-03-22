package com.cs.config.strategy.plugin.usecase.downloadtracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.asset.services.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.downloadtracker.IGetConfigInformationForDownloadTrackerRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetConfigInformationForDownloadTracker extends AbstractOrientPlugin {
  
  public static final List<String> fieldsToFetch        = Arrays.asList(CommonConstants.ID_PROPERTY, CommonConstants.LABEL_PROPERTY,
      CommonConstants.CODE_PROPERTY);
  
  public GetConfigInformationForDownloadTracker(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigInformationForDownloadTracker/*" };
  }
  
  @Override
  @SuppressWarnings("unchecked")
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> responseList = new ArrayList<>();
    List<String> klassIds = (List<String>) requestMap.get(IGetConfigInformationForDownloadTrackerRequestModel.ASSET_CLASS_IDS);
    Iterable<Vertex> assetVertices = UtilClass.getVerticesByIds(klassIds, VertexLabelConstants.ENTITY_TYPE_ASSET);
    for (Vertex assetNode : assetVertices) {
      Map<String, Object> assetMap = UtilClass.getMapFromVertex(fieldsToFetch, assetNode);
      Map<String, Object> responseMap = new HashMap<>();
      responseMap.put(IIdLabelModel.ID, assetMap.get(CommonConstants.ID_PROPERTY));
      String label = ((String) assetMap.get(CommonConstants.LABEL_PROPERTY)).isEmpty()
          ? (String) assetMap.get(CommonConstants.CODE_PROPERTY)
          : (String) assetMap.get(CommonConstants.LABEL_PROPERTY);
      responseMap.put(IIdLabelModel.LABEL, label);
      responseList.add(responseMap);
    }
    
    Map<String, Object> response = new HashMap<>();
    response.put(IListModel.LIST, responseList);
    return response;
  }
  
}
