package com.cs.core.config.interactor.usecase.variantcontext;

import java.util.Map;

import coms.cs.core.config.businessapi.variantcontext.IDeleteVariantContextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.variantcontext.IDeleteVariantContextStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantContextReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteVariantContext
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteVariantContext {
  
  @Autowired
  protected IDeleteVariantContextService deleteVariantContextService;

  @Override
  public IBulkDeleteVariantContextReturnModel executeInternal(IIdsListParameterModel variantContextModel)
      throws Exception
  {
    return deleteVariantContextService.execute(variantContextModel);
  }
}
