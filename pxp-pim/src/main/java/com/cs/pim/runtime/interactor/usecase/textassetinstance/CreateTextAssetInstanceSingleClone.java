package com.cs.pim.runtime.interactor.usecase.textassetinstance;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceSingleCloneModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.usecase.instance.AbstractCreateInstanceSingleClone;
import com.cs.pim.runtime.textassetinstance.ICreateTextAssetInstanceSingleCloneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTextAssetInstanceSingleClone extends AbstractCreateInstanceSingleClone<ICreateKlassInstanceSingleCloneModel, IGetKlassInstanceModel>
    implements ICreateTextAssetInstanceSingleClone {

  @Autowired
  protected ICreateTextAssetInstanceSingleCloneService createTextAssetInstanceSingleCloneService;

  @Override
  protected IGetKlassInstanceModel executeInternal(ICreateKlassInstanceSingleCloneModel dataModel) throws Exception
  {
    return createTextAssetInstanceSingleCloneService.execute(dataModel);
  }

}
