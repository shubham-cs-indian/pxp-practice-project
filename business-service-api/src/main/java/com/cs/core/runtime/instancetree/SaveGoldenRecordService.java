package com.cs.core.runtime.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.goldenrecord.bucket.ISaveGoldenRecordService;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordTypeInfoModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IIdBucketIdModel;
import com.cs.core.runtime.interactor.model.goldenrecord.ISaveGoldenRecordRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

@Service
public class SaveGoldenRecordService
    extends AbstractSaveGoldenRecordService<ISaveGoldenRecordRequestModel, IGetKlassInstanceModel>
    implements ISaveGoldenRecordService {
  
  @Autowired
  protected Long goldenArticleKlassCounter;
  
  @Override
  public IGetKlassInstanceModel executeInternal(ISaveGoldenRecordRequestModel model)
      throws Exception
  {
    
    return super.executeInternal(model);
  }
  
  @Override
  protected Long getCounter()
  {
    return goldenArticleKlassCounter++;
  }

  @Override
  protected IGoldenRecordTypeInfoModel getTypeIdFromBucketId(IIdBucketIdModel idModel)
      throws Exception
  {
    return null;
  }

  @Override
  protected BaseType getBaseType()
  {
    return BaseType.ARTICLE;
  }
  
}
