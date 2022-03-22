package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component
public class ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistStrategy
    extends OrientDBBaseStrategy
    implements IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistStrategy {
  
  private static final String useCase = "ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklist";
  
  @Override
  public IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel execute(
      IConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistRequestModel model)
      throws Exception
  {
    return execute(useCase, model,
        ConfigDetailsForTaxonomyTreeByLeafIdsForVariantLinkedInstancesQuicklistResponseModel.class);
  }
}
