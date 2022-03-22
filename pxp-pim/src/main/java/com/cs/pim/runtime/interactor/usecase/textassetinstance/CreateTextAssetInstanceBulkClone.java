
package com.cs.pim.runtime.interactor.usecase.textassetinstance;
import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.AbstractCreateInstanceBulkClone;
import com.cs.pim.runtime.textassetinstance.ICreateTextAssetInstanceBulkCloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTextAssetInstanceBulkClone extends AbstractCreateInstanceBulkClone<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel>
    implements ICreateTextAssetInstanceBulkClone {

  @Autowired
  protected ICreateTextAssetInstanceBulkCloneService createTextAssetInstanceBulkCloneService;

  @Override
  protected IBulkCreateKlassInstanceCloneResponseModel executeInternal(ICreateKlassInstanceBulkCloneModel dataModel) throws Exception
  {
    return createTextAssetInstanceBulkCloneService.execute(dataModel);
  }

}