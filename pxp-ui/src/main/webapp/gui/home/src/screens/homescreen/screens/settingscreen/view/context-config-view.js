import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CommonConfigSectionView } from '../../../../../viewlibraries/commonconfigsectionview/common-config-section-view.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as CustomDatePicker } from '../../../../../viewlibraries/customdatepickerview/customdatepickerview.js';
import { view as TagSelectorView } from '../../../../../viewlibraries/tagselectorview/tag-selector-view';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import SectionLayout from '../tack/context-config-layout-data';
import MockDataForContextTypes from '../tack/mock/mock-data-for-context-types';
import MockDataForContextViews from '../tack/mock/mock-data-for-context-views';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import ContextTypeDictionary from '../../../../../commonmodule/tack/context-type-dictionary';
import CreateDialogLayoutData from '../tack/mock/create-dialog-layout-data';
import ViewUtils from '../view/utils/view-utils';
import { view as SelectionToggleView } from '../../../../../viewlibraries/selectiontoggleview/selection-toggle-view';
import { view as ContextualAttributeEditSectionViewSelect } from './contextual-attribute-edit-section-select-view';
import Constants from '../../../../../commonmodule/tack/constants';

const oEvents = {
  HANDLE_CONTEXT_TAG_CHECK_ALL_CHANGED: "handle_context_tag_check_all_changed",
  HANDLE_DEFAULT_FROM_DATE_VALUE_CHANGED: "handle_default_from_date_value_changed",
  HANDLE_CONTEXT_CONFIG_DIALOG_BUTTON_CLICKED: "handle_context_config_dialog_button_clicked",
  HANDLE_CUSTOM_CONTEXT_CONFIG_DIALOG_BUTTON_CLICKED: "handle_custom_context_config_dialog_button_clicked",
  CONTEXT_CONFIG_ADD_TAG_COMBINATION: "context_config_add_tag_combination"
};

const oPropTypes = {
  contextTagMap: ReactPropTypes.array,
  activeContext: ReactPropTypes.object,
  masterContextList: ReactPropTypes.object,
  subContexts: ReactPropTypes.array,
  masterEntitiesList: ReactPropTypes.array,
  entities: ReactPropTypes.array,
  timeEnabledModel: ReactPropTypes.object,
  customTabList: ReactPropTypes.array,
  isContextDirty: ReactPropTypes.bool,
  requestResponseInfo: ReactPropTypes.object,
  context:ReactPropTypes.string,
  showCheckboxColumn:ReactPropTypes.bool,
  disableDeleteButton:ReactPropTypes.bool,
  enableImportExportButton:ReactPropTypes.bool,
  disableCreate:ReactPropTypes.bool,
  isDialogOpen:ReactPropTypes.bool,
  oManageEntityDialog:ReactPropTypes.object,
  iconLibraryData:ReactPropTypes.object,
};

// @CS.SafeComponent
class ContextConfigView extends React.Component {
  constructor (props) {
    super(props)
    this.state = {
      contextLabel: "",
      contextType: "",
      contextCode: "",
      id: !CS.isEmpty(props.activeContext)? props.activeContext.id : ""
    };
    this.contextIconUpload = React.createRef();
  }


  static getDerivedStateFromProps (oNextProps, oState) {
    if (!CS.isEmpty(oNextProps.activeContext) && oNextProps.activeContext.id !== oState.id) {
      let oActiveContext = oNextProps.activeContext;
      if (oActiveContext.isCreated) {
        return {
          contextLabel: oActiveContext.label,
          contextType: oActiveContext.type,
          id: oActiveContext.id
        };
      }
      return {
        id: oActiveContext.id
      }
    }
    return null;
  }

  componentDidMount() {
    this.setValues();
  }

  componentDidUpdate() {
    this.setValues();
  }

  handleContextDialogButtonClicked = (sContext) => {

    EventBus.dispatch(oEvents.HANDLE_CONTEXT_CONFIG_DIALOG_BUTTON_CLICKED, sContext);

    this.setState({
      contextLabel: "",
      contextType: ""
    });
  };

