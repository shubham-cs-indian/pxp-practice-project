package com.cs.core.runtime.instancetree;

import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterAndSortDataForLIQRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;

@Service
public class GetNewFilterAndSortDataForLinkedInstanceQuicklistService extends AbstractGetNewFilterAndSortData<IGetFilterAndSortDataForLIQRequestModel, 
    IGetNewFilterAndSortDataResponseModel> implements IGetNewFilterAndSortDataForLinkedInstanceQuicklistService {
    
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
  
  public ServiceType getServiceType()
  {
    return ServiceType.GET;
  }

  @Override
  protected void additionalInformationForRelationshipFilter(IGetFilterAndSortDataForLIQRequestModel model,
      IGetNewFilterAndSortDataResponseModel responseModel)
  {
    
  }
}
