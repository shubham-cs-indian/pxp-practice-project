
package com.cs.pim.runtime.interactor.usecase.targetinstance.market;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.AbstractCreateInstanceBulkClone;
import com.cs.pim.runtime.targetinstance.market.ICreateMarketInstanceBulkCloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateMarketInstanceBulkClone extends AbstractCreateInstanceBulkClone<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel>
    implements ICreateMarketInstanceBulkClone {

  @Autowired
  protected ICreateMarketInstanceBulkCloneService createMarketInstanceBulkCloneService;

  @Override
  protected IBulkCreateKlassInstanceCloneResponseModel executeInternal(ICreateKlassInstanceBulkCloneModel dataModel) throws Exception
  {
    return createMarketInstanceBulkCloneService.execute(dataModel);
  }

}