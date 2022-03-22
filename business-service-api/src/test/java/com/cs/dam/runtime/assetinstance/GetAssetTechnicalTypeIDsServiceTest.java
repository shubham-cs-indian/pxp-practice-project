package com.cs.dam.runtime.assetinstance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.businessapi.base.TestConfigurationForBSA;
import com.cs.core.config.interactor.model.asset.IdLabelCodeDownloadPermissionModel;
import com.cs.core.config.strategy.usecase.asset.IGetAllRenditionKlassesWithPermissionStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.assetinstance.AssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.assetinstance.BulkDownloadConfigInformationResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationWithTIVModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetDownloadWithVariantsModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkDownloadConfigInformationResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IIdLabelCodeDownloadPermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.technical.ijosn.IJSONContent;
/**
 * Test case file to test GetAssetTechnicalTypeIDsService
 * @author pranav.huchche
 *
 */
@RunWith(PowerMockRunner.class)
public class GetAssetTechnicalTypeIDsServiceTest extends TestConfigurationForBSA {
  
  @InjectMocks
  @Autowired
  GetAssetTechnicalTypeIDsService               getAssetTechnicalTypeIDsService;
  
  @Mock
  IGetAllRenditionKlassesWithPermissionStrategy getAllRenditionKlassesWithPermissionStrategy;
  
  @Mock
  BaseEntityDAO                                 baseEntityDAO;
  
  /**
   * Test case with complete mock.
   * @throws Exception
   */
  @Test
  public void testGetTechnicalTypeIds() 
      throws Exception
  {
    IBulkDownloadConfigInformationResponseModel mockResponseForGetRenditionKlasses = PowerMockito.mock(IBulkDownloadConfigInformationResponseModel.class);
    when(rdbmsComponentUtils.getBaseEntityDAO(anyLong())).thenReturn(PowerMockito.mock(IBaseEntityDAO.class));
    when(getAllRenditionKlassesWithPermissionStrategy.execute(Mockito.any())).thenReturn(mockResponseForGetRenditionKlasses);
    
    // Calling actual service with mock data
    IBulkAssetDownloadWithVariantsModel actualReturnModel = getAssetTechnicalTypeIDsService.execute(PowerMockito.mock(IIdsListParameterModel.class));
    
    // Assert response
    assertNotNull(actualReturnModel);
    assertNotNull(actualReturnModel.getDownloadInformation());
  }
  
  /**
   * Test case to test if totalContent is calculated properly or not.
   * @throws Exception
   */
  @Test
  public void testGetTechnicalTypeIdsPrepareResponse() throws Exception
  {
    // Mock LocaleCatalogDAO
    LocaleCatalogDAO mockLocaleCatalogDAO = PowerMockito.mock(LocaleCatalogDAO.class);
    when(rdbmsComponentUtils.getLocaleCatlogDAO()).thenReturn(mockLocaleCatalogDAO);
    
    // Mock IBulkDownloadConfigInformationResponseModel Response
    IBulkDownloadConfigInformationResponseModel configResponse = prepareConfigMockData();
    
    // Mock strategy for getting RenditionKlassesWithPermission
    // No need of actual values in request as it's not used in response
    when(getAllRenditionKlassesWithPermissionStrategy.execute(Mockito.any())).thenReturn(configResponse);
    
    // Prepare mock data for baseEntityDTO
    IBaseEntityDTO baseEntityDTO = new BaseEntityDTO();
    ClassifierDTO classDTO = new ClassifierDTO();
    classDTO.setCode("image_asset");
    baseEntityDTO.setNatureClassifier(classDTO);
    
    IAssetDownloadInformationWithTIVModel downloadInfoModel =PowerMockito.mock(AssetDownloadInformationWithTIVModel.class);
//    AssetDownloadInformationWithTIVModel downloadInfoModel = new AssetDownloadInformationWithTIVModel();
    PowerMockito.when(downloadInfoModel.getAssetInstanceId()).thenReturn(1234l);
    
    // Mock baseEntityDAO and baseEntityDTO
    // For main asset
    when(rdbmsComponentUtils.getBaseEntityDAO(12345l)).thenReturn(baseEntityDAO);
    // For TIV
    when(rdbmsComponentUtils.getBaseEntityDAO(10005l)).thenReturn(baseEntityDAO);
    when(baseEntityDAO.getBaseEntityDTO()).thenReturn(baseEntityDTO);
    when(rdbmsComponentUtils.getEmptyBaseEntityDAO()).thenReturn(baseEntityDAO);
    
    // Mock getIIdsByParentIIdAndClassifierCode response
    List<Long> tivIIds = new ArrayList<Long>();
    tivIIds.add(10005l);
//    when(baseEntityDAO.getIIdsByParentIIdAndClassifierCode(12345l, "CFR1111")).thenReturn(tivIIds);
    when(baseEntityDAO.getIIdsByParentIIdAndClassifierCode(any(long.class), any(String.class))).thenReturn(tivIIds);    
    // Set mock entity extension in baseEntityDTO
    IJSONContent entityExtension = new JSONContent();
    entityExtension.setField(IAssetInformationModel.FILENAME, "test_asset.png");
    baseEntityDTO.setEntityExtension(entityExtension.toString());
    
    // Prepare dummy request model.
    IIdsListParameterModel request = prepareMockRequestModel();
    
    // Call Actual Service class to be tested with request
    IBulkAssetDownloadWithVariantsModel actualReturnModel = getAssetTechnicalTypeIDsService.execute(request);
    
    // Necessary assertions
    assertNotNull(actualReturnModel);
    assertEquals(actualReturnModel.getDownloadInformation().get(0).getTotalContent(), 2);
  }
  
