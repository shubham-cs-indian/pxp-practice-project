package com.cs.core.runtime.interactor.model.templating;

import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.variants.IVariantReferencedInstancesModel;

public interface IGetKlassInstanceModel extends IWarningsModel {
  
  public static final String KLASS_INSTANCE         = "klassInstance";
  public static final String GLOBAL_PERMISSION      = "globalPermission";
  public static final String FROM                   = "from";
  public static final String TOTAL_CONTENTS         = "totalContents";
  public static final String CONFIG_DETAILS         = "configDetails";
  public static final String VARIANTS_COUNT         = "variantsCount";
  public static final String BRANCH_OF_LABEL        = "branchOfLabel";
  public static final String REFERENCED_ASSETS      = "referencedAssets";
  public static final String TIME_RANGE             = "timeRange";
  public static final String TASKS_COUNT            = "tasksCount";
  public static final String REFERENCED_COLLECTIONS = "referencedCollections";
  public static final String VERSION_OF_LABEL       = "versionOfLabel";
  public static final String REFERENCED_INSTANCES   = "referencedInstances";
  public static final String VARIANT_OF_LABEL       = "variantOfLabel";
  
  public IContentInstance getKlassInstance();
  
  public void setKlassInstance(IContentInstance klassInstance);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public Map<String, ? extends IAssetAttributeInstanceInformationModel> getReferencedAssets();
  
  public void setReferencedAssets(
      Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets);
  
  public int getFrom();
  
  public void setFrom(int from);
  
  public long getTotalContents();
  
  public void setTotalContents(long totalContents);
  
  public IGetConfigDetailsModel getConfigDetails();
  
  public void setConfigDetails(IGetConfigDetailsModel klassDetails);
  
  public Map<String, Integer> getVariantsCount();
  
  public void setVariantsCount(Map<String, Integer> variantsCount);
  
  public String getBranchOfLabel();
  
  public void setBranchOfLabel(String branchOfLabel);
  
  public Integer getTasksCount();
  
  public void setTasksCount(Integer tasksCount);
  
  public Map<String, IInstanceTimeRange> getTimeRange();
  
  public void setTimeRange(Map<String, IInstanceTimeRange> timeRange);
  
  public List<IConfigEntityInformationModel> getReferencedCollections();
  
  public void setReferencedCollections(List<IConfigEntityInformationModel> referencedCollections);
  
  public String getVersionOfLabel();
  
  public void setVersionOfLabel(String versionOfLabel);
  
  public Map<String, IVariantReferencedInstancesModel> getReferencedInstances();
  
  public void setReferencedInstances(
      Map<String, IVariantReferencedInstancesModel> referencedInstances);
  
  public String getVariantOfLabel();
  
  public void setVariantOfLabel(String variantOfLabel);
}
