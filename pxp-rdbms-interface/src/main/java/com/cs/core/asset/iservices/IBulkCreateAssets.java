package com.cs.core.asset.iservices;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;

import java.util.Map;

public interface IBulkCreateAssets {
  
  public static final String PROPERTY_MAP                           = "propertyMap";
  public static final String PRIORITY                               = "priority";
  public static final String GET_CONFIG_DETAILS_WITHOUT_PERMISSIONS = "GetConfigDetailsWithoutPermissions";
  public static final String FETCH_ASSET_CONFIGURATION_DETAILS      = "FetchAssetConfigurationDetails";
  public static final String GET_CONFIG_DETAILS_FOR_AUTO_CREATE_TIV = "GetConfigDetailsForAutoCreateTIV";
  
  public Map<String, Object> bulkCreate(Map<String, Object> bulklistModel)
          throws Exception, CSFormatException, CSInitializationException;
}
