package com.cs.core.config.interactor.usecase.goldenrecord;

import com.cs.core.config.goldenrecord.IGetAllGoldenRecordRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.goldenrecord.IGetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IGetAllGoldenRecordRulesStrategy;

@Service
public class GetAllGoldenRecordRules extends AbstractGetConfigInteractor<IConfigGetAllRequestModel, IGetAllGoldenRecordRulesModel>
    implements IGetAllGoldenRecordRules {
  
  @Autowired
  protected IGetAllGoldenRecordRulesService getAllGoldenRecordRulesService;
  
  @Override
  public IGetAllGoldenRecordRulesModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllGoldenRecordRulesService.execute(dataModel);
  }
}
