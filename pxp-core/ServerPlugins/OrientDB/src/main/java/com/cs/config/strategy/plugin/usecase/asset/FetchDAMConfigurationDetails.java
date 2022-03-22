package com.cs.config.strategy.plugin.usecase.asset;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.asset.IDAMConfigurationDetailsResponseModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;


public class FetchDAMConfigurationDetails extends AbstractOrientPlugin {
  
  public FetchDAMConfigurationDetails(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|FetchDAMConfigurationDetails/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Vertex assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET, VertexLabelConstants.ENTITY_TYPE_ASSET);
    returnMap.put(IDAMConfigurationDetailsResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME,
        assetVertex.getProperty(IDAMConfigurationDetailsResponseModel.SHOULD_DOWNLOAD_ASSET_WITH_ORIGINAL_FILENAME));
    return returnMap;
  }
}
