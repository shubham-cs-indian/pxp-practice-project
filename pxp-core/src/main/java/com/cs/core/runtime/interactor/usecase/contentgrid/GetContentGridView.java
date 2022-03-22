package com.cs.core.runtime.interactor.usecase.contentgrid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.contentgrid.IGetContentGridViewService;
import com.cs.core.runtime.interactor.model.contentgrid.IGetContentGridKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.gridcontent.IGetContentGridRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetContentGridView extends AbstractRuntimeInteractor<IGetContentGridRequestModel, IGetContentGridKlassInstanceResponseModel>
    implements IGetContentGridView {
  
  @Autowired
  protected IGetContentGridViewService getContentGridViewService;
  
  @Override
  public IGetContentGridKlassInstanceResponseModel executeInternal(IGetContentGridRequestModel dataModel) throws Exception
  {
    return getContentGridViewService.execute(dataModel);
  }
}
