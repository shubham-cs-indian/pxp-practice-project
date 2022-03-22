package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.klass.IGetSectionInfoForKlassService;
import com.cs.core.config.strategy.usecase.klass.IGetSectionInfoForKlassStrategy;

@Service
public class GetSectionInfoForKlassService
    extends AbstractGetSectionInfoService<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel>
    implements IGetSectionInfoForKlassService {
  
  @Autowired
  protected IGetSectionInfoForKlassStrategy getSectionInfoForKlassStrategy;
  
  @Override
  public IGetSectionInfoModel execute(IGetSectionInfoForTypeRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IGetSectionInfoModel executeGetSectionInfo(IGetSectionInfoForTypeRequestModel model)
      throws Exception
  {
    return getSectionInfoForKlassStrategy.execute(model);
  }
}
