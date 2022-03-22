package com.cs.core.config.interactor.model.role;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.organization.IReferencedSystemModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IGetRoleModel extends IModel {
  
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  public static final String ROLE                  = "role";
  public static final String REFERENCED_ENDPOINTS  = "referencedEndpoints";
  public static final String REFERENCED_KPIS       = "referencedKPIs";
  public static final String REFERENCED_SYSTEMS    = "referencedSystems";
  
  public IRole getRole();
  
  public void setRole(IRole role);
  
  public Collection<? extends IKlassInformationModel> getArticles();
  
  public void setArticles(Collection<? extends IKlassInformationModel> klasses);
  
  public Collection<? extends IKlassInformationModel> getAssets();
  
  public void setAssets(Collection<? extends IKlassInformationModel> assets);
  
  public Collection<? extends IKlassInformationModel> getCollectionAssets();
  
  public void setCollectionAssets(Collection<? extends IKlassInformationModel> collectionAssets);
  
  public Collection<? extends IKlassInformationModel> getMarkets();
  
  public void setMarkets(Collection<? extends IKlassInformationModel> markets);
  
  public Collection<? extends IKlassInformationModel> getCollectionTargets();
  
  public void setCollectionTargets(Collection<? extends IKlassInformationModel> collectionTargets);
  
  public Collection<? extends IUserInformationModel> getUsers();
  
  public void setUsers(Collection<? extends IUserInformationModel> users);
  
  public Collection<? extends IKlassInformationModel> getCollections();
  
  public void setCollections(Collection<? extends IKlassInformationModel> klasses);
  
  public Collection<? extends IKlassInformationModel> getSets();
  
  public void setSets(Collection<? extends IKlassInformationModel> klasses);
  
  public Collection<? extends IKlassInformationModel> getTasks();
  
  public void setTasks(Collection<? extends IKlassInformationModel> tasks);
  
  public Collection<? extends IKlassInformationModel> getEditorials();
  
  public void setEditorials(Collection<? extends IKlassInformationModel> editorials);
  
  public Collection<? extends IKlassInformationModel> getCollectionEditorials();
  
  public void setCollectionEditorials(
      Collection<? extends IKlassInformationModel> collectionEditorials);
  
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
  
  public void setReferencedSystems(Map<String, IReferencedSystemModel> referenceSystems);
}
