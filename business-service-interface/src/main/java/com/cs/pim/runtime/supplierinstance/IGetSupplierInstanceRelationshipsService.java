package com.cs.pim.runtime.supplierinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;

public interface IGetSupplierInstanceRelationshipsService extends
    IRuntimeService<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel> {
  
}