package com.cs.core.config.interactor.usecase.datarule;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.datarule.IGetAllDataRulesService;
import com.cs.core.config.interactor.model.datarule.IGetAllDataRulesResponseModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllDataRules extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllDataRulesResponseModel>
    implements IGetAllDataRules {
  
  @Autowired
  protected IGetAllDataRulesService getAllDataRulesService;
  
  @Override
  public IGetAllDataRulesResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllDataRulesService.execute(dataModel);
  }
}
