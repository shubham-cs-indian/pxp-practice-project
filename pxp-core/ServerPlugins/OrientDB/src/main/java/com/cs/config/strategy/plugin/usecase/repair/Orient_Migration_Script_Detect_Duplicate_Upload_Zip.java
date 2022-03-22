package com.cs.config.strategy.plugin.usecase.repair;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

/**
 * This migration will set Detect Duplicate and Upload Zip as false. Required
 * else they will be null in the existing data
 */
public class Orient_Migration_Script_Detect_Duplicate_Upload_Zip extends AbstractOrientPlugin {
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Detect_Duplicate_Upload_Zip/*" };
  }
  
  public Orient_Migration_Script_Detect_Duplicate_Upload_Zip(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Vertex assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    assetVertex.setProperty(IAsset.DETECT_DUPLICATE, false);
    assetVertex.setProperty(IAsset.UPLOAD_ZIP, false);
    UtilClass.getGraph()
        .commit();
    
    return new HashMap<>();
  }
}
