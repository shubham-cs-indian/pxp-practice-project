package com.cs.di.workflow.constants;

import java.util.Arrays;
import java.util.List;

public enum DiDataType
{
  JSON(false, "json"), EXCEL(false, "xls", "xlsx"), ASSET(true),
  UNSUPPORTED(false);
  private DiDataType(Boolean isDynamic, String... types)
  {
    this.supportedExtentions = Arrays.asList(types);
    this.isDynamic = isDynamic;
  }
  
  final private List<String> supportedExtentions;
  final Boolean              isDynamic;
  
  /**
   * Return extension for JSON and Excel files 
   * 
   * @return List of extensions
   * @throws Exception
   */
  public List<String> getSupportedExtentions() throws Exception
  {
    if (!isDynamic)
      return this.supportedExtentions;
    else {
      throw new Exception(
          "The extension are dynamic. Retrieve supported extensions from Configuration");
    }
  }
  
  public static DiDataType getDiDataTypeForExtension(String extension)
  {
    if (JSON.supportedExtentions.contains(extension)) {
      return JSON;
    }
    else if (EXCEL.supportedExtentions.contains(extension)) {
      return EXCEL;
    }
    else {
      return UNSUPPORTED;
    }
  }

  public boolean isDynamic() {
    return this.isDynamic;
  }
}
