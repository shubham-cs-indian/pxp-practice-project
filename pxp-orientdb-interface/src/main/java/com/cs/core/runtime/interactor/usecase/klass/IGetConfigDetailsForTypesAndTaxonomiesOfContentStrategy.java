package com.cs.core.runtime.interactor.usecase.klass;


import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.klassinstance.IReferencedTypesAndTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;

public interface IGetConfigDetailsForTypesAndTaxonomiesOfContentStrategy
    extends IConfigStrategy<ITypesTaxonomiesModel, IReferencedTypesAndTaxonomiesModel> {
  
}
