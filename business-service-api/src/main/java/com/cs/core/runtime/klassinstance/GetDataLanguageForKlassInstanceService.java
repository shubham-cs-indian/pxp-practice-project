package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetDataLanguageForKlassInstanceStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDataLanguageForKlassInstanceService
    extends AbstractRuntimeService<IGetAllDataLanguagesModel, IListModel<IGetLanguagesInfoModel>>
    implements IGetDataLanguageForKlassInstanceService {
  
  @Autowired
  protected IGetDataLanguageForKlassInstanceStrategy getDataLanguageForContentStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                      rdbmsComponentUtils;
  
  @Override
  public IListModel<IGetLanguagesInfoModel> executeInternal(IGetAllDataLanguagesModel dataModel)
      throws Exception
  {
    long baseEntityIID = dataModel.getId();
    
    List<String> localeIds = baseEntityIID != 0l
                ? rdbmsComponentUtils.getBaseEntityDTO(baseEntityIID).getLocaleIds()
                : new ArrayList<>();
                
    dataModel.setLanguageCodes(localeIds);
    return getDataLanguageForContentStrategy.execute(dataModel);
  }
}