package com.cs.core.dataintegration.idto;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

import java.nio.file.Path;

/**
 * Defines the source and the mode to import data through the PXON Importer/Exporter engine
 *
 * @author vallee
 */
public interface IPXONImporterPlanDTO extends IInitializeBGProcessDTO {
  // Import mode not supported in first version
  public enum ImportMode {
    UNDEFINED, CREATE_ONLY, CREATE_UPDATE, CREATE_UPDATE_DELETE, ADVANCED;
  }

  /**
   * @return the mode applicable to configuration objects
   */
  public ImportMode getConfigurationMode();

  /**
   * @return the mode applicable to real-time data objects
   */
  public ImportMode getDataMode();

  /**
   * Define the modes for importing
   * @param dataMode mode for runtime data
   * @param configMode mode for configuration data
   */
  public void setModes( ImportMode dataMode, ImportMode configMode);

  /**
   * @return the file to be imported relatively to the NFS path of the BGP server
   */
  public Path getRelativeImportFile();
  
  /**
   * @param relativePath overwritten import file path 
   */
  public void setRelativeImportFile( String relativePath);
  
  /**
   * @return the file that is moved relatively to the NFS path after import (null means the file is removed)
   */
  public Path getRelativeAfterImportFile();
  
  /**
   * @param relativePath overwritten after import file path 
   */
  public void setRelativeAfterImportFile( String relativePath);

  /**
   *
   * @return locale ID for current PXON import
   */
  String getLocaleID();

  /**
   *
   * @param locale locale ID for current PXON import
   */
  void setLocaleID(String locale) ;

  public String getSessionID();
  public void setSessionID(String sessionID);
  
  /**
   * @return the partner authorization mapping id to authorized properties while import.
   */
  public String getPartnerAuthorizationId();

  /**
   * @param partnerAuthorizationId the partner authorization mapping id to authorized properties while import.
   */
  public void setPartnerAuthorizationId(String partnerAuthorizationId);
}
