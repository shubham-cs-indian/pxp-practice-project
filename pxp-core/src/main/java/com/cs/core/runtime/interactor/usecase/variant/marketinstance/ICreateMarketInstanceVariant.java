package com.cs.core.runtime.interactor.usecase.variant.marketinstance;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateMarketInstanceVariant extends
    IRuntimeInteractor<ICreateVariantModel, IGetKlassInstanceModel> {
  
}
