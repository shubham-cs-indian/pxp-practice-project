package com.cs.runtime.interactor.model.indsserver;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;

public interface IINDSProcessingTaskRequestModel extends IINDSTaskRequestModel {
  
  public final static String INDS_SCRIPT_REQUEST_MODEL    = "indsScriptRequestModel";
  public final static String INDS_LOAD_BALANCER           = "indsLoadBalancer";
  
  public IInDesignServerInstance getIndsLoadBalancer();
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance);
  
  public IINDSScriptRequestModel getScriptRequestModel();
  public void setScriptRequestModel(IINDSScriptRequestModel iNDSScriptRequestModel);
  
}