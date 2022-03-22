package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.klass.IGetSectionInfoForKlassService;

@Service
public class GetSectionInfoForKlass
    extends AbstractGetConfigInteractor<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel>
    implements IGetSectionInfoForKlass {
  
  @Autowired
  protected IGetSectionInfoForKlassService getSectionInfoForKlassService;
  
  @Override
  protected IGetSectionInfoModel executeInternal(IGetSectionInfoForTypeRequestModel model)
      throws Exception
  {
    return getSectionInfoForKlassService.execute(model);
  }
}
