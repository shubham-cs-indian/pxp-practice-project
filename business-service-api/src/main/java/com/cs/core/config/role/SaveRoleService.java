package com.cs.core.config.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.role.CreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.ICreateOrSaveRoleResponseModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;
import com.cs.core.config.strategy.usecase.role.ISaveRoleStrategy;

@Service
public class SaveRoleService extends AbstractSaveConfigService<IRoleSaveModel, ICreateOrSaveRoleResponseModel> implements ISaveRoleService {
  
  @Autowired
  ISaveRoleStrategy saveRoleStrategy;
  
  @Autowired
  SessionRegistry sessionRegistry;
  
  @Override
  public ICreateOrSaveRoleResponseModel executeInternal(IRoleSaveModel model) throws Exception
  {
    
    IGetRoleStrategyModel roleStrategyModel = saveRoleStrategy.execute(model);
    expireUserSessions(roleStrategyModel.getDeletedUserNames());
    
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
    getRoleModel.setReferencedTaxonomies(roleStrategyModel.getReferencedTaxonomies());
    getRoleModel.setReferencedKlasses(roleStrategyModel.getReferencedKlasses());
    getRoleModel.setReferencedKPIs(roleStrategyModel.getReferencedKPIs());
    getRoleModel.setReferencedEndpoints(roleStrategyModel.getReferencedEndpoints());
    getRoleModel.setAuditLogInfo(roleStrategyModel.getAuditLogInfo());
    return getRoleModel;
  }
  
  public void expireUserSessions(List<String> usernames) {
	  List<Object> principals = null;
	  if(usernames.size() > 0) {
	    principals = sessionRegistry.getAllPrincipals();
	  }
		for (String username : usernames) {
			for (Object principal : principals) {
				if (principal.equals(username)) {
					for (SessionInformation information : sessionRegistry.getAllSessions(principal, true)) {
						 information.expireNow();
					}
				}
			}
		}
	}

}
