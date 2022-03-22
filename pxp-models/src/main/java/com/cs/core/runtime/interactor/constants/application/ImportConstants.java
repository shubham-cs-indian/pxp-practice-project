package com.cs.core.runtime.interactor.constants.application;

import com.cs.config.standard.IStandardConfig;

public class ImportConstants {
  
  public static final String   MAPPED_TO_PROPERTY                                = "mappedTo";
  
  public static final String   IMPORT_SYSTEM_FILE_ARTICLE_KLASS                  = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportSystemFileArticle";
  
  public static final String   IMPORT_FILE_KLASS                                 = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportFile";
  
  public static final String   IMPORT_SYSTEM_KLASS                               = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportSystem";
  
  public static final String   IMPORT_ARTICLE_KLASS                              = "com.cs.imprt.config.interactor.entity.concrete.klass.ImportArticle";
  
  public static final String   MASTER_ARTICLE_KLASS                              = "com.cs.imprt.config.interactor.entity.concrete.klass.MasterImportArticle";
  
  public static final String   IMPORT_SYSTEM_INSTANCE_BASE_TYPE                  = "com.cs.imprt.runtime.interactor.entity.importsystem.ImportSystemInstance";
  
  public static final String   MASTER_ARTICLE_INSTANCE_BASE_TYPE                 = "com.cs.imprt.runtime.interactor.entity.importsystem.MasterArticleInstance";
  
  public static final String   MASTER_ARTICLE_INSTANCE_DOC_TYPE                  = "masterarticleinstance";
  public static final String   MASTER_ARTICLE_INSTANCE_PROPERTY_VERSION_DOC_TYPE = "masterarticleinstancepropertyversion";
  
  public static final String   IMPORT_ARTICLE_INSTANCE_DOC_TYPE                  = "importsysteminstance";
  
  public static final String   MASTER_ARTICLE_STATUS_TAG_ID                      = "masterArticleStatusTag";
  
  public static final String   IMPORT_ARTICLE_STATUS_TAG_ID                      = "importArticleStatusTag";
  
  public static final String   IMPORT_SYSTEM_ATTRIBUTE_ID                        = "importsystem_attribute";
  
  public static final String   IGNORED_PROPERTY_ATTRIBUTE_ID                     = "ignoredpropertyattribute";
  
  public static final String[] propertiesToExcludeForMatchAndMerge               = {
      MASTER_ARTICLE_STATUS_TAG_ID, IMPORT_ARTICLE_STATUS_TAG_ID, IMPORT_SYSTEM_ATTRIBUTE_ID,
      IGNORED_PROPERTY_ATTRIBUTE_ID,
      IStandardConfig.StandardProperty.lastmodifiedbyattribute.toString(),
      IStandardConfig.StandardProperty.lastmodifiedattribute.toString(),
      IStandardConfig.StandardProperty.createdbyattribute.toString(),
      IStandardConfig.StandardProperty.createdonattribute.toString() };
}
