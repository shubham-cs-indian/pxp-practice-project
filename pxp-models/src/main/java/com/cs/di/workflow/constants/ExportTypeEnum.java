package com.cs.di.workflow.constants;

public enum ExportTypeEnum
{
  
  PRODUCT("Runtime"), ENTITY("Config");
  
  private String exportType;
  
  ExportTypeEnum(String exportType)
  {
    this.exportType = exportType;
  }
  
  public String getExportType()
  {
    return exportType;
  }
}