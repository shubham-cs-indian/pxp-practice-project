package com.cs.config.strategy.plugin.usecase.klass.supplier;

import com.cs.config.strategy.plugin.usecase.base.klass.AbstractGetKlassTree;
import com.cs.core.config.interactor.exception.market.MarketKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetSupplierTree extends AbstractGetKlassTree {
  
  public GetSupplierTree(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetSupplierTree/*" };
  }
  
  @Override
  public String getTypeKlass()
  {
    return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
  }
  
  @Override
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.executeInternal(requestMap);
    }
    catch (KlassNotFoundException e) {
      throw new MarketKlassNotFoundException(e);
    }
  }
}
