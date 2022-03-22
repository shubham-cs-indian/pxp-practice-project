package com.cs.config.strategy.plugin.usecase.asset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.config.interactor.model.asset.ISaveDAMConfigurationRequestModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

/**
 * Save DAM configuration OrientDB plugin.
 * @author pranav.huchche
 *
 */
public class SaveDAMConfiguration extends AbstractOrientPlugin {
  
  public SaveDAMConfiguration(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveDAMConfiguration/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    boolean shouldDownloadAssetWithOriginalFilename = (boolean) requestMap.get(ISaveDAMConfigurationRequestModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME);
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> assetMap = null;
    Vertex assetVertex = null;
    
    // Get Asset Node from Orient
    try {
      assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET, VertexLabelConstants.ENTITY_TYPE_ASSET);
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    
    // Convert to map and put configuration part
    if(assetVertex != null) {
      assetMap = UtilClass.getMapFromNode(assetVertex);
      assetMap.put(IDAMConfigurationDetailsResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME, shouldDownloadAssetWithOriginalFilename);
    }
    
    // Save node in Orient
    if(assetMap != null) {
      UtilClass.saveNode(assetMap, assetVertex, Arrays.asList());
    }
    UtilClass.getGraph().commit();
    
    // Fill return map
    returnMap.put(IDAMConfigurationDetailsResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME,
        assetVertex.getProperty(IDAMConfigurationDetailsResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME));
    return returnMap;
  }
  
}
