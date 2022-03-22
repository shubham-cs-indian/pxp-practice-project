package com.cs.core.bgprocess.services.dataintegration;

import com.cs.config.dto.ConfigClassifierDTO;
import com.cs.config.idto.IConfigClassifierDTO;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.config.strategy.usecase.klass.IGetTaxonomyHierarchyIdsStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.GetTaxonomyHierarchyIdsStrategy;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.model.configuration.IIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IIIDsListModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.cs.core.services.CSConfigServer;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.json.simple.JSONObject;

import java.util.*;
import java.util.Map.Entry;

/** @author vallee */
public class ImportHierarchyTaxonomy extends AbstractHierarchyTree  implements IImportEntity {

  private static final String                           UPSERT_HIERARCHY_TAXONOMY = "UpsertHierarchyTaxonomy";
  static               IGetTaxonomyHierarchyIdsStrategy getHierarchy              = BGProcessApplication.getApplicationContext().getBean(
      GetTaxonomyHierarchyIdsStrategy.class);
  @Override
  public void importEntity(PXONImporter importer) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> taxonomyBlocks = importer.getBlocks().getStepBlocks(ImportSteps.IMPORT_HIERARCHY_TAXONOMY);
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    try(PXONFileParser pxonFileParser = new PXONFileParser(importer.getPath().toString())) {
      List<IConfigClassifierDTO> taxonomyDTOList = new ArrayList<>();
      Node<String> root = new Node<>(ROOT);
      Map<String, ConfigClassifierDTO> taxonomies = new HashMap<>();
      for (Entry taxonomyBlock : taxonomyBlocks.entrySet()) {
        ConfigClassifierDTO configTaxonomyDTO = new ConfigClassifierDTO();
        configTaxonomyDTO.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, (ImportBlockInfo) taxonomyBlock.getValue()));
        // Upsert one by one into RDBMS
        IClassifierDTO classifier = configTaxonomyDTO.getClassifierDTO();
        IClassifierDTO classifierDTO = configurationDAO.createClassifier(
            classifier.getClassifierCode(), classifier.getClassifierType());
        configTaxonomyDTO.setClassifierIID(classifierDTO.getIID());
        taxonomyDTOList.add(configTaxonomyDTO);
        List<String> levelCodes = configTaxonomyDTO.getLevelCodes();
        List<String> levelCodeWithIID = new ArrayList<>();
        for(String levelCode : levelCodes) {
          IPropertyDTO tagDTO = configurationDAO.createProperty(levelCode, PropertyType.TAG);
          String codeWithIID = levelCode + ":" + tagDTO.getPropertyIID();
          levelCodeWithIID.add(codeWithIID);
        }
        configTaxonomyDTO.getLevelCodes().clear();
        configTaxonomyDTO.getLevelCodes().addAll(levelCodeWithIID);
        taxonomies.put(classifier.getClassifierCode(), configTaxonomyDTO);
        prepareHierarchyTree(root, configTaxonomyDTO, classifierDTO);
      }
      prepareList(root, taxonomyDTOList,taxonomies);
      // Upserting classifiers in ODB
      Map<String, Object> responseMap = configurationImport( importer.getImportDTO().getLocaleID(), taxonomyDTOList.toArray(new IConfigClassifierDTO[0]));
      updateHierarchyIIDs(taxonomyDTOList);
      importer.logIds(responseMap);
    } catch (Exception e) {
      importer.incrementNumberOfException();
      throw new RDBMSException(10000, "IOException", e.getMessage());
    }
  }

  public Map<String,Object> configurationImport(String localeID, IConfigClassifierDTO... taxonomies)
      throws CSInitializationException, CSFormatException {
    List<IConfigClassifierDTO> classifierList =  Arrays.asList(taxonomies);
    JSONObject requestModel = new JSONObject();
    requestModel.put(LIST, classifierList);

    return CSConfigServer.instance()
        .request(requestModel, UPSERT_HIERARCHY_TAXONOMY, localeID);
  }

  private void updateHierarchyIIDs(List<IConfigClassifierDTO> taxonomyDTOList) throws Exception
  {
    for (IConfigClassifierDTO taxonomy : taxonomyDTOList) {
      String parentCode = taxonomy.getParentCode();
      IIIDsListModel execute;
      if (!parentCode.equals("-1")) {
        execute = getHierarchy.execute(new IdLabelCodeModel(parentCode));
      }
      else {
        execute = new IIDsListModel();
        ArrayList<Long> iids = new ArrayList<>();
        iids.add(-1L);
        execute.setIids(iids);
      }
      execute.getIids().add(taxonomy.getClassifierDTO().getClassifierIID());
      ConfigurationDAO.instance().updateHierarchyIIDs(taxonomy.getClassifierDTO().getClassifierIID(), execute.getIids());
    }
  }
}
