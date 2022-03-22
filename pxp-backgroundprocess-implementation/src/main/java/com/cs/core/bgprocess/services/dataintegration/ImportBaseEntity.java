package com.cs.core.bgprocess.services.dataintegration;

import java.io.IOException;
import java.util.Map;

import com.cs.core.bgprocess.dto.BGPLog;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.services.dataintegration.PXONImporterBlocksMap.ImportBlockInfo;
import com.cs.core.dataintegration.dto.PXONImporterPlanDTO;
import com.cs.core.pxon.parser.PXONFileParser;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.authorization.utils.PartnerAuthorizationUtils;
import com.cs.di.runtime.entity.dao.IPartnerAuthorizationDAO;
import com.cs.di.runtime.entity.dao.PartnerAuthorizationDAO;

import java.io.IOException;
import java.util.*;


public class ImportBaseEntity implements IImportEntity  {

  private final PXONImporterPlanDTO  importerPlanDTO         = new PXONImporterPlanDTO();
  protected IPartnerAuthorizationDAO partnerAuthorizationDAO = new PartnerAuthorizationDAO();

  @SuppressWarnings("unchecked")
  @Override
  public void importEntity(PXONImporter pxonImporter) throws CSInitializationException, CSFormatException, RDBMSException
  {
    Map<ImportBlockIdentifier, ImportBlockInfo> baseEntityMetaList = pxonImporter.getBlocks().getStepBlocks(ImportSteps.IMPORT_BASEENTITY);
    PXONFileParser pxonFileParser = new PXONFileParser(pxonImporter.getPath().toString());
    BaseEntityDTO baseEntity = new BaseEntityDTO();
    int successCount = 0;
    BGProcessDTO jobData = pxonImporter.getJobData();
    jobData.getSummary().setTotalCount(jobData.getSummary().getTotalCount() + baseEntityMetaList.size());

    importerPlanDTO.fromJSON(jobData.getEntryData().toString());
    Map<String, Object> authorizationMapping = !importerPlanDTO.getPartnerAuthorizationId().equals("null")
        ? partnerAuthorizationDAO.getPartnerAuthMapping(importerPlanDTO.getPartnerAuthorizationId(), importerPlanDTO.getLocaleID(),true)
        : null;
        
    for (Map.Entry<ImportBlockIdentifier, ImportBlockInfo> baseEntityMeta : baseEntityMetaList.entrySet()) {
      BGPLog log = jobData.getLog();
      try {
        ImportBlockInfo baseEntityMetaInfo = baseEntityMeta.getValue();
        baseEntity.fromPXON(PXONImporterBlocksMap.getPXONBlockFromFile(pxonFileParser, baseEntityMetaInfo));
        if (authorizationMapping != null) {
         // LocaleCatalogDAO localeCatalogDAO = pxonImporter.getDAS().getLocaleCatalogDAO(baseEntity);
         // IBaseEntityDTO existingBaseEntityDTO = localeCatalogDAO.getEntityByIID(baseEntity.getBaseEntityIID());

          IBaseEntityDTO existingEntityDTOWithProperties = new BaseEntityDTO();
          /* Set<String> taxonomyIds = new HashSet<>();
          PartnerAuthorizationUtils.getTaxonomyIdsFromBaseEntity(baseEntity.getOtherClassifiers(), taxonomyIds);

          if (existingBaseEntityDTO != null) {
            IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(existingBaseEntityDTO);
            Collection<IPropertyDTO> allEntityProperties = localeCatalogDAO.getAllEntityProperties(baseEntity.getBaseEntityIID());
            existingEntityDTOWithProperties = baseEntityDAO
                .loadPropertyRecords(allEntityProperties.toArray(new IPropertyDTO[allEntityProperties.size()]));

            PartnerAuthorizationUtils.getTaxonomyIdsFromBaseEntity(existingEntityDTOWithProperties.getOtherClassifiers(), taxonomyIds);
          }*/
          
          Map<String, Object> partnerAuthorization = (Map<String, Object>) authorizationMapping.get(PartnerAuthorizationUtils.ENTITY);
          /*((List<String>) partnerAuthorization.get("taxonomyMappings")).addAll(
              partnerAuthorizationDAO.addAuthorizedChildTaxonomies(taxonomyIds, importerPlanDTO.getLocaleID(), partnerAuthorization));*/
          PartnerAuthorizationUtils.importAuthorizationfilter(baseEntity, existingEntityDTOWithProperties, partnerAuthorization);
        }
        pxonImporter.getDAS().createORUpdateBaseEntity(pxonFileParser, baseEntity, baseEntityMetaInfo, baseEntityMetaList, log, importerPlanDTO);
        successCount += 1;
        jobData.getSuccessIds().add(String.valueOf(baseEntity.getBaseEntityIID()));
      }
      catch (CSFormatException | RDBMSException | IOException e) {
        pxonImporter.incrementNumberOfException();
        if (baseEntity.getBaseEntityIID() == 0L) {
          jobData.getFailedIds().add(baseEntity.getBaseEntityID());
        }
        else {
          jobData.getFailedIds().add(String.valueOf(baseEntity.getBaseEntityIID()));
        }
        log.error(e.getMessage());
      }
      catch (Exception e) {
        if (baseEntity.getBaseEntityIID() == 0L) {
          jobData.getFailedIds().add(baseEntity.getBaseEntityID());
        }
        else {
          jobData.getFailedIds().add(String.valueOf(baseEntity.getBaseEntityIID()));
        }
        log.error(e.getMessage());
      }
    }
    jobData.getSummary().setSuccessCount(jobData.getSummary().getSuccessCount() + successCount);
    try {
      pxonFileParser.close();
    }
    catch (IOException e) {
      pxonImporter.incrementNumberOfException();
      jobData.getLog().error(e.getMessage());
    }
  }
}
