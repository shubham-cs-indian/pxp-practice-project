package com.cs.dam.config.strategy.usecase.downloadtracker;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.dam.config.interactor.model.downloadtracker.IGetLabelsByIdsRequestModel;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class GetLabelsByIdsStrategy extends OrientDBBaseStrategy
    implements IGetLabelsByIdsStrategy {

  @Override
  public IListModel<IIdLabelCodeModel> execute(IGetLabelsByIdsRequestModel model) throws Exception
  {
    return execute(GET_LABELS_BY_IDS, model, new TypeReference<ListModel<IdLabelCodeModel>>() {});
  }

}
