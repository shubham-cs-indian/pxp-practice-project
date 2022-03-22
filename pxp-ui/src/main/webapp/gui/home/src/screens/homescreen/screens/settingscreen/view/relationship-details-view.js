import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import { view as DataTransferView } from './data-transfer-view';
import { view as KlassSelectorView } from '../../../../../viewlibraries/klassselectorview/klass-selector-view';
import ConfigRelationshipViewModel from './model/config-relationship-view-model.js';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import ViewUtils from './utils/view-utils';
import { view as CommonConfigSectionView } from './../../../../../viewlibraries/commonconfigsectionview/common-config-section-view';
import ViewContextConstants from '../../../../../commonmodule/tack/view-context-constants';
import Constants from '../../../../../commonmodule/tack/constants';
import RelationshipLayout from '../tack/data-model-relationship-config-layout';

const oEvents = {
  CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED: 'relationship_details_data_changed',
};

const oPropTypes = {
  selectedRelationshipDetailedModel: ReactPropTypes.instanceOf(ConfigRelationshipViewModel).isRequired,
  isRelationshipDirty : ReactPropTypes.bool,
  disabledFields: ReactPropTypes.array,
  iconLibraryData: ReactPropTypes.object
};

// @CS.SafeComponent
class RelationshipDetailsView extends React.Component {
  constructor (props) {
    super(props);

    this.side1Input = React.createRef();
    this.side2Input = React.createRef();
  }

  static propTypes = oPropTypes;

  updateTextInputValues = () => {

    var oModel = this.props.selectedRelationshipDetailedModel;

    var oSide = oModel.side;
    var oSide1 = oSide.side1;
    var sSide1Label = oSide1.label;
    this.side1Input.current && (this.side1Input.current.value = sSide1Label);

    var oSide2 = oSide.side2;
    var sSide2Label = oSide2.label;
    this.side2Input.current && (this.side2Input.current.value = sSide2Label);
  };

  componentDidMount() {
    this.updateTextInputValues();
  }

  componentDidUpdate() {
    this.updateTextInputValues();
  }

