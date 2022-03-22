package com.cs.core.config.portal;

import com.cs.base.interactor.model.portal.GetProtalModel;
import com.cs.base.interactor.model.portal.IGetPortalModel;
import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetPortalsService extends AbstractGetConfigService<IModel, IGetPortalModel>
    implements IGetPortalsService {
  
  @Autowired
  protected List<IPortalModel> portalModels;
  
  @Override
  public IGetPortalModel executeInternal(IModel dataModel) throws Exception
  {
    IGetPortalModel portalModel = new GetProtalModel();
    portalModel.setPortals(portalModels);
    return portalModel;
  }
}
