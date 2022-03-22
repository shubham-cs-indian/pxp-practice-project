package com.cs.core.config.interactor.model.mapping;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public interface IGetTagValueFromColumnModel extends IModel {
  
  public static final String FILE_ID             = "fileId";
  public static final String COLUMN_NAME         = "columnName";
  public static final String TAG_GROUP_ID        = "tagGroupId";
  public static final String DATA_ROW_NUMBER     = "dataRowNumber";
  public static final String HEADER_ROW_NUMBER   = "headerRowNumber";
  public static final String SHEETS_FROM_PROCESS = "sheetsFromProcess";
  public static final String MAPPING_ID          = "mappingId";
  public static final String WORK_BOOK           = "workBook";
  
  public String getFileId();
  
  public void setFileId(String fileId);
  
  public String getColumnName();
  
  public void setColumnName(String columnName);
  
  public String getTagGroupId();
  
  public void setTagGroupId(String tagGroupId);
  
  public Integer getDataRowNumber();
  
  public void setDataRowNumber(Integer dataRowNumber);
  
  public Integer getHeaderRowNumber();
  
  public void setHeaderRowNumber(Integer headerRowNumber);
  
  public List<String> getSheetsFromProcess();
  
  public void setSheetsFromProcess(List<String> sheetsFromProcess);
  
  public IMappingModel getMappingModel();
  
  public void setMappingModel(IMappingModel mappingModel);
  
  public XSSFWorkbook getWorkBook();
  
  public void setWorkBook(XSSFWorkbook workBook);
}
