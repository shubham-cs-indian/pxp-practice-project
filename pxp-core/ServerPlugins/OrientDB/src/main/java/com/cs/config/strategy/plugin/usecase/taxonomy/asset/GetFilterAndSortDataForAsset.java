package com.cs.config.strategy.plugin.usecase.taxonomy.asset;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.AbstractGetFilterAndSortDataForKlass;
import com.cs.core.config.interactor.exception.asset.AssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public class GetFilterAndSortDataForAsset extends AbstractGetFilterAndSortDataForKlass {
  
  public GetFilterAndSortDataForAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetFilterAndSortDataForAsset/*" };
  }
  
  @Override
  public Map<String, Object> executeInternal(Map<String, Object> requestMap) throws Exception
  {
    try {
      return super.executeInternal(requestMap);
    }
    catch (KlassNotFoundException e) {
      throw new AssetKlassNotFoundException(e);
    }
  }
  
  @Override
  public String getKlassVertexType()
  {
    return VertexLabelConstants.ENTITY_TYPE_ASSET;
  }
}
