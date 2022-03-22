package com.cs.core.config.idsserver;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.config.businessapi.base.AbstractConfigService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.dam.config.interactor.model.idsserver.IINDSPingTaskRequestModel;
import com.cs.dam.config.strategy.usecase.dtp.idsserver.IGetAllInDesignServerInstancesStrategy;
import com.cs.dam.config.strategy.usecase.idsserver.IPerformINDSPingTaskStrategy;

/**
 * Get all configured server instances to load balancer
 * 
 * @author mrunali.dhenge
 *
 * @param <P>
 * @param <R>
 */
public abstract class AbstractIDSNServerTask<P extends IModel, R extends IModel> extends AbstractConfigService<P, R> {
  
  @Autowired
  protected IGetAllInDesignServerInstancesStrategy getAllInDesignServerInstancesStrategy;
  
  @Autowired
  protected IPerformINDSPingTaskStrategy           performINDSPingAndAddTaskStrategy;
  
  public R executeInternal(P requestData) throws Exception
  {
    // gets all connected indesign servers
    IINDSPingTaskRequestModel pingTaskRequestModel = getAllInDesignServerInstancesStrategy.execute(null);
    // Throws Exception if load balancer server is not running
    if (pingTaskRequestModel.getIndsLoadBalancer() == null) {
      
      return null;
    }
    // Throws Exception if no Instance Indesing server is not running
    if (pingTaskRequestModel.getServersToPing().isEmpty()) {
      
      return null;
    }
    // Sends all configured server instances to load balancer and gets their
    // status.
    performINDSPingAndAddTaskStrategy.execute(pingTaskRequestModel);
    
    return null;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
}
