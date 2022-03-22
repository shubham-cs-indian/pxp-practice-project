package com.cs.dam.config.strategy.usecase.dtp.idsserver;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.interactor.model.idsserver.INDSPingTaskRequestModel;
import com.cs.dam.config.strategy.usecase.dtp.idsserver.IGetAllInDesignServerInstancesStrategy;

@Component("getAllInDesignServerInstancesStrategy")
public class OrientDBGetAllInDesignServerInstancesStrategy extends OrientDBBaseStrategy
    implements IGetAllInDesignServerInstancesStrategy {
  

  @Override
  public IINDSPingTaskRequestModel execute(IModel model) throws Exception
  {
    return super.execute(GET_ALL_INDS_INSTANCES, new HashMap<String, Object>(), INDSPingTaskRequestModel.class);
  }
  
}
