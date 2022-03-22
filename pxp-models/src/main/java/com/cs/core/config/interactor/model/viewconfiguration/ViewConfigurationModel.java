package com.cs.core.config.interactor.model.viewconfiguration;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.config.interactor.model.auditlog.AuditLogModel;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.config.interactor.entity.viewconfiguration.IViewConfiguration;
import com.cs.core.config.interactor.entity.viewconfiguration.ViewConfiguration;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ViewConfigurationModel extends ConfigResponseWithAuditLogModel
    implements IViewConfigurationModel {

	private static final long serialVersionUID = 1L;
	private IViewConfiguration entity;

	public ViewConfigurationModel() {
		this.entity = new ViewConfiguration();
	}

	public ViewConfigurationModel(IViewConfiguration entity) {
		super();
		this.entity = entity;
	}

	@Override
	public IViewConfiguration getEntity() {
		return entity;
	}

	@Override
	public String getCode() {
		return entity.getCode();
	}

	@Override
	public void setCode(String code) {
		entity.setCode(code);
	}

	@Override
	public String getId() {
		return entity.getId();
	}

	@Override
	public void setId(String id) {
		entity.setId(id);
	}

	@Override
	@JsonIgnore
	public Long getVersionId() {
		return entity.getVersionId();
	}

	@Override
	@JsonIgnore
	public void setVersionId(Long versionId) {
	     entity.getVersionId();
	}

	@Override
	@JsonIgnore
	public Long getVersionTimestamp() {
		return entity.getVersionTimestamp();
	}

	@Override
	@JsonIgnore
	public void setVersionTimestamp(Long versionTimestamp) {
		entity.getVersionTimestamp();
	}

	@Override
	@JsonIgnore
	public String getLastModifiedBy() {
		return entity.getLastModifiedBy();
	}

	@Override
	@JsonIgnore
	public void setLastModifiedBy(String lastModifiedBy) {
		entity.setLastModifiedBy(lastModifiedBy);
	}

	@Override
	public Boolean getIsLandingPageExpanded() {
		return entity.getIsLandingPageExpanded();
	}

	@Override
	public void setIsLandingPageExpanded(Boolean isLandingPageExpanded) {
		entity.setIsLandingPageExpanded(isLandingPageExpanded);
	}

	@Override
	public Boolean getIsProductInfoPageExpanded() {
		return entity.getIsProductInfoPageExpanded();
	}

	@Override
	public void setIsProductInfoPageExpanded(Boolean isProductPageExpanded) {
		entity.setIsProductInfoPageExpanded(isProductPageExpanded);
	}
}
