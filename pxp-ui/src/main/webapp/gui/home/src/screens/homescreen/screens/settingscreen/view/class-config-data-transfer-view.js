import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import ViewUtils from './utils/view-utils';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import SectionLayout from '../tack/class-config-layout-data';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { relAndContextCouplingTypes as VersionVariantCouplingTypeDictionary } from '../../../../../commonmodule/tack/version-variant-coupling-types';
import AttributeTypeDictionary from '../../../../../commonmodule/tack/attribute-type-dictionary-new';
import { view as LazyContextMenuView } from '../../../../../viewlibraries/lazycontextmenuview/lazy-context-menu-view';
import ConfigDataEntitiesDictionary from '../../../../../commonmodule/tack/config-data-entities-dictionary';

const oEvents = {
  HANDLE_REMOVE_KLASS_CLICKED: "handle_remove_klass_clicked",
  DATA_TRANSFER_VIEW_ADD_PROPERTIES: "handle_class_data_transfer_properties_added",
  DATA_TRANSFER_REMOVE_PROPERTY: "handle_data_transfer_remove_property",
  DATA_TRANSFER_PROPERTY_COUPLING_CHANGE: "handle_data_transfer_coupling_changed"
};

const oPropTypes = {
  activeClass: ReactPropTypes.object,
  propertyMSSModel: ReactPropTypes.object,
  referencedData: ReactPropTypes.object,
  property: ReactPropTypes.string,
  section: ReactPropTypes.string,
  context: ReactPropTypes.string,
};

// @CS.SafeComponent
class ClassConfigDataTransferView extends React.Component {
  static propTypes = oPropTypes;

  handleRemoveClassClicked = (sClassId) => {
    EventBus.dispatch(oEvents.HANDLE_REMOVE_KLASS_CLICKED, this.props.property, sClassId, this.props.context)
  };

  handleCouplingTypeChanged = (sClassId, sPropertyId, sContext, oEvent) => {
    var sNewValue = oEvent.target.value;
    EventBus.dispatch(oEvents.DATA_TRANSFER_PROPERTY_COUPLING_CHANGE, sClassId, sPropertyId, sNewValue, sContext, this.props.context);
  };

  handleRemoveProperty = (sClassId, sPropertyId, sContext) => {
    EventBus.dispatch(oEvents.DATA_TRANSFER_REMOVE_PROPERTY, sClassId, sPropertyId, sContext, this.props.context);
  };

  getSelectOptions = (aList) => {
    var iIndex = 0;
    return CS.map(aList, function (oItem) {
      return (<option value={oItem.id} key={iIndex++}>{CS.getLabelOrCode(oItem)}</option>);
    });
  };

  getCouplingDom = (sClassId, sContext, oVisualElement) => {
    var oReferencedTags = this.props.referencedData.referencedTags;
    var oReferencedAttributes = this.props.referencedData.referencedAttributes;
    var oAttributeMap = ViewUtils.getReferencedAttributes();
    var oTagMap = ViewUtils.getReferencedTags();

    var oMasterItem = {};
    if (sContext == 'attributes') {
      oMasterItem = oReferencedAttributes[oVisualElement.id] || oAttributeMap[oVisualElement.id] || {};
    } else {
      oMasterItem = oReferencedTags[oVisualElement.id] || oTagMap[oVisualElement.id] || {};
    }

    let oVersionVariantCouplingTypeDictionary = new VersionVariantCouplingTypeDictionary();
    var aCouplingOptions = this.getSelectOptions(oVersionVariantCouplingTypeDictionary);
    var sPropertyLabel = oMasterItem ? CS.getLabelOrCode(oMasterItem) : "";

    let sPropertyLabelForToolTip =oMasterItem.code? sPropertyLabel + " : " + oMasterItem.code : sPropertyLabel;
    return (
        <div className="relationshipPropertyCouplingWrapper" key={sClassId + oVisualElement.id}>
          <div className={"propertyTypeIcon " + sContext}></div>
          <div className="relationshipPropertyLabel" title={sPropertyLabelForToolTip}>{sPropertyLabel}</div>
          <div className="relationshipPropertyContainer">
            <select className="relationshipCouplingOption"
                    value={oVisualElement.couplingType}
                    onChange={this.handleCouplingTypeChanged.bind(this, sClassId, oVisualElement.id, sContext)}>
              {aCouplingOptions}
            </select>
          </div>
          <div className="removeProperty" title={getTranslation().REMOVE}
               onClick={this.handleRemoveProperty.bind(this, sClassId, oVisualElement.id, sContext)}/>
        </div>
    );
  };

