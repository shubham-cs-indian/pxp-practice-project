package com.cs.core.runtime.interactor.model.templating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.globalpermissions.GlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.variants.IVariantReferencedInstancesModel;
import com.cs.core.runtime.interactor.model.variants.VariantReferencedInstancesModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class AbstractGetKlassInstanceModel extends WarningsModel
    implements IGetKlassInstanceModel {
  
  private static final long                                                serialVersionUID = 1L;
  
  protected IContentInstance                                               klassInstance;
  protected IGlobalPermission                                              globalPermission;
  protected int                                                            from             = 0;
  protected long                                                           totalContents    = 0l;
  protected IGetConfigDetailsModel                                         configDetails;
  protected Map<String, Integer>                                           variantsCount    = new HashMap<>();
  protected String                                                         branchOfLabel;
  protected Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets = new HashMap<>();
  protected Map<String, IInstanceTimeRange>                                timeRange;
  protected Integer                                                        tasksCount       = 0;
  protected List<IConfigEntityInformationModel>                            referencedCollections;
  protected String                                                         versionOfLabel;
  protected Map<String, IVariantReferencedInstancesModel>                  referencedInstances;
  protected String                                                         variantOfLabel;
  
  @Override
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets()
  {
    return referencedAssets;
  }
  
  @JsonDeserialize(contentAs = AssetAttributeInstanceInformationModel.class)
  @Override
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets)
  {
    this.referencedAssets = referencedAssets;
  }
  
  @Override
  public IContentInstance getKlassInstance()
  {
    return this.klassInstance;
  }
  
  @Override
  public void setKlassInstance(IContentInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public IGlobalPermission getGlobalPermission()
  {
    return globalPermission;
  }
  
  @JsonDeserialize(as = GlobalPermission.class)
  @Override
  public void setGlobalPermission(IGlobalPermission globalPermission)
  {
    this.globalPermission = globalPermission;
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
  public IGetConfigDetailsModel getConfigDetails()
  {
    return configDetails;
  }
  
  @Override
  @JsonDeserialize(as = AbstractGetConfigDetailsModel.class)
  public void setConfigDetails(IGetConfigDetailsModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public Map<String, Integer> getVariantsCount()
  {
    return variantsCount;
  }
  
  @Override
  public void setVariantsCount(Map<String, Integer> variantsCount)
  {
    this.variantsCount = variantsCount;
  }
  
  @Override
  public String getBranchOfLabel()
  {
    return branchOfLabel;
  }
  
  @Override
  public void setBranchOfLabel(String branchOfLabel)
  {
    this.branchOfLabel = branchOfLabel;
  }
  
  @Override
  public Map<String, IInstanceTimeRange> getTimeRange()
  {
    return timeRange;
  }
  
  @JsonDeserialize(contentAs = InstanceTimeRange.class)
  @Override
  public void setTimeRange(Map<String, IInstanceTimeRange> timeRange)
  {
    this.timeRange = timeRange;
  }
  
  @Override
  public Integer getTasksCount()
  {
    return tasksCount;
  }
  
  @Override
  public void setTasksCount(Integer tasksCount)
  {
    this.tasksCount = tasksCount;
  }
  
  @Override
  public List<IConfigEntityInformationModel> getReferencedCollections()
  {
    return referencedCollections;
  }
  
  @Override
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  public void setReferencedCollections(List<IConfigEntityInformationModel> referencedCollections)
  {
    this.referencedCollections = referencedCollections;
  }
  
  @Override
  public String getVersionOfLabel()
  {
    return versionOfLabel;
  }
  
  @Override
  public void setVersionOfLabel(String versionOfLabel)
  {
    this.versionOfLabel = versionOfLabel;
  }
  
  @Override
  public Map<String, IVariantReferencedInstancesModel> getReferencedInstances()
  {
    return referencedInstances;
  }
  
  @Override
  @JsonDeserialize(contentAs = VariantReferencedInstancesModel.class)
  public void setReferencedInstances(
      Map<String, IVariantReferencedInstancesModel> referencedInstances)
  {
    this.referencedInstances = referencedInstances;
  }

  @Override
  public String getVariantOfLabel()
  {
    return variantOfLabel;
  }

  @Override
  public void setVariantOfLabel(String variantOfLabel)
  {
    this.variantOfLabel = variantOfLabel;
  }

}
