
package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

public interface IGetNewInstanceTreeResponseModel extends IModel {
  
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String CHILDREN              = "children";
  public static final String REFERENCED_ASSETS     = "referencedAssets";
  public static final String FROM                  = "from";
  public static final String TOTAL_CONTENTS        = "totalContents";
  public static final String APPLIED_SORT_DATA     = "appliedSortData";
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String FILTER_DATA           = "filterData";
  public static final String FUNCTION_PERMISSION   = "functionPermission";
  
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public List<IKlassInstanceInformationModel> getChildren();
  public void setChildren(List<IKlassInstanceInformationModel> childrens);
  
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets();
  public void setReferencedAssets(Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets);
  
  public Integer getFrom();
  public void setFrom(Integer from);

  public Long getTotalContents();
  public void setTotalContents(Long totalContents);
  
  public List<IAppliedSortModel> getAppliedSortData();
  public void setAppliedSortData(List<IAppliedSortModel> appliedSortData);
  
  public Map<String, IAttribute> getReferencedAttributes();
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  public Map<String, ITag> getReferencedTags();
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public List<INewApplicableFilterModel> getFilterData();
  public void setFilterData(List<INewApplicableFilterModel> filterData);
  
  public IFunctionPermissionModel getFunctionPermission();
  public void setFunctionPermission(IFunctionPermissionModel functionPermission);
}
