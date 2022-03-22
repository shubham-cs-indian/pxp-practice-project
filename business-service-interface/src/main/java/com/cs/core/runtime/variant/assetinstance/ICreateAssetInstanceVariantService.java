package com.cs.core.runtime.variant.assetinstance;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.variants.ICreateImageVariantsModel;

public interface ICreateAssetInstanceVariantService
    extends IRuntimeService<ICreateImageVariantsModel, IGetKlassInstanceModel> {
}
