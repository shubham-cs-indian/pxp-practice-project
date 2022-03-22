package com.cs.core.runtime.staticcollection;


import org.springframework.stereotype.Service;

import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.entity.dto.CollectionFilterDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.runtime.instancetree.AbstractGetNewFilterAndSortData;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataForCollectionRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;

@Service
public class GetNewFilterAndSortDataForCollectionService extends AbstractGetNewFilterAndSortData<IGetNewFilterAndSortDataForCollectionRequestModel, IGetNewFilterAndSortDataResponseModel>
    implements IGetNewFilterAndSortDataForCollectionService {

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
  protected void additionalInformationForRelationshipFilter(IGetNewFilterAndSortDataForCollectionRequestModel model,
      IGetNewFilterAndSortDataResponseModel responseModel)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  protected void fillFilterData(IGetNewFilterAndSortDataForCollectionRequestModel model, IGetNewFilterAndSortDataResponseModel responseModel) throws Exception
  {
    evaluateAndFillFilterData(model, responseModel);
  }
  
  @Override
  protected void fillUsecaseSpecificFilters(IGetNewFilterAndSortDataForCollectionRequestModel dataModel,
      IGetNewFilterAndSortDataResponseModel configDetails, ISearchDTOBuilder searchBuilder) throws Exception
  {
    CollectionFilterDTO collectionFilterDto = new CollectionFilterDTO(dataModel.getIsQuicklist(),dataModel.getCollectionId());
    searchBuilder.addCollectionFilters(collectionFilterDto);
  }
  
  
}