package com.cs.core.config.interactor.model.role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.klass.Asset;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IEditorial;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.ITarget;
import com.cs.core.config.interactor.entity.klass.Market;
import com.cs.core.config.interactor.entity.klass.ProjectKlass;
import com.cs.core.config.interactor.entity.klass.TargetCollectionKlass;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.organization.IReferencedSystemModel;
import com.cs.core.config.interactor.model.organization.ReferencedSystemModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetRoleStrategyModel extends ConfigResponseWithAuditLogModel implements IGetRoleStrategyModel {
  
  private static final long                               serialVersionUID     = 1L;
  
  protected Collection<? extends IKlass>                  klasses              = new ArrayList<>();
  protected Collection<? extends IKlass>                  collections          = new ArrayList<>();
  protected Collection<? extends IKlass>                  sets                 = new ArrayList<>();
  protected Collection<? extends IAsset>                  assets               = new ArrayList<>();
  protected Collection<? extends IAsset>                  collectionAssets     = new ArrayList<>();
  protected Collection<? extends ITarget>                 markets              = new ArrayList<>();
  protected Collection<? extends ITarget>                 collectionTargets    = new ArrayList<>();
  protected Collection<? extends IEditorial>              editorials           = new ArrayList<>();
  protected Collection<? extends IEditorial>              collectionEditorials = new ArrayList<>();
  protected IRole                                         role;
  protected Map<String, IReferencedArticleTaxonomyModel>  referencedTaxonomies;
  protected List<? extends IConfigEntityInformationModel> referencedKlasses;
  protected Map<String, IConfigEntityInformationModel>    referencedEndpoints;
  protected Collection<? extends IUserInformationModel>   users;
  protected Map<String, IConfigEntityInformationModel>    referencedKPIs;
  protected Map<String, IReferencedSystemModel>           referencedSystems;
  protected List<String>                                  deletedUserNames;
  
  @Override
  public Collection<? extends IKlass> getArticles()
  {
    return klasses;
  }
  
  @JsonDeserialize(contentAs = ProjectKlass.class)
  @Override
  public void setArticles(Collection<? extends IKlass> klasses)
  {
    this.klasses = klasses;
  }
  
  @Override
  public Collection<? extends IKlass> getCollections()
  {
    return collections;
  }
  
  @JsonDeserialize(contentAs = ProjectKlass.class)
  @Override
  public void setCollections(Collection<? extends IKlass> collections)
  {
    this.collections = collections;
  }
  
  @Override
  public Collection<? extends IKlass> getSets()
  {
    return sets;
  }
  
  @JsonDeserialize(contentAs = ProjectKlass.class)
  @Override
  public void setSets(Collection<? extends IKlass> sets)
  {
    this.sets = sets;
  }
  
  @Override
  public Collection<? extends IAsset> getAssets()
  {
    return assets;
  }
  
  @JsonDeserialize(contentAs = Asset.class)
  @Override
  public void setAssets(Collection<? extends IAsset> assets)
  {
    this.assets = assets;
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
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public Collection<? extends IAsset> getCollectionAssets()
  {
    return collectionAssets;
  }
  
  @JsonDeserialize(contentAs = Asset.class)
  @Override
  public void setCollectionAssets(Collection<? extends IAsset> collectionAssets)
  {
    this.collectionAssets = collectionAssets;
  }
  
  @Override
  public Collection<? extends ITarget> getMarkets()
  {
    return markets;
  }
  
  @JsonDeserialize(contentAs = Market.class)
  @Override
  public void setMarkets(Collection<? extends ITarget> markets)
  {
    this.markets = markets;
  }
  
  @Override
  public Collection<? extends ITarget> getCollectionTargets()
  {
    return collectionTargets;
  }
  
  @JsonDeserialize(contentAs = TargetCollectionKlass.class)
  @Override
  public void setCollectionTargets(Collection<? extends ITarget> collectionTargets)
  {
    this.collectionTargets = collectionTargets;
  }
  
  @Override
  public Collection<? extends IEditorial> getEditorials()
  {
    return editorials;
  }
  
  @Override
  public void setEditorials(Collection<? extends IEditorial> editorials)
  {
    this.editorials = editorials;
  }
  
  @Override
  public Collection<? extends IEditorial> getCollectionEditorials()
  {
    return collectionEditorials;
  }
  
  @Override
  public void setCollectionEditorials(Collection<? extends IEditorial> collectionEditorials)
  {
    this.collectionEditorials = collectionEditorials;
  }
  
  @Override
  public List<? extends IConfigEntityInformationModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new ArrayList<>();
    }
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedKlasses(List<? extends IConfigEntityInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
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
  
  @Override
  public List<String> getDeletedUserNames() {
  	if (deletedUserNames == null) {
  	   deletedUserNames = new ArrayList<>();
  	}
  	return deletedUserNames;
  }

  @Override
  public void setDeletedUserNames(List<String> deletedUserNames) {
    this.deletedUserNames = deletedUserNames;
  }

}
