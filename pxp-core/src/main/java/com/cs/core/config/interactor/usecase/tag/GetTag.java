package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.tag.IGetTagService;
import com.cs.core.config.interactor.model.tag.IGetTagModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

@Service
public class GetTag extends AbstractGetConfigInteractor<IGetTagModel, ITagModel>
    implements IGetTag {
  
  @Autowired
  IGetTagService getTagService;
  
  @Override
  public ITagModel executeInternal(IGetTagModel dataModel) throws Exception
  {
    return getTagService.execute(dataModel);
  }
}
