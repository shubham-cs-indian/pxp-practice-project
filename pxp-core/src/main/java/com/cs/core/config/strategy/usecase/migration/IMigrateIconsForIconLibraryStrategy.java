package com.cs.core.config.strategy.usecase.migration;

import com.cs.core.config.interactor.model.asset.IGetAssetDetailsRequestModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrateIconsForIconLibraryStrategy extends IConfigStrategy<IGetAssetDetailsRequestModel, IVoidModel> {

}
