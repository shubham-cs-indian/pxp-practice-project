package com.cs.pim.runtime.supplierinstance;

import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceRelationshipsStrategyModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceRelationshipPaginationModel;
import com.cs.core.runtime.klassinstance.AbstractGetInstanceRelationshipsService;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetSupplierInstanceRelationshipsService extends AbstractGetInstanceRelationshipsService<IGetKlassInstanceRelationshipsStrategyModel, IGetKlassInstanceRelationshipPaginationModel>
    implements IGetSupplierInstanceRelationshipsService {
  
  @Autowired
  IGetKlassInstanceTypeStrategy               getSupplierInstanceTypeStrategy;
  
  @Override
  protected IGetKlassInstanceRelationshipPaginationModel executeGetInstanceRelationships(
      IGetKlassInstanceRelationshipsStrategyModel getKlassInstanceRelationshipsStrategyModel)
      throws Exception
  {
    return new GetKlassInstanceRelationshipPaginationModel();
  }
  
  
  @Override
  protected IKlassInstanceTypeModel getKlassInstanceType(String id) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(id);
    return getSupplierInstanceTypeStrategy.execute(idParameterModel);
  }

  @Override
  protected BaseType getBaseType()
  {
    return BaseType.SUPPLIER;
  }

  
}
