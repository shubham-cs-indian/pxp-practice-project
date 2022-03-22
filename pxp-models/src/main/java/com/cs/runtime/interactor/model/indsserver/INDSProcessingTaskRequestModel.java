package com.cs.runtime.interactor.model.indsserver;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.config.interactor.entity.indsserver.InDesignServerInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class INDSProcessingTaskRequestModel extends INDSTaskRequestModel implements IINDSProcessingTaskRequestModel {
  
  private static final long   serialVersionUID = 1L;
  private IINDSScriptRequestModel       indsScriptRequestModel;
  private IInDesignServerInstance       indsLoadBalancer;
  
  @Override
  public IInDesignServerInstance getIndsLoadBalancer()
  {
    
    return indsLoadBalancer;
  }

  @Override
  @JsonDeserialize(as = InDesignServerInstance.class)
  public void setIndsLoadBalancer(IInDesignServerInstance loadBalancerInstance)
  {
    this.indsLoadBalancer = loadBalancerInstance;
    
  }
  
  @Override
  public IINDSScriptRequestModel getScriptRequestModel()
  {
    if (indsScriptRequestModel == null) {
      indsScriptRequestModel = new INDSScriptRequestModel();
    }
    return indsScriptRequestModel;
  }
  
  @Override
  @JsonDeserialize(as = INDSScriptRequestModel.class)
  public void setScriptRequestModel(IINDSScriptRequestModel indsScriptRequestModel)
  {
    this.indsScriptRequestModel = indsScriptRequestModel;
  }
  
}