package com.cs.core.config.physicalcatalog;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.physicalcatalog.GetPhysicalCatalogIdsModel;
import com.cs.core.config.interactor.model.physicalcatalog.IGetPhysicalCatalogIdsModel;
import com.cs.core.config.physicalcatalog.util.PhysicalCatalogUtils;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.springframework.stereotype.Service;

@Service
public class GetPhysicalCatalogIdsService extends AbstractGetConfigService<IModel, IGetPhysicalCatalogIdsModel>
    implements IGetPhysicalCatalogIdsService {
  
  @Override
  public IGetPhysicalCatalogIdsModel executeInternal(IModel dataModel) throws Exception
  {
    IGetPhysicalCatalogIdsModel model = new GetPhysicalCatalogIdsModel();
    model.setAvailablePhysicalCatalogIds(PhysicalCatalogUtils.getAvailablePhysicalCatalogs());
    return model;
  }
}
