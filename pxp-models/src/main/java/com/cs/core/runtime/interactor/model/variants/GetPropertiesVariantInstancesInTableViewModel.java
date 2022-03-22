package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.runtime.interactor.model.assetinstance.AssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetPropertiesVariantInstancesInTableViewModel
    implements IGetPropertiesVariantInstancesInTableViewModel {
  
  private static final long                                                serialVersionUID = 1L;
  
  protected IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails;
  protected List<IIdParameterModel>                                        columns;
  protected List<IPropertyVariantRowIdParameterModel>                      rows;
  protected Integer                                                        from;
  protected IGetFilterInfoModel                                            filterInfo;
  protected Map<String, IVariantReferencedInstancesModel>                  referencedInstances;
  protected Long                                                           totalContents    = 0L;
  protected Map<String, ? extends IAssetAttributeInstanceInformationModel> referencedAssets = new HashMap<>();
  
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
  public IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.class)
  @Override
  public void setConfigDetails(
      IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel configDetails)
  {
    this.configDetails = configDetails;
  }
  
  @Override
  public List<IIdParameterModel> getColumns()
  {
    return columns;
  }
  
  @JsonDeserialize(contentAs = IdParameterModel.class)
  @Override
  public void setColumns(List<IIdParameterModel> columns)
  {
    this.columns = columns;
  }
  
  @Override
  public List<IPropertyVariantRowIdParameterModel> getRows()
  {
    return rows;
  }
  
  @JsonDeserialize(contentAs = PropertyVariantRowIdParameterModel.class)
  @Override
  public void setRows(List<IPropertyVariantRowIdParameterModel> rows)
  {
    this.rows = rows;
  }
  
  @Override
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @Override
  @JsonDeserialize(as = GetFilterInfoModel.class)
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
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
}
