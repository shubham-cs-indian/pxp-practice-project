package com.cs.core.config.interactor.entity.viewconfiguration;

public class ViewConfiguration implements IViewConfiguration {

	private static final long serialVersionUID = 1L;

	private String id;
	private Long versionId;
	private Long versionTimestamp;
	private String lastModifiedBy;
	private String code;
	private Boolean isLandingPageExpanded;
	private Boolean isProductInfoPageExpanded;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public Long getVersionId() {
		return versionId;
	}

	@Override
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}

	@Override
	public Long getVersionTimestamp() {
		return versionTimestamp;
	}

	@Override
	public void setVersionTimestamp(Long versionTimestamp) {
		this.versionTimestamp = versionTimestamp;
	}

	@Override
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	@Override
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public Boolean getIsLandingPageExpanded() {
		return isLandingPageExpanded;
	}

	@Override
	public void setIsLandingPageExpanded(Boolean isLandingPageExpanded) {
		this.isLandingPageExpanded = isLandingPageExpanded;
	}

	@Override
	public Boolean getIsProductInfoPageExpanded() {
		return isProductInfoPageExpanded;
	}

	@Override
	public void setIsProductInfoPageExpanded(Boolean isProductInfoPageExpanded) {
		this.isProductInfoPageExpanded =isProductInfoPageExpanded;
	}

}
