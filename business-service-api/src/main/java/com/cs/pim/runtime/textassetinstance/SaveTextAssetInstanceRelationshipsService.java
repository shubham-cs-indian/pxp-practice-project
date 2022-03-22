package com.cs.pim.runtime.textassetinstance;

import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.exception.textasset.TextAssetKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.runtime.interactor.model.relationship.ISaveRelationshipInstanceModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.klassinstance.AbstractSaveRelationshipInstances;

@Service
public class SaveTextAssetInstanceRelationshipsService
    extends AbstractSaveRelationshipInstances<ISaveRelationshipInstanceModel, IGetKlassInstanceModel>
    implements ISaveTextAssetInstanceRelationshipsService {
  
  @Override
  protected IGetKlassInstanceModel executeInternal(ISaveRelationshipInstanceModel klassInstancesModel) throws Exception
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
}