  @Test
  public void testGetTechnicalTypeIdsPrepareResponseWithTwoTIVs() throws Exception
  {
    // Mock LocaleCatalogDAO
    LocaleCatalogDAO mockLocaleCatalogDAO = PowerMockito.mock(LocaleCatalogDAO.class);
    when(rdbmsComponentUtils.getLocaleCatlogDAO()).thenReturn(mockLocaleCatalogDAO);
    
    // Mock IBulkDownloadConfigInformationResponseModel Response
    IBulkDownloadConfigInformationResponseModel configResponse = prepareConfigMockData();
    
    // Mock strategy for getting RenditionKlassesWithPermission
    // No need of actual values in request as it's not used in response
    when(getAllRenditionKlassesWithPermissionStrategy.execute(Mockito.any())).thenReturn(configResponse);
    
    // Prepare mock data for baseEntityDTO
    IBaseEntityDTO baseEntityDTO = new BaseEntityDTO();
    ClassifierDTO classDTO = new ClassifierDTO();
    classDTO.setCode("image_asset");
    baseEntityDTO.setNatureClassifier(classDTO);
    
    IAssetDownloadInformationWithTIVModel downloadInfoModel =PowerMockito.mock(AssetDownloadInformationWithTIVModel.class);
//    AssetDownloadInformationWithTIVModel downloadInfoModel = new AssetDownloadInformationWithTIVModel();
    PowerMockito.when(downloadInfoModel.getAssetInstanceId()).thenReturn(1234l);
    
    // Mock baseEntityDAO and baseEntityDTO
    // For main asset
    when(rdbmsComponentUtils.getBaseEntityDAO(12345l)).thenReturn(baseEntityDAO);
    // For TIV
    when(rdbmsComponentUtils.getBaseEntityDAO(10005l)).thenReturn(baseEntityDAO);
    when(rdbmsComponentUtils.getBaseEntityDAO(10006l)).thenReturn(baseEntityDAO);
    when(baseEntityDAO.getBaseEntityDTO()).thenReturn(baseEntityDTO);
    when(rdbmsComponentUtils.getEmptyBaseEntityDAO()).thenReturn(baseEntityDAO);
    
    // Mock getIIdsByParentIIdAndClassifierCode response
    List<Long> tivIIds = new ArrayList<Long>();
    tivIIds.add(10005l);
    tivIIds.add(10006l);
//    when(baseEntityDAO.getIIdsByParentIIdAndClassifierCode(12345l, "CFR1111")).thenReturn(tivIIds);
    when(baseEntityDAO.getIIdsByParentIIdAndClassifierCode(any(long.class), any(String.class))).thenReturn(tivIIds);    
    // Set mock entity extension in baseEntityDTO
    IJSONContent entityExtension = new JSONContent();
    entityExtension.setField(IAssetInformationModel.FILENAME, "test_asset.png");
    baseEntityDTO.setEntityExtension(entityExtension.toString());
    
    // Prepare dummy request model.
    IIdsListParameterModel request = prepareMockRequestModel();
    
    // Call Actual Service class to be tested with request
    IBulkAssetDownloadWithVariantsModel actualReturnModel = getAssetTechnicalTypeIDsService.execute(request);
    
    // Necessary assertions
    assertNotNull(actualReturnModel);
    assertEquals(actualReturnModel.getDownloadInformation().get(0).getTotalContent(), 3);
  }
  
