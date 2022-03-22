package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.Map;

public class GetConfigDetailsForTaxonomyHierarchy extends AbstractOrientPlugin {
  
  public GetConfigDetailsForTaxonomyHierarchy(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForTaxonomyHierarchy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IIdParameterModel.ID);
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    GlobalPermissionUtils.fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userId,
        mapToReturn);
    return mapToReturn;
  }
}
