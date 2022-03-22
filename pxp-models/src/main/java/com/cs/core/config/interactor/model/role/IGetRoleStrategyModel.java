package com.cs.core.config.interactor.model.role;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IEditorial;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.organization.IReferencedSystemModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IGetRoleStrategyModel extends IConfigResponseWithAuditLogModel {
  
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String REFERENCED_ENDPOINTS  = "referencedEndpoints";
  public static final String REFERENCED_KPIS       = "referencedKPIs";
  public static final String REFERENCED_SYSTEMS    = "referencedSystems";
  public static final String DELETED_USER_NAMES    = "deletedUserNames";
  
  public List<String> getDeletedUserNames();
  
  public void setDeletedUserNames(List<String> deletedUserNames);

  public IRole getRole();
  
  public void setRole(IRole role);
  
  public Collection<? extends IKlass> getArticles();
  
  public void setArticles(Collection<? extends IKlass> klasses);
  
  public Collection<? extends IAsset> getAssets();
  
  public void setAssets(Collection<? extends IAsset> assets);
  
  public Collection<? extends IAsset> getCollectionAssets();
  
  public void setCollectionAssets(Collection<? extends IAsset> collectionAssets);
  
  public Collection<? extends IKlass> getCollections();
  
  public void setCollections(Collection<? extends IKlass> klasses);
  
  public Collection<? extends IKlass> getSets();
  
  public void setSets(Collection<? extends IKlass> klasses);
  
  public Collection<? extends ITarget> getMarkets();
  
  public void setMarkets(Collection<? extends ITarget> markets);
  
  public Collection<? extends ITarget> getCollectionTargets();
  
  public void setCollectionTargets(Collection<? extends ITarget> collectionTargets);
  
  public Collection<? extends IEditorial> getEditorials();
  
  public void setEditorials(Collection<? extends IEditorial> editorials);
  
  public Collection<? extends IEditorial> getCollectionEditorials();
  
  public void setCollectionEditorials(Collection<? extends IEditorial> collectionEditorials);
  
  public List<? extends IConfigEntityInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(List<? extends IConfigEntityInformationModel> referencedKlasses);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public Map<String, IConfigEntityInformationModel> getReferencedEndpoints();
  
  public void setReferencedEndpoints(
      Map<String, IConfigEntityInformationModel> referencedEndpoints);
  
  public Map<String, IConfigEntityInformationModel> getReferencedKPIs();
  
  public void setReferencedKPIs(Map<String, IConfigEntityInformationModel> referencedKPIs);
  
  public Map<String, IReferencedSystemModel> getReferencedSystems();
  
  public void setReferencedSystems(Map<String, IReferencedSystemModel> referencedSystems);
}
