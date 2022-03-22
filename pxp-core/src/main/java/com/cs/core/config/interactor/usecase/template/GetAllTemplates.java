package com.cs.core.config.interactor.usecase.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.template.IGetGridTemplatesResponseModel;
import com.cs.core.config.template.IGetAllTemplatesService;

@Service
public class GetAllTemplates extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetGridTemplatesResponseModel>
    implements IGetAllTemplates {
  
  @Autowired
  protected IGetAllTemplatesService getAllTemplatesService;
  
  @Override
  public IGetGridTemplatesResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllTemplatesService.execute(dataModel);
  }
}
