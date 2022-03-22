package com.cs.core.runtime.interactor.model.instancetree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.permission.FunctionPermissionModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetNewInstanceTreeResponseModel implements IGetNewInstanceTreeResponseModel{

  private static final long serialVersionUID = 1L;
  protected Map<String, IReferencedKlassDetailStrategyModel>               referencedKlasses;
  protected List<IKlassInstanceInformationModel>                           childrens;
  protected Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets;
  protected Integer                                                        from;
  protected Long                                                           totalContents;
  protected List<IAppliedSortModel>                                        appliedSortData;
  protected Map<String, IAttribute>                                        referencedAttributes;
  protected Map<String, ITag>                                              referencedTags;
  protected List<INewApplicableFilterModel>                                filterData;
  protected IFunctionPermissionModel                                       functionPermission;
  
  @Override
  @JsonDeserialize(contentAs=ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }

  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }

  @Override
  public List<IKlassInstanceInformationModel> getChildren()
  {
    if(childrens == null) {
      childrens = new ArrayList<>();
    }
    return childrens;
  }

  @Override
  @JsonDeserialize(contentAs=KlassInstanceInformationModel.class)
  public void setChildren(List<IKlassInstanceInformationModel> childrens)
  {
    this.childrens = childrens;
  }

  @Override
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    if(referencedAssets == null) {
      referencedAssets = new HashMap<String, IAssetAttributeInstanceInformationModel>();
    }
    return referencedAssets;
  }

  @Override
  @JsonDeserialize(contentAs=AssetAttributeInstanceInformationModel.class)
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }

  @Override
  public Integer getFrom()
  {
    return from;
  }

  @Override
  public void setFrom(Integer from)
  {
    this.from = from;
  }
  
  @Override
  public Long getTotalContents()
  {
    return totalContents;
  }

  @Override
  public void setTotalContents(Long totalContents)
  {
    this.totalContents = totalContents;
  }

  @Override
  public List<IAppliedSortModel> getAppliedSortData()
  {
    if(appliedSortData == null){
      appliedSortData = new ArrayList<>();
    }
    return appliedSortData;
  }

  @Override
  @JsonDeserialize(contentAs=AppliedSortModel.class)
  public void setAppliedSortData(List<IAppliedSortModel> appliedSortData)
  {
    this.appliedSortData = appliedSortData;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<INewApplicableFilterModel> getFilterData()
  {
    if(filterData == null) {
      filterData = new ArrayList<INewApplicableFilterModel>();
    }
    return this.filterData;
  }

  @Override
  @JsonDeserialize(contentAs = NewApplicableFilterModel.class)
  public void setFilterData(List<INewApplicableFilterModel> filterData)
  {
    this.filterData = filterData;
  }
  
  @Override
  public IFunctionPermissionModel getFunctionPermission()
  {
    return functionPermission;
  }
  
  @Override
  @JsonDeserialize(as = FunctionPermissionModel.class)
  public void setFunctionPermission(IFunctionPermissionModel functionPermission)
  {
    this.functionPermission = functionPermission;
  }
}
