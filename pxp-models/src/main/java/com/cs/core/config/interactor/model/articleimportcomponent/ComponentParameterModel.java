package com.cs.core.config.interactor.model.articleimportcomponent;

import java.util.*;

public class ComponentParameterModel implements IComponentParameterModel {
  
  private static final long           serialVersionUID  = 1L;
  
  protected String                    sheet;
  protected String                    primaryKeyColumn;
  protected Integer                   headerRowNumber;
  protected Integer                   dataRowNumber;
  protected String                    type;
  protected Boolean                   isTypeFromColumn;
  protected String                    klassColumn;
  protected Boolean                   isSecondaryTypeFromColumn;
  protected Boolean                   isTaxonomyEnabled = false;
  protected String                    taxonomyColumn;
  protected Boolean                   isMultiClassificationEnabled;
  protected String                    secondaryType;
  protected List<String>              dataRules         = new ArrayList<String>();
  protected List<String>              secondaryClasses  = new ArrayList<String>();
  protected String                    relationshipDestinationColumn;
  protected String                    relationshipSourceColumn;
  protected String                    relationshipId;
  protected String                    relationshipIdColumn;
  protected String                    imageFolderPath;
  protected String                    filePathColumnName;
  protected String                    relationshipCountColumn;
  protected Map<String, List<String>> taxonomyMap;
  protected Set<String>               requiredFieldSet  = new HashSet<String>();
  protected String                    contextIdColumn;
  protected String                    contextTagsColumn;
  protected String                    fromDateColumn;
  protected String                    toDateColumn;
  protected String                    fileSource;
  protected Set<String>               taxonomyColumnList;
  
  @Override
  public String getSheet()
  {
    
    return sheet;
  }
  
  @Override
  public void setSheet(String sheet)
  {
    this.sheet = sheet;
  }
  
  @Override
  public String getPrimaryKeyColumn()
  {
    
    return primaryKeyColumn;
  }
  
