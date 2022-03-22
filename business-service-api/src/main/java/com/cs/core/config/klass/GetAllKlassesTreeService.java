package com.cs.core.config.klass;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.klass.IGetAllKlassesTreeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassTaxonomyTreeModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllKlassesTreeService
    extends AbstractGetConfigService<IIdParameterModel, IListModel<IKlassTaxonomyTreeModel>>
    implements IGetAllKlassesTreeService {
  
  @Autowired
  IGetAllKlassesTreeStrategy getAllKlassesTreeStrategy;
  
  @Override
  public IListModel<IKlassTaxonomyTreeModel> executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllKlassesTreeStrategy.execute(model);
  }
}
