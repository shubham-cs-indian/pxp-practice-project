package com.cs.core.dataintegration.dto;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.dataintegration.idto.IPXONImporterPlanDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author vallee
 */
public class PXONImporterPlanDTO extends InitializeBGProcessDTO implements IPXONImporterPlanDTO {

  private static final String CONFIG_MODE = "configurationMode";
  ImportMode configMode = ImportMode.UNDEFINED;
  private static final String DATA_MODE = "runtimedataMode";
  ImportMode dataMode = ImportMode.UNDEFINED;
  private static final String IMPORT_FILE = "importFile";
  Path relativeImportFile;
  private static final String AFTER_IMPORT_FILE = "archivedImportFile";
  Path relativeAferImportFile;
  private static final String LOCALE_ID = "localeID";
  private String localeID;
  private static final String SESSION_ID = "sessionID";
  private String sessionID;
  private static final String PARTNER_AUTHORIZATION_ID = "partnerAuthorizationId";
  private String partnerAuthorizationId;

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException {
    super.fromJSON(json);
    configMode = json.getEnum( ImportMode.class, CONFIG_MODE);
    dataMode = json.getEnum( ImportMode.class, DATA_MODE);
    String filePath = json.getString(IMPORT_FILE);
    relativeImportFile = filePath.isEmpty() ? null : Paths.get(filePath);
    filePath = json.getString(AFTER_IMPORT_FILE);
    relativeAferImportFile = filePath.isEmpty() ? null : Paths.get(filePath);
    localeID = json.getString(LOCALE_ID);
    sessionID = json.getString(SESSION_ID);
    partnerAuthorizationId = json.getString(PARTNER_AUTHORIZATION_ID);
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {

    return JSONBuilder.assembleJSONBuffer(
        super.toJSONBuffer(),
        JSONBuilder.newJSONField(SESSION_ID, sessionID),
        JSONBuilder.newJSONField(LOCALE_ID, localeID),
            JSONBuilder.newJSONField(CONFIG_MODE, configMode),
            JSONBuilder.newJSONField(DATA_MODE, dataMode),
            relativeImportFile != null ? 
                JSONBuilder.newJSONField( IMPORT_FILE, relativeImportFile.toString()) : JSONBuilder.VOID_FIELD,
            relativeAferImportFile != null ? 
                JSONBuilder.newJSONField( AFTER_IMPORT_FILE, relativeAferImportFile.toString()) : JSONBuilder.VOID_FIELD,
                JSONBuilder.newJSONField(PARTNER_AUTHORIZATION_ID, partnerAuthorizationId)
    );
  }

  @Override
  public ImportMode getConfigurationMode() {
    return configMode;
  }
  
  @Override
  public ImportMode getDataMode() {
    return dataMode;
  }
  
  @Override
  public void setModes( ImportMode dataMode, ImportMode configMode) {
    if ( dataMode != ImportMode.UNDEFINED )
      this.dataMode = dataMode;
    if ( configMode != ImportMode.UNDEFINED )
      this.configMode = configMode;
  }

  @Override
  public Path getRelativeImportFile() {
    return relativeImportFile;
  }

  @Override
  public void setRelativeImportFile( String relativePath) {
    relativeImportFile = Paths.get( relativePath);
  }

  @Override
  public Path getRelativeAfterImportFile() {
    return relativeAferImportFile;
  }
  
  @Override
  public void setRelativeAfterImportFile( String relativePath) {
    relativeAferImportFile = Paths.get( relativePath);
  }

  @Override
  public String getLocaleID() {
    return localeID;
  }

  @Override
  public void setLocaleID(String locale) {
    this.localeID = locale;
  }

  @Override
  public String getSessionID()
  {
    return sessionID;
  }

  @Override
  public void setSessionID(String sessionID)
  {
    this.sessionID = sessionID;
  }

  @Override
  public String getPartnerAuthorizationId()
  {
    return partnerAuthorizationId;
  }

  @Override
  public void setPartnerAuthorizationId(String partnerAuthorizationId)
  {
    this.partnerAuthorizationId = partnerAuthorizationId;
  }
}
