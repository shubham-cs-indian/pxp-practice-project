package com.cs.dam.runtime.assetinstance.smartdocument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.smartdocument.IGetSmartDocumentInfoModel;
import com.cs.core.config.interactor.model.smartdocument.preset.IGetSmartDocumentPresetModel;
import com.cs.core.config.strategy.usecase.smartdocument.IGetConfigDetailsWithoutPermissionsForSmartDocumentStrategy;
import com.cs.core.data.Text;
import com.cs.core.exception.base.smartdocument.ProductsNotEligibleException;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.smartdocument.GetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGenerateSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.IGetInstancesForSmartDocumentResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.IMulticlassificationRequestModelForSmartDocument;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentRendererRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentRendererResponseModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTemplateEngineRequestModel;
import com.cs.core.runtime.interactor.model.smartdocument.ISmartDocumentTemplateEngineResponseModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.strategy.usecase.smartdocument.IGetSmartDocumentConfigInfoStrategy;
import com.cs.core.strategy.renderer.pdfreactor.RenderFromPdfReactor;
import com.cs.core.strategy.templateengine.velocity.VelocityTemplateEngineStrategy;
import com.cs.dam.runtime.smartdocument.IGenerateSmartDocumentService;
import com.cs.dam.runtime.util.AssetInstanceUtils;

/**
 * This service is used to generate smart documents.
 * 
 * @author mrunali.dhenge
 *
 */

