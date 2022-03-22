package com.cs.core.config.interactor.usecase.rulelist;

import com.cs.core.config.rulelist.IGetAllRuleListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.rulelist.IGetAllRuleListsResponseModel;
import com.cs.core.config.strategy.usecase.rulelist.IGetAllRuleListStrategy;

@Service
public class GetAllRuleList extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllRuleListsResponseModel> implements
    IGetAllRuleList {
  
  @Autowired
  protected IGetAllRuleListService getAllRuleListService;
  
  @Override
  public IGetAllRuleListsResponseModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllRuleListService.execute(dataModel);
  }
}
