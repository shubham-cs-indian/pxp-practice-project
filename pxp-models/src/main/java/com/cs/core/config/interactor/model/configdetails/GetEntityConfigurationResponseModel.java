package com.cs.core.config.interactor.model.configdetails;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetEntityConfigurationResponseModel implements IGetEntityConfigurationResponseModel {

	private static final long serialVersionUID = 1L;
	protected Map<String, IIdLabelCodeModel> referenceData;
	protected Map<String, Object> configDetails;
	protected List<IUsageSummary> usageSummary;
	protected boolean hasChildDependency = false;

	@Override
	public Map<String, IIdLabelCodeModel> getReferenceData() {
		return referenceData;
	}

	@Override
	public Map<String, Object> getConfigDetails() {
		return configDetails;
	}

	@Override
	public void setConfigDetails(Map<String, Object> configDetails) {
		this.configDetails = configDetails;
	}

	@Override
	@JsonDeserialize(contentAs = IdLabelCodeModel.class)
	public void setReferenceData(Map<String, IIdLabelCodeModel> referenceData) {
		this.referenceData = referenceData;
	}

	@Override
	public List<IUsageSummary> getUsageSummary() {
		return usageSummary;
	}

	@Override
	@JsonDeserialize(contentAs = UsageSummary.class)
	public void setUsageSummary(List<IUsageSummary> usageSummary) {
		this.usageSummary = usageSummary;
	}

	@Override
	public boolean isHasChildDependency() {
		return hasChildDependency;
	}

	@Override
	public void setHasChildDependency(boolean hasChildDependency) {
		this.hasChildDependency = hasChildDependency;
	}
}
