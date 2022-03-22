package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.goldenrecord.bucket.ICreateGoldenRecordService;
import com.cs.core.runtime.interactor.model.goldenrecord.ICreateGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateGoldenRecord;

@Service
public class CreateGoldenRecord extends AbstractRuntimeInteractor<ICreateGoldenRecordRequestModel, IGetKlassInstanceModel>
    implements ICreateGoldenRecord {
  
  @Autowired
  ICreateGoldenRecordService createGoldenRecordService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateGoldenRecordRequestModel model) throws Exception
  {
    return createGoldenRecordService.execute(model);
  }
  
}
