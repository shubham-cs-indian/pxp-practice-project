package com.cs.core.runtime.interactor.usecase.workflow.callback;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IModel;

@Service
public class BGPCallBackHandler extends AbstractCallBackHandler implements IBGPCallBackHandler{

  @Override
  protected IModel executeInternal(IModel model) throws Exception
  {

    return null;
  }
}
