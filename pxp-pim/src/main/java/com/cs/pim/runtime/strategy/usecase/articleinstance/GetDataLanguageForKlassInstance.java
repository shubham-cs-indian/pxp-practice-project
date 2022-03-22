package com.cs.pim.runtime.strategy.usecase.articleinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.klassinstance.IGetDataLanguageForKlassInstanceService;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IGetDataLanguageForKlassInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetDataLanguageForKlassInstance
    extends AbstractRuntimeInteractor<IGetAllDataLanguagesModel, IListModel<IGetLanguagesInfoModel>>
    implements IGetDataLanguageForKlassInstance {

  @Autowired
  protected IGetDataLanguageForKlassInstanceService getDataLanguageForKlassInstanceService;
  
  @Override
  public IListModel<IGetLanguagesInfoModel> executeInternal(IGetAllDataLanguagesModel dataModel)
      throws Exception
  {
    return getDataLanguageForKlassInstanceService.execute(dataModel);
  }
}