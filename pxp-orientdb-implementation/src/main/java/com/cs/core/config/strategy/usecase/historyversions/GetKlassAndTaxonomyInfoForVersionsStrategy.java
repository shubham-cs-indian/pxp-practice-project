package com.cs.core.config.strategy.usecase.historyversions;

import com.cs.core.config.interactor.model.historyversions.IKlassesAndTaxonomiesForVersionsInfoRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.version.GetKlassInstanceVersionsForComparisonModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForComparisonModel;
import org.springframework.stereotype.Component;

@Component("getKlassAndTaxonomyInfoForVersionsStrategy")
public class GetKlassAndTaxonomyInfoForVersionsStrategy extends OrientDBBaseStrategy
    implements IGetKlassAndTaxonomyInfoForVersionsStrategy {
  
  public static final String useCase = "GetKlassAndTaxonomyInfoForVersions";
  
  @Override
  public IGetKlassInstanceVersionsForComparisonModel execute(
      IKlassesAndTaxonomiesForVersionsInfoRequestModel model) throws Exception
  {
    return execute(useCase, model, GetKlassInstanceVersionsForComparisonModel.class);
  }
}
