package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetEntityModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetTagsListByTagTypeStrategy extends OrientDBBaseStrategy
    implements IGetTagsListByTagTypeStrategy {
  
  public static final String useCase = "GetTagsListByType";
  
  @SuppressWarnings("unchecked")
  @Override
  public IListModel<IGetEntityModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(useCase, model, new TypeReference<ListModel<GetEntityModel>>()
    {
      
    });
  }
}
