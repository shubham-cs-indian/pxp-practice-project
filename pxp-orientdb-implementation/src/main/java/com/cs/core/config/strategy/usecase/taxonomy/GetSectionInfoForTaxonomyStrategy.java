package com.cs.core.config.strategy.usecase.taxonomy;

import com.cs.core.config.interactor.model.klass.GetSectionInfoModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class GetSectionInfoForTaxonomyStrategy extends OrientDBBaseStrategy
    implements IGetSectionInfoForTaxonomyStrategy {
  
  @Override
  public IGetSectionInfoModel execute(IGetSectionInfoForTypeRequestModel model) throws Exception
  {
    return execute(GET_SECTION_INFO_FOR_TAXONOMY, model, GetSectionInfoModel.class);
  }
}
