package com.cs.core.config.interactor.usecase.governancerule;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.governancerule.IDeleteKeyPerformanceIndexService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteKeyPerformanceIndex extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteKeyPerformanceIndex {
  
  @Autowired
  protected IDeleteKeyPerformanceIndexService deleteKeyPerformanceIndexService;
@Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return deleteKeyPerformanceIndexService.execute(dataModel);
  }
  
}
