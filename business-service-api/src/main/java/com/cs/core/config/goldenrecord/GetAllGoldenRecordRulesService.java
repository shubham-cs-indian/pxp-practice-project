package com.cs.core.config.goldenrecord;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.goldenrecord.IGetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IGetAllGoldenRecordRulesStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GetAllGoldenRecordRulesService extends AbstractGetConfigService<IConfigGetAllRequestModel, IGetAllGoldenRecordRulesModel>
    implements IGetAllGoldenRecordRulesService {
  
  @Autowired
  protected IGetAllGoldenRecordRulesStrategy getAllGoldenRecordRulesStrategy;
  
  @Override
  public IGetAllGoldenRecordRulesModel executeInternal(IConfigGetAllRequestModel dataModel) throws Exception
  {
    return getAllGoldenRecordRulesStrategy.execute(dataModel);
  }
}
