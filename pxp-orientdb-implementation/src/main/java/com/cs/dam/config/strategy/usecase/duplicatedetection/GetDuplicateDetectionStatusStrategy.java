package com.cs.dam.config.strategy.usecase.duplicatedetection;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.attribute.BooleanReturnModel;
import com.cs.core.config.interactor.model.attribute.IBooleanReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Component("getDuplicateDetectionStatusStrategy")
public class GetDuplicateDetectionStatusStrategy extends OrientDBBaseStrategy
    implements IGetDuplicateDetectionStatusStrategy {
  
  @Override
  public IBooleanReturnModel execute(IVoidModel model) throws Exception
  {
    return execute(GET_DUPLICATE_DETECTION_STATUS, model, BooleanReturnModel.class);
  }
  
}
