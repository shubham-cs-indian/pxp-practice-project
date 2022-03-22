package com.cs.core.config.interactor.model.mapping;

import java.util.ArrayList;
import java.util.List;

public class RuntimeMappingModel extends MappingModel implements IRuntimeMappingModel {
  
  private static final long serialVersionUID        = 1L;
  
  protected String          fileInstanceID;
  protected List<String>    unmappedColumns         = new ArrayList<String>();
  protected Boolean         isRuntimeMappingEnabled = false;
  protected List<String>    unmappedKlassColumns    = new ArrayList<>();
  protected List<String>    unmappedTaxonomyColumns = new ArrayList<>();
  protected Boolean         isShowMapping           = false;
  
  @Override
  public String getFileInstanceId()
  {
    
    return fileInstanceID;
  }
  
  @Override
  public void setFileInstanceId(String fileInstanceId)
  {
    this.fileInstanceID = fileInstanceId;
  }
  
  @Override
  public List<String> getUnmappedColumns()
  {
    
    return unmappedColumns;
  }
  
  @Override
  public void setUnmappedColumns(List<String> unmappedColumns)
  {
    this.unmappedColumns = unmappedColumns;
  }
  
  @Override
  public Boolean getIsRuntimeMappingEnabled()
  {
    return isRuntimeMappingEnabled;
  }
  
  @Override
  public void setIsRuntimeMappingEnabled(Boolean isRuntimeMappingEnabled)
  {
    this.isRuntimeMappingEnabled = isRuntimeMappingEnabled;
  }
  
  @Override
  public List<String> getUnmappedKlassColumns()
  {
    return unmappedKlassColumns;
  }
  
  @Override
  public void setUnmappedKlassColumns(List<String> unmappedKlassColumns)
  {
    this.unmappedKlassColumns = unmappedKlassColumns;
  }
  
  @Override
  public List<String> getUnmappedTaxonomyColumns()
  {
    return unmappedTaxonomyColumns;
  }
  
  @Override
  public void setUnmappedTaxonomyColumns(List<String> unmappedTaxonomyColumns)
  {
    this.unmappedTaxonomyColumns = unmappedTaxonomyColumns;
  }
  
  @Override
  public Boolean getIsShowMapping()
  {
    return isShowMapping;
  }
  
  @Override
  public void setIsShowMapping(Boolean isShowMapping)
  {
    this.isShowMapping = isShowMapping;
  }
}
