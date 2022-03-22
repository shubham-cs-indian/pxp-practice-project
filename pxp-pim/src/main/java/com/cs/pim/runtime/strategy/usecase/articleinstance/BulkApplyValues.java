package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.bulkpropagation.IBulkApplyValuesService;
import com.cs.core.runtime.interactor.model.configuration.IBulkApplyValueRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.AbstractBulkApplyValues;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IBulkApplyValues;

@Service
public class BulkApplyValues extends AbstractBulkApplyValues<IBulkApplyValueRequestModel, IIdsListParameterModel>
    implements IBulkApplyValues {
  
  @Autowired
  protected IBulkApplyValuesService         bulkApplyValueService ;
  
  protected IIdsListParameterModel executeInternal(IBulkApplyValueRequestModel requestModel) throws Exception
  {
    return bulkApplyValueService.execute(requestModel);
  }
}
