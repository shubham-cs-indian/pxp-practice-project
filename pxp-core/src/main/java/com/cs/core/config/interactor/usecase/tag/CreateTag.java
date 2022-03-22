package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.businessapi.tag.ICreateTagService;
import com.cs.core.config.interactor.model.tag.ICreateTagResponseModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

@Service
public class CreateTag extends AbstractCreateConfigInteractor<ITagModel, ICreateTagResponseModel> implements ICreateTag {
  
  @Autowired
  ICreateTagService createTagService;
  
  @Override
  public ICreateTagResponseModel executeInternal(ITagModel dataModel) throws Exception
  {
    return createTagService.execute(dataModel);
  }
}
