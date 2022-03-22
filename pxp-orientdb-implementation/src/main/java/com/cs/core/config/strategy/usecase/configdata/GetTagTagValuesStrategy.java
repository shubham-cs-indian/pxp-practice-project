package com.cs.core.config.strategy.usecase.configdata;

import com.cs.core.config.interactor.model.tag.GetTagTagValuesResponseModel;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesRequestModel;
import com.cs.core.config.interactor.model.tag.IGetTagTagValuesResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetTagTagValuesStrategy extends OrientDBBaseStrategy
    implements IGetTagTagValuesStrategy {
  
  private static final String useCase = "GetTagTagValues";
  
  @Override
  public IGetTagTagValuesResponseModel execute(IGetTagTagValuesRequestModel model) throws Exception
  {
    return super.execute(useCase, model, GetTagTagValuesResponseModel.class);
  }
}
