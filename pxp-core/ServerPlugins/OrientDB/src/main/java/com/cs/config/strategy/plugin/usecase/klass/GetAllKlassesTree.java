package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.AbstractGetAllKlassTaxonomiesTree;
import com.cs.core.exception.PluginException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.List;
import java.util.Map;

public class GetAllKlassesTree extends AbstractGetAllKlassTaxonomiesTree {
  
  public GetAllKlassesTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllKlassesTree/*" };
  }
  
  @Override
  public String getType()
  {
    return VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS;
  }
  
  @Override
  public List<Map<String, Object>> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.executeInternal(requestMap);
    }
    catch (Exception e) {
      throw new PluginException();
    }
  }
}
