package com.cs.core.config.interactor.entity.variantconfiguration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class VariantConfiguration implements IVariantConfiguration {

	private static final long serialVersionUID = 1L;

	protected String id;
	protected String code;
	protected Boolean isSelectVariant;

	@Override
	public void setIsSelectVariant(Boolean isSelectVariant) {
		this.isSelectVariant = isSelectVariant;
	}

	@Override
	public Boolean getIsSelectVariant() {
		return isSelectVariant;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public void setCode(String code) {
		this.code = code;
	}

	@JsonIgnore
	@Override
	public Long getVersionId() {
		// TODO Auto-generated method stub
		return null;
	}

	@JsonIgnore
	@Override
	public void setVersionId(Long versionId) {
		// TODO Auto-generated method stub

	}

	@JsonIgnore
	@Override
	public Long getVersionTimestamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@JsonIgnore
	@Override
	public void setVersionTimestamp(Long versionTimestamp) {
		// TODO Auto-generated method stub

	}

	@JsonIgnore
	@Override
	public void setLastModifiedBy(String lastModifiedBy) {
		// TODO Auto-generated method stub

	}

	@JsonIgnore
	@Override
	public String getLastModifiedBy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
  public String getId() {
    return id;
  }

  @Override
  public void setId(String id) {
    this.id = id;

  }
}
