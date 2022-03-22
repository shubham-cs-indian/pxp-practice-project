package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GetOrCreateVariantContextsStrategy extends OrientDBBaseStrategy
    implements IGetOrCreateVariantContextsStrategy {
  
  @SuppressWarnings("unchecked")
  @Override
  public IListModel<ISaveVariantContextModel> execute(IListModel<ISaveVariantContextModel> model)
      throws Exception
  {
    Map<String, Object> requestList = new HashMap<>();
    requestList.put("list", model.getList());
    return execute(GET_OR_CREATE_VARIANT_CONTEXTS, requestList, ListModel.class);
  }
}
