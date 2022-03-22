package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.klass.IGetAllKlassesTreeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassTaxonomyTreeModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllKlassesTree
    extends AbstractRuntimeInteractor<IIdParameterModel, IListModel<IKlassTaxonomyTreeModel>>
    implements IRuntimeInteractor<IIdParameterModel, IListModel<IKlassTaxonomyTreeModel>> {
  
  @Autowired
  protected IGetAllKlassesTreeService getAllKlassesTreeService;
  
  @Override
  public IListModel<IKlassTaxonomyTreeModel> executeInternal(IIdParameterModel model) throws Exception
  {
    return getAllKlassesTreeService.execute(model);
  }
}
