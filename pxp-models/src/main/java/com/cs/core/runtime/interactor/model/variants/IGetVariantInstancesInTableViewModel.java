package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;

import java.util.List;
import java.util.Map;

public interface IGetVariantInstancesInTableViewModel extends IModel {
  
  public static final String CONFIG_DETAILS       = "configDetails";
  public static final String COLUMNS              = "columns";
  public static final String ROWS                 = "rows";
  public static final String FROM                 = "from";
  public static final String FILTER_INFO          = "filterInfo";
  public static final String REFERENCED_INSTANCES = "referencedInstances";
  public static final String TOTAL_CONTENTS       = "totalContents";
  
  public IConfigDetailsForGetVariantInstancesInTableViewModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForGetVariantInstancesInTableViewModel configDetails);
  
  public List<IIdParameterModel> getColumns();
  
  public void setColumns(List<IIdParameterModel> columns);
  
  public List<IRowIdParameterModel> getRows();
  
  public void setRows(List<IRowIdParameterModel> rows);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public IGetFilterInfoModel getFilterInfo();
  
  public void setFilterInfo(IGetFilterInfoModel filterInfo);
  
  public Map<String, IVariantReferencedInstancesModel> getReferencedInstances();
  
  public void setReferencedInstances(
      Map<String, IVariantReferencedInstancesModel> referencedInstances);
  
  public Long getTotalContents();
  
  public void setTotalContents(Long totalContents);
}
