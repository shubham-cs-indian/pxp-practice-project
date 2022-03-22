package com.cs.pim.runtime.interactor.usecase.articleinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetDataLanguageForKlassInstance
    extends IRuntimeInteractor<IGetAllDataLanguagesModel, IListModel<IGetLanguagesInfoModel>> {
  
}
