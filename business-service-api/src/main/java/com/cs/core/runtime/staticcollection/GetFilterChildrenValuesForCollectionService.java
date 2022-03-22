package com.cs.core.runtime.staticcollection;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.instancetree.ConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.rdbms.entity.dto.CollectionFilterDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.runtime.instancetree.AbstractGetFilterChildrenValues;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenForCollectionRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;

@Service
public class GetFilterChildrenValuesForCollectionService
    extends AbstractGetFilterChildrenValues<IGetFilterChildrenForCollectionRequestModel, IGetFilterChildrenResponseModel>
    implements IGetFilterChildrenValuesForCollectionService {
  
  @Override
  protected IConfigDetailsForGetFilterChildrenResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetFilterChildrenRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetFilterChildrenStrategy.execute(configRequestModel);
  }
  
  @Override
  protected IConfigDetailsForGetFilterChildrenRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetFilterChildrenRequestModel();
  }
  
  @Override
  protected List<IGetFilterChildrenModel> executeRuntimeStrategy(IGetFilterChildrenForCollectionRequestModel model,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception
  {
    return getFilterChildrenValues(model, configDetails);
  }
  
  @Override
  protected void fillUsecaseSpecificFilters(IGetFilterChildrenForCollectionRequestModel dataModel,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails, ISearchDTOBuilder searchBuilder) throws Exception
  {
    CollectionFilterDTO collectionFilterDto = new CollectionFilterDTO(dataModel.getIsQuicklist(),dataModel.getCollectionId());
    searchBuilder.addCollectionFilters(collectionFilterDto);
  }
  
}