@Service
public class GenerateSmartDocumentService extends
    AbstractGenerateSmartDocument<IGenerateSmartDocumentRequestModel, IGenerateSmartDocumentResponseModel>
    implements IGenerateSmartDocumentService {
  
  @Autowired
  protected IGetSmartDocumentConfigInfoStrategy                         getSmartDocumentConfigInfoStrategy;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsForSmartDocumentStrategy getConfigDetailsWithoutPermissionsForSmartDocumentStrategy;
  
  @Autowired
  protected VelocityTemplateEngineStrategy                              velocityTemplateEngineStrategy;
  
  @Autowired
  protected RenderFromPdfReactor                                        renderFromPdfReactor;
  
  /**
   * This method returns the license key of PDFreactor, zipTemplateID, preset
   * information from orientDB.
   */
  @Override
  protected IGetSmartDocumentInfoModel getSmartDocumentConfigInformation(
      String smartDocumentPresetId) throws Exception
  {
    return getSmartDocumentConfigInfoStrategy.execute(new IdParameterModel(smartDocumentPresetId));
  }
  
  /**
   * This method returns filtered klass instances (filters according to selected
   * language, klass and taxonomies in preset configuration), labels of klass
   * instances which are not available in selected language, image information
   * of instances.
   */
  @Override
  protected IGetInstancesForSmartDocumentResponseModel getInstancesForSmartDocument(
      IGetInstancesForSmartDocumentRequestModel instanceIdModel) throws Exception
  {
    IGetInstancesForSmartDocumentResponseModel responseModel = new GetInstancesForSmartDocumentResponseModel();
    Map<String, IAssetInformationModel> instancesImageAttribute = new HashMap<>();
    List<Long> baseEntityIIds = getFilteredBaseEntityIIDs(instanceIdModel, responseModel);
    
    List<IBaseEntityDTO> baseEntities = new ArrayList<>();
    for (Long entityIID : baseEntityIIds) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(entityIID);
      IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
      baseEntities.add(baseEntityDTO);
      
      long defaultImageIID = baseEntityDTO.getDefaultImageIID();
      if (defaultImageIID != 0) {
        IBaseEntityDAO assetEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(defaultImageIID);
        IBaseEntityDTO assetEntityDTO = assetEntityDAO.getBaseEntityDTO();
        IAssetInformationModel assetInfoModel = AssetInstanceUtils
            .getAssetInformationModel(assetEntityDTO, assetEntityDTO.getEntityExtension());
        instancesImageAttribute.put(baseEntityDTO.getBaseEntityID(), assetInfoModel);
      }
    }
    responseModel.setKlassInstances(baseEntities);
    responseModel.setInstancesImageAttribute(instancesImageAttribute);
    return responseModel;
  }
  
  /**
   * Returns the list of base entity name of passed baseEntityIIds
   * 
   * @param baseEntityIIds
   * @return
   * @throws Exception
   */
  private List<String> getInstancesLabels(List<Long> baseEntityIIds) throws Exception
  {
    List<String> labels = new ArrayList<>();
    for (Long entityIID : baseEntityIIds) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(entityIID);
      labels.add(baseEntityDAO.getBaseEntityDTO()
          .getBaseEntityName());
    }
    
    return labels;
  }
  
  /**
   * Returns the list of filtered baseEntityIIds on the basis of language, klass
   * and taxonomies selected in preset configuration.
   * 
   * @param instanceIdModel
   * @param responseModel
   * @return
   * @throws Exception
   */
  private List<Long> getFilteredBaseEntityIIDs(
      IGetInstancesForSmartDocumentRequestModel instanceIdModel,
      IGetInstancesForSmartDocumentResponseModel responseModel) throws Exception
  {
    IGetSmartDocumentPresetModel smartDocumentPresetConfigData = instanceIdModel
        .getSmartDocumentPresetConfigData();
    List<String> klassClassfierCodes = smartDocumentPresetConfigData.getKlassIds();
    List<String> taxonomyClassifierCodes = smartDocumentPresetConfigData.getTaxonomyIds();
    String languageCode = smartDocumentPresetConfigData.getLanguageCode();
    List<Long> notAvailableIIds = instanceIdModel.getKlassInstanceIds();
    
    // Fetch baseEntityIIds exists in selected preset language
    String entityIIdsString = Text.join(" | ", instanceIdModel.getKlassInstanceIds());
    StringBuilder searchExpression = searchAssembler.getBaseQuery(BaseType.UNDEFINED);
    searchExpression.append(String.format("$entityiid = (%s)", entityIIdsString));
    searchExpression.append(String.format(" $entity existin  [N>%s] ", languageCode));
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    List<Long> baseEntityIIds = localeCatlogDAO
        .getAllEntityIIdsBySearchExpression(searchExpression.toString());
    
    notAvailableIIds.removeAll(baseEntityIIds);
    responseModel.setKlassInstancesNotAvailableLabels(getInstancesLabels(notAvailableIIds));
    
    // Filter baseEntityIIds by klasses and taxonomies selected in preset
    if (!baseEntityIIds.isEmpty()
        && !(klassClassfierCodes.isEmpty() && taxonomyClassifierCodes.isEmpty())) {
      entityIIdsString = Text.join(" | ", baseEntityIIds);
      searchExpression = searchAssembler.getBaseQuery(BaseType.UNDEFINED);
      searchExpression.append(String.format("$entityiid = (%s)", entityIIdsString));
      searchExpression.append(searchAssembler.generateEntityFilterExpression(klassClassfierCodes,
          taxonomyClassifierCodes, false, new ArrayList<>()));
      baseEntityIIds = rdbmsComponentUtils.getLocaleCatlogDAO()
          .getAllEntityIIdsBySearchExpression(searchExpression.toString());
    }
    
    if (baseEntityIIds.isEmpty()) {
      throw new ProductsNotEligibleException();
    }
    
    return baseEntityIIds;
  }
  
  /**
   * This method returns the config details of given article instance, according
   * to selected language.
   */
  @Override
  protected IGetConfigDetailsForCustomTabModel getConfigDetailsForInstance(
      IMulticlassificationRequestModelForSmartDocument requestModel) throws Exception
  {
    return getConfigDetailsWithoutPermissionsForSmartDocumentStrategy.execute(requestModel);
  }
  
  /**
   * This method returns the generated html for given instances and .vm template
   * files and json file using Velocity renderer.
   */
  @Override
  protected ISmartDocumentTemplateEngineResponseModel generateHtml(
      ISmartDocumentTemplateEngineRequestModel smartTemplateEngineRequestModel) throws Exception
  {
    return velocityTemplateEngineStrategy.execute(smartTemplateEngineRequestModel);
  }
  
  /**
   * This method returns the pdfBytes of the generated pdfDocument from the
   * given html using renderer license key, .css and .font files and
   * pdfConfiguration.
   */
  @Override
  protected ISmartDocumentRendererResponseModel generatePdf(
      ISmartDocumentRendererRequestModel requestModel) throws Exception
  {
    return renderFromPdfReactor.execute(requestModel);
  }
  
}
