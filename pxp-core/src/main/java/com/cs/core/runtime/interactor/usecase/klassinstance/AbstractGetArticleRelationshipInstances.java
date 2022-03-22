package com.cs.core.runtime.interactor.usecase.klassinstance;

import com.cs.core.config.interactor.model.relationship.IGetReferencedRelationshipsAndElementsModel;
import com.cs.core.config.strategy.usecase.relationship.IGetRelationshipsAndReferencedElementsByIdsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdsListWithUserModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.relationship.GetAllRelationshipsModel;
import com.cs.core.runtime.interactor.model.relationship.IGetAllRelationshipInstancesModel;
import com.cs.core.runtime.interactor.model.relationship.IGetAllRelationshipsModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetArticleRelationshipInstancesStrategy;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("unchecked")
public abstract class AbstractGetArticleRelationshipInstances<P extends IKlassRelationshipInstancesModel, R extends IGetAllRelationshipInstancesModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  protected ISessionContext                                     context;
  
  @Autowired
  protected IGetRelationshipsAndReferencedElementsByIdsStrategy getRelationshipsAndReferencedElementsByIdsStrategy;
  
  @Autowired
  protected IGetArticleRelationshipInstancesStrategy            getArticleRelationshipInstancesStrategy;
  
  @Override
  protected R executeInternal(IKlassRelationshipInstancesModel model) throws Exception
  {
    IIdsListWithUserModel ids = new IdsListWithUserModel();
    ids.setIds(model.getTypes());
    ids.setUserId(context.getUserId());
    
    IGetReferencedRelationshipsAndElementsModel returnModel = getRelationshipsAndReferencedElementsByIdsStrategy
        .execute(ids);
    IGetAllRelationshipsModel getAllRelationshipsModel = new GetAllRelationshipsModel();
    getAllRelationshipsModel.setId(model.getId());
    getAllRelationshipsModel.setRelationshipIds(returnModel.getRelationshipIds());
    getAllRelationshipsModel.setNatureRelationshipIds(returnModel.getNatureRelationshipIds());
    getAllRelationshipsModel.setKlassIds(returnModel.getKlassIds());
    getAllRelationshipsModel.setSize(model.getSize());
    getAllRelationshipsModel.setCurrentUserId(context.getUserId());
    
    IGetAllRelationshipInstancesModel returnInstancesModel = getArticleRelationshipInstancesStrategy
        .execute(getAllRelationshipsModel);
    returnInstancesModel.setId(model.getId());
    returnInstancesModel.setReferencedElements(returnModel.getReferencedElements());
    returnInstancesModel.setReferencedRelationships(returnModel.getReferencedRelationships());
    returnInstancesModel
        .setReferencedNatureRelationships(returnModel.getReferencedNatureRelationships());
    returnInstancesModel
        .setReferencedRelationshipsMapping(returnModel.getReferencedRelationshipsMapping());
    
    return (R) returnInstancesModel;
  }
}
