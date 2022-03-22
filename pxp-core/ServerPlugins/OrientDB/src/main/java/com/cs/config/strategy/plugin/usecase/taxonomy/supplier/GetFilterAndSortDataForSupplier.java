package com.cs.config.strategy.plugin.usecase.taxonomy.supplier;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.AbstractGetFilterAndSortDataForKlass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetFilterAndSortDataForSupplier extends AbstractGetFilterAndSortDataForKlass {
  
  public GetFilterAndSortDataForSupplier(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetFilterAndSortDataForSupplier/*" };
  }
  
  @Override
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    return super.executeInternal(requestMap);
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
  }
}
