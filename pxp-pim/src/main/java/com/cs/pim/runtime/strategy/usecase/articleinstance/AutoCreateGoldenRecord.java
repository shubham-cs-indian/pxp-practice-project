package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.goldenrecord.bucket.IAutoCreateGoldenRecordService;
import com.cs.core.runtime.interactor.model.goldenrecord.ICreateGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.interactor.usecase.articleinstance.IAutoCreateGoldenRecord;

@Service
public class AutoCreateGoldenRecord
    extends AbstractRuntimeInteractor<ICreateGoldenRecordRequestModel, IGetKlassInstanceModel>
    implements IAutoCreateGoldenRecord {
  
  @Autowired
  IAutoCreateGoldenRecordService autoCreateGoldenRecordService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateGoldenRecordRequestModel model)
      throws Exception
  {
    return autoCreateGoldenRecordService.execute(model);
  }
  
}
