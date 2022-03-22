package com.cs.core.config.businessapi.attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.attribute.IGetAllAttributeListStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IAttributeInfoModel;

@Service
public class GetAttributeListService extends AbstractGetConfigService<IIdParameterModel, IListModel<IAttributeInfoModel>>
    implements IGetAttributeListService {
  
  @Autowired
  IGetAllAttributeListStrategy orientDBGetAttributeListStrategy;
  
  @Override
  public IListModel<IAttributeInfoModel> executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return orientDBGetAttributeListStrategy.execute(dataModel);
  }
}
