package com.cs.core.runtime.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForFilterAndSortDataStrategy;

@Service
public class GetNewFilterAndSortDataService extends AbstractGetNewFilterAndSortData<IGetNewFilterAndSortDataRequestModel, 
    IGetNewFilterAndSortDataResponseModel> implements IGetNewFilterAndSortDataService {
  
  @Autowired
  protected IGetConfigDetailsForFilterAndSortDataStrategy getConfigDetailsForFilterAndSortDataStrategy;

  
  @Override
  protected ConfigDetailsForFilterAndSortInfoRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForFilterAndSortInfoRequestModel();
  }

  @Override
  protected IGetNewFilterAndSortDataResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForFilterAndSortInfoRequestModel configRequsetModel) throws Exception
  {
    return getConfigDetailsForFilterAndSortDataStrategy.execute(configRequsetModel);
  }

  @Override
  protected void additionalInformationForRelationshipFilter(
      IGetNewFilterAndSortDataRequestModel model,
      IGetNewFilterAndSortDataResponseModel responseModel)
  {
    // Nothing to add
  }
  
  @Override
  protected void fillFilterData(IGetNewFilterAndSortDataRequestModel model, IGetNewFilterAndSortDataResponseModel responseModel) throws Exception
  {
    if (!responseModel.getFilterData().isEmpty()) {
      evaluateAndFillFilterData(model, responseModel);
    }
  }
  
}
