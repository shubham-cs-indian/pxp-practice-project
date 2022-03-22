package com.cs.pim.runtime.interactor.usecase.articleinstance;


import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.klassinstance.IReferencedTypesAndTaxonomiesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;

public interface IGetTypesAndTaxonomiesOfContent
    extends IConfigStrategy<IAllowedTypesRequestModel, IReferencedTypesAndTaxonomiesModel> {
  
}