  getBasicInformationView = () => {
    let oProps = this.props;
    let oActiveRelationship = oProps.activeRelationship;
    let oModel = this.props.selectedRelationshipDetailedModel;
    let aCustomTabList = oModel.properties['customTabList'];
    let aCustomTypeList = oModel.properties['customTypeList'];

    let oReferencedData = oModel.properties["configDetails"].referencedTabs ;
    let sSelectedTabId = (oActiveRelationship.tab && oActiveRelationship.tab.id) || oActiveRelationship.tabId || "";
    let oSelectedTab = oReferencedData[sSelectedTabId] || CS.find(aCustomTabList, {id:sSelectedTabId});
    let aSelectedTabObject = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab];
    let aSelectedTabIds = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab.id];

    let aDisabledFields = this.props.disabledFields || [];
    let aDisabledFieldsForCommonConfig = [];
    let sErrorText = getTranslation().CODE_SHOULD_NOT_BE_EMPTY;

    if(!oModel.properties["isCreated"]){
      aDisabledFieldsForCommonConfig.push('code');
      aDisabledFieldsForCommonConfig.push('type');
    }
    if (aDisabledFields.indexOf("tabs") !== -1) {
      aDisabledFieldsForCommonConfig.push('tab');
    }

    let oDataGovernanceRelationshipConfigLayout = new RelationshipLayout();

    let oReqResObj = {
      requestType: "configData",
      entityName: "tabs",
      typesToExclude: [Constants.TAB_RENDITION],
    };

    let oSelectedType = oActiveRelationship.isLite ? CS.find(aCustomTypeList, {id: "liteRelationship"})
                                                   : CS.find(aCustomTypeList, {id: "relationship"});

    let oDataMode = {
      code: oModel.properties.code,
      label: oModel.label,
      type: {
        isMultiSelect: false,
        items: aCustomTypeList,
        selectedItems: oSelectedType,
        selectedObjects: [oSelectedType.id],
        showCreateButton: false,
        context: `relationshipType`,
        isLoadMoreEnabled: false,
        referencedData: aCustomTypeList,
        cannotRemove: true,
      },
      tab: {
        isMultiSelect: false,
        items: aCustomTabList,
        selectedItems: aSelectedTabIds,
        selectedObjects: aSelectedTabObject,
        searchText: ViewUtils.getEntitySearchText(),
        showCreateButton: true,
        context: `tab`,
        isLoadMoreEnabled: true,
        referencedData: oReferencedData,
        requestResponseInfo: oReqResObj,
        cannotRemove: true,
      },
      icon: {
        context: "relationship",
        icon: oActiveRelationship ? oActiveRelationship.icon : "",
        iconKey: oActiveRelationship ? oActiveRelationship.iconKey : "",
      },
      showSelectIconDialog: oActiveRelationship.showSelectIconDialog,
      selectIconData: this.props.iconLibraryData
    };

    return (<CommonConfigSectionView context={ViewContextConstants.RELATIONSHIP_DETAILS_VIEW_CONFIG}
                                     data={oDataMode} disabledFields={aDisabledFieldsForCommonConfig}
                                     sectionLayout={oDataGovernanceRelationshipConfigLayout.dataGovernanceConfigInformation}
                                     errorTextForCodeEntity={sErrorText}
    />)
  };

  handleRelationshipIsVisible1Change = (oEvent) => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var bCheckbox = oEvent.target.checked;
    var oSide = oModel.side;
    var oSide1 = oSide.side1;
    if (oSide1.isVisible != bCheckbox) {
      oSide1.isVisible = bCheckbox;
      var oProperties = {};
      oProperties.code = oModel.properties['code'];
      var oNewModel = new ConfigRelationshipViewModel(
          oModel.id,
          oModel.label,
          oModel.tooltip,
          oSide,
          oProperties
      );
      EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel);
    }
  };

  handleRelationshipIsVisible2Change = (oEvent) => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var bCheckbox = oEvent.target.checked;
    var oSide = oModel.side;
    var oSide2 = oSide.side2;
    if (oSide2.isVisible != bCheckbox) {
      oSide2.isVisible = bCheckbox;
      var oProperties = {};
      oProperties.code = oModel.properties['code'];
      var oNewModel = new ConfigRelationshipViewModel(
          oModel.id,
          oModel.label,
          oModel.tooltip,
          oSide,
          oProperties
      );
      EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel);
    }
  };

  handleRelationshipSide1LabelChanged = (oEvent) => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var sNewSide1Label = oEvent.target.value;
    var oSide = oModel.side;
    var oSide1 = oSide.side1;
    if (oSide1.label != sNewSide1Label) {
      oSide1.label = sNewSide1Label;
      oSide1.isCustomLabel = true;
      var oProperties = {};
      oProperties.code = oModel.properties['code'];
      var oNewModel = new ConfigRelationshipViewModel(
          oModel.id,
          oModel.label,
          oModel.tooltip,
          oSide,
          oProperties
      );
      EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel);
    }
  };

  handleRelationshipSide2LabelChanged = (oEvent) => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var sNewSide2Label = oEvent.target.value;
    var oSide = oModel.side;
    var oSide2 = oSide.side2;
    if (oSide2.label != sNewSide2Label) {
      oSide2.label = sNewSide2Label;
      oSide2.isCustomLabel = true;
      var oProperties = {};
      oProperties.code = oModel.properties['code'];
      var oNewModel = new ConfigRelationshipViewModel(
          oModel.id,
          oModel.label,
          oModel.tooltip,
          oSide,
          oProperties
      );
      EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel);
    }

  };

  handleRelationshipSide1TypeDropDownChanged = (sContext, aKlassTypeIds) => {
    this.handleRelationshipSide1DropDownChanged(sContext, [], {}, aKlassTypeIds);
  };

  handleRelationshipSide1DropDownChanged = (sContext, aKlassIds, oReferencedKlasses, aKlassTypeIds = []) => {
    let oModel = this.props.selectedRelationshipDetailedModel;
    let oSide = oModel.side;
    let oSide1 = oSide.side1;
    let sSide1KlassId = aKlassIds[0] || "";

    oSide1.klassId = sSide1KlassId;

    let oReferencedKlass = oReferencedKlasses[sSide1KlassId];
    if (!oSide1.isCustomLabel) {
      oSide1.label = !CS.isEmpty(oReferencedKlass) ? oReferencedKlass.label : "";
    }
    oSide1.type = !CS.isEmpty(oReferencedKlass) ? oReferencedKlass.type : "";

    let oProperties = {};
    oProperties.code = oModel.properties['code'];
    oProperties.context = sContext;
    oProperties.classChanged = true;
    oProperties.parentKlassType = aKlassTypeIds.length ? aKlassTypeIds[0] : "";

    let oNewModel = new ConfigRelationshipViewModel(
        oModel.id,
        oModel.label,
        oModel.tooltip,
        oSide,
        oProperties
    );
    EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel, oReferencedKlasses);
  };

  handleRelationshipSide2TypeDropDownChanged = (sContext, aKlassTypeIds) => {
    this.handleRelationshipSide2DropDownChanged(sContext, [], {}, aKlassTypeIds);
  };

  handleRelationshipSide2DropDownChanged = (sContext, aKlassIds, oReferencedKlasses, aKlassTypeIds = []) => {
    let oModel = this.props.selectedRelationshipDetailedModel;
    let oSide = oModel.side;
    let oSide2 = oSide.side2;
    let sSide2KlassId = aKlassIds[0] || "";

    oSide2.klassId = sSide2KlassId;

    let oReferencedKlass = oReferencedKlasses[sSide2KlassId];
    if (!oSide2.isCustomLabel) {
      oSide2.label = !CS.isEmpty(oReferencedKlass) ? oReferencedKlass.label : "";
    }
    oSide2.type = !CS.isEmpty(oReferencedKlass) ? oReferencedKlass.type : "";

    let oProperties = {};
    oProperties.code = oModel.properties['code'];
    oProperties.context = sContext;
    oProperties.classChanged = true;
    oProperties.parentKlassType = aKlassTypeIds.length ? aKlassTypeIds[0] : "";

    let oNewModel = new ConfigRelationshipViewModel(
        oModel.id,
        oModel.label,
        oModel.tooltip,
        oSide,
        oProperties
    );

    EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel, oReferencedKlasses);
  };

  handleRelationshipSide1CardinalityDropDownChanged = (oEvent) => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var sNewSide1CardinalityDropDownValue = oEvent.target.value;
    var oSide = oModel.side;
    var oSide1 = oSide.side1;
    oSide1.cardinality = sNewSide1CardinalityDropDownValue;
    var oProperties = {};
    oProperties.code = oModel.properties['code'];
    var oNewModel = new ConfigRelationshipViewModel(
        oModel.id,
        oModel.label,
        oModel.tooltip,
        oSide,
        oProperties
    );
    EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel);
  };

  handleRelationshipSide2CardinalityDropDownChanged = (oEvent) => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var sNewSide2CardinalityDropDownValue = oEvent.target.value;
    var oSide = oModel.side;
    var oSide2 = oSide.side2;
      oSide2.cardinality = sNewSide2CardinalityDropDownValue;
      var oProperties = {};
      oProperties.code = oModel.properties['code'];
      var oNewModel = new ConfigRelationshipViewModel(
          oModel.id,
          oModel.label,
          oModel.tooltip,
          oSide,
          oProperties
      );
      EventBus.dispatch(oEvents.CONFIG_RELATIONSHIP_DETAILS_DATA_CHANGED, this, oNewModel);
  };

  getSelectOptions = (aList) => {
    var iIndex = 0;
    return CS.map(aList, function (oItem) {
      return (<option value={oItem.id} key={iIndex++}>{oItem.label}</option>);
    });
  };

  getCardinalityDropDownList = () => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var aDropDownList = oModel.properties["cardinalityDropDown"];
    return this.getSelectOptions(aDropDownList);
  };

  getContextViewDom = (sExtraClassName, oRelationshipContextList,aSide1SelectedContext, aSide2SelectedContext) => {

    let oReqResObj = {
      requestType: "configData",
      entityName: "variantContexts",
      types: [ContextTypeDictionary.RELATIONSHIP_VARIANT],
    };

    let sContext = 'relationship';
    let sSplitter = ViewUtils.getSplitter();
    let sContextForSide1 = sContext + sSplitter + 'relationshipSide1';
    let sContextForSide2 = sContext + sSplitter + 'relationshipSide2';
    return <div className="relationshipVariantContainer">
      <div className="relationshipVariantHeader">{getTranslation().CONTEXT}</div>

      <div className={"relationshipVariantToggleContainer " + sExtraClassName}>
        <div className="relationshipContextToggleWrapper">
          <LazyMSSView isMultiSelect={false}
                       context={sContextForSide1}
                       disabled={false}
                       selectedItems={aSide1SelectedContext}
                       showColor={false}
                       cannotRemove={false}
                       showSelectedInDropdown={true}
                       referencedData={oRelationshipContextList}
                       requestResponseInfo={oReqResObj}
          />
        </div>
      </div>
      <div className="relationshipVariantToggleContainer">
        <div className="relationshipContextToggleWrapper">
          <LazyMSSView isMultiSelect={false}
                       context={sContextForSide2}
                       disabled={false}
                       selectedItems={aSide2SelectedContext}
                       showColor={false}
                       cannotRemove={false}
                       showSelectedInDropdown={true}
                       referencedData={oRelationshipContextList}
                       requestResponseInfo={oReqResObj}
          />
        </div>
      </div>
    </div>
  };

  getDataTransferView = () => {
    let aDisabledFields = this.props.disabledFields || [];
    if (aDisabledFields.indexOf("dataTransfer") !== -1) {
      return null;
    }

    let oModel = this.props.selectedRelationshipDetailedModel;
    let oSide = oModel.side;
    let oSide1 = oSide.side1;
    let oSide2 = oSide.side2;

    let oDataGovernanceRelationshipConfigLayout = new RelationshipLayout();

    let oData = {};
    let oActiveRelationship = this.props.activeRelationship;
    let bIsAfterSaveEnabled = oActiveRelationship.enableAfterSave || false;
    oData.enableAfterSave = {
      context: "relationship",
      isSelected: bIsAfterSaveEnabled
    };

    let sSide1Label = oSide1.label;
    let sSide2Label = oSide2.label;
      oData.configDetails = (
          <div>
            <DataTransferView
                relationshipId={oModel.id}
                side1={oSide1}
                side1Label={sSide1Label}
                side2={oSide2}
                side2Label={sSide2Label}
                parentContext="relationship"
                referencedAttributes={ViewUtils.getReferencedAttributes()}
                referencedTags={ViewUtils.getReferencedTags()}
            />
          </div>);

    return (
        <CommonConfigSectionView context={ViewContextConstants.RELATIONSHIP_DETAILS_VIEW_CONFIG}
                                 data={oData}
                                 sectionLayout={oDataGovernanceRelationshipConfigLayout.dataModelExtensionLayout}
        />
    )
  };

  getClassSelectionView = () => {
    let oModel = this.props.selectedRelationshipDetailedModel;
    let oRelationshipContextList = this.props.relationshipContextList;
    let aDisabledFields = this.props.disabledFields || [];
    let oActiveRelationship = this.props.activeRelationship;
    let oSide = oModel.side;

    let oSide1 = oSide.side1;
    let sSide1Value = oSide1.klassId;
    let sSide1Cardinality = oSide1.cardinality;

    let oSide2 = oSide.side2;
    let sSide2Value = oSide2.klassId;
    let sSide2Cardinality = oSide2.cardinality;

    let sExtraClassName = "";

    let aCardinalityDropDownList1 = this.getCardinalityDropDownList();
    let aCardinalityDropDownList2 = this.getCardinalityDropDownList();

    let bDisabledFlag = oModel.properties["isCreated"] ? false : true;
    let sDisabledClassName = bDisabledFlag ? " disabled " : "";
    let sCardinalityDisabledClassName = sDisabledClassName;
    let bCardinalityDropDown2DisableFlag = bDisabledFlag;

    let aSide1SelectedContext = [];
    let aSide2SelectedContext = [];

    if (oSide1.contextId) {
      aSide1SelectedContext.push(oSide1.contextId);
    }

    if (oSide2.contextId) {
      aSide2SelectedContext.push(oSide2.contextId);
    }

    let oEditableViewDOM = aDisabledFields.indexOf("editable") == -1 ? this.getEditableView(sExtraClassName) : null;
    let oContextViewDom = aDisabledFields.indexOf("context") == -1 ?
        this.getContextViewDom(sExtraClassName, oRelationshipContextList,aSide1SelectedContext, aSide2SelectedContext) : null;

    let oRelationshipConfigDetials = this.props.relationshipConfigDetails;
    let oReferencedKlasses = !CS.isEmpty(oRelationshipConfigDetials) ? oRelationshipConfigDetials.referencedKlasses : {};
    let aSide1Klasses = !CS.isEmpty(sSide1Value) ? [sSide1Value] : [];
    let aSide2Klasses = !CS.isEmpty(sSide2Value) ? [sSide2Value] : [];
    let oDataGovernanceRelationshipConfigLayout = new RelationshipLayout();
    let aSide1FieldsToExclude = oSide1.fieldsToExclude ? oSide1.fieldsToExclude : [];
    let aSide2FieldsToExclude = oSide2.fieldsToExclude ? oSide2.fieldsToExclude : [];
    let aCustomTypeList = oModel.properties['customTypeList'];

    let oSelectedType = oActiveRelationship.isLite ? CS.find(aCustomTypeList, {id: "liteRelationship"})
                                                   : CS.find(aCustomTypeList, {id: "relationship"});
    let sContext = oSelectedType.id;

    let oData = {};
    oData.klassview = (
        <div className="sidesContainer">
          <div className="sideHeaders">
            <div className="side1Header">{getTranslation().SIDE} 1
            </div>

            <div className="side2Header">{getTranslation().SIDE} 2
            </div>
          </div>
          <div className="sideDropDownContainer">
            <div className="sideDropDownLabel">{getTranslation().CLASSES}</div>
            <div className="side1DropDownContainer">
              <KlassSelectorView
                  context={sContext}
                  cannotRemove={true}
                  isTypeListDisabled={bDisabledFlag}
                  onTypeListChanged={this.handleRelationshipSide1TypeDropDownChanged.bind(this, 'side1')}
                  isKlassListDisabled={bDisabledFlag}
                  isKlassListNatureType={oModel.properties["isKlassListNatureType"]}
                  referencedKlasses={oReferencedKlasses}
                  selectedKlasses={aSide1Klasses}
                  onKlassListChanged={this.handleRelationshipSide1DropDownChanged.bind(this, 'side1')}
                  updateEntityForcefully={!this.props.isRelationshipDirty}
                  shouldShowTaxonomyType={oModel.shouldShowTaxonomyType}
                  excludedKlasses = {aSide1FieldsToExclude}
              />
            </div>
            <div className="side2DropDownContainer">
              <KlassSelectorView
                  context={sContext}
                  cannotRemove={true}
                  isTypeListDisabled={bDisabledFlag}
                  onTypeListChanged={this.handleRelationshipSide2TypeDropDownChanged.bind(this, 'side2')}
                  isKlassListDisabled={bDisabledFlag}
                  isKlassListNatureType={oModel.properties["isKlassListNatureType"]}
                  referencedKlasses={oReferencedKlasses}
                  selectedKlasses={aSide2Klasses}
                  onKlassListChanged={this.handleRelationshipSide2DropDownChanged.bind(this, 'side2')}
                  updateEntityForcefully={!this.props.isRelationshipDirty}
                  shouldShowTaxonomyType={oModel.shouldShowTaxonomyType}
                  excludedKlasses = {aSide2FieldsToExclude}
              />
            </div>
          </div>
          <div className="sideLabelContainer ">
            <div className="sideLabelLabel">{getTranslation().LABEL}</div>
            <div className={"side1LabelContainer " + sExtraClassName}>
              <input className={"side1Input"}
                     ref={this.side1Input}
                     onBlur={this.handleRelationshipSide1LabelChanged}/>
            </div>
            <div className="side2LabelContainer">
              <input className={"side2Input"}
                     ref={this.side2Input}
                     onBlur={this.handleRelationshipSide2LabelChanged}/>
            </div>
          </div>
          <div className="cardinalityContainer">
            <div className="cardinalityHeader">{getTranslation().CARDINALITY}
            </div>
            <div className={"cardinality1Container " + sExtraClassName}>
              <div className="cardinality1DropDownContainer">
                <select disabled={bDisabledFlag}
                        className={sDisabledClassName}
                        value={sSide1Cardinality}
                        onChange={this.handleRelationshipSide1CardinalityDropDownChanged}>
                  {aCardinalityDropDownList1}
                </select>
              </div>
            </div>
            <div className="cardinality2Container">
              <div className="cardinality2DropDownContainer">
                <select disabled={bCardinalityDropDown2DisableFlag}
                        className={sCardinalityDisabledClassName}
                        value={sSide2Cardinality}
                        onChange={this.handleRelationshipSide2CardinalityDropDownChanged}>
                  {aCardinalityDropDownList2}
                </select>
              </div>
            </div>
          </div>
          {oEditableViewDOM}
          {oContextViewDom}
        </div>
    );

    return (
        <CommonConfigSectionView context={ViewContextConstants.RELATIONSHIP_DETAILS_VIEW_CONFIG}
                                 data={oData}
                                 sectionLayout={oDataGovernanceRelationshipConfigLayout.dataModelKlassSelectionLayout1}
        />
    )
  };

  getEditableView = (sExtraClassName) => {
    var oModel = this.props.selectedRelationshipDetailedModel;
    var oSide = oModel.side;
    var oSide1 = oSide.side1;
    var oSide2 = oSide.side2;


    return (
    <div className="visibilityContainer">
      <div className="visibilityHeader">{getTranslation().EDITABLE}
      </div>
      <div className={"isVisible1DropDownContainer " + sExtraClassName}>
        <input type="checkbox"
               checked={oSide1.isVisible}
               className="relIsVisible1"
               onChange={this.handleRelationshipIsVisible1Change}/>
      </div>
      <div className="isVisible2DropDownContainer">
        <input type="checkbox"
               checked={oSide2.isVisible}
               className="relIsVisible2"
               onChange={this.handleRelationshipIsVisible2Change}/>
      </div>
    </div>
    )
  }

  render() {
    let oBasicInformationView = this.getBasicInformationView();
    let oClassSelectionView = this.getClassSelectionView();
    let oDataTransferView = this.getDataTransferView();

    return (
        <div className="relDetailedViewForm">
          <div className='relLeftDetailViewContainer'>
            {oBasicInformationView}
            {oClassSelectionView}
            {oDataTransferView}

          </div>
        </div>
    );
  }
}

export const view = RelationshipDetailsView;
export const events = oEvents;
