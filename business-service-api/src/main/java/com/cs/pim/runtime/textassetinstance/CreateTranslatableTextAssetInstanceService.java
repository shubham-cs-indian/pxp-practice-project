package com.cs.pim.runtime.textassetinstance;

import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.abstrct.AbstractCreateTranslatableInstanceService;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.textassetinstance.ITextAssetInstanceSaveModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateTranslatableTextAssetInstanceService
    extends AbstractCreateTranslatableInstanceService<ITextAssetInstanceSaveModel, IGetKlassInstanceModel>
    implements ICreateTranslatableTextAssetInstanceService {
  
  @Autowired protected Long textassetKlassCounter;
  
  @Override
  protected IGetKlassInstanceModel executeInternal(
      ITextAssetInstanceSaveModel klassInstancesModel) throws Exception
  {
    IGetKlassInstanceModel response = null;
    try {
      response = super.executeInternal(klassInstancesModel);
    }
    catch (KlassNotFoundException e) {
      throw new TextAssetKlassNotFoundException(e);
    }
    return response;
  }
  
  @Override
  protected Long getCounter()
  {
    return textassetKlassCounter++;
  }

  }
