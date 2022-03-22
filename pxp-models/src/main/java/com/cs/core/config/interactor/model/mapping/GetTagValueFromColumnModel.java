package com.cs.core.config.interactor.model.mapping;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class GetTagValueFromColumnModel implements IGetTagValueFromColumnModel {
  
  private static final long serialVersionUID = 1L;
  protected String          fileId;
  protected String          columnName;
  protected String          tagGroupId;
  protected Integer         dataRowNumber;
  protected Integer         headerRowNumber;
  protected List<String>    sheetsFromProcess;
  protected IMappingModel   mappingModel;
  protected XSSFWorkbook    workBook;
  
  @Override
  public String getFileId()
  {
    
    return fileId;
  }
  
  @Override
  public void setFileId(String fileId)
  {
    this.fileId = fileId;
  }
  
  @Override
  public String getColumnName()
  {
    
    return columnName;
  }
  
  @Override
  public void setColumnName(String columnName)
  {
    this.columnName = columnName;
  }
  
  @Override
  public String getTagGroupId()
  {
    return tagGroupId;
  }
  
  @Override
  public void setTagGroupId(String tagGroupId)
  {
    this.tagGroupId = tagGroupId;
  }
  
  @Override
  public Integer getDataRowNumber()
  {
    return dataRowNumber;
  }
  
  @Override
  public void setDataRowNumber(Integer dataRowNumber)
  {
    this.dataRowNumber = dataRowNumber;
  }
  
  @Override
  public Integer getHeaderRowNumber()
  {
    return headerRowNumber;
  }
  
  @Override
  public void setHeaderRowNumber(Integer headerRowNumber)
  {
    this.headerRowNumber = headerRowNumber;
  }
  
  @Override
  public List<String> getSheetsFromProcess()
  {
    return sheetsFromProcess;
  }
  
  @Override
  public void setSheetsFromProcess(List<String> sheetsFromProcess)
  {
    this.sheetsFromProcess = sheetsFromProcess;
  }
  
  @Override
  public IMappingModel getMappingModel()
  {
    return mappingModel;
  }
  
  @Override
  public void setMappingModel(IMappingModel mappingModel)
  {
    this.mappingModel = mappingModel;
  }
  
  @Override
  public XSSFWorkbook getWorkBook()
  {
    return workBook;
  }
  
  @Override
  public void setWorkBook(XSSFWorkbook workBook)
  {
    this.workBook = workBook;
  }
}
