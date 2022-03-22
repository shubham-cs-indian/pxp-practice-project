package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IComponentParameterModel extends IModel {
  
  public static final String SHEET                           = "sheet";
  public static final String PRIMARY_KEY_COLUMN              = "primaryKeyColumn";
  public static final String HEADER_ROW_NUMBER               = "headerRowNumber";
  public static final String DATA_ROW_NUMBER                 = "dataRowNumber";
  public static final String TYPE                            = "type";
  public static final String IS_TYPE_FROM_COLUMN             = "isTypeFromColumn";
  public static final String KLASS_COLUMN                    = "klassColumn";
  public static final String IS_SECONDARY_TYPE_FROM_COLUMN   = "isSecondaryTypeFromColumn";
  public static final String SECONDARY_TYPE                  = "secondaryType";
  public static final String IS_TAXONOMY_ENABLED             = "isTaxonomyEnabled";
  public static final String TAXONOMY_COLUMN                 = "taxonomyColumn";
  public static final String DATA_RULES                      = "dataRules";
  public static final String SECONDARY_CLASSES               = "secondaryClasses";
  public static final String IS_MULTICLASSIFICATION_ENABLED  = "isMultiClassificationEnabled";
  public static final String RELATIONSHIP_DESTINATION_COLUMN = "relationshipDestinationColumn";
  public static final String RELATIONSHIP_SOURCE_COLUMN      = "relationshipSourceColumn";
  public static final String RELATIONSHIP_ID_COLUMN          = "relationshipIdColumn";
  public static final String RELATIONSHIP_ID                 = "relationshipId";
  public static final String TAXONOMY_MAP                    = "taxonomyMap";
  public static final String TAXONOMY_COLUMN_LIST            = "taxonomyColumnList";
  public static final String IMAGE_FOLDER_PATH               = "imageFolderPath";
  public static final String FILE_PATH_COLUMN_NAME           = "filePathColumnName";
  public static final String RELATIONSHIP_COUNT_COLUMN       = "relationshipCountColumn";
  public static final String CONTEXT_ID_COLUMN               = "contextIdColumn";
  public static final String CONTEXT_TAGS_COLUMN             = "contextTagsColumn";
  public static final String FROM_DATE_COLUMN                = "fromDateColumn";
  public static final String TO_DATE_COLUMN                  = "toDateColumn";
  public static final String FILE_SOURCE                     = "fileSource";
  
  public String getSheet();
  
  public void setSheet(String sheet);
  
  public String getPrimaryKeyColumn();
  
  public void setPrimaryKeyColumn(String primaryKeyColumn);
  
  public Integer getHeaderRowNumber();
  
  public void setHeaderRowNumber(Integer headerRowNumber);
  
  public Integer getDataRowNumber();
  
  public void setDataRowNumber(Integer dataRowNumber);
  
  public String getType();
  
  public void setType(String type);
  
  public Boolean getIsTypeFromColumn();
  
  public void setIsTypeFromColumn(Boolean isTypeFromColumn);
  
  public String getKlassColumn();
  
  public void setKlassColumn(String klassColumn);
  
  public Boolean getIsSecondaryTypeFromColumn();
  
  public void setIsSecondaryTypeFromColumn(Boolean isSecondaryTypeFromColumn);
  
  public Boolean getIsTaxonomyEnabled();
  
  public void setIsTaxonomyEnabled(Boolean isTaxonomyEnabled);
  
  public String getTaxonomyColumn();
  
  public void setTaxonomyColumn(String taxonomyColumn);
  
  public List<String> getDataRules();
  
  public void setDataRules(List<String> dataRules);
  
  public List<String> getSecondaryClasses();
  
  public void setSecondaryClasses(List<String> secondaryClasses);
  
  public Boolean getIsMultiClassificationEnabled();
  
  public void setIsMultiClassificationEnabled(Boolean isMultiClassificationEnabled);
  
  public String getRelationshipDestinationColumn();
  
  public void setRelationshipDestinationColumn(String relationshipDestinationColumn);
  
  public String getRelationshipSourceColumn();
  
  public void setRelationshipSourceColumn(String relationshipSourceColumn);
  
  public String getRelationshipIdColumn();
  
  public void setRelationshipIdColumn(String relationshipIdColumn);
  
  public String getRelationshipId();
  
  public void setRelationshipId(String relationshipId);
  
  public Map<String, List<String>> getTaxonomyMap();
  
  public void setTaxonomyMap(Map<String, List<String>> taxonomyMap);
  
  public String getImageFolderPath();
  
  public void setImageFolderPath(String imageFolderPath);
  
  public String getFilePathColumnName();
  
  public void setFilePathColumnName(String filePathColumnName);
  
  public String getRelationshipCountColumn();
  
  public void setRelationshipCountColumn(String relationshipCountColumn);
  
  public String getContextIdColumn();
  
  public void setContextIdColumn(String contextIdColumn);
  
  public String getContextTagsColumn();
  
  public void setContextTagsColumn(String contextTagsColumn);
  
  public String getFromDateColumn();
  
  public void setFromDateColumn(String fromDateColumn);
  
  public String getToDateColumn();
  
  public void setToDateColumn(String toDateColumn);
  
  public String getFileSource();
  
  public void setFileSource(String fileSource);
  
  public Set<String> getTaxonomyColumnList();
  
  public void setTaxonomyColumnList(Set<String> taxonomyColumnList);
}
