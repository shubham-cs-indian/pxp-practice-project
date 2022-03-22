package com.cs.core.runtime.klassinstance;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.relationship.IRelationshipInstance;
import com.cs.core.config.interactor.entity.relationship.RelationshipInstance;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.*;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetKlassInstancesToCompare;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.Map.Entry;

public abstract class AbstractGetInstancesDetailForComparisonService<P extends IGetInstancesDetailForComparisonModel, R extends IKlassInstancesListForComparisonModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForGetKlassInstancesToCompare getConfigDetailsForGetKlassInstancesToCompare;
  
  @Autowired
  protected RDBMSComponentUtils                            rdbmsUtils;
  
  @Autowired
  protected ISessionContext                                context;
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P requestModel) throws Exception
  {
    IKlassInstancesForComparisonStrategyModel strategyModel = new KlassInstancesForComparisonStrategyModel();
    List<IKlassInstancesForComparisonModel> informationModels = new ArrayList<>();
    List<IIdAndBaseType> productListsToCompare = requestModel.getProductListsToCompare();
    Set<String> types = new HashSet<>();
    Set<String> taxonomyIds = new HashSet<>();
    List<IBaseEntityDAO> baseEntityDAOS = fillTypeInfoAndGetBaseEntityDAOS(productListsToCompare, types, taxonomyIds);
    strategyModel.setTaxonomyIds(new ArrayList<>(taxonomyIds));
    strategyModel.setTypes(new ArrayList<>(types));
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(types, taxonomyIds);
    
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, String> propertyCodeVsSideId = getPropertyCodeVsSideIdMap(referencedElements);
    
    Set<String> side2InstanceIds = new HashSet<>();
    List<String> productIds = new ArrayList<>();
    for (IBaseEntityDAO baseEntityDAO : baseEntityDAOS) {
      IKlassInstancesForComparisonModel comparison = fillKlassInstanceInfoForComparison(referencedElements,propertyCodeVsSideId, side2InstanceIds, productIds, baseEntityDAO);
      informationModels.add(comparison);
    }
    
    fillSide2InstanceInfo(informationModels, side2InstanceIds, productIds);
    
    strategyModel.setKlassInstancesDetails(informationModels);
    strategyModel.setConfigDetails(configDetails);
    return (R) strategyModel;
  }

  private void fillSide2InstanceInfo(List<IKlassInstancesForComparisonModel> informationModels,
      Set<String> side2InstanceIds, List<String> productIds) throws Exception
  {
    for (String side2InstanceId : side2InstanceIds) {
      IKlassInstanceInformationModel klassInstanceInformationModel = fillKlassInstanceInfoModelForSide2Instance(
          informationModels, side2InstanceId);
 
      String defaultAssetInstanceId = klassInstanceInformationModel.getDefaultAssetInstanceId();
      if (defaultAssetInstanceId != null && !defaultAssetInstanceId.isEmpty() && !(side2InstanceIds.contains(defaultAssetInstanceId)) && !(productIds.contains(defaultAssetInstanceId))) {
        fillKlassInstanceInfoModelForSide2Instance(informationModels, defaultAssetInstanceId);
      }
    }
  }

  private IKlassInstanceInformationModel fillKlassInstanceInfoModelForSide2Instance(
      List<IKlassInstancesForComparisonModel> informationModels, String side2InstanceId)
      throws Exception
  {
    IKlassInstancesForComparisonModel comparison = new KlassInstancesForComparisonModel();
    IBaseEntityDTO baseEntityDTO = rdbmsUtils.getBaseEntityDTO(Long.parseLong(side2InstanceId));
    IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO, rdbmsUtils);
    if (baseEntityDTO.getBaseType().equals(BaseType.ASSET)) {
      List<IContentAttributeInstance> attributes = new ArrayList<>();
      KlassInstanceBuilder.fillEntityExtensionInAssetCoverFlowAttribute(attributes, baseEntityDTO);
      klassInstanceInformationModel.setAttributes(attributes);
    }
    comparison.setKlassInstance(klassInstanceInformationModel);
    informationModels.add(comparison);
    return klassInstanceInformationModel;
  }

  private IKlassInstancesForComparisonModel fillKlassInstanceInfoForComparison(
      Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, String> propertyCodeVsSideId, Set<String> side2InstanceIds,
      List<String> productIds, IBaseEntityDAO baseEntityDAO)
      throws Exception, RDBMSException, CSFormatException
  {
    IKlassInstancesForComparisonModel comparison = new KlassInstancesForComparisonModel();
    Map<String, List<IRelationshipInstance>> contentRelationship = new HashMap<>();
    Map<String, List<IRelationshipInstance>> natureRelationship = new HashMap<>();
    
    List<IContentAttributeInstance> attributes = new ArrayList<>();
    List<ITagInstance> tags = new ArrayList<>();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO, rdbmsUtils);
    
    Collection<IPropertyDTO> propertyDTOs = rdbmsUtils.getAllBaseEntityProperties(baseEntityDTO.getBaseEntityIID());
    productIds.add(String.valueOf(baseEntityDTO.getBaseEntityIID()));
    IBaseEntityDTO baseEntityDTOByIID = baseEntityDAO.loadPropertyRecords(propertyDTOs.toArray(new IPropertyDTO[0]));
    
    for (IPropertyRecordDTO property : baseEntityDTOByIID.getPropertyRecords()) {
      int ordinal = property.getProperty().getSuperType().ordinal();
      
      if (ordinal == SuperType.ATTRIBUTE.ordinal()) {
        PropertyInstanceUtils.fillAttributeInstances(property, baseEntityDTO, attributes);
      }
      else if (ordinal == SuperType.TAGS.ordinal()) {
        PropertyInstanceUtils.fillTagInstances(property, baseEntityDTO, tags);
      }
      else if (ordinal == SuperType.RELATION_SIDE.ordinal()) {
        IReferencedSectionRelationshipModel iReferencedSectionElementModel = (IReferencedSectionRelationshipModel) referencedElements.get(propertyCodeVsSideId.get(property.getProperty().getPropertyCode()));
        if (iReferencedSectionElementModel != null) {
          List<IRelationshipInstance> relationshipInstance = getRelationshipInstance(property, baseEntityDTO, iReferencedSectionElementModel, side2InstanceIds);
          if (iReferencedSectionElementModel.getIsNature()) {
            natureRelationship.put(iReferencedSectionElementModel.getId(), relationshipInstance);
          }
          else {
            contentRelationship.put(iReferencedSectionElementModel.getId(), relationshipInstance);
          }
        }
      }
    }
        
    if (baseEntityDTO.getBaseType().equals(BaseType.ASSET)) {
      KlassInstanceBuilder.fillEntityExtensionInAssetCoverFlowAttribute(attributes, baseEntityDTO);
    }
    
    klassInstanceInformationModel.setAttributes(attributes);
    klassInstanceInformationModel.setTags(tags);
    comparison.setKlassInstance(klassInstanceInformationModel);
    comparison.setNatureRelationships(natureRelationship);
    comparison.setContentRelationships(contentRelationship);
    return comparison;
  }

  private Map<String, String> getPropertyCodeVsSideIdMap(
      Map<String, IReferencedSectionElementModel> referencedElements)
  {
    Map<String, String> propertyCodeVsSideId = new HashMap<>();
    
    for (Entry<String, IReferencedSectionElementModel> entry : referencedElements.entrySet()) {
      IReferencedSectionElementModel value = entry.getValue();
      //TODO:handle self relationship
      if (value.getType().equals(CommonConstants.RELATIONSHIP_PROPERTY)) {
        propertyCodeVsSideId.put(value.getPropertyId(), value.getId());
      }
    }
    return propertyCodeVsSideId;
  }

  private IGetConfigDetailsForCustomTabModel getConfigDetails(Set<String> types,
      Set<String> taxonomyIds) throws Exception
  {
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(new ArrayList<>(types));
    multiclassificationRequestModel.setSelectedTaxonomyIds(new ArrayList<>(taxonomyIds));
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsForGetKlassInstancesToCompare.execute(multiclassificationRequestModel);
    return configDetails;
  }

  private List<IBaseEntityDAO> fillTypeInfoAndGetBaseEntityDAOS(List<IIdAndBaseType> productListsToCompare, Set<String> types, Set<String> taxonomyIds)
      throws Exception
  {
    List<IBaseEntityDAO> baseEntityDAOS = new ArrayList<>();
    for (IIdAndBaseType idAndbase : productListsToCompare) {
      IBaseEntityDAO baseEntityDAO = rdbmsUtils.getBaseEntityDAO(Long.parseLong(idAndbase.getId()));
      baseEntityDAOS.add(baseEntityDAO);
      IBaseEntityDTO baseEntityDTOByIID = baseEntityDAO.getBaseEntityDTO();
      List<String> taxonomy = new ArrayList<>();
      List<String> classifierCodes = new ArrayList<String>();
      classifierCodes.add(baseEntityDTOByIID.getNatureClassifier().getClassifierCode());
      for (IClassifierDTO classifier : baseEntityDTOByIID.getOtherClassifiers()) {
        ClassifierType classifierType = classifier.getClassifierType();
        if (classifierType.equals(ClassifierType.CLASS)) {
          classifierCodes.add(classifier.getClassifierCode());
        }
        else {
          taxonomy.add(classifier.getClassifierCode());
        }
      }
      types.addAll(classifierCodes);
      taxonomyIds.addAll(taxonomy);
    }
    return baseEntityDAOS;
  }

  protected List<IRelationshipInstance> getRelationshipInstance(IPropertyRecordDTO property,
      IBaseEntityDTO baseEntityDTO, IReferencedSectionRelationshipModel referencedElement,
      Set<String> side2InstanceIds) throws NumberFormatException, Exception
  {
    List<IRelationshipInstance> list = new ArrayList<>();
    IRelationsSetDTO relationsSetDTO = (IRelationsSetDTO) property;
    Set<IEntityRelationDTO> relations = relationsSetDTO.getRelations();
    for (IEntityRelationDTO relation : relations) {
      IRelationshipInstance relationshipInstance = new RelationshipInstance();
      relationshipInstance.setId(relationsSetDTO.getProperty().getPropertyCode());
      relationshipInstance.setRelationshipId(relationsSetDTO.getProperty().getPropertyCode());
      relationshipInstance.setSide2InstanceId(String.valueOf(relation.getOtherSideEntityIID()));
      relationshipInstance.setSideId(referencedElement.getId());
      side2InstanceIds.add(String.valueOf(relation.getOtherSideEntityIID()));
      list.add(relationshipInstance);
    }
    return list;
  }
}
