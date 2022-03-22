package com.cs.runtime.strategy.plugin.usecase.duplicatedetection;

import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetDuplicateDetectionStatus extends AbstractOrientPlugin {
  
  public GetDuplicateDetectionStatus(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetDuplicateDetectionStatus/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Boolean> duplicateStatus = new HashMap<String, Boolean>();
    Vertex assetVertex = UtilClass.getVertexById(SystemLevelIds.ASSET, VertexLabelConstants.ENTITY_TYPE_ASSET);
    Boolean isDuplicateDetectionOn = assetVertex.getProperty(IAsset.DETECT_DUPLICATE);
    duplicateStatus.put("duplicateStatus", isDuplicateDetectionOn);
    return duplicateStatus;
  }
  
}
