package com.cs.core.config.strategy.usecase.globalpermissions;

import com.cs.core.config.interactor.model.configdetails.CoverFlowModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.assetinstance.ICoverFlowModel;
import com.cs.core.runtime.interactor.model.assetinstance.IUserIDAndTypesAndCoverFlowModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetCoverFlowForNatureKlassWithDownloadPermissionStrategy extends OrientDBBaseStrategy
    implements IGetCoverFlowForNatureKlassWithDownloadPermissionStrategy {
  
  @Override
  public IListModel<ICoverFlowModel> execute(IUserIDAndTypesAndCoverFlowModel model)
      throws Exception
  {
    return execute(OrientDBBaseStrategy.GET_COVERFLOW_FOR_NATURE_KLASS_WITH_DOWNLOAD_PERMISSION,
        model, new TypeReference<ListModel<CoverFlowModel>>()
        {
          
        });
  }
}
