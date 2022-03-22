package com.cs.core.config.interactor.usecase.portal;

import java.util.List;

import com.cs.core.config.portal.IGetPortalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.base.interactor.model.portal.GetProtalModel;
import com.cs.base.interactor.model.portal.IGetPortalModel;
import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetPortals extends AbstractGetConfigInteractor<IModel, IGetPortalModel>
    implements IGetPortals {
  
  @Autowired
  protected IGetPortalsService getPortalsService;
  
  @Override
  public IGetPortalModel executeInternal(IModel dataModel) throws Exception
  {
    return getPortalsService.execute(dataModel);
  }
}
