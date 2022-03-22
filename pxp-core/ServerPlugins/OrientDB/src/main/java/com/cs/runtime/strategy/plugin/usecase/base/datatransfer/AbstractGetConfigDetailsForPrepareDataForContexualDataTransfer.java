package com.cs.runtime.strategy.plugin.usecase.base.datatransfer;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForPrepareDataForContexualDataTransfer
    extends AbstractConfigDetails {
  
  public AbstractGetConfigDetailsForPrepareDataForContexualDataTransfer(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> selectedTaxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    
    return null;
  }
}
