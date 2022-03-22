package com.cs.runtime.strategy.plugin.usecase.assetinstance.utils;

import java.util.List;

import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Vertex;

public class AssetInstanceUtils {
  
  /**
   * Description: Get Ids of asset instance tabs rendered at runtime. Duplicate
   * Tab Id is not added for contextual classes.
   *
   * @param helperModel
   *          - config details helper model
   * @param tabIds
   *          - ids of tabs initailly present.
   * @return tabIds of asset instances.
   * @throws Exception
   */
  public static List<String> getRuntimeAssetInstanceTabs(IGetConfigDetailsHelperModel helperModel,
      List<String> tabIds) throws Exception
  {
    
    Vertex assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    Boolean isDuplicateDetectionOn = assetVertex.getProperty(IAsset.DETECT_DUPLICATE);
    if (isDuplicateDetectionOn) {
      tabIds.add(SystemLevelIds.DUPLICATE_TAB);
    }
    return tabIds;
  }
}