  handleContextConfigDetailDialogButtonClicked = (sContext) => {
    EventBus.dispatch(oEvents.HANDLE_CUSTOM_CONTEXT_CONFIG_DIALOG_BUTTON_CLICKED, sContext);
  }

  setValues = () => {
    if(this.contextIconUpload.current) {
      this.contextIconUpload.current.value = '';
    }
  };

  handleElementTagCheckAllChanged = (sContextTagId, oEvent) => {
    EventBus.dispatch(oEvents.HANDLE_CONTEXT_TAG_CHECK_ALL_CHANGED, this, sContextTagId);
  };

  handleAddTagCombinationClicked = () => {
    EventBus.dispatch(oEvents.CONTEXT_CONFIG_ADD_TAG_COMBINATION);
  };

  handleDefaultFromDateValueChanged = (sContext, sNull, sValue) => {
    if(sContext != "fromDefaultCurrentDate") {
      sValue = new Date(sValue).getTime();
    } else {
      sValue = "";
    }
    EventBus.dispatch(oEvents.HANDLE_DEFAULT_FROM_DATE_VALUE_CHANGED, sValue, sContext);
  };

  getEntitiesDOM = () => {
    var aEnabledIn = [ContextTypeDictionary.CONTEXTUAL_VARIANT,
                      ContextTypeDictionary.LANGUAGE_VARIANT];
    var _this = this;
    var __props = _this.props;
    var oActiveContext = __props.activeContext;
    if(CS.includes(aEnabledIn, oActiveContext.type)) {
      var aMasterEntitiesList = __props.masterEntitiesList;
      var aSelectedEntities = oActiveContext.entities;

      return (
          <div className="contextEntitiesContainer">
            <div className="contextEntitiesHeader commonConfigSectionElementHeader">{getTranslation().ENTITIES}</div>
            <SelectionToggleView
                items={aMasterEntitiesList}
                selectedItems={aSelectedEntities}
                context="context"
                contextKey="entities"
            />
          </div>
      );
    }
    return null;
  };

  getTagSectionViewDOM = () => {
    var _this = this;
    let _props = _this.props;
    var oContext = _props.activeContext;
    let sActiveContextType = oContext.type;
    var aTagSectionDisabledIn = [ContextTypeDictionary.GTIN_CONTEXT];
    if (CS.includes(aTagSectionDisabledIn, sActiveContextType)) {
      return null;
    }

    var aContextTags = oContext.contextTags;
    let oPaginationData = ViewUtils.getEntityPaginationData();
    let sSearchText = ViewUtils.getEntitySearchText();
    let sSplitter = ViewUtils.getSplitter();

    let oReqResObj = _props.requestResponseInfo;
    let aTagTypesToFetch = [TagTypeConstants.TAG_TYPE_BOOLEAN, TagTypeConstants.RANGE_TAG_TYPE, TagTypeConstants.RULER_TAG_TYPE, TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE, TagTypeConstants.YES_NEUTRAL_TAG_TYPE, TagTypeConstants.TAG_TYPE_MASTER];
    oReqResObj = ViewUtils.getConfigDataLazyRequestResponseObjectByEntityName("tags", aTagTypesToFetch);

    let oTagMap = {};
    return (
        <div className="contextTagListsContainer">
          <TagSelectorView
              context={oContext.id + sSplitter + "context"}
              isTagValueListLoadMoreEnabled={true}
              isTagValueListSearchEnabled={true}
              paginationData={oPaginationData}
              requestResponseInfo={oReqResObj}
              searchText={sSearchText}
              selectedTagsData={aContextTags}
              tagMap={oTagMap}
          />
        </div>
    )
  };

  getContextViewPropertyModel = () => {
    var oSelectedContext = this.props.activeContext;
    var aSelectedViews = oSelectedContext.defaultView ? [oSelectedContext.defaultView] : [];
    var aAllowedTypes = [];

    if (oSelectedContext && CS.indexOf(aAllowedTypes, oSelectedContext.type) != -1) {

      return {
        disabled: false,
        label: "",
        items: new MockDataForContextViews(),
        selectedItems: aSelectedViews,
        singleSelect: true,
        context: "contextView",
        disableCross: true
      };
    }

    return null;
  };

