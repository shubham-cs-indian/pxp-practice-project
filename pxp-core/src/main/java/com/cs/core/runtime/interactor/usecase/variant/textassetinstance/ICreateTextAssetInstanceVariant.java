package com.cs.core.runtime.interactor.usecase.variant.textassetinstance;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateVariantModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTextAssetInstanceVariant extends
    IRuntimeInteractor<ICreateVariantModel, IGetKlassInstanceModel> {
  
}
