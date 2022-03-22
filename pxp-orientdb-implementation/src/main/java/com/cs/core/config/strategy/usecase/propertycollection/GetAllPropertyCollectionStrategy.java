package com.cs.core.config.strategy.usecase.propertycollection;

import com.cs.core.config.interactor.model.propertycollection.GetAllPropertyCollectionResponseModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionRequestModel;
import com.cs.core.config.interactor.model.propertycollection.IGetAllPropertyCollectionResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("getAllPropertyCollectionStrategy")
public class GetAllPropertyCollectionStrategy extends OrientDBBaseStrategy
    implements IGetAllPropertyCollectionStrategy {
  
  @Override
  public IGetAllPropertyCollectionResponseModel execute(IGetAllPropertyCollectionRequestModel model)
      throws Exception
  {
    return execute(GET_ALL_PROPERTY_COLLECTION, model, GetAllPropertyCollectionResponseModel.class);
  }
}