  getDateForCustomDatePicker = (sDefaultFromDate) => {
    sDefaultFromDate = sDefaultFromDate ? +sDefaultFromDate ? new Date(+sDefaultFromDate) : null : null;
    if (CS.isNaN(Date.parse(sDefaultFromDate))) {
      sDefaultFromDate = null;
    }
    return sDefaultFromDate;
  };

  getDefaultFromDateView = () => {
    var oActiveContext = this.props.activeContext;
    oActiveContext.defaultTimeRange || (oActiveContext.defaultTimeRange = {});
    var oDefaultTimeRange = oActiveContext.defaultTimeRange;
    var sDefaultFromDate = oDefaultTimeRange.from;
    var sDefaultToDate = oDefaultTimeRange.to;
    sDefaultToDate = this.getDateForCustomDatePicker(sDefaultToDate);
    sDefaultFromDate = this.getDateForCustomDatePicker(sDefaultFromDate);
    var bIsEnabled = oDefaultTimeRange.isCurrentTime || false;
    var sClassForCurrentDateEnabled = bIsEnabled? ' noTextBoxForDate': '';
    return <div className="defaultFromDateViewWrapper">
      <CustomDatePicker
          value={sDefaultFromDate}
          maxDate={sDefaultToDate || undefined}
          disabled={bIsEnabled}
          className="datePickerCustom"
          onChange={this.handleDefaultFromDateValueChanged.bind(this, "fromDefaultDate")}/>
      <div className={"currentDateSelectorWrapper" + sClassForCurrentDateEnabled}
           onClick={this.handleDefaultFromDateValueChanged.bind(this, "fromDefaultCurrentDate")}>
        <input type="checkbox" checked={bIsEnabled}/>
        <span className="useCurrentDateLabel">{getTranslation().USE_DEFAULT_DATE}</span>
      </div>
    </div>
  };

  getDefaultToDateView = () => {
    var oActiveContext = this.props.activeContext;
    var oDefaultTimeRange = oActiveContext.defaultTimeRange || {};
    var oDefaultFromDate = this.getDateForCustomDatePicker(oDefaultTimeRange.from); //getDateForCustomDatePicker is returning object
    var oDefaultToDate = this.getDateForCustomDatePicker(oDefaultTimeRange.to);
    let bIsDisabled = false;
    if (oDefaultTimeRange.isCurrentTime) {
      var oCurrentDate = new Date();
      oDefaultFromDate = oCurrentDate;
      bIsDisabled = true;
    }

    return <div className="defaultFromDateViewWrapper">
      <CustomDatePicker
        value={oDefaultToDate}
        minDate={oDefaultFromDate || undefined}
        allowInfinity={true}
        className="datePickerCustom"
        onChange={this.handleDefaultFromDateValueChanged.bind(this, "toDefaultDate")}
        disabled={bIsDisabled}/>

    </div>
  };

  getAutoCreateModel = (oSelectedContext) => {
    let sSelectedContextType = oSelectedContext.type;
    let aAutoCreateDisabledIn = [ContextTypeDictionary.ATTRIBUTE_CONTEXT_VARIANT, ContextTypeDictionary.RELATIONSHIP_VARIANT,
                                  ContextTypeDictionary.GTIN_CONTEXT, ContextTypeDictionary.PRODUCT_VARIANT];
    if (!CS.includes(aAutoCreateDisabledIn, sSelectedContextType)) {
      return ({
        context: "context",
        isSelected: oSelectedContext.isAutoCreate
      });
    } else {
      return null;
    }
  };

  getAllowDuplicatedModel = (oSelectedContext) => {
    let sSelectedContextType = oSelectedContext.type;
    let aAllowDuplicatedDisabledIn = [ContextTypeDictionary.RELATIONSHIP_VARIANT, ContextTypeDictionary.GTIN_CONTEXT];
    if (!CS.includes(aAllowDuplicatedDisabledIn, sSelectedContextType)) {
      return ({
        context: "context",
        isSelected: oSelectedContext.isDuplicateVariantAllowed
      });
    } else {
      return null;
    }
  };

