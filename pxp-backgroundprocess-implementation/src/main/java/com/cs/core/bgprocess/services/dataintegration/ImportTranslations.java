package com.cs.core.bgprocess.services.dataintegration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONObject;

import com.cs.config.dto.ConfigTranslationDTO;
import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class ImportTranslations implements IImportEntity{
  
  private static final String IMPORT_TRANSLATIONS = "ImportTranslations";

  @Override
  public void importEntity(PXONImporter pxonImporter) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> translationBlocks = pxonImporter.getBlocks().getStepBlocks(ImportSteps.IMPORT_TRANSLATION);
    try (PXONFileParser pxonFileParser = new PXONFileParser(pxonImporter.getPath().toString())) {
      List<IConfigTranslationDTO> translationDTOList = new ArrayList<>();
      for (Entry translationBlock : translationBlocks.entrySet()) {
        ConfigTranslationDTO translationDTO = new ConfigTranslationDTO();
        String pxonBlock = PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) translationBlock.getValue());
        translationDTO.fromPXON(pxonBlock);
        translationDTOList.add(translationDTO);
      }
      // Upsert Translation in ODB
      Map<String, Object> responseMap = configurationImport(pxonImporter.getImportDTO().getLocaleID(), translationDTOList.toArray(new IConfigTranslationDTO[0]));
      pxonImporter.logIds(responseMap);

    }
    catch (IOException e) {
      pxonImporter.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }
  
  public Map<String, Object> configurationImport(String localeID, IConfigTranslationDTO... translations)
      throws CSInitializationException, CSFormatException
  {

    List<IConfigTranslationDTO> translationList = Arrays.asList(translations);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, translationList);

    return CSConfigServer.instance().request(requestModel, IMPORT_TRANSLATIONS, localeID);
  }
  
}
