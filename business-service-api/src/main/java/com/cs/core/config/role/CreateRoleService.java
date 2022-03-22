package com.cs.core.config.role;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.role.CreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateRoleModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.strategy.usecase.role.ICreateRoleStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CreateRoleService extends AbstractCreateConfigService<ICreateRoleModel, ICreateOrSaveRoleResponseModel>
    implements ICreateRoleService {
  
  @Autowired
  ICreateRoleStrategy createRoleStrategy;
  
  @Override
  public ICreateOrSaveRoleResponseModel executeInternal(ICreateRoleModel roleModel) throws Exception
  {
    
    String roleId = roleModel.getId();
    ;
    if (roleId == null || roleId.equals("")) {
      roleId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.ROLE.getPrefix());
      roleModel.setId(roleId);
    }
    String roleType = roleModel.getRoleType();
    if (roleType == null || roleType.equals("")) {
      roleModel.setRoleType(CommonConstants.USER);
    }
    IGetRoleStrategyModel roleStrategyModel = createRoleStrategy.execute(roleModel);
    
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
    
    ICreateOrSaveRoleResponseModel getRoleModel = new CreateOrSaveRoleResponseModel();
    getRoleModel.setArticles(articleModels);
    getRoleModel.setCollections(collectionModels);
    getRoleModel.setSets(setModels);
    getRoleModel.setAssets(assetModels);
    getRoleModel.setCollectionAssets(collectionAssetModels);
    getRoleModel.setRole(roleStrategyModel.getRole());
    getRoleModel.setMarkets(marketModels);
    getRoleModel.setAuditLogInfo(roleStrategyModel.getAuditLogInfo());
    return getRoleModel;
  }
}