  @Override
  public void setPrimaryKeyColumn(String primaryKeyColumn)
  {
    this.primaryKeyColumn = primaryKeyColumn;
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
  public String getType()
  {
    
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsTypeFromColumn()
  {
    
    return isTypeFromColumn;
  }
  
  @Override
  public void setIsTypeFromColumn(Boolean isTypeFromColumn)
  {
    this.isTypeFromColumn = isTypeFromColumn;
  }
  
  @Override
  public String getKlassColumn()
  {
    
    return klassColumn;
  }
  
  @Override
  public void setKlassColumn(String klassColumn)
  {
    this.klassColumn = klassColumn;
  }
  
  @Override
  public Boolean getIsSecondaryTypeFromColumn()
  {
    
    return isSecondaryTypeFromColumn;
  }
  
  @Override
  public void setIsSecondaryTypeFromColumn(Boolean isSecondaryTypeFromColumn)
  {
    this.isSecondaryTypeFromColumn = isSecondaryTypeFromColumn;
  }
  
  @Override
  public Boolean getIsTaxonomyEnabled()
  {
    
    return isTaxonomyEnabled;
  }
  
  @Override
  public void setIsTaxonomyEnabled(Boolean isTaxonomyEnabled)
  {
    this.isTaxonomyEnabled = isTaxonomyEnabled;
  }
  
  @Override
  public String getTaxonomyColumn()
  {
    
    return taxonomyColumn;
  }
  
  @Override
  public void setTaxonomyColumn(String taxonomyColumn)
  {
    this.taxonomyColumn = taxonomyColumn;
  }
  
  @Override
  public List<String> getDataRules()
  {
    
    return dataRules;
  }
  
  @Override
  public void setDataRules(List<String> dataRules)
  {
    this.dataRules = dataRules;
  }
  
  @Override
  public List<String> getSecondaryClasses()
  {
    if (secondaryClasses == null) {
      secondaryClasses = new ArrayList<>();
    }
    return secondaryClasses;
  }
  
  @Override
  public void setSecondaryClasses(List<String> secondaryClasses)
  {
    this.secondaryClasses = secondaryClasses;
  }
  
  @Override
  public Boolean getIsMultiClassificationEnabled()
  {
    
    return isMultiClassificationEnabled;
  }
  
  @Override
  public void setIsMultiClassificationEnabled(Boolean isMultiClassificationEnabled)
  {
    this.isMultiClassificationEnabled = isMultiClassificationEnabled;
  }
  
  @Override
  public String getRelationshipDestinationColumn()
  {
    return relationshipDestinationColumn;
  }
  
  @Override
  public void setRelationshipDestinationColumn(String relationshipDestinationColumn)
  {
    this.relationshipDestinationColumn = relationshipDestinationColumn;
  }
  
  @Override
  public String getRelationshipSourceColumn()
  {
    return relationshipSourceColumn;
  }
  
  @Override
  public void setRelationshipSourceColumn(String relationshipSourceColumn)
  {
    this.relationshipSourceColumn = relationshipSourceColumn;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getRelationshipIdColumn()
  {
    return relationshipIdColumn;
  }
  
  @Override
  public void setRelationshipIdColumn(String relationshipIdColumn)
  {
    this.relationshipIdColumn = relationshipIdColumn;
  }
  
  @Override
  public Map<String, List<String>> getTaxonomyMap()
  {
    return taxonomyMap;
  }
  
  @Override
  public void setTaxonomyMap(Map<String, List<String>> taxonomyMap)
  {
    this.taxonomyMap = taxonomyMap;
  }
  
  @Override
  public String getImageFolderPath()
  {
    
    return imageFolderPath;
  }
  
  @Override
  public void setImageFolderPath(String imageFolderPath)
  {
    this.imageFolderPath = imageFolderPath;
  }
  
  @Override
  public String getFilePathColumnName()
  {
    
    return filePathColumnName;
  }
  
  @Override
  public void setFilePathColumnName(String filePathColumnName)
  {
    this.filePathColumnName = filePathColumnName;
  }
  
  @Override
  public String getRelationshipCountColumn()
  {
    return relationshipCountColumn;
  }
  
  @Override
  public void setRelationshipCountColumn(String relationshipCountColumn)
  {
    this.relationshipCountColumn = relationshipCountColumn;
  }
  
  @Override
  public String getContextIdColumn()
  {
    return contextIdColumn;
  }
  
  @Override
  public void setContextIdColumn(String contextIdColumn)
  {
    this.contextIdColumn = contextIdColumn;
  }
  
  @Override
  public String getContextTagsColumn()
  {
    return contextTagsColumn;
  }
  
  @Override
  public void setContextTagsColumn(String contextTagsColumn)
  {
    this.contextTagsColumn = contextTagsColumn;
  }
  
  @Override
  public String getFromDateColumn()
  {
    return fromDateColumn;
  }
  
  @Override
  public void setFromDateColumn(String fromDateColumn)
  {
    this.fromDateColumn = fromDateColumn;
  }
  
  @Override
  public String getToDateColumn()
  {
    return toDateColumn;
  }
  
  @Override
  public void setToDateColumn(String toDateColumn)
  {
    this.toDateColumn = toDateColumn;
  }
  
  @Override
  public String getFileSource()
  {
    return fileSource;
  }
  
  @Override
  public void setFileSource(String fileSource)
  {
    this.fileSource = fileSource;
  }
  
  @Override
  public Set<String> getTaxonomyColumnList()
  {
    return taxonomyColumnList;
  }
  
  @Override
  public void setTaxonomyColumnList(Set<String> taxonomyColumnList)
  {
    if (taxonomyColumnList == null) {
      taxonomyColumnList = new HashSet<>();
    }
    this.taxonomyColumnList = taxonomyColumnList;
  }
}
