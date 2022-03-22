package com.cs.core.config.interactor.usecase.physicalcatalog;

import com.cs.core.config.physicalcatalog.IGetPhysicalCatalogIdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.physicalcatalog.GetPhysicalCatalogIdsModel;
import com.cs.core.config.interactor.model.physicalcatalog.IGetPhysicalCatalogIdsModel;
import com.cs.core.config.physicalcatalog.util.PhysicalCatalogUtils;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class GetPhysicalCatalogIds
    extends AbstractGetConfigInteractor<IModel, IGetPhysicalCatalogIdsModel>
    implements IGetPhysicalCatalogIds {

  @Autowired
  protected IGetPhysicalCatalogIdsService getPhysicalCatalogIdsService;

  @Override
  public IGetPhysicalCatalogIdsModel executeInternal(IModel dataModel) throws Exception
  {
    return getPhysicalCatalogIdsService.execute(dataModel);
  }
}
