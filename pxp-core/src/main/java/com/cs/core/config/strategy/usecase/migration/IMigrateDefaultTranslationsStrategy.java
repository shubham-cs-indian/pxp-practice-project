package com.cs.core.config.strategy.usecase.migration;

import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrateDefaultTranslationsStrategy extends IConfigStrategy<IIdParameterModel, IVoidModel> {

}
