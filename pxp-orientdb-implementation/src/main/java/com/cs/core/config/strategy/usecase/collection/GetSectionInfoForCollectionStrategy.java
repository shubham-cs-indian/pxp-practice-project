package com.cs.core.config.strategy.usecase.collection;

import com.cs.core.config.interactor.model.klass.GetSectionInfoModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetSectionInfoForCollectionStrategy extends OrientDBBaseStrategy
    implements IGetSectionInfoForCollectionStrategy {
  
  @Override
  public IGetSectionInfoModel execute(IGetSectionInfoForTypeRequestModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_SECTION_INFO_FOR_COLLECTION, model,
        GetSectionInfoModel.class);
  }
}
