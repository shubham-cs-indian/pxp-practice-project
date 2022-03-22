package com.cs.core.config.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.template.IGetGridTemplatesResponseModel;
import com.cs.core.config.strategy.usecase.template.IGetAllTemplatesStrategy;
import com.cs.core.config.template.IGetAllTemplatesService;

@Service
public class GetAllTemplatesService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetGridTemplatesResponseModel>
    implements IGetAllTemplatesService {
  
  @Autowired
  protected IGetAllTemplatesStrategy getAllTemplatesStrategy;
  
  @Override
  public IGetGridTemplatesResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllTemplatesStrategy.execute(dataModel);
  }
}
