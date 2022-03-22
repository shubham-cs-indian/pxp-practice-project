package com.cs.core.config.interactor.usecase.tagtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.business.tagtype.IGetAllTagTypesService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;

@Service
public class GetAllTagTypes
    extends AbstractGetConfigInteractor<ITagTypeModel, IListModel<ITagTypeModel>>
    implements IGetAllTagTypes {
  
  @Autowired
  IGetAllTagTypesService getAllTagTypesService;
  
  @Override
  public IListModel<ITagTypeModel> executeInternal(ITagTypeModel dataModel) throws Exception
  {
    return getAllTagTypesService.execute(dataModel);
  }
}
