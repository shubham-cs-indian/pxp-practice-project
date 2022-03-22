package com.cs.config.strategy.plugin.usecase.base.organization;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.Map;

public abstract class AbstractGetOrganization extends AbstractOrientPlugin {
  
  public AbstractGetOrganization(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String organizationId = (String) requestMap.get(IIdParameterModel.ID);
    return OrganizationUtil.getOrganizationMapToReturnWithReferencedData(organizationId);
  }
}
