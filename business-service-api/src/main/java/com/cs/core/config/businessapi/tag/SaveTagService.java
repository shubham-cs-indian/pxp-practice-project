package com.cs.core.config.businessapi.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.IBulkSaveTagResponseModel;
import com.cs.core.config.interactor.model.tag.ISaveTagModel;
import com.cs.core.config.strategy.usecase.tag.ISaveTagStrategy;

@Service
public class SaveTagService extends AbstractSaveConfigService<IListModel<ISaveTagModel>, IBulkSaveTagResponseModel>
    implements ISaveTagService {
  
  @Autowired
  protected ISaveTagStrategy saveTagStrategy;
  
  @Override
  protected IBulkSaveTagResponseModel executeInternal(IListModel<ISaveTagModel> model) throws Exception
  {
    for(ISaveTagModel tagModel : model.getList()) {
      Validations.validateLabel(tagModel.getLabel());
    }
    return saveTagStrategy.execute(model);
  }
  
}
