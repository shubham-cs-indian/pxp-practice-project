package com.cs.core.config.interactor.usecase.tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.businessapi.tag.IGetAllTagsService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;

@Service
public class GetAllTags extends AbstractGetConfigInteractor<ITagModel, IListModel<ITagModel>>
    implements IGetAllTags {
  
  @Autowired
  IGetAllTagsService getAllTagsService;
  
  @Override
  public IListModel<ITagModel> executeInternal(ITagModel dataModel) throws Exception
  {
    return getAllTagsService.execute(dataModel);
  }

}