  @Test
  public void testGetTechnicalTypeIdsPrepareResponseWithNoTIVs() throws Exception
  {
    // Mock LocaleCatalogDAO
    LocaleCatalogDAO mockLocaleCatalogDAO = PowerMockito.mock(LocaleCatalogDAO.class);
    when(rdbmsComponentUtils.getLocaleCatlogDAO()).thenReturn(mockLocaleCatalogDAO);
    
    // Mock IBulkDownloadConfigInformationResponseModel Response
    IBulkDownloadConfigInformationResponseModel configResponse = prepareConfigMockData();
    
    // Mock strategy for getting RenditionKlassesWithPermission
    // No need of actual values in request as it's not used in response
    when(getAllRenditionKlassesWithPermissionStrategy.execute(Mockito.any())).thenReturn(configResponse);
    
    // Prepare mock data for baseEntityDTO
    IBaseEntityDTO baseEntityDTO = new BaseEntityDTO();
    ClassifierDTO classDTO = new ClassifierDTO();
    classDTO.setCode("image_asset");
    baseEntityDTO.setNatureClassifier(classDTO);
    
    IAssetDownloadInformationWithTIVModel downloadInfoModel =PowerMockito.mock(AssetDownloadInformationWithTIVModel.class);
//    AssetDownloadInformationWithTIVModel downloadInfoModel = new AssetDownloadInformationWithTIVModel();
    PowerMockito.when(downloadInfoModel.getAssetInstanceId()).thenReturn(1234l);
    
    // Mock baseEntityDAO and baseEntityDTO
    // For main asset
    when(rdbmsComponentUtils.getBaseEntityDAO(12345l)).thenReturn(baseEntityDAO);
    when(baseEntityDAO.getBaseEntityDTO()).thenReturn(baseEntityDTO);
    when(rdbmsComponentUtils.getEmptyBaseEntityDAO()).thenReturn(baseEntityDAO);
    
    // Mock getIIdsByParentIIdAndClassifierCode response
    List<Long> tivIIds = new ArrayList<Long>();
//    when(baseEntityDAO.getIIdsByParentIIdAndClassifierCode(12345l, "CFR1111")).thenReturn(tivIIds);
    when(baseEntityDAO.getIIdsByParentIIdAndClassifierCode(any(long.class), any(String.class))).thenReturn(tivIIds);    
    // Set mock entity extension in baseEntityDTO
    IJSONContent entityExtension = new JSONContent();
    entityExtension.setField(IAssetInformationModel.FILENAME, "test_asset.png");
    baseEntityDTO.setEntityExtension(entityExtension.toString());
    
    // Prepare dummy request model.
    IIdsListParameterModel request = prepareMockRequestModel();
    
    // Call Actual Service class to be tested with request
    IBulkAssetDownloadWithVariantsModel actualReturnModel = getAssetTechnicalTypeIDsService.execute(request);
    
    // Necessary assertions
    assertNotNull(actualReturnModel);
    assertEquals(actualReturnModel.getDownloadInformation().get(0).getTotalContent(), 1);
  }

  private IIdsListParameterModel prepareMockRequestModel()
  {
    IIdsListParameterModel request = new IdsListParameterModel();
    List<String> ids = new ArrayList<String>();
    ids.add("12345");
    request.setIds(ids);
    return request;
  }

  /**
   * Prepare mock config data
   * @return
   */
  private IBulkDownloadConfigInformationResponseModel prepareConfigMockData()
  {
    IBulkDownloadConfigInformationResponseModel configResponse = new BulkDownloadConfigInformationResponseModel();
    configResponse.setShouldDownloadAssetWithOriginalFilename(false);
    IIdLabelCodeDownloadPermissionModel idLabelCodeDownloadPermissionModel =  new IdLabelCodeDownloadPermissionModel();
    idLabelCodeDownloadPermissionModel.setCanDownload(true);
    idLabelCodeDownloadPermissionModel.setCode("image_asset");
    idLabelCodeDownloadPermissionModel.setId("image_asset");
    idLabelCodeDownloadPermissionModel.setLabel("Image");
    configResponse.getMasterAssetKlassInformation().put("image_asset", idLabelCodeDownloadPermissionModel);
    configResponse.getMasterAssetTivKlassMap().put("image_asset", Arrays.asList("CFR1111"));
    IIdLabelCodeDownloadPermissionModel idLabelCodeDownloadPermissiontivModel =  new IdLabelCodeDownloadPermissionModel();
    idLabelCodeDownloadPermissiontivModel.setCanDownload(true);
    idLabelCodeDownloadPermissiontivModel.setCode("CFR1111");
    idLabelCodeDownloadPermissiontivModel.setId("CFR1111");
    idLabelCodeDownloadPermissiontivModel.setLabel("TIV");
    configResponse.getTivAssetKlassInformation().put("CFR1111", idLabelCodeDownloadPermissiontivModel);
    return configResponse;
  }
  
}
