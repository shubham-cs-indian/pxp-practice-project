package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class GetVariantInstancesInTableViewModel implements IGetVariantInstancesInTableViewModel {
  
  private static final long                                      serialVersionUID = 1L;
  
  protected IConfigDetailsForGetVariantInstancesInTableViewModel configDetails;
  protected List<IIdParameterModel>                              columns;
  protected List<IRowIdParameterModel>                           rows;
  protected Integer                                              from;
  protected IGetFilterInfoModel                                  filterInfo;
  protected Map<String, IVariantReferencedInstancesModel>        referencedInstances;
  protected Long                                                 totalContents    = 0L;
  
  @JsonDeserialize
  public IGetFilterInfoModel getFilterInfo()
  {
    return filterInfo;
  }
  
  @JsonDeserialize(as = GetFilterInfoModel.class)
  public void setFilterInfo(IGetFilterInfoModel filterInfo)
  {
    this.filterInfo = filterInfo;
  }
  
  @Override
  public IConfigDetailsForGetVariantInstancesInTableViewModel getConfigDetails()
  {
    return configDetails;
  }
  
  @JsonDeserialize(as = ConfigDetailsForGetVariantInstancesInTableViewModel.class)
  @Override
  public void setConfigDetails(IConfigDetailsForGetVariantInstancesInTableViewModel configDetails)
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
  public List<IRowIdParameterModel> getRows()
  {
    return rows;
  }
  
  @JsonDeserialize(contentAs = RowIdParameterModel.class)
  @Override
  public void setRows(List<IRowIdParameterModel> rows)
  {
    this.rows = rows;
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
}
