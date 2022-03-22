package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.customtemplate.ISaveCustomTemplateModel;
import com.cs.core.config.interactor.model.template.IBulkSaveTemplatesResponseModel;
import com.cs.core.config.template.IBulkSaveTemplateService;

@Service
public class BulkSaveTemplate extends AbstractSaveConfigInteractor<IListModel<ISaveCustomTemplateModel>, IBulkSaveTemplatesResponseModel>
    implements IBulkSaveTemplate {
  
  @Autowired
  protected IBulkSaveTemplateService bulkSaveTemplateService;
  
  @Override
  public IBulkSaveTemplatesResponseModel executeInternal(IListModel<ISaveCustomTemplateModel> idModel) throws Exception
  {
    return bulkSaveTemplateService.execute(idModel);
  }
}
