package com.cs.config.strategy.plugin.usecase.asset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.asset.IGetAssetExtensionsModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class GetAllAssetExtensions extends AbstractOrientPlugin {
  
  public GetAllAssetExtensions(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllAssetExtensions/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, List<String>> assetExtensions = new HashMap<>();
    
    List<String> assetTypeList = (List<String>) requestMap.get(IIdsListParameterModel.IDS) == null
        ? new ArrayList<String>()
        : (List<String>) requestMap.get(IIdsListParameterModel.IDS);
    
    if (assetTypeList.isEmpty()) {
      assetTypeList.add(SystemLevelIds.IMAGE);
      assetTypeList.add(SystemLevelIds.VIDEO);
      assetTypeList.add(SystemLevelIds.DOCUMENT);
    }
    
    for (String assetType : assetTypeList) {
      Vertex assetNode = UtilClass.getVertexById(assetType, VertexLabelConstants.ENTITY_TYPE_ASSET);
      AssetUtils.getExtensionsByType(assetNode, assetExtensions, assetType);
    }
    
    if (!assetExtensions.entrySet()
        .isEmpty()) {
      List<String> allAssetList = new ArrayList<>();
      for (Entry<String, List<String>> entry : assetExtensions.entrySet()) {
        List<String> extensionList = (List<String>) entry.getValue();
        for (String extension : extensionList) {
          allAssetList.add(extension);
        }
      }
      assetExtensions.put("allExtensions", allAssetList);
    }
    
    Map<String, Map<String, List<String>>> response = new HashMap<>();
    response.put(IGetAssetExtensionsModel.ASSET_EXTENSIONS, assetExtensions);
    
    return response;
  }
}
