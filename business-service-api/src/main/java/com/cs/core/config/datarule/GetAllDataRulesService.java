package com.cs.core.config.datarule;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.datarule.IGetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.datarule.IGetAllDataRulesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllDataRulesService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllDataRulesResponseModel>
    implements IGetAllDataRulesService {
  
  @Autowired
  protected IGetAllDataRulesStrategy orientDBGetDataRulesStrategy;
  
  @Override
  public IGetAllDataRulesResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return orientDBGetDataRulesStrategy.execute(dataModel);
  }
}
