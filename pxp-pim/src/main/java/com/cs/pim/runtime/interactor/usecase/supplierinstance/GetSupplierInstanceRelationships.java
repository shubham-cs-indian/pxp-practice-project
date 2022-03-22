package com.cs.pim.runtime.interactor.usecase.supplierinstance;


import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.supplierinstance.IGetSupplierInstanceRelationshipsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSupplierInstanceRelationships extends AbstractRuntimeInteractor<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel>
    implements IGetSupplierInstanceRelationships {
  
  @Autowired
  protected IGetSupplierInstanceRelationshipsService getSupplierInstanceRelationshipsService;

  @Override protected IGetKlassInstanceRelationshipPaginationModel executeInternal(IGetKlassInstanceRelationshipsStrategyModel model)
      throws Exception
  {
    return getSupplierInstanceRelationshipsService.execute(model);
  }
}
