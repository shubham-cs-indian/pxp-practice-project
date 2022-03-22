package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.SystemLevelIds;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.exception.goldenrecord.MultipleGoldenRecordsFoundInBucket;
import com.cs.core.runtime.interactor.model.goldenrecord.GoldenRecordRuleKlassInstancesMergeViewResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.utils.BaseEntityUtils;

public abstract class AbstractGetGoldenRecordMergeView<P extends IGoldenRecordRuleKlassInstancesMergeViewRequestModel, R extends IGoldenRecordRuleKlassInstancesMergeViewResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected GoldenRecordUtils                                    goldenRecordUtils;
  
  protected abstract void getRecommendedPropertyDataForMergeView(P dataModel, IConfigDetailsForGetKlassInstancesToMergeModel configDetails,
      IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel, List<IBaseEntityDTO> baseEntitiesDTOs,
      ILocaleCatalogDAO localeCatlogDAO) throws Exception;
  
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    GoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    IGoldenRecordDTO goldenRecordDTO = goldenRecordBucketDAO.getGoldenRecordRuleAndBaseEntityIIDs(dataModel.getBucketInstanceId(),
        dataModel.getGoldenRecordId());
    List<String> baseEntityIids = goldenRecordDTO.getLinkedBaseEntities().stream().map(baseEntityIID -> String.valueOf(baseEntityIID))
        .collect(Collectors.toList());
    String ruleId = goldenRecordDTO.getRuleId();
    
    List<String> taxonomyIds = new ArrayList<>();
    List<String> types = new ArrayList<>();
    List<String> languageCodes = new ArrayList<>();
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    List<IBaseEntityDTO> baseEntitiesDTOs = localeCatlogDAO.getBaseEntitiesByIIDs(baseEntityIids);
    for (IBaseEntityDTO baseEntityDTO : baseEntitiesDTOs) {
      languageCodes.addAll(baseEntityDTO.getLocaleIds());
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
      types.addAll(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO));
      taxonomyIds.addAll(BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers()));
    }
    
    int goldenRecordInstancesCount = Collections.frequency(types, SystemLevelIds.GOLDEN_ARTICLE_KLASS);
    if (goldenRecordInstancesCount > 1) {
      throw new MultipleGoldenRecordsFoundInBucket();
    }
    
    IConfigDetailsForGetKlassInstancesToMergeModel configDetails = goldenRecordUtils.getConfigDetails(ruleId, taxonomyIds, types, languageCodes);
    dataModel.setMergeEffect(configDetails.getGoldenRecordRule().getMergeEffect());
    dataModel.setDependentAttributeIds(configDetails.getDependendAttributeIds());
    
    IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel = prepareResponseModel(baseEntityIids, taxonomyIds, types,
        dataModel.getBucketInstanceId());
    
    getRecommendedPropertyDataForMergeView(dataModel, configDetails, responseModel, baseEntitiesDTOs, localeCatlogDAO);
    
    responseModel.setConfigDetails(configDetails);
    return (R) responseModel;
  }
  
  private IGoldenRecordRuleKlassInstancesMergeViewResponseModel prepareResponseModel(List<String> baseEntityIids, List<String> taxonomyIds,
      List<String> types, String bucketId) throws Exception
  {
    IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel = new GoldenRecordRuleKlassInstancesMergeViewResponseModel();
    List<String> klassIds = types.stream().distinct().collect(Collectors.toList());
    List<String> taxonomies = taxonomyIds.stream().distinct().collect(Collectors.toList());
    responseModel.setKlassIds(klassIds);
    responseModel.setTaxonomyIds(taxonomies);
    responseModel.setKlassInstanceIds(baseEntityIids);
    goldenRecordUtils.fillMatchPropertiesModelAndRule(responseModel, bucketId);
    return responseModel;
  }
  
}
