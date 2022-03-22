package com.cs.runtime.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class GetAllLinkedVariantPropertyCodes extends AbstractOrientPlugin {
  
  public GetAllLinkedVariantPropertyCodes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllLinkedVariantPropertyCodes/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    ConfigDetailsUtils.fillLinkedVariantPropertyCodes(returnMap);
    return returnMap;
  }
}
