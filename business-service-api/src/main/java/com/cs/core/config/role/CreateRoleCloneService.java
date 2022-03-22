package com.cs.core.config.role;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.role.CreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateRoleCloneModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.usecase.role.ICreateRoleCloneStrategy;

@Service
public class CreateRoleCloneService
    extends AbstractCreateConfigService<ICreateRoleCloneModel, ICreateOrSaveRoleResponseModel>
    implements ICreateRoleCloneService {
  
  @Autowired
  ICreateRoleCloneStrategy createRoleCloneStrategy;

  @Override
  public ICreateOrSaveRoleResponseModel executeInternal(ICreateRoleCloneModel dataModel)
      throws Exception
  {
    IGetRoleStrategyModel roleStrategyModel = createRoleCloneStrategy.execute(dataModel);
    return prepareResponseModel(roleStrategyModel);
  }

  private ICreateOrSaveRoleResponseModel prepareResponseModel(
      IGetRoleStrategyModel roleStrategyModel)
  {
    ICreateOrSaveRoleResponseModel getRoleModel = new CreateOrSaveRoleResponseModel();
    
    getRoleModel.setArticles(fillModel(roleStrategyModel.getArticles()));
    getRoleModel.setCollections(fillModel(roleStrategyModel.getCollections()));
    getRoleModel.setSets(fillModel(roleStrategyModel.getSets()));
    getRoleModel.setAssets(fillModel(roleStrategyModel.getAssets()));
    getRoleModel.setCollectionAssets(fillModel(roleStrategyModel.getCollectionAssets()));
    getRoleModel.setMarkets(fillModel(roleStrategyModel.getMarkets()));
    getRoleModel.setCollectionTargets(fillModel(roleStrategyModel.getCollectionTargets()));
    getRoleModel.setEditorials(fillModel(roleStrategyModel.getEditorials()));
    getRoleModel.setCollectionEditorials(fillModel(roleStrategyModel.getCollectionEditorials()));
    getRoleModel.setRole(roleStrategyModel.getRole());
    getRoleModel.setReferencedTaxonomies(roleStrategyModel.getReferencedTaxonomies());
    getRoleModel.setReferencedKlasses(roleStrategyModel.getReferencedKlasses());
    getRoleModel.setReferencedEndpoints(roleStrategyModel.getReferencedEndpoints());
    getRoleModel.setReferencedKPIs(roleStrategyModel.getReferencedKPIs());
    getRoleModel.setAuditLogInfo(roleStrategyModel.getAuditLogInfo());
   
    return getRoleModel;
  }
  
  private Collection<IKlassInformationModel> fillModel(Collection<? extends IKlass> collection)
  {
    Collection<IKlassInformationModel> models = new ArrayList<IKlassInformationModel>();
    collection.forEach(entity -> models.add(new KlassInformationModel(entity)));
    return models;
  }
  
}
