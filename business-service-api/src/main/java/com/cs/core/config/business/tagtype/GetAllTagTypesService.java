package com.cs.core.config.business.tagtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.config.strategy.usecase.tagtype.IGetAllTagTypesStrategy;

@Service
public class GetAllTagTypesService extends AbstractGetConfigService<ITagTypeModel, IListModel<ITagTypeModel>>
    implements IGetAllTagTypesService {
  
  @Autowired
  IGetAllTagTypesStrategy neo4jGetAllTagTypesStrategy;
  
  @Override
  public IListModel<ITagTypeModel> executeInternal(ITagTypeModel dataModel) throws Exception
  {
    return neo4jGetAllTagTypesStrategy.execute(dataModel);
  }
}
