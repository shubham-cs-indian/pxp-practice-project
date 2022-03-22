package com.cs.config.strategy.plugin.usecase.asset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.asset.IAssetConfigurationDetailsResponseModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

public class FetchAssetConfigurationDetails extends AbstractOrientPlugin {
  
  public FetchAssetConfigurationDetails(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> extensionConfiguration = new HashMap<>();
    Vertex assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    returnMap.put(IAssetConfigurationDetailsResponseModel.DETECT_DUPLICATE,
        assetVertex.getProperty(IAssetConfigurationDetailsResponseModel.DETECT_DUPLICATE));
    returnMap.put(IAssetConfigurationDetailsResponseModel.UPLOAD_ZIP,
        assetVertex.getProperty(IAssetConfigurationDetailsResponseModel.UPLOAD_ZIP));
    returnMap.put(IAssetConfigurationDetailsResponseModel.IS_INDESIGN_SERVER_ENABLED,
        assetVertex.getProperty(IAsset.INDESIGN_SERVER));
    Vertex configuartionVertexImage = UtilClass.getVertexById(SystemLevelIds.IMAGE,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    Vertex configuartionVertexVideo = UtilClass.getVertexById(SystemLevelIds.VIDEO,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    Vertex configuartionVertexDocument = UtilClass.getVertexById(SystemLevelIds.DOCUMENT,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    
    AssetUtils.addAssetExtensionConfigurationDetails(configuartionVertexImage,
        extensionConfiguration, CommonConstants.MAM_NATURE_TYPE_IMAGE);
    AssetUtils.addAssetExtensionConfigurationDetails(configuartionVertexVideo,
        extensionConfiguration, CommonConstants.MAM_NATURE_TYPE_VIDEO);
    AssetUtils.addAssetExtensionConfigurationDetails(configuartionVertexDocument,
        extensionConfiguration, CommonConstants.MAM_NATURE_TYPE_DOCUMENT);
    returnMap.put(IAssetConfigurationDetailsResponseModel.EXTENSION_CONFIGURATION,
        extensionConfiguration);
    
    String klassId = (String) map.get(IIdParameterModel.TYPE);
    if (klassId != null && !klassId.isEmpty()) {
      Vertex assetNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ASSET);
      String natureType = assetNode
          .getProperty(IAssetConfigurationDetailsResponseModel.NATURE_TYPE);
      returnMap.put(IAssetConfigurationDetailsResponseModel.NATURE_TYPE, natureType);
      returnMap.put(IAssetConfigurationDetailsResponseModel.KLASS_ID, klassId);
      returnMap.put(IAssetConfigurationDetailsResponseModel.IS_AUTO_CREATE_TIV_EXIST, checkForAutoCreateTIV(assetNode));
    }
    
    
    return returnMap;
  }

  /**
   * This method returns true if any auto-create TIV is linked to the klass.
   * @param returnMap
   * @param assetNode
   */
  private boolean checkForAutoCreateTIV(Vertex assetNode)
  {
    Iterable<Vertex> contextKlassIterable = assetNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex contextKlassNode : contextKlassIterable) {
      Iterator<Vertex> variantContextsIterator = contextKlassNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF).iterator();
      Vertex variantContextNode = variantContextsIterator.next();
      Map<String, Object> variantContextMap = UtilClass
          .getMapFromVertex(Arrays.asList(IVariantContext.IS_AUTO_CREATE), variantContextNode);
      Boolean isAutoCreate = (Boolean) variantContextMap.get(IVariantContext.IS_AUTO_CREATE);
      if (Boolean.TRUE.equals(isAutoCreate)) {
        return true;
      }
    }
    return false;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|FetchAssetConfigurationDetails/*" };
  }
}
