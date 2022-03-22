package com.cs.pim.runtime.goldenrecord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.goldenrecord.ConfigDetailsForTypeInfoModel;
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForTypeInfoModel;
import com.cs.core.config.interactor.model.klass.ITypesListModel;
import com.cs.core.config.interactor.model.klass.TypesListModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IGetConfigDetailsForTypeInfoStrategy;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.goldenrecordbucket.dao.GoldenRecordBucketDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.GetTypeInfoForSourcesResponseModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetTypeInfoForSourcesRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGetTypeInfoForSourcesResponseModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.utils.BaseEntityUtils;

@Service
public class GetTypeInfoForSources extends AbstractRuntimeService<IGetTypeInfoForSourcesRequestModel, IGetTypeInfoForSourcesResponseModel> implements IGetTypeInfoForSources {

	@Autowired
	protected RDBMSComponentUtils rdbmsComponentUtils;

	@Autowired
	IGetConfigDetailsForTypeInfoStrategy getConfigDetailsForTypeInfoStrategy;

	@Override
	public IGetTypeInfoForSourcesResponseModel executeInternal(IGetTypeInfoForSourcesRequestModel dataModel) throws Exception {
		IGetTypeInfoForSourcesResponseModel responseModel = new GetTypeInfoForSourcesResponseModel();
		String type = dataModel.getType();
    GoldenRecordBucketDAO dao = new GoldenRecordBucketDAO();
    IGoldenRecordDTO goldenRecordDTO = dao.getGoldenRecordRuleAndBaseEntityIIDs(
        dataModel.getBucketId(), dataModel.getGoldenRecordId());
    List<String> goldenRecordLinkedBaseEntityIids = goldenRecordDTO.getLinkedBaseEntities().stream()
        .map(baseEntityIID -> String.valueOf(baseEntityIID))
        .collect(Collectors.toList());
    
		ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO(new ArrayList<String>(Arrays.asList(dataModel.getLanguageCode())));
		List<IBaseEntityDTO> baseEntitiesDTO = localeCatlogDAO.getBaseEntitiesByIIDs(goldenRecordLinkedBaseEntityIids);
		IConfigDetailsForTypeInfoModel configDetails = new ConfigDetailsForTypeInfoModel();

		if ((CommonConstants.TAXONOMIES).equals(type)) {
			Map<String, ITypesListModel> typeListModelMap = new HashMap<String, ITypesListModel>();
			List<String> allTaxonomyIds = new ArrayList<String>();

			for (IBaseEntityDTO iBaseEntityDTO : baseEntitiesDTO) {
				IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(iBaseEntityDTO);
				List<String> taxonomyIds = BaseEntityUtils
						.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers());
				allTaxonomyIds.addAll(taxonomyIds);
				ITypesListModel typeListModel = new TypesListModel();
				typeListModel.setTaxonomyIds(taxonomyIds);
				typeListModelMap.put(String.valueOf(iBaseEntityDTO.getBaseEntityIID()), typeListModel);
			}
			configDetails = getConfigDetails(new HashSet<String>(allTaxonomyIds), true);
			responseModel.setConfigDetails(configDetails);
			responseModel.setSourceIds(goldenRecordLinkedBaseEntityIids);
			responseModel.setSourceIdTypeInfoMap(typeListModelMap);
		} else {
			Set<String> classifierCodeList = new HashSet<String>();
			Map<String, ITypesListModel> typeListModelMap = new HashMap<String, ITypesListModel>();
			ITypesListModel typeListModel = new TypesListModel();
			for (IBaseEntityDTO iBaseEntityDTO : baseEntitiesDTO) {
				List<String> klassIds = new ArrayList<String>();
				IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(iBaseEntityDTO);
				List<String> allReferenceTypeFromBaseEntity = BaseEntityUtils
						.getAllReferenceTypeFromBaseEntity(baseEntityDAO);
				classifierCodeList.addAll(allReferenceTypeFromBaseEntity);
				klassIds.addAll(allReferenceTypeFromBaseEntity);
				typeListModel.setKlassIds(klassIds);
				typeListModelMap.put(String.valueOf(iBaseEntityDTO.getBaseEntityIID()), typeListModel);
			}
			configDetails = getConfigDetails(classifierCodeList, false);
			responseModel.setConfigDetails(configDetails);
			responseModel.setSourceIds(goldenRecordLinkedBaseEntityIids);
			responseModel.setSourceIdTypeInfoMap(typeListModelMap);
		}
		return responseModel;
	}

	private IConfigDetailsForTypeInfoModel getConfigDetails(Set<String> ids, boolean forTaxonomy) throws Exception {
		Set<String> allKlassIds = new HashSet<>();
		Set<String> allTaxonomyIds = new HashSet<>();
		if (forTaxonomy) {
			allTaxonomyIds.addAll(ids);
		} else {
			allKlassIds.addAll(ids);
		}
		IMulticlassificationRequestModel request = new MulticlassificationRequestModel();
		request.setKlassIds(new ArrayList<>(allKlassIds));
		request.setSelectedTaxonomyIds(new ArrayList<>(allTaxonomyIds));
		IConfigDetailsForTypeInfoModel configDetails = getConfigDetailsForTypeInfoStrategy.execute(request);
		return configDetails;
	}
}
