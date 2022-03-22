package com.cs.core.config.strategy.usecase.grideditpropertylist;

import com.cs.core.config.interactor.model.grideditpropertylist.GetGridEditablePropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListRequestModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("getAllGridEditablePropertyListWithAvaliableSequenceStrategy")
public class GetAllGridEditablePropertyListWithAvaliableSequenceStrategy extends
    OrientDBBaseStrategy implements IGetAllGridEditablePropertyListWithAvaliableSequenceStrategy {
  
  @Override
  public IGetGridEditPropertyListSuccessModel execute(IGetGridEditPropertyListRequestModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_ALL_GRID_EDITABLE_PROPERTY_LIST_WITH_AVALIABLE_SEQUENCE,
        model, GetGridEditablePropertyListSuccessModel.class);
  }
}
