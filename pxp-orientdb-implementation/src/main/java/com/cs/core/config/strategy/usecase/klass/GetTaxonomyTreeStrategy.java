package com.cs.core.config.strategy.usecase.klass;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.CategoryTreeInformationModel;
import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetTaxonomiesTreeStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component("getTaxonomyTreeStrategy")
public class GetTaxonomyTreeStrategy extends OrientDBBaseStrategy
    implements IGetTaxonomiesTreeStrategy {
  
  @Override
  public ICategoryTreeInformationModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(GET_TAXONOMY_TREE, requestMap, CategoryTreeInformationModel.class);
  }
}
