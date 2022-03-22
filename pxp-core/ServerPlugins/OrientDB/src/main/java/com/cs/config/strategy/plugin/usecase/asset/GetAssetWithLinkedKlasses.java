package com.cs.config.strategy.plugin.usecase.asset;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetKlassWithLinkedKlasses;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetAssetWithLinkedKlasses extends AbstractGetKlassWithLinkedKlasses {
  
  public GetAssetWithLinkedKlasses(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> klassMap = executeInternal(map);
    return klassMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAssetWithLinkedKlasses/*" };
  }
  
  @Override
  public String getVertexLabel()
  {
    return VertexLabelConstants.ENTITY_TYPE_ASSET;
  }
}
