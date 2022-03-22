package com.cs.core.config.strategy.usecase.tag;

import com.cs.core.config.interactor.model.configdetails.IGetEntityModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.datarule.IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel;
import com.cs.core.config.interactor.model.klass.GetEntityModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetAvailableMasterTagsForArticleTaxonomyStrategy extends OrientDBBaseStrategy
    implements IGetAvailableMasterTagsForArticleTaxonomyStrategy {
  
  @Override
  public IListModel<IGetEntityModel> execute(
      IGetAllowedMasterTagsForHierarchyTaxonomyRequestModel model) throws Exception
  {
    return execute(GET_AVAILABLE_MASTER_TAGS_FOR_ARTICLE_TAXONOMY, model,
        new TypeReference<ListModel<GetEntityModel>>()
        {
          
        });
  }
}
