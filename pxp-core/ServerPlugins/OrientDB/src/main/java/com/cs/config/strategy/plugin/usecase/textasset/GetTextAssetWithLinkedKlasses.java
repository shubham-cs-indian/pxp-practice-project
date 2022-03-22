package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetKlassWithLinkedKlasses;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetTextAssetWithLinkedKlasses extends AbstractGetKlassWithLinkedKlasses {
  
  public GetTextAssetWithLinkedKlasses(OServerCommandConfiguration iConfiguration)
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
    return new String[] { "POST|GetTextAssetWithLinkedKlasses/*" };
  }
  
  @Override
  public String getVertexLabel()
  {
    return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
  }
}