  getContextTypeModel = (sSelectedContextType) => {
    var aSelectedTypes = [sSelectedContextType];
    return ({
      disabled: true,
      label: "",
      items: new MockDataForContextTypes(),
      selectedItems: aSelectedTypes,
      singleSelect: true,
      context: "contextType",
      disableCross: true
    })
  };

  getIconModel = (oSelectedContext) => {
    let sSelectedContextType = oSelectedContext.type;
    let aIconDisabledIn = [];
    if (!CS.includes(aIconDisabledIn, sSelectedContextType)) {
      return ({
        icon: oSelectedContext.icon,
        context: "context",
        iconKey: oSelectedContext.iconKey
      });
    } else {
      return null;
    }
  };

  getTagCombinationViewData = (oSelectedContext) => {
    let sActiveUniqueSelectorId = this.props.activeTagUniqueSelectionId;

    return {
      tags: oSelectedContext.contextTags,
      uniqueSelectors: oSelectedContext.uniqueSelectors,
      activeUniqueSelector: sActiveUniqueSelectorId
    };
  };


  getTagCombinationView = (oSelectedContext) => {
    let _props = this.props;
    let sContext =_props.context;
    let aSelectedTagsData = oSelectedContext.contextTags;
    let bIsAutoCreate = oSelectedContext.isAutoCreate;

    if(!bIsAutoCreate || CS.isEmpty(aSelectedTagsData)){
      return null;
    }

    let oTagCombinationData = this.getTagCombinationViewData(oSelectedContext);

    return (<div className="tagPossibleCombinationViewWrapper">
      <div className="tagCombinationButtonContainer">
        <div className="tagCombinationAddButton" onClick={this.handleAddTagCombinationClicked}></div>
      </div>
      <div className="tagPossibleCombinationViewBody">
        <ContextualAttributeEditSectionViewSelect viewData={oTagCombinationData}
                                                  context={sContext}/>
      </div>
    </div>)
  };

  getCommonConfigData = () => {
    var __props = this.props;
    var oActiveContext = __props.activeContext;
    var oSelectedContext = __props.activeContext;
    var sSelectedContextType = oSelectedContext.type;
    var oTagSectionViewDOM = this.getTagSectionViewDOM();

    var oContextTypeModel = this.getContextTypeModel(sSelectedContextType);
    var oDefaultViewModel = this.getContextViewPropertyModel();
    /** Status tags in context config are not required. Also backend has no support for it**/
    /** Status tags now won't be displayed **/

    var oTimeEnabledModel = null;
    var oDefaultFromDateView = null;
    var oDefaultToDateView = null;
    if (sSelectedContextType !== ContextTypeDictionary.GTIN_CONTEXT && sSelectedContextType !== ContextTypeDictionary.LANGUAGE_VARIANT) {
      oTimeEnabledModel = this.props.timeEnabledModel;
      oDefaultFromDateView = this.getDefaultFromDateView();
      oDefaultToDateView = this.getDefaultToDateView();
    }

    //Sub context selection currently not required anywhere :
    var oSubContextDOM = null;
    var oEntitiesDOM = this.getEntitiesDOM();
    var oIsAutoCreate = this.getAutoCreateModel(oSelectedContext);
    var oAllowDuplicates = this.getAllowDuplicatedModel(oSelectedContext);
    let oIconDOM = this.getIconModel(oSelectedContext);
    let oTagCombinationView = this.getTagCombinationView(oSelectedContext);
    let oSelectIconData = this.props.iconLibraryData;
    let oCommonConfigData = {
      label: oSelectedContext.label,
      type: oContextTypeModel,
      icon: oIconDOM,
      tagGroups: oTagSectionViewDOM,
      subContexts: oSubContextDOM,
      entities: oEntitiesDOM,
      isLimitedObject: null,
      isAutoCreate: oIsAutoCreate,
      isTimeEnabled: oTimeEnabledModel,
      isDuplicateVariantAllowed: oAllowDuplicates,
      defaultFromDate: oActiveContext.isTimeEnabled ? oDefaultFromDateView : null,
      defaultToDate: oActiveContext.isTimeEnabled ? oDefaultToDateView : null,
      defaultView: oDefaultViewModel,
      code: oActiveContext.code,
      tagCombinationView: oTagCombinationView,
      showSelectIconDialog: oActiveContext.showSelectIconDialog,
      selectIconData: oSelectIconData,
    };

    if (oActiveContext.type === ContextTypeDictionary.GTIN_CONTEXT ||
        oActiveContext.type === ContextTypeDictionary.CONTEXTUAL_VARIANT ||
        oActiveContext.type === ContextTypeDictionary.LANGUAGE_VARIANT) {
      let aCustomTabList = CS.values(oActiveContext.configDetails.referencedTabs);
      let sSelectedTabId = oActiveContext.tabId;
      let oSelectedTab = CS.find(aCustomTabList, {id: sSelectedTabId});
      let aSelectedTab = CS.isEmpty(oSelectedTab) ? [] : [oSelectedTab];
      let aSelectedTabId = CS.isEmpty(sSelectedTabId) ? [] : [sSelectedTabId];

      let oReqResObj = {
        requestType: "configData",
        entityName: "tabs",
        typesToExclude: [Constants.TAB_RENDITION, Constants.TAB_DUPLICATE_ASSETS],
      };

      let oReferencedData = {};
      if(!CS.isEmpty(oSelectedTab) && oSelectedTab.label) {
        oReferencedData[oSelectedTab.id] = oSelectedTab;
      }

      oCommonConfigData.tab =  {
        context: 'tabId',
        isMultiSelect: false,
        showCreateButton: true,
        items: aCustomTabList,
        selectedItems: aSelectedTabId,
        selectedObjects: aSelectedTab,
        isLoadMoreEnabled: true,
        searchText: ViewUtils.getEntitySearchText(),
        referencedData: oReferencedData,
        requestResponseInfo: oReqResObj,
        cannotRemove: true
      };
    }

    return (oCommonConfigData);
  };

  getCommonConfigView = () => {
    let oActiveContext = this.props.activeContext;
    var oCommonConfigData = this.getCommonConfigData();
    let oSectionLayout = new SectionLayout();
    var aDisabledFields = (oActiveContext && oActiveContext.isCloneContext) ? ViewUtils.getAllKeysFromSectionLayout(oSectionLayout) : ["code"];

    return (
      <CommonConfigSectionView context="context" sectionLayout={oSectionLayout} data={oCommonConfigData} disabledFields={aDisabledFields}/>
    );
  };

  getContextDetailedView = () => {
    let oActiveContext = this.props.activeContext;
    if (CS.isEmpty(oActiveContext)) {
      return null;
    }

    let oBodyStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
      overflowY: "auto"
    };
    let aButtonData = [];

    if (this.props.isContextDirty) {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "save",
          label: getTranslation().SAVE,
          isDisabled: false,
          isFlat: false,
        }];
    } else {
      aButtonData = [{
        id: "ok",
        label: getTranslation().OK,
        isFlat: false,
        isDisabled: false
      }];
    }

    let oCommonConfigViewDOM = this.getCommonConfigView();
    let sTitle = getTranslation().CONTEXT_DETAILS;
    let bIsDialogOpen = this.props.isDialogOpen;
    let fButtonHandler = this.handleContextConfigDetailDialogButtonClicked;

    return (<CustomDialogView modal={true}
                              open={bIsDialogOpen}
                              title={sTitle}
                              bodyStyle={oBodyStyle}
                              contentStyle={oBodyStyle}
                              bodyClassName=""
                              contentClassName="contextConfigDetail"
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler}
                              children={oCommonConfigViewDOM}>
    </CustomDialogView>);
  };

  getCreateDialogCommonConfigData = (oActiveContext) => {
    oActiveContext = oActiveContext.clonedObject ? oActiveContext.clonedObject : oActiveContext;

    let oModel = {
      label: "",
      type: {},
      code: ""
    };

    let aDataForContextTypes =[];
    let oMockDataForContextTypes = new MockDataForContextTypes();
    CS.forEach(oMockDataForContextTypes, function (oData) {
      switch (oData.id) {
        case ContextTypeDictionary.GTIN_CONTEXT:
        case ContextTypeDictionary.PROMOTIONAL_VERSION:
        case ContextTypeDictionary.CONTEXTUAL_VARIANT:
        case ContextTypeDictionary.IMAGE_VARIANT:
        case ContextTypeDictionary.LANGUAGE_VARIANT:
          break;
        default:
          aDataForContextTypes.push(oData);
          break;
      }
    });

    let sSplitter = ViewUtils.getSplitter();
    oModel.type = {
      items: aDataForContextTypes,
      selectedItems: [oActiveContext.type],
      cannotRemove: true,
      context: "context" + sSplitter + "type",
      disabled: false,
      label: "",
      isMultiSelect: false,
      disableCross: true,
      hideTooltip: true,
    };

    oModel.label = oActiveContext.label;
    oModel.code = oActiveContext.code;
    oModel.showSelectIconDialog = oActiveContext.showSelectIconDialog;
    oModel.selectIconData =this.props.iconLibraryData;
    return oModel;
  };

  getCreateContextDialog = () => {
    let oActiveContext = this.props.activeContext;
    if (oActiveContext.isCreated) {

      var bIsDisableCreate = true;
      var sErrorText = "";
      if (CS.isEmpty(oActiveContext.code.trim())) {
        sErrorText = getTranslation().CODE_SHOULD_NOT_BE_EMPTY;
      } else if (!ViewUtils.isValidEntityCode(oActiveContext.code)) {
        sErrorText = getTranslation().PLEASE_ENTER_VALID_CODE;
      } else {
        bIsDisableCreate = false;
      }

      let oModel = this.getCreateDialogCommonConfigData(oActiveContext);
      var oBodyStyle = {
        padding: '0 10px 20px 10px'
      };
      var aButtonData = [
        {
          id: "cancel",
          label: getTranslation().CANCEL,
          isDisabled: false,
          isFlat: true,
        },
        {
          id: "create",
          label: getTranslation().CREATE,
          isDisabled: bIsDisableCreate,
          isFlat: false,
        }

      ];
      let oCreateDialogLayoutData = new CreateDialogLayoutData();
      return (<CustomDialogView modal={true} open={true}
                                title={getTranslation().CREATE}
                                bodyStyle={oBodyStyle}
                                bodyClassName=""
                                contentClassName="createContextModalDialog"
                                buttonData={aButtonData}
                                onRequestClose={this.handleContextDialogButtonClicked.bind(this, aButtonData[0].id)}
                                buttonClickHandler={this.handleContextDialogButtonClicked}>
        <CommonConfigSectionView context={"context"} sectionLayout={oCreateDialogLayoutData} data={oModel}
                                 errorTextForCodeEntity={sErrorText}/>
      </CustomDialogView>);

    }
    else {
      return null;
    }
  };

  render() {

    /**Context detail view */
    let oContextDetailedView = this.getContextDetailedView();

    /**Create Context Dialog */
    let oCreateContextDialog = this.getCreateContextDialog();

    /**Grid Required Data */
    let sContext =  this.props.context;
    let bShowCheckboxColumn = this.props.showCheckboxColumn;
    let bDisableDeleteButton = this.props.disableDeleteButton;
    let bEnableImportExportButton = this.props.enableImportExportButton;
    let bDisableCreate = this.props.disableCreate;
    let oManageEntityDialog = this.props.oManageEntityDialog;
    let bEnableManageEntityButton = this.props.enableManageEntityButton;
    let oSelectIconDialog = this.props.selectIconDialog;

    return (
        <div className="configGridViewContainer" key="contextConfigGridViewContainer">

          <ContextualGridView
              context={sContext}
              tableContextId={sContext}
              showCheckboxColumn={bShowCheckboxColumn}
              disableDeleteButton={bDisableDeleteButton}
              enableImportExportButton={bEnableImportExportButton}
              disableCreate={bDisableCreate}
              enableManageEntityButton={bEnableManageEntityButton}
          />
          {oContextDetailedView}
          {oCreateContextDialog}
          {oManageEntityDialog}
          {oSelectIconDialog}
        </div>
    );
  }
}

export const view = ContextConfigView;
export const events = oEvents;
