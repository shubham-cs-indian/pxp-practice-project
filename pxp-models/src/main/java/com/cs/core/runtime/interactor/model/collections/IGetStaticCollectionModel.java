package com.cs.core.runtime.interactor.model.collections;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

public interface IGetStaticCollectionModel extends IStaticCollection, IModel {
  
  public static final String KLASS_INSTANCES       = "klassInstances";
  public static final String CHILDREN_HIERARCHY    = "childrenHierarchy";
  public static final String IS_COLLECTION_MOVABLE = "isCollectionMovable";
  public static final String FROM                  = "from";
  public static final String TOTAL_CONTENTS        = "totalContents";
  public static final String TAXONOMIES            = "taxonomies";
  public static final String HIERARCHIES           = "hierarchies";
  public static final String DEFAULT_TYPES         = "defaultTypes";
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String X_RAY_CONFIG_DETAILS  = "xrayConfigDetails";
  public static final String APPLIED_SORT_DATA     = "appliedSortData";
  public static final String FILTER_DATA           = "filterData";
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";


  public List<IKlassInstanceInformationModel> getKlassInstances();
  
  public void setKlassInstances(List<IKlassInstanceInformationModel> klassInstances);
  
  public List<IConfigEntityInformationModel> getChildrenHierarchy();
  
  public void setChildrenHierarchy(List<IConfigEntityInformationModel> childrenHierarchy);
  
  public Boolean getIsCollectionMovable();
  
  public void setIsCollectionMovable(Boolean isCollectionMovable);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public long getTotalContents();
  
  public void setTotalContents(long totalContents);
  
  public List<ICategoryInformationModel> getTaxonomies();
  
  public Map<String, IAttribute> getReferencedAttributes();
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public void setTaxonomies(List<ICategoryInformationModel> taxonomies);
  
  public List<IConfigEntityInformationModel> getHierarchies();
  
  public void setHierarchies(List<IConfigEntityInformationModel> hierarchies);
  
  public IGetDefaultKlassesModel getDefaultTypes();
  
  public void setDefaultTypes(IGetDefaultKlassesModel defaultTypes);
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
  
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses);
  
  public List<IAppliedSortModel> getAppliedSortData();
  
  public void setAppliedSortData(List<IAppliedSortModel> appliedSortData);

  public List<INewApplicableFilterModel> getFilterData();
  
  public void setFilterData(List<INewApplicableFilterModel> filterData); 
}
