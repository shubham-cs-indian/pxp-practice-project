package com.cs.config.strategy.plugin.usecase.taxonomy.target;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.AbstractGetFilterAndSortDataForKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetFilterAndSortDataForTarget extends AbstractGetFilterAndSortDataForKlass {
  
  public GetFilterAndSortDataForTarget(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetFilterAndSortDataForTarget/*" };
  }
  
  @Override
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    return super.executeInternal(requestMap);
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ENTITY_TYPE_TARGET;
  }
}
