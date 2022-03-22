package com.cs.core.runtime.interactor.model.collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.config.interactor.model.configdetails.IGetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.GetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.klass.ReferencedKlassDetailsModel;
import com.cs.core.config.interactor.model.xray.XRayConfigDetailsModel;
import com.cs.core.runtime.interactor.entity.collection.StaticCollection;
import com.cs.core.runtime.interactor.entity.collections.IStaticCollection;
import com.cs.core.runtime.interactor.entity.configuration.base.ISortEntity;
import com.cs.core.runtime.interactor.entity.configuration.base.SortEntity;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.instancetree.AppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.IAppliedSortModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.NewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetStaticCollectionModel implements IGetStaticCollectionModel {
  
  private static final long                                  serialVersionUID  = 1L;
  
  protected IStaticCollection                                entity;
  protected List<IKlassInstanceInformationModel>             klassInstances;
  protected List<IConfigEntityInformationModel>              childrenHierarchy;
  protected Boolean                                          isCollectionMovable;
  protected int                                              from              = 0;
  protected long                                             totalContents     = 0l;
  protected List<ICategoryInformationModel>                  taxonomies;
  protected List<IConfigEntityInformationModel>              hierarchies;
  protected IGetDefaultKlassesModel                          defaultTypes      = new GetDefaultKlassesModel();
  protected Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = new HashMap<>();
  protected IXRayConfigDetailsModel                          xRayConfigDetails;
  protected List<IAppliedSortModel>                          appliedSortData;
  protected List<INewApplicableFilterModel>                  filterData;
  protected Map<String, IAttribute>                          referencedAttributes;

  
  public GetStaticCollectionModel()
  {
    entity = new StaticCollection();
  }
  
  @Override
  public String getCreatedBy()
  {
    return entity.getCreatedBy();
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    entity.setCreatedBy(createdBy);
  }
  
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  @Override
  public String getParentId()
  {
    return entity.getParentId();
  }
  
  @Override
  public void setParentId(String parentId)
  {
    entity.setParentId(parentId);
  }
  
  @Override
  public String toString()
  {
    return entity.toString();
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    return entity.getKlassInstanceIds();
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    entity.setKlassInstanceIds(klassInstanceIds);
  }
  
  @Override
  public List<ISortEntity> getSortOptions()
  {
    return entity.getSortOptions();
  }
  
  @JsonDeserialize(contentAs = SortEntity.class)
  @Override
  public void setSortOptions(List<ISortEntity> sortOptions)
  {
    entity.setSortOptions(sortOptions);
  }
  
  @Override
  public List<IKlassInstanceInformationModel> getKlassInstances()
  {
    if (klassInstances == null) {
      klassInstances = new ArrayList<>();
    }
    return this.klassInstances;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceInformationModel.class)
  @Override
  public void setKlassInstances(List<IKlassInstanceInformationModel> klassInstances)
  {
    this.klassInstances = klassInstances;
  }
  
  @Override
  public List<IConfigEntityInformationModel> getChildrenHierarchy()
  {
    return childrenHierarchy;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setChildrenHierarchy(List<IConfigEntityInformationModel> childrenHierarchy)
  {
    this.childrenHierarchy = childrenHierarchy;
  }
  
  @Override
  public Boolean getIsCollectionMovable()
  {
    if (isCollectionMovable == null) {
      isCollectionMovable = false;
    }
    return isCollectionMovable;
  }
  
  @Override
  public void setIsCollectionMovable(Boolean isCollectionMovable)
  {
    this.isCollectionMovable = isCollectionMovable;
  }
  
  @Override
  public Boolean getIsPublic()
  {
    return entity.getIsPublic();
  }
  
  @Override
  public void setIsPublic(Boolean isPublic)
  {
    entity.setIsPublic(isPublic);
  }
  
  @Override
  public int getFrom()
  {
    return from;
  }
  
  @Override
  public void setFrom(int from)
  {
    this.from = from;
  }
  
  @Override
  public long getTotalContents()
  {
    return totalContents;
  }
  
  @Override
  public void setTotalContents(long totalContents)
  {
    this.totalContents = totalContents;
  }
  
  @Override
  public List<ICategoryInformationModel> getTaxonomies()
  {
    if (taxonomies == null) {
      taxonomies = new ArrayList<>();
    }
    return taxonomies;
  }
  
  @Override
  public void setTaxonomies(List<ICategoryInformationModel> taxonomyIds)
  {
    this.taxonomies = taxonomyIds;
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @Override
  public List<IConfigEntityInformationModel> getHierarchies()
  {
    if (hierarchies == null) {
      hierarchies = new ArrayList<>();
    }
    return hierarchies;
  }
  
  @Override
  public void setHierarchies(List<IConfigEntityInformationModel> hierarchies)
  {
    this.hierarchies = hierarchies;
  }
  
  @Override
  public IGetDefaultKlassesModel getDefaultTypes()
  {
    return defaultTypes;
  }
  
  @Override
  @JsonDeserialize(as = GetDefaultKlassesModel.class)
  public void setDefaultTypes(IGetDefaultKlassesModel defaultTypes)
  {
    this.defaultTypes = defaultTypes;
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
  }
  
  @Override
  public IXRayConfigDetailsModel getXRayConfigDetails()
  {
    return xRayConfigDetails;
  }
  
  @JsonDeserialize(as = XRayConfigDetailsModel.class)
  @Override
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails)
  {
    this.xRayConfigDetails = xRayConfigDetails;
  }
  
  @Override
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @Override
  @JsonDeserialize(contentAs = ReferencedKlassDetailsModel.class)
  public void setReferencedKlasses(
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public String getOrganizationId()
  {
    return entity.getOrganizationId();
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    entity.setOrganizationId(organizationId);
  }
  
  @Override
  public String getSystemId()
  {
    return entity.getSystemId();
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    entity.setSystemId(systemId);
  }
  
  @Override
  public String getPhysicalCatelogId()
  {
    return entity.getPhysicalCatelogId();
  }
  
  @Override
  public void setPhysicalCatelogId(String physicalCatelogId)
  {
    entity.setPhysicalCatelogId(physicalCatelogId);
  }
  
  @Override
  public String getPortalId()
  {
    return entity.getPortalId();
  }
  
  @Override
  public void setPortalId(String portalId)
  {
    entity.setPortalId(portalId);
  }
  
  @Override
  public String getLogicalCatelogId()
  {
    return entity.getLogicalCatelogId();
  }
  
  @Override
  public void setLogicalCatelogId(String logicalCatelogId)
  {
    entity.setLogicalCatelogId(logicalCatelogId);
  }
  
  @Override
  public String getEndpointId()
  {
    return entity.getEndpointId();
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    entity.setEndpointId(endpointId);
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
}
