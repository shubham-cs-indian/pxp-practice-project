package com.cs.core.config.strategy.usecase.grideditpropertylist;

import com.cs.core.config.interactor.model.grideditpropertylist.GetGridEditablePropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.IGetGridEditPropertyListSuccessModel;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("saveGridEditablePropertyListStrategy")
public class SaveGridEditablePropertyListStrategy extends OrientDBBaseStrategy
    implements ISaveGridEditablePropertyListStrategy {
  
  @Override
  public IGetGridEditPropertyListSuccessModel execute(ISaveGridEditablePropertyListModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.SAVE_GRID_EDITABLE_PROPERTY_LIST, model,
        GetGridEditablePropertyListSuccessModel.class);
  }
}