  getCouplingView = (oReferencedData, sId) => {
    var _this = this;
    var fGetAttributeCouplingDOM = _this.getCouplingDom.bind(this, sId, 'attributes');
    var fGetTagCouplingDOM = _this.getCouplingDom.bind(this, sId, 'tags');
    var aAttributes = oReferencedData.referencedKlasses[sId].propagableAttributes;
    var aTags = oReferencedData.referencedKlasses[sId].propagableTags;

    if (!(CS.isEmpty(aAttributes) && CS.isEmpty(aTags))) {
      return (
          <div className="propertyCouplingContainer">
            <div className="propertyCouplingHeader">
              <div className="propertyName">{getTranslation().PROPERTY}</div>
              <div className="propertyCoupling">{getTranslation().COUPLING}</div>
            </div>
            {CS.map(aAttributes, fGetAttributeCouplingDOM)}
            {CS.map(aTags, fGetTagCouplingDOM)}
          </div>
      )
    }
    return null;
  };

  handleSearchLoadMore = (oData) => {
    EventBus.dispatch(oEvents.DATA_TRANSFER_VIEW_SEARCH_LOAD_MORE_PROPERTIES, oData);
  };

  handleOnLoadMore = (sEntity) => {
    var oData = {
      loadMore: true,
      entities: [sEntity]
    };
    this.handleSearchLoadMore(oData);
  };

  getRequestResponseInfoForAttributes = () => {
    return {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.ATTRIBUTES,
      typesToExclude: [AttributeTypeDictionary.CONCATENATED, AttributeTypeDictionary.CALCULATED],
      entitiesToExclude: ['assetdownloadcountattribute']
    }
  };

  getRequestResponseInfoForTags = () => {
    return {
      requestType: "configData",
      entityName: ConfigDataEntitiesDictionary.TAGS,
    }
  };

  handleOnApply = (sEntity, aSelectedIds, oReferencedData, sContext) => {
    EventBus.dispatch(oEvents.DATA_TRANSFER_VIEW_ADD_PROPERTIES, sEntity, aSelectedIds, oReferencedData, sContext, this.props.context);
  };

  handleOnSearch = (sEntity, sSearchText) => {
    var oData = {
      searchText: sSearchText,
      entities: [sEntity]
    };
    this.handleSearchLoadMore(oData);
  };

  getAddPropertyView = (oData) => {
    var oAnchorOrigin = {horizontal: 'left', vertical: 'bottom'};
    var oTargetOrigin = {horizontal: 'left', vertical: 'top'};
    var sSearchText = ViewUtils.getEntitySearchText();
    return (
        <div className="addPropertyViewContainer" key={oData.context}>
          <LazyContextMenuView
              context={oData.context}
              className=" addAttribute "
              selectedItems={oData.selectedAttributes}
              isMultiselect={oData.multiSelect}
              onApplyHandler={this.handleOnApply.bind(this, ConfigDataEntitiesDictionary.ATTRIBUTES)}
              showSelectedItems={true}
              showColor={true}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              searchHandler={this.handleOnSearch.bind(this, ConfigDataEntitiesDictionary.ATTRIBUTES)}
              searchText={sSearchText}
              loadMoreHandler={this.handleOnLoadMore.bind(this, ConfigDataEntitiesDictionary.ATTRIBUTES)}
              disabled={oData.disabled}
              requestResponseInfo={this.getRequestResponseInfoForAttributes()}
          />
          <LazyContextMenuView
              context={oData.context}
              className=" addTag "
              selectedItems={oData.selectedTags}
              isMultiselect={oData.multiSelect}
              onApplyHandler={this.handleOnApply.bind(this, ConfigDataEntitiesDictionary.TAGS)}
              showSelectedItems={true}
              showColor={true}
              anchorOrigin={oAnchorOrigin}
              targetOrigin={oTargetOrigin}
              useAnchorElementWidth={true}
              searchHandler={this.handleOnSearch.bind(this, ConfigDataEntitiesDictionary.TAGS)}
              searchText={sSearchText}
              loadMoreHandler={this.handleOnLoadMore.bind(this, ConfigDataEntitiesDictionary.TAGS)}
              disabled={oData.disabled}
              requestResponseInfo={this.getRequestResponseInfoForTags()}
          />
        </div>
    );
  };

