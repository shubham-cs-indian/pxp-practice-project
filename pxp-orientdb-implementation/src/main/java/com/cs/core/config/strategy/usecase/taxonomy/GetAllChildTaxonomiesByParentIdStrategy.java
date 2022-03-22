package com.cs.core.config.strategy.usecase.taxonomy;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetAllChildTaxonomiesByParentIdStrategy extends OrientDBBaseStrategy implements IGetAllChildTaxonomiesByParentIdStrategy {
  
  public static final String useCase = "GetAllChildTaxonomiesByParentId";
  
  @Override
  public IListModel<IIdLabelCodeModel> execute(IGetChildMajorTaxonomiesRequestModel model) throws Exception
  {
    return execute(useCase, model, new TypeReference<ListModel<IdLabelCodeModel>>()
    {
      
    });
  }
}
