package com.cs.core.config.interactor.model.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.interactor.model.organization.IReferencedSystemModel;
import com.cs.core.config.interactor.model.organization.ReferencedSystemModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetRoleModel implements IGetRoleModel {
  
  private static final long                               serialVersionUID     = 1L;
  
  protected Collection<? extends IKlassInformationModel>  klasses              = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  collections          = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  sets                 = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  assets               = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  collectionAssets     = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  markets              = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  collectionTargets    = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  tasks                = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  editorials           = new ArrayList<>();
  protected Collection<? extends IKlassInformationModel>  collectionEditorials = new ArrayList<>();
  protected IRole                                         role;
  protected Map<String, IReferencedArticleTaxonomyModel>  referencedTaxonomies;
  protected List<? extends IConfigEntityInformationModel> referencedKlasses;
  protected Map<String, IConfigEntityInformationModel>    referencedEndpoints;
  protected Map<String, IConfigEntityInformationModel>    referencedKPIs;
  protected Collection<? extends IUserInformationModel>   users;
  protected Map<String, IReferencedSystemModel>           referencedSystems;
  
  @Override
  public Collection<? extends IKlassInformationModel> getArticles()
  {
    return klasses;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setArticles(Collection<? extends IKlassInformationModel> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getCollections()
  {
    return collections;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setCollections(Collection<? extends IKlassInformationModel> collections)
  {
    this.collections = collections;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getSets()
  {
    return sets;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setSets(Collection<? extends IKlassInformationModel> sets)
  {
    this.sets = sets;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getAssets()
  {
    return assets;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setAssets(Collection<? extends IKlassInformationModel> assets)
  {
    this.assets = assets;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getMarkets()
  {
    return markets;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setMarkets(Collection<? extends IKlassInformationModel> markets)
  {
    this.markets = markets;
  }
  
  @Override
  public IRole getRole()
  {
    return role;
  }
  
  @Override
  public void setRole(IRole role)
  {
    this.role = role;
  }
  
  @Override
  public Collection<? extends IUserInformationModel> getUsers()
  {
    if (users == null) {
      users = new ArrayList<IUserInformationModel>();
    }
    return users;
  }
  
  @Override
  public void setUsers(Collection<? extends IUserInformationModel> users)
  {
    this.users = users;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getCollectionAssets()
  {
    return collectionAssets;
  }
  
  @Override
  public void setCollectionAssets(Collection<? extends IKlassInformationModel> collectionAssets)
  {
    this.collectionAssets = collectionAssets;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getCollectionTargets()
  {
    return collectionTargets;
  }
  
  @Override
  public void setCollectionTargets(Collection<? extends IKlassInformationModel> collectionTargets)
  {
    this.collectionTargets = collectionTargets;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getTasks()
  {
    return tasks;
  }
  
  @Override
  public void setTasks(Collection<? extends IKlassInformationModel> tasks)
  {
    this.tasks = tasks;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getEditorials()
  {
    return editorials;
  }
  
  @Override
  public void setEditorials(Collection<? extends IKlassInformationModel> editorials)
  {
    this.editorials = editorials;
  }
  
  @Override
  public Collection<? extends IKlassInformationModel> getCollectionEditorials()
  {
    return collectionEditorials;
  }
  
  @Override
  public void setCollectionEditorials(
      Collection<? extends IKlassInformationModel> collectionEditorials)
  {
    this.collectionEditorials = collectionEditorials;
  }
  
  @Override
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = ReferencedArticleTaxonomyModel.class)
  @Override
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public List<? extends IConfigEntityInformationModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new ArrayList<>();
    }
    return referencedKlasses;
  }
  
  @Override
  public void setReferencedKlasses(List<? extends IConfigEntityInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedEndpoints()
  {
    return referencedEndpoints;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedEndpoints(Map<String, IConfigEntityInformationModel> referencedEndpoints)
  {
    this.referencedEndpoints = referencedEndpoints;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedKPIs()
  {
    return referencedKPIs;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedKPIs(Map<String, IConfigEntityInformationModel> referencedKPIs)
  {
    this.referencedKPIs = referencedKPIs;
  }
  
  @Override
  public Map<String, IReferencedSystemModel> getReferencedSystems()
  {
    return referencedSystems;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedSystemModel.class)
  public void setReferencedSystems(Map<String, IReferencedSystemModel> referencedSystems)
  {
    this.referencedSystems = referencedSystems;
  }
}
