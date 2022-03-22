package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.GetSectionInfoModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetSectionInfoForKlassStrategy extends OrientDBBaseStrategy
    implements IGetSectionInfoForKlassStrategy {
  
  @Override
  public IGetSectionInfoModel execute(IGetSectionInfoForTypeRequestModel model) throws Exception
  {
    return execute(GET_SECTION_INFO_FOR_KLASS, model, GetSectionInfoModel.class);
  }
}
