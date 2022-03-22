package com.cs.core.config.strategy.usecase.klass.contentgrid;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.usecase.contentgrid.IGetContentGridPropertiesListStrategy;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetContentGridPropertiesListStrategy extends OrientDBBaseStrategy
    implements IGetContentGridPropertiesListStrategy {
  
  public static final String useCase = "GetContentGridPropertiesList";
  
  @Override
  public IListModel<IConfigEntityInformationModel> execute(IIdParameterModel model) throws Exception
  {
    return execute(useCase, model, new TypeReference<ListModel<ConfigEntityInformationModel>>()
    {
      
    });
  }
}
