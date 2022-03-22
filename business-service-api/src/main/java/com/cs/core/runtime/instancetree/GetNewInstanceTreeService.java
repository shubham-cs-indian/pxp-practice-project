package com.cs.core.runtime.instancetree;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.process.idto.IRuleViolationDTO;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.message.MessageInformation;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.GetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewInstanceTreeResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGetNewInstanceTreeStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class GetNewInstanceTreeService extends AbstractNewInstanceTree<IGetInstanceTreeRequestModel, 
          IGetNewInstanceTreeResponseModel> implements IGetNewInstanceTreeService{
  
  @Autowired
  private IGetConfigDetailsForGetNewInstanceTreeStrategy  getConfigDetailsForGetNewInstanceTreeStrategy;
  
  
  @Override
  protected void prepareRuntimeRequestModel(IGetInstanceTreeRequestModel model,
      IConfigDetailsForNewInstanceTreeModel configDetails)
  {
    super.prepareRuntimeRequestModel(model, configDetails);
    model.setSide2LinkedVariantKrIds(((IConfigDetailsForGetNewInstanceTreeModel)configDetails).getSide2LinkedVariantKrIds());
  }
  
  @Override
  protected IGetConfigDetailsForGetNewInstanceTreeRequestModel getConfigDetailsRequestModel()
  {
    return new GetConfigDetailsForGetNewInstanceTreeRequestModel();
  }
  
  protected IGetNewInstanceTreeResponseModel executeRuntimeStrategy(
      IGetInstanceTreeRequestModel dataModel, IConfigDetailsForNewInstanceTreeModel configDetails,
      Map<String, List<Map<String, Object>>> idVsHighlightsMap)
      throws Exception
  {
    IGetNewInstanceTreeResponseModel responseModel = new GetNewInstanceTreeResponseModel();
    
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();

    List<IBaseEntityDTO> baseEntities = getBaseEntityDTOsAsPerSearchCriteria(
        dataModel, configDetails, localeCatalogDao, responseModel, idVsHighlightsMap);
    
    List<IKlassInstanceInformationModel> children = responseModel.getChildren();
    if (baseEntities != null && !baseEntities.isEmpty()) {
      populateResponseList(children, baseEntities, dataModel, configDetails, localeCatalogDao);
    }
    prepareResponseModel(dataModel, responseModel, baseEntities);
    
    return responseModel;
  }

  @Override
  protected IConfigDetailsForNewInstanceTreeModel executeConfigDetailsStrategy(
      IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel) throws Exception
  {
    return getConfigDetailsForGetNewInstanceTreeStrategy.execute(configRequestModel);
  }
  
  @Override
  protected List<String> getModuleEntities(IGetInstanceTreeRequestModel model) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(model.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }
  
  protected void populateResponseList(List<IKlassInstanceInformationModel> responseList,
      List<IBaseEntityDTO> listOfDTO, IGetInstanceTreeRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, ILocaleCatalogDAO localeCatalogDAO) throws Exception
  {
    Set<Long> baseEntityIIds = new HashSet<>();
    Set<Long> parentIIds = new HashSet<>();
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      baseEntityIIds.add(baseEntityDTO.getBaseEntityIID());
      long parentId = baseEntityDTO.getOriginBaseEntityIID();
      if (parentId != 0 && baseEntityDTO.isClone()) {
        parentIIds.add(parentId);
      }
    }
    IRuleCatalogDAO openRuleDAO = rdbmsComponentUtils.openRuleDAO();
    Map<Long,Set<IRuleViolationDTO>> violations = openRuleDAO.loadViolations(baseEntityIIds);
    
    IUniquenessViolationDAO uniquenessDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openUniquenessDAO();
    Map<Long, Integer> uniquenessViolationCount = uniquenessDAO.getUniquenessViolationCount(baseEntityIIds);
    
    List<String> linkedVariantRelationshipCodes = ((IConfigDetailsForGetNewInstanceTreeModel)configDetails).getLinkedVariantCodes();
    Map<Long, Long> variantIdParentIdMap = getVariantIdParentIdMap(listOfDTO, localeCatalogDAO,linkedVariantRelationshipCodes);
    parentIIds.addAll(variantIdParentIdMap.values());
    
    Map<Long, String> parentEntityNames = localeCatalogDAO.getBaseEntityNamesByIID(parentIIds);
    
    Set<IPropertyDTO> properties = getPropertyDTOs(configDetails, dataModel.getXRayAttributes());
    Map<Long, Set<IPropertyRecordDTO>> propertyRecordsForEntities = localeCatalogDAO.getPropertyRecordsForEntities(baseEntityIIds, properties.toArray(new IPropertyDTO[properties.size()]));
    
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      Long baseEntityIId = baseEntityDTO.getBaseEntityIID();
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModelWithoutViolations(baseEntityDTO, rdbmsComponentUtils);
      klassInstanceInformationModel.setBranchOfLabel(getBranchOfLabel(baseEntityDTO, parentEntityNames));
      fillViolations(klassInstanceInformationModel,violations.get(baseEntityIId),uniquenessViolationCount.get(baseEntityIId));
      fillAttributesAndTags(configDetails, baseEntityDTO, propertyRecordsForEntities.get(baseEntityIId), klassInstanceInformationModel,properties);
      fillVariantOfInfo(variantIdParentIdMap, parentEntityNames, klassInstanceInformationModel, baseEntityIId);
      responseList.add(klassInstanceInformationModel);
    }
  }
  
  private void fillViolations(IKlassInstanceInformationModel klassInstanceInformationModel,Set<IRuleViolationDTO> violations, Integer uniquenessCount) throws RDBMSException
  {
    IMessageInformation messages = klassInstanceInformationModel.getMessages();
    if (messages == null) {
      klassInstanceInformationModel.setMessages(new MessageInformation());
    }
    messages = klassInstanceInformationModel.getMessages();
    uniquenessCount = uniquenessCount == null? 0: uniquenessCount;
    messages.setIsUniqueViolationCount(uniquenessCount);
    klassInstanceInformationModel.setIsUniqueViolationCount(uniquenessCount);

    if(violations == null) {
      return;
    }
    for (IRuleViolationDTO violation : violations) {
      Long ruleExpressionIID = Long.valueOf(violation.getRuleExpressionIID());
      String color = violation.getColor();
      if (ruleExpressionIID.equals(-1l)) {
        if (color.equals("red")) {
          messages.setMandatoryViolationCount(messages.getMandatoryViolationCount() + 1);
          klassInstanceInformationModel.setMandatoryViolationCount(messages.getMandatoryViolationCount());
          messages.setIsRed(true);
        }
        else {
          messages.setShouldViolationCount(messages.getShouldViolationCount() + 1);
          klassInstanceInformationModel.setShouldViolationCount(messages.getShouldViolationCount());
          messages.setIsOrange(true);
        }
      }
      else if (violation.getRuleCode().isEmpty()) {
        IClassifierDTO classifier = ConfigurationDAO.instance().getClassifierByIID(ruleExpressionIID);
        if (klassInstanceInformationModel.getTypes().contains(classifier.getCode()) || klassInstanceInformationModel.getSelectedTaxonomyIds().contains(classifier.getCode())) {
          messages.setIsRed(true);
          messages.setIsUniqueViolationCount(messages.getIsUniqueViolationCount() + 1);
          klassInstanceInformationModel.setIsUniqueViolationCount(klassInstanceInformationModel.getIsUniqueViolationCount() + 1);
        }
      }
      else {
        if (color.equals("red")) {
          messages.setRedCount(messages.getRedCount() + 1);
          messages.setIsRed(true);
        }
        else if (color.equals("yellow")) {
          messages.setYellowCount(messages.getYellowCount() + 1);
          messages.setIsYellow(true);
        }
        else if (color.equals("orange")) {
          messages.setOrangeCount(messages.getOrangeCount() + 1);
          messages.setIsOrange(true);
        }
      }
    }

  }

  private String getBranchOfLabel(IBaseEntityDTO baseEntityDTO, Map<Long,String> parentEntityNames) throws Exception
  {
    long parentId = baseEntityDTO.getOriginBaseEntityIID();
    String branchOfLabel = "";
    if (parentId != 0 && baseEntityDTO.isClone()) {
      branchOfLabel = parentEntityNames.get(parentId);
    }
    return branchOfLabel;
  }
  
  protected void fillVariantOfInfo(Map<Long, Long> variantIdParentIdMap,Map<Long, String> parentEntityNames,
      IKlassInstanceInformationModel klassInstanceInformationModel, Long baseEntityIID)
      throws Exception
  {
    if(variantIdParentIdMap.containsKey(baseEntityIID)) {
      Long parentId = variantIdParentIdMap.get(baseEntityIID);
      klassInstanceInformationModel.setVariantOf(parentId.toString());
      klassInstanceInformationModel.setVariantOfLabel(parentEntityNames.get(parentId));
    }
  }
}
