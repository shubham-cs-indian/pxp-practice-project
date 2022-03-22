package com.cs.core.runtime.interactor.usecase.klass;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;

public interface IGetDataLanguageForKlassInstanceStrategy
    extends IConfigStrategy<IGetAllDataLanguagesModel, IListModel<IGetLanguagesInfoModel>> {
}
