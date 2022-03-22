package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.language.IGetAllDataLanguagesInfoModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;

public interface IGetAllDataLanguageInfoStrategy extends
    IConfigStrategy<IGetAllDataLanguagesModel, IGetAllDataLanguagesInfoModel> {
}
