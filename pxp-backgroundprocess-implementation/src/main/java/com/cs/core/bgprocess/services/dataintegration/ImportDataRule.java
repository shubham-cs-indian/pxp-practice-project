package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigDataRuleDTO;
import com.cs.config.idto.IConfigDataRuleDTO;
import com.cs.core.bgprocess.dto.BGPLog;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.Map.Entry;

/** @author vallee */
public class ImportDataRule implements IImportEntity {

  private static final String UPSERT_DATA_RULE                = "UpsertDataRule";

  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> dataRuleBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_DATA_RULE);
    BGPLog log = importer.getJobData().getLog();
    try (PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigDataRuleDTO> dataRuleDTOList = new ArrayList<>();
      for (Entry dataRuleBlock : dataRuleBlocks.entrySet()) {
        try {
          IConfigDataRuleDTO configRuleDTO = new ConfigDataRuleDTO();
          configRuleDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) dataRuleBlock.getValue()));
          dataRuleDTOList.add(configRuleDTO);
        }catch (Exception e) {
          importer.incrementNumberOfException();
          log.error(e.getMessage());
        }
      }
      // create or save data rule in ODB
      Map<String, Object> responseMap = configurationImport(importer.getImportDTO().getLocaleID(), dataRuleDTOList.toArray(new IConfigDataRuleDTO[0]));
      log.info("Successfully Created in OrienDB " + responseMap.get("success").toString());
      log.error(responseMap.get("failure").toString());
      log.error(responseMap.get("failedIds").toString());
      List<Map<String, Object>> failureList = (List<Map<String, Object>>) responseMap.get("failedIds");
      importer.incrementNumberOfException(failureList.size());
      List<Map<String, Object>> successDataRuleList = (List<Map<String, Object>>) responseMap.get("successDataRuleList");

      if (successDataRuleList == null)
        successDataRuleList = new ArrayList<>();
      // create rule into RDBMS
      PXONImportDataRuleDAS.instance().upsertDataRule(successDataRuleList);
      importer.logIds(responseMap);
    }
    catch (Exception e) {
      importer.incrementNumberOfException();
      log.error(e.getMessage());
    }
  }

  public Map<String,Object> configurationImport(String localeID, IConfigDataRuleDTO... rules) throws CSInitializationException, CSFormatException
  {
    List<IConfigDataRuleDTO> tasksList =  Arrays.asList(rules);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, tasksList);

    return CSConfigServer.instance().request(requestModel, UPSERT_DATA_RULE, localeID);
  }
}
