package com.cs.core.config.interactor.model.mapping;

import java.util.List;

public interface IRuntimeMappingModel extends IMappingModel {
  
  public static final String FILE_INSTANCE_ID           = "fileInstanceId";
  public static final String UNMAPPED_COLUMNS           = "unmappedColumns";
  public static final String IS_RUNTIME_MAPPING_ENABLED = "isRuntimeMappingEnabled";
  public static final String UNMAPPED_KLASS_COLUMNS     = "unmappedKlassColumns";
  public static final String UNMAPPED_TAXONOMY_COLUMNS  = "unmappedTaxonomyColumns";
  public static final String IS_SHOW_MAPPING            = "isShowMapping";
  
  public String getFileInstanceId();
  
  public void setFileInstanceId(String fileInstanceId);
  
  public List<String> getUnmappedColumns();
  
  public void setUnmappedColumns(List<String> unmappedColumns);
  
  public Boolean getIsRuntimeMappingEnabled();
  
  public void setIsRuntimeMappingEnabled(Boolean isRuntimeMappingEnabled);
  
  public List<String> getUnmappedKlassColumns();
  
  public void setUnmappedKlassColumns(List<String> unmappedKlassColumns);
  
  public List<String> getUnmappedTaxonomyColumns();
  
  public void setUnmappedTaxonomyColumns(List<String> unmappedTaxonomyColumns);
  
  public Boolean getIsShowMapping();
  
  public void setIsShowMapping(Boolean isShowMapping);
}
