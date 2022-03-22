package com.cs.core.bgprocess.services.dataintegration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigOrganizationDTO;
import com.cs.config.idto.IConfigOrganizationDTO;
import com.cs.constants.Constants;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.instance.CreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.BulkCreateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateInstanceModel;
import com.cs.core.services.CSConfigServer;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

/**
 * @author vallee
 */
public class ImportOrganization implements IImportEntity {
  private static final String LANGUAGE_PLACEHOLDER = "\\$\\{language\\}";
  private static final String ORG_PLACEHOLDER = "\\$\\{org\\}";
  private static final String CTLG_PLACEHOLDER = "\\$\\{ctlg\\}";
  private static final String SESSION_PLACEHOLDER = "\\$\\{sessionId\\}";

  private static final String IMPORT_ORGANIZATIONS = "ImportOrganizations";
  private static final String URL = "%s/%s/runtime/bulkcreate/suppliers?lang=${language}&organizationId=${org}&physicalCatalogId=${ctlg}&dataLanguage=${language}";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> userBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_ORGANIZATION);
    try (PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigOrganizationDTO> organizations = new ArrayList<>();
      for (Entry userBlock : userBlocks.entrySet()) {
        IConfigOrganizationDTO organization = new ConfigOrganizationDTO();
        organization.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) userBlock.getValue()));
        // Upsert one by one into RDBMS
        organizations.add(organization);
      }
      // create Organization in ODB
      Map<String, Object> responseMap = configurationImport(importer.getImportDTO().getLocaleID(), organizations);
      //create Organization at runtime.
      List<String> successIDs = getSuccessIDs(responseMap);
      List<String> existingBaseEntityIDs = LocaleCatalogDAO.getExistingBaseEntityIDs(successIDs);
      fillDataForPostProcessing(importer, organizations, existingBaseEntityIDs);
      importer.logIds(responseMap);
    }
    catch (IOException e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  private void fillDataForPostProcessing(PXONImporter importer, List<IConfigOrganizationDTO> organizations,
      List<String> existingBaseEntityIDs) throws CSInitializationException
  {
    List<ICreateInstanceModel> callbackData = new ArrayList<>();
    for (IConfigOrganizationDTO organization : organizations) {
      String code = organization.getCode();
      String type = organization.getType();
      if (existingBaseEntityIDs.contains(code)  && !Constants.partnerTypes.contains(type)) {
        continue; //the entity that is created on import should be the
        // only one who's supplier instance is created.
      }
      ICreateInstanceModel createModel = new CreateInstanceModel();
      createModel.setId(code);
      createModel.setType(BaseEntityUtils.getSupplierClass(type));
      createModel.setName(organization.getLabel());
      createModel.setParentId("-1");
      callbackData.add(createModel);
    }
    IBulkCreateInstanceModel model = new BulkCreateInstanceModel();
    model.setCreationList(callbackData);


    if(model.getCreationList().size() > 0){
      importer.setDataForPostProcessing( model);
      String tomcatURL = CSProperties.instance().getString("tomcat.server.url");
      String warName = CSProperties.instance().getString("tomcat.war.name");
      String url = String.format(URL, tomcatURL, warName);
      url = url.replaceAll(LANGUAGE_PLACEHOLDER, importer.getImportDTO().getLocaleID())
          .replaceAll(ORG_PLACEHOLDER, "-1")
          .replaceAll(CTLG_PLACEHOLDER, "pim");

      importer.setJSessionID(importer.getImportDTO().getSessionID());
      importer.setPostProcessingUrl(url);
      importer.setShouldPostProcess(true);
    }
  }

  private List<String> getSuccessIDs(Map<String, Object> responseMap)
  {
    List<Map<String, Object>> success = (List<Map<String, Object>>) responseMap.get(IPluginSummaryModel.SUCCESS);
    List<String> successIds = success.stream().map(x -> (String) x.get(ISummaryInformationModel.ID)).collect(Collectors.toList());
    return successIds;
  }

  public Map<String, Object> configurationImport(String localeID, Collection<IConfigOrganizationDTO> organizations)
      throws CSInitializationException, CSFormatException
  {
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, organizations);

    return CSConfigServer.instance().request(requestModel, IMPORT_ORGANIZATIONS, localeID);
  }

}
