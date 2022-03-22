package com.cs.core.runtime.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.goldenrecord.bucket.ICreateGoldenRecordService;
import com.cs.core.runtime.interactor.model.goldenrecord.ICreateGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

@Service
public class CreateGoldenRecordService extends AbstractCreateGoldenRecordService<ICreateGoldenRecordRequestModel, IGetKlassInstanceModel>
    implements ICreateGoldenRecordService {
  
  @Autowired
  protected Long goldenArticleKlassCounter;
  
  @Override
  public IGetKlassInstanceModel executeInternal(ICreateGoldenRecordRequestModel model) throws Exception
  {
    
    return super.executeInternal(model);
  }
  
  @Override
  protected Long getCounter()
  {
    return goldenArticleKlassCounter++;
  }
  
}
