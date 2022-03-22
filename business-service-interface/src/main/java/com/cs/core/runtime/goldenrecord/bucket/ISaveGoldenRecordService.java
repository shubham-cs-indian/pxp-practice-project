package com.cs.core.runtime.goldenrecord.bucket;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.goldenrecord.ISaveGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

public interface ISaveGoldenRecordService
    extends IRuntimeService<ISaveGoldenRecordRequestModel, IGetKlassInstanceModel> {
  
}
