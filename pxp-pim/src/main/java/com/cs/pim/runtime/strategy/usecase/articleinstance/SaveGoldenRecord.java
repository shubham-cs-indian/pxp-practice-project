package com.cs.pim.runtime.strategy.usecase.articleinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.goldenrecord.bucket.ISaveGoldenRecordService;
import com.cs.core.runtime.interactor.model.goldenrecord.ISaveGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ISaveGoldenRecord;

@Service
public class SaveGoldenRecord
    extends AbstractRuntimeInteractor<ISaveGoldenRecordRequestModel, IGetKlassInstanceModel>
    implements ISaveGoldenRecord {
  
  @Autowired
  ISaveGoldenRecordService saveGoldenRecordService;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveGoldenRecordRequestModel model)
      throws Exception
  {
    return saveGoldenRecordService.execute(model);
  }
  
}
