package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.rulelist.IGetAllRuleListsResponseModel;
import com.cs.core.config.strategy.usecase.rulelist.IGetAllRuleListStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllRuleListService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllRuleListsResponseModel>
    implements IGetAllRuleListService {
  
  @Autowired
  protected IGetAllRuleListStrategy getAllRuleListStrategy;
  
  @Override
  public IGetAllRuleListsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllRuleListStrategy.execute(dataModel);
  }
}
