package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesResponseModel;
import com.cs.core.config.strategy.usecase.template.IBulkSaveTemplateStrategy;
import com.cs.core.config.template.IBulkSaveTemplateService;

@Service
public class BulkSaveTemplateService extends
    AbstractSaveConfigService<IListModel<ISaveCustomTemplateModel>, IBulkSaveTemplatesResponseModel> implements IBulkSaveTemplateService {
  
  @Autowired
  protected IBulkSaveTemplateStrategy bulkSaveTemplateStrategy;
  
  @Override
  public IBulkSaveTemplatesResponseModel executeInternal(IListModel<ISaveCustomTemplateModel> idModel) throws Exception
  {
    return bulkSaveTemplateStrategy.execute(idModel);
  }
}
