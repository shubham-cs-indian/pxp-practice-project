package com.cs.core.bgprocess.services.dataintegration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigGoldenRecordRuleDTO;
import com.cs.config.idto.IConfigGoldenRecordRuleDTO;
import com.cs.core.bgprocess.dto.BGPLog;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class ImportGoldenRecordRule  extends PXONImporter implements IImportEntity{
  
  private static final String IMPORT_GOLDEN_RECORD_RULE     = "ImportGoldenRecordRule";
  
  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> goldenRuleBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_GOLDEN_RULES);
    BGPLog log = importer.getJobData().getLog();;
    try (PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigGoldenRecordRuleDTO> goldenRuleDTOList = new ArrayList<>();
      for (Entry goldenRuleBlock : goldenRuleBlocks.entrySet()) {
        IConfigGoldenRecordRuleDTO configGoldenRuleDTO = new ConfigGoldenRecordRuleDTO();
        configGoldenRuleDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) goldenRuleBlock.getValue()));
        goldenRuleDTOList.add(configGoldenRuleDTO);
      }
      // create or save data rule in ODB
      Map<String, Object> responseMap = configurationImport(importer.getImportDTO().getLocaleID(), goldenRuleDTOList.toArray(new IConfigGoldenRecordRuleDTO[0]));
      importer.logIds(responseMap);
    }catch(Exception e) {
      importer.incrementNumberOfException();
      log.error(e.getMessage());
    }
  }
  
  public Map<String,Object> configurationImport(String localeID, IConfigGoldenRecordRuleDTO... goldenrules) throws CSInitializationException, CSFormatException
  {
    List<IConfigGoldenRecordRuleDTO> goldenRuleList =  Arrays.asList(goldenrules);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, goldenRuleList);

    return CSConfigServer.instance().request(requestModel, IMPORT_GOLDEN_RECORD_RULE, localeID);
  }
}
