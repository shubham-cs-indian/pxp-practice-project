package com.cs.core.runtime.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;

public interface IGetDataLanguageForKlassInstanceService
    extends IRuntimeService<IGetAllDataLanguagesModel, IListModel<IGetLanguagesInfoModel>> {
  
}