  getDataTransferView = (sClassId, oReferencedData) => {
    var sExtendedContext = ViewUtils.getSplitter() + sClassId;
    let oReferencedClassData = oReferencedData.referencedKlasses[sClassId];

     var oMSSData = {
        selectedAttributes: CS.map(oReferencedClassData.propagableAttributes, "id"),
        selectedTags: CS.map(oReferencedClassData.propagableTags, "id"),
        multiSelect: true,
        showIcon: true,
        context: "dataTransfer" + sExtendedContext,
        disabled: false
      };


    let oViewForSide = (
        <div className="otherSideContainer" key={sClassId}>
          {this.getAddPropertyView(oMSSData)}
          <div className="sideDropDownContainer">
            {this.getCouplingView(oReferencedData, sClassId)}
          </div>
        </div>
    );

    return oViewForSide
  };

  getDeleteKlassView = (bIsDeleteKlassEnabled, sId) => {
    let deleteKlassView;
    if(bIsDeleteKlassEnabled){
        deleteKlassView = (<div className='classConfigClassesDelete'
                                onClick={this.handleRemoveClassClicked.bind(this, sId)} />);
    }
    return deleteKlassView;
  };

  getView = (sId) => {
    let oSectionLayout = new SectionLayout();
    var sSectionId = this.props.section;
    let sSplitter = ViewUtils.getSplitter();
    let oReferencedData = this.props.referencedData;
    let sCommonConfigContext = `classConfig${sSectionId.charAt(0).toUpperCase()}${sSplitter}${sId}`;
    let oDataTransferView = this.getDataTransferView(sId, oReferencedData);

    let oModel = {
      label: CS.getLabelOrCode(oReferencedData.referencedKlasses[sId]),
      dataTransferProperties: oDataTransferView
    };
    let oDOM = (<div className={"classConfigSelectedClassesContainer"} key={sId}>
      { this.getDeleteKlassView(this.props.isDeleteKlassEnabled, sId) }
      <CommonConfigSectionView context={sCommonConfigContext}
                               sectionLayout={oSectionLayout["classBasicInformation"]}
                               data={oModel} disabledFields={["label"]}/>
    </div>);

    return oDOM;
  };



  render () {
    let aDOM = [];
    let _this = this;
    let oSectionLayout = new SectionLayout();
    var oActiveClass = this.props.activeClass;
    if (CS.isArray(oActiveClass[this.props.property])) {
      CS.forEach(oActiveClass[this.props.property], function (sId) {
        aDOM.push(_this.getView(sId));
      });
    } else if (oActiveClass[this.props.property]) {
      aDOM.push(this.getView(oActiveClass[this.props.property]));
    }

    if (CS.isEmpty(oActiveClass[this.props.property]) || (this.props.isAddKlassEnabled && this.props.property === 'embeddedKlassIds')) {
      aDOM.push(<div className='addClass' key="addClass">
        <CommonConfigSectionView context="class" sectionLayout={oSectionLayout["classBasicInformation"]}
                                 data={this.props.propertyMSSModel} disabledFields={[]}/>
      </div>);
    }

    return (aDOM);
  }
}

export const view = ClassConfigDataTransferView;
export const events = oEvents;
