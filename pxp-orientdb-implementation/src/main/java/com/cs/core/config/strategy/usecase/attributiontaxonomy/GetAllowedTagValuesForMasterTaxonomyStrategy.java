package com.cs.core.config.strategy.usecase.attributiontaxonomy;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.interactor.model.tag.TagModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetAllowedTagValuesForMasterTaxonomyStrategy extends OrientDBBaseStrategy
    implements IGetAllowedTagValuesForMasterTaxonomyStrategy {
  
  @Override
  public IListModel<ITagModel> execute(IIdParameterModel model) throws Exception
  {
    return super.execute(OrientDBBaseStrategy.GET_ALLOWED_TAG_VALUES, model,
        new TypeReference<ListModel<TagModel>>()
        {
          
        });
  }
}
