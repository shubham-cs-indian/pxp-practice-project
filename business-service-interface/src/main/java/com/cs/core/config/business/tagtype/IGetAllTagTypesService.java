package com.cs.core.config.business.tagtype;

import com.cs.config.businessapi.base.IGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;

public interface IGetAllTagTypesService extends IGetConfigService<ITagTypeModel, IListModel<ITagTypeModel>> {
  
}
