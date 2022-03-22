package com.cs.core.config.role;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IEditorial;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.role.GetRoleModel;
import com.cs.core.config.interactor.model.role.IGetRoleModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.usecase.role.IGetRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class GetRoleService extends AbstractGetConfigService<IIdParameterModel, IGetRoleModel>
    implements IGetRoleService {
  
  @Autowired
  IGetRoleStrategy getRoleStrategy;
  
  @Override
  public IGetRoleModel executeInternal(IIdParameterModel roleModel) throws Exception
  {
    IGetRoleStrategyModel roleStrategyModel = getRoleStrategy.execute(roleModel);
    
    Collection<? extends IKlass> articles = roleStrategyModel.getArticles();
    Collection<IKlassInformationModel> articleModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : articles) {
      articleModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends IKlass> collections = roleStrategyModel.getCollections();
    Collection<IKlassInformationModel> collectionModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : collections) {
      collectionModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends IKlass> sets = roleStrategyModel.getSets();
    Collection<IKlassInformationModel> setModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : sets) {
      setModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends IAsset> assets = roleStrategyModel.getAssets();
    Collection<IKlassInformationModel> assetModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : assets) {
      assetModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends IAsset> collectionAsset = roleStrategyModel.getCollectionAssets();
    Collection<IKlassInformationModel> collectionAssetModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : collectionAsset) {
      collectionAssetModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends ITarget> markets = roleStrategyModel.getMarkets();
    Collection<IKlassInformationModel> marketModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : markets) {
      marketModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends ITarget> collectionTargets = roleStrategyModel.getCollectionTargets();
    Collection<IKlassInformationModel> collectionTargetModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : collectionTargets) {
      collectionTargetModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends IEditorial> editorials = roleStrategyModel.getEditorials();
    Collection<IKlassInformationModel> editorialModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : editorials) {
      editorialModels.add(new KlassInformationModel(iKlass));
    }
    
    Collection<? extends IEditorial> collectionEditorials = roleStrategyModel
        .getCollectionEditorials();
    Collection<IKlassInformationModel> collectionEditorialModels = new ArrayList<IKlassInformationModel>();
    for (IKlass iKlass : collectionEditorials) {
      collectionEditorialModels.add(new KlassInformationModel(iKlass));
    }
    
    IGetRoleModel getRoleModel = new GetRoleModel();
    getRoleModel.setArticles(articleModels);
    getRoleModel.setCollections(collectionModels);
    getRoleModel.setSets(setModels);
    getRoleModel.setAssets(assetModels);
    getRoleModel.setCollectionAssets(collectionAssetModels);
    getRoleModel.setMarkets(marketModels);
    getRoleModel.setCollectionTargets(collectionTargetModels);
    getRoleModel.setEditorials(editorialModels);
    getRoleModel.setCollectionEditorials(collectionEditorialModels);
    getRoleModel.setRole(roleStrategyModel.getRole());
    getRoleModel.setReferencedTaxonomies(roleStrategyModel.getReferencedTaxonomies());
    getRoleModel.setReferencedKlasses(roleStrategyModel.getReferencedKlasses());
    getRoleModel.setReferencedEndpoints(roleStrategyModel.getReferencedEndpoints());
    getRoleModel.setReferencedKPIs(roleStrategyModel.getReferencedKPIs());
    return getRoleModel;
  }
}
