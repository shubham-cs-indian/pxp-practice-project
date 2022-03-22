package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.goldenrecord.GetConfigDetailsForMatchedInstancesRequestModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForMatchedInstancesRequestModel;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetAttributeInstanceInformationModel;
import com.cs.core.runtime.interactor.model.configuration.ContentIdentifierModel;
import com.cs.core.runtime.interactor.model.configuration.IContentIdentifierModel;
import com.cs.core.runtime.interactor.model.goldenrecord.BucketKlassInstanceInfoModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GetBucketInstancesResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IBucketKlassInstanceInfoModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetBucketInstancesResponseModel;
import com.cs.core.runtime.interactor.model.templating.IGetAllLinkedVariantPropertyCodesResponseModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetAllLinkedVariantPropertyCodesStrategy;
import com.cs.core.runtime.strategy.usecase.goldenrecord.IGetConfigDetailsForGoldenRecordMatchedInstancesStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.utils.BaseEntityUtils;

public abstract class AbstractGetGoldenRecordMatchedInstances<P extends IGetBucketInstancesRequestModel, R extends IGetBucketInstancesResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForGoldenRecordMatchedInstancesStrategy getConfigDetailsForGoldenRecordMatchedInstancesStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                      rdbmsComponentUtils;
  
  @Autowired
  protected IGetAllLinkedVariantPropertyCodesStrategy                getAllLinkedVariantPropertyCodesStrategy;
  
  @SuppressWarnings("unchecked")
  @Override
  public R executeInternal(P dataModel) throws Exception
  {
    IGetAllLinkedVariantPropertyCodesResponseModel linkedVariantKRIds = getAllLinkedVariantPropertyCodesStrategy.execute(null);
    IGoldenRecordBucketDAO goldenRecordBucketDAO = new GoldenRecordBucketDAO();
    List<String> baseEntityIids = goldenRecordBucketDAO.getGoldenRecordLinkedBaseEntityIidsById(dataModel.getId());
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<IBaseEntityDTO> baseEntityDTOs = localeCatalogDAO.getBaseEntitiesByIIDs(baseEntityIids);
    Map<Long, Long> linkedVariantIds = localeCatalogDAO.getLinkedVariantIds(baseEntityDTOs,linkedVariantKRIds.getLinkedVariantCodes());
    Set<String> klassIds = new HashSet<>();
    Set<String> taxonomyIds = new HashSet<>();
    Set<String> organizationIds = new HashSet<>();
    
    IGetBucketInstancesResponseModel response = new GetBucketInstancesResponseModel();
    Map<String, IAssetAttributeInstanceInformationModel> referenecedAssets = new HashedMap<>();
    response.setReferencedAssets(referenecedAssets);
    List<IBucketKlassInstanceInfoModel> klassInstancesList = new ArrayList<>();
    for (IBaseEntityDTO baseEntityDTO : baseEntityDTOs) {
      IBucketKlassInstanceInfoModel klassInstance = new BucketKlassInstanceInfoModel();
      klassInstance.setId(baseEntityDTO.getBaseEntityID());
      klassInstance.setName(baseEntityDTO.getBaseEntityName());
      long defaultImageIID = baseEntityDTO.getDefaultImageIID();
      if(defaultImageIID!=0L) {
        IBaseEntityDTO baseEntityDTOByIID = rdbmsComponentUtils.getBaseEntityDTO(defaultImageIID);
        IAssetAttributeInstanceInformationModel fillAssetInformationModel = BaseEntityUtils.fillAssetInformationModel(baseEntityDTOByIID);
        referenecedAssets.put(fillAssetInformationModel.getAssetInstanceId(), fillAssetInformationModel);
        klassInstance.setDefaultAssetInstanceId(String.valueOf(defaultImageIID));
      }
      List<String> baseEntityKlassIds = new ArrayList<>();
      List<String> baseEntityTaxonomyIds = new ArrayList<>();
      baseEntityKlassIds.add(baseEntityDTO.getNatureClassifier().getCode());
      for (IClassifierDTO otherClassifierDTO : baseEntityDTO.getOtherClassifiers()) {
        if (otherClassifierDTO.getClassifierType().equals(IClassifierDTO.ClassifierType.CLASS)) {
          baseEntityKlassIds.add(otherClassifierDTO.getCode());
        }
        else {
          baseEntityTaxonomyIds.add(otherClassifierDTO.getCode());
        }
      }
      klassInstance.setTypes(baseEntityKlassIds);
      klassInstance.setTaxonomyIds(baseEntityTaxonomyIds);
      klassIds.addAll(baseEntityKlassIds);
      taxonomyIds.addAll(baseEntityTaxonomyIds);
      
      IContentIdentifierModel contentIdentifierModel = new ContentIdentifierModel();
      ICatalogDTO catalogDTO = baseEntityDTO.getCatalog();
      String physicalCatalogId = catalogDTO.getCatalogCode();
      contentIdentifierModel.setPhysicalCatalogId(physicalCatalogId);
      contentIdentifierModel.setPortalId(physicalCatalogId);
      String organizationCode = baseEntityDTO.getSourceOrganizationCode().equals(IStandardConfig.STANDARD_ORGANIZATION_RCODE)
          ? IStandardConfig.STANDARD_ORGANIZATION_CODE
          : baseEntityDTO.getSourceOrganizationCode();
      contentIdentifierModel.setOrganizationId(organizationCode);
      contentIdentifierModel.setLogicalCatalogId(organizationCode);
      klassInstance.setPartnerSource(contentIdentifierModel);
      organizationIds.add(organizationCode);
      fillBranchOfLabelAndVariantOfLabel(baseEntityDTO, klassInstance, linkedVariantIds, localeCatalogDAO);
      klassInstancesList.add(klassInstance);
    }
    response.setCount((long) klassInstancesList.size());
    response.setKlassInstancesList(klassInstancesList);
    
    IGetConfigDetailsForMatchedInstancesRequestModel requestModel = new GetConfigDetailsForMatchedInstancesRequestModel();
    requestModel.setKlassIds(new ArrayList<>(klassIds));
    requestModel.setSelectedTaxonomyIds(new ArrayList<>(taxonomyIds));
    requestModel.setOrganizationIds(new ArrayList<>(organizationIds));
    IGetBucketInstancesResponseModel configDetails = getConfigDetailsForGoldenRecordMatchedInstancesStrategy.execute(requestModel);
    response.setReferencedKlasses(configDetails.getReferencedKlasses());
    response.setReferencedTaxonomies(configDetails.getReferencedTaxonomies());
    response.setReferencedOrganizations(configDetails.getReferencedOrganizations());
    
    return (R) response;
  }

  private void fillBranchOfLabelAndVariantOfLabel(IBaseEntityDTO baseEntityDTO, IBucketKlassInstanceInfoModel klassInstance,
      Map<Long, Long> linkedVariantIds, ILocaleCatalogDAO localeCatalogDAO) throws RDBMSException
  {
    Set<Long> baseEntityIids = new HashSet<>();
    Long cloneBaseEntityIid = null, variantBaseEntityIid = null;
    if(baseEntityDTO.isClone()) {
      cloneBaseEntityIid = baseEntityDTO.getOriginBaseEntityIID();
      if(cloneBaseEntityIid != null)
        baseEntityIids.add(cloneBaseEntityIid);
    }
    
    Long baseEntityIID = baseEntityDTO.getBaseEntityIID();
    if(!linkedVariantIds.isEmpty() && linkedVariantIds.containsKey(baseEntityIID)) {
      variantBaseEntityIid = linkedVariantIds.get(baseEntityIID);
      baseEntityIids.add(variantBaseEntityIid);
    }
    
    Map<Long, String> baseEntityIidVsName = localeCatalogDAO.getBaseEntityNamesByIID(baseEntityIids);
    if(baseEntityDTO.isClone()) {
      klassInstance.setBranchOfLabel(baseEntityIidVsName.get(cloneBaseEntityIid));
    }
    
    if(!linkedVariantIds.isEmpty() && linkedVariantIds.containsKey(baseEntityIID)) {
      klassInstance.setVariantOfLabel(baseEntityIidVsName.get(variantBaseEntityIid));
    }
  }
}