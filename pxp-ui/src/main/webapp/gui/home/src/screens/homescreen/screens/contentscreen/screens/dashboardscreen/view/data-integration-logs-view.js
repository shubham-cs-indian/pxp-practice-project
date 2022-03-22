import CS from "../../../../../../../libraries/cs";
import React from "react";
import Moment from "moment";
import ReactPropTypes from "prop-types";
import {view as RadioButtonView} from "../../../../../../../viewlibraries/radiobuttonview/radio-button-view";
import {view as LazyMSSView} from '../../../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';
import {view as DateRangePickerView} from "../../../../../../../viewlibraries/customdaterangepickerview/custom-date-range-picker-view";
import {view as CustomMaterialButtonView} from "../../../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view";
import {view as JobListView} from "../../../view/job-list-view";
import {view as NothingFoundView} from "../../../../../../../viewlibraries/nothingfoundview/nothing-found-view";
import {view as CustomDialogView} from "../../../../../../../viewlibraries/customdialogview/custom-dialog-view";
import {view as MSSView} from "../../../../../../../viewlibraries/multiselectview/multi-select-search-view";
import {getTranslations as getTranslation} from "../../../../../../../commonmodule/store/helper/translation-manager";
import EventBus from "../../../../../../../libraries/eventdispatcher/EventDispatcher";
import ViewLibraryUtils from "../../../../../../../viewlibraries/utils/view-library-utils";
import CommonUtils from "../../../../../../../commonmodule/util/common-utils";
import ContentUtils from "../../../store/helper/content-utils";
import TooltipView from "../../../../../../../viewlibraries/tooltipview/tooltip-view";
import ConfigDataEntitiesDictionary from "../../../../../../../commonmodule/tack/config-data-entities-dictionary";
import MockDataForPhysicalCatalogAndPortal from "../../../../../../../commonmodule/tack/mock-data-physical-catalog-and-portal-types";
import oPhysicalCatalogDictionary from "../../../../../../../commonmodule/tack/physical-catalog-dictionary";
import DashboardScreenRequestMapping from './../tack/dashboard-screen-request-mapping';
import mockDataForDataIntegrationLogViewMessageType from "../tack/mockDataForDataIntegrationLogViewMessageType";


const MockDataForPhysicalCatalog = MockDataForPhysicalCatalogAndPortal.physicalCatalogs;
const oEvents = {
  DATA_INTEGRATION_LOGS_VIEW_RADIO_BUTTON_CLICKED: "data_integration_logs_view_radio_button_clicked",
  DATA_INTEGRATION_LOGS_VIEW_LAZY_MSS_CLICKED: "data_integration_logs_view_lazy_mss_clicked",
  DATA_INTEGRATION_LOGS_VIEW_DOWNLOAD_BUTTON_CLICKED: "data_integration_logs_view_download_button_clicked",
  DATA_INTEGRATION_LOGS_VIEW_MSS_CLEAR_BUTTON_CLICKED: "data_integration_logs_view_mss_clear_button_clicked",
  DATA_INTEGRATION_LOGS_VIEW_SEARCH_BUTTON_CLICKED: "data_integration_logs_view_search_button_clicked",
  MESSAGE_TYPE_DIALOG_BUTTON_CLICKED : "message_type_download_button_clicked"
};

const oPropTypes = {
  dataForDataIntegrationLogsView: ReactPropTypes.object,
  context: ReactPropTypes.string
};


// @CS.SafeComponent
class DataIntegrationLogsView extends React.Component {
  constructor (props) {
    super(props);
  }


  handleRadioButtonClicked = (sContext) => {
    EventBus.dispatch(oEvents.DATA_INTEGRATION_LOGS_VIEW_RADIO_BUTTON_CLICKED, sContext);
  };

  handleSearchButtonClicked = () => {
    EventBus.dispatch(oEvents.DATA_INTEGRATION_LOGS_VIEW_SEARCH_BUTTON_CLICKED);
  };

  handleLazyMssApplyClicked = (sContext, aSelectedItems, oReferencedData) => {
    EventBus.dispatch(oEvents.DATA_INTEGRATION_LOGS_VIEW_LAZY_MSS_CLICKED, sContext, aSelectedItems, oReferencedData);
  };

  handleClearButtonClicked = (sContext) =>{
    EventBus.dispatch(oEvents.DATA_INTEGRATION_LOGS_VIEW_MSS_CLEAR_BUTTON_CLICKED,sContext);
  };

   handleDownloadButtonClicked= () => {
    EventBus.dispatch(oEvents.DATA_INTEGRATION_LOGS_VIEW_DOWNLOAD_BUTTON_CLICKED);
  };

  handleMessageTypeDialogButtonClicked = (sButton)=> {
    EventBus.dispatch(oEvents.MESSAGE_TYPE_DIALOG_BUTTON_CLICKED, sButton);
  };

  getLazyMSSView = (aSelectedItems, oReferencedData, oReqResInfo, bIsMultiSelect, sLabelToShow, sContext) => {
    let _this = this;
    let sCannotRemove = false;
    let sCustomPlaceholder = getTranslation().PLEASE_SELECT;
    let oPopoverStyle = {
      marginTop: "10px",
    };
    return (
        <div className="lazyDiv">
          <div className="lazyLabel">{sLabelToShow}</div>
          <div className="lazyValue">
            <LazyMSSView
                popoverStyle={oPopoverStyle}
                selected={true}
                customPlaceholder={sCustomPlaceholder}
                selectedItems={aSelectedItems}
                referencedData={oReferencedData}
                requestResponseInfo={oReqResInfo}
                isMultiSelect={bIsMultiSelect}
                label={sLabelToShow}
                context={sContext}
                cannotRemove={sCannotRemove}
                showClearButton={true}
                onClearHandler={_this.handleClearButtonClicked.bind(_this, sContext)}
                onApply={_this.handleLazyMssApplyClicked.bind(_this, sContext)}
            />
          </div>
        </div>
    );
  };


  getMSSView = (aSelectedItems, aEntityTypesToShow, sEntityType, sEntityName, bIsDisable) => {
    let _this = this;
    let sSplitter = ContentUtils.getSplitter();
    let sContext = "dashboardLogs" + sSplitter + sEntityType;
    let sCustomPlaceholder = getTranslation().PLEASE_SELECT;
    return (
        <div className="entityType">
          <TooltipView placement="top" label={sEntityName}>
            <div className="entityName">{sEntityName}</div>
          </TooltipView>
          <div className="mssViewBorder">
            <MSSView
                selected={true}
                customPlaceholder={sCustomPlaceholder}
                disabled={bIsDisable}
                items={aEntityTypesToShow}
                selectedItems={aSelectedItems}
                isMultiSelect={true}
                cannotRemove={false}
                onApply={_this.handleLazyMssApplyClicked.bind(_this, sEntityType)}
                showClearButton={true}
                onClearHandler={_this.handleClearButtonClicked.bind(_this, sContext)}
            />
          </div>
        </div>)

  };

  getEndpointView = (oData, sIconClass, bIsEndpointSelected) => {
    let _this = this;
    let endpointCss = "endpoint";
    if (!bIsEndpointSelected) {
      endpointCss = endpointCss + " makeDivInactive";
      sIconClass = sIconClass + " disabled";
    }
    return (<div className="endpointDiv">
      <div className="RadioButtonViewCSS">
        <RadioButtonView context={sIconClass} selected={bIsEndpointSelected}
                         clickHanlder={_this.handleRadioButtonClicked.bind(_this, "endpoint")}/>
      </div>
      <div className={endpointCss}>
        {this.getLazyMSSView(oData.selectedEndpoints, null, {
          requestType: "configData",
          entityName: ConfigDataEntitiesDictionary.ENDPOINTS,
        }, true, getTranslation().ENDPOINT, "endpoint")}
      </div>
    </div>);

  };

  getWorkflowView = (oData, sIconClass, bIsWorkflowSelected) => {
    let _this = this;
    let workflowCss = "workflow";
    if (!bIsWorkflowSelected) {
      workflowCss = workflowCss + " makeDivInactive";
      sIconClass = sIconClass + " disabled";
    }
    return (<div className="workflowDiv">
      <div className="RadioButtonViewCSS">
        <RadioButtonView context={sIconClass} selected={bIsWorkflowSelected}
                         clickHanlder={_this.handleRadioButtonClicked.bind(_this, "workflow")}/>
      </div>
      <div className={workflowCss}>
        {this.getLazyMSSView(oData.selectedWorkflows, null, {
          requestType: "customType",
          requestURL: DashboardScreenRequestMapping.getWorkflows,
          responsePath: ["success", "list"],
          entityName: "dashboard",
          customRequestModel: {}
        }, true, getTranslation().WORKFLOW, "workflow")}
      </div>
    </div>);

  };

  getPhysicalCatalogView (oData, sIconClass, bIPhysicalCatalogSelected) {
    let _this = this;
    let sEntityType = "physicalCatalog";
    let physicalCatalogCss = "physicalCatalog";
    let aMockData = MockDataForPhysicalCatalog();
    let aEntityTypesToShow = CS.filter(aMockData, function (oDataCatalogType) {
      return oDataCatalogType.id !== oPhysicalCatalogDictionary.DATAINTEGRATION;
    });
    if (!bIPhysicalCatalogSelected) {
      physicalCatalogCss = physicalCatalogCss + " makeDivInactive";
      sIconClass = sIconClass + " disabled";
    }

    return (<div className="physicalCatalogDiv">
      <div className="RadioButtonViewCSS">
        <RadioButtonView context={sIconClass} selected={bIPhysicalCatalogSelected}
                         clickHanlder={_this.handleRadioButtonClicked.bind(_this, sEntityType)}/>
      </div>
      <div className={physicalCatalogCss}>
        {this.getMSSView(oData.selectedPhysicalCatalogs, aEntityTypesToShow, sEntityType, getTranslation().PHYSICAL_CATALOG, false)}
      </div>
    </div>);
  };

  getFilteredProcessInstanceView = () => {
    let oJobViewDom = this.getJobListView();
    return oJobViewDom;
  };

  getJobListView = () => {
    let oDataForDataIntegrationLogsView = this.props.dataForDataIntegrationLogsView;
    let oView = <NothingFoundView/>;
    if (CS.isNotEmpty(oDataForDataIntegrationLogsView.jobList)) {
      let oJobData = {
        jobList: oDataForDataIntegrationLogsView.jobList,
        activeJob: oDataForDataIntegrationLogsView.activeJob,
        activeJobGraphData: oDataForDataIntegrationLogsView.activeJobGraphData,
        isProcessInstanceDialogOpen: oDataForDataIntegrationLogsView.isProcessInstanceDialogOpen,
        instanceIID: oDataForDataIntegrationLogsView.instanceIID
      };
      oView = (
          <div className="jobContainer">
            <JobListView {...oJobData}/>
          </div>
      );
    }
    return oView;
  };

  getView = () => {
    let _this = this;
    let oData = _this.props.dataForDataIntegrationLogsView;
    let bIsEndpointSelected = oData.isEndpointSelected;
    let bIsWorkflowSelected = oData.isWorkflowSelected;
    let bIsPhysicalCatalogSelected = oData.isPhysicalCatalogSelected == undefined ? false : oData.isPhysicalCatalogSelected;
    // TODO: #Refact20 removed sIconClass = "selectIcon"
    let sIconClass = "";

    let oButtonRootStyle = {
      height: "28px",
      width: "90px",
      lineHeight: "28px",
      margin: "0 5px",
      padding: '0 10px',
      minWidth: '90px',
      minHeight: '28px',
      boxShadow: 'none',
    };
    let oJobListViewDom = this.getFilteredProcessInstanceView();
    let oDataIntegrationMainViewDom = (<div className="dataIntegrationMainViewWrapper">
      <div className="dataIntegrationFilterViewWrapper">
        <div className="filterBlock1">
          {this.getEndpointView(oData, sIconClass, bIsEndpointSelected)}
          {this.getWorkflowView(oData, sIconClass, bIsWorkflowSelected)}
          {this.getPhysicalCatalogView(oData, sIconClass, bIsPhysicalCatalogSelected)}
        </div>
        <div className="filterBlock2">
          <div className="userDivWrapper">
            {this.getLazyMSSView(oData.selectedUsers, null, {
              requestType: "configData",
              entityName: ConfigDataEntitiesDictionary.USERS,
            }, true, getTranslation().USER, "user")}
          </div>
          <div className="timestampDivWrapper">
            <div className="timeStampWrapper">
              <div className="timestampLabel">{getTranslation().TIMESTAMP}</div>
              {this.getCalenderView()}
            </div>
          </div>
        </div>
        <div className="filterBlock3">
          <div className="buttonWrapper">
            <CustomMaterialButtonView
                label={getTranslation().SEARCH_FILTER}
                isRaisedButton={true}
                isDisabled={false}
                style={oButtonRootStyle}
                onButtonClick={_this.handleSearchButtonClicked.bind(_this)}/>
          </div>
          <div className="buttonWrapper">
            <CustomMaterialButtonView
                label={getTranslation().DOWNLOAD}
                isRaisedButton={true}
                isDisabled={false}
                style={oButtonRootStyle}
                onButtonClick={_this.handleDownloadButtonClicked.bind(_this)}/>
          </div>
        </div>
      </div>
      <div className="filteredProcessInstanceViewWrapper">
        {oJobListViewDom}
      </div>
        {this.getMessageTypeDialog()}
    </div>);
    return oDataIntegrationMainViewDom;
  };


  getDateInLabelFormat = (oDownloadRange) => {
    let sStartDate = ViewLibraryUtils.getDateAttributeInDateTimeFormat(oDownloadRange.startTime).date;
    let sEndDate = ViewLibraryUtils.getDateAttributeInDateTimeFormat(oDownloadRange.endTime).date;
    return sStartDate + " - " + sEndDate;
  };

  getMessageTypeDialog = () => {

    let oBodyStyle = {
      maxWidth: "800px",
      minWidth: "600px",
      overflowY: "auto",
      width: "50%"
    };

    let aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isDisabled: false,
        isFlat: true,
      },
      {
        id: "download",
        label: getTranslation().DOWNLOAD,
        isDisabled: false,
        isFlat: false,
      }];
    let oMessageTypeViewDOM = this.getMessageTypeView();
    let sTitle = getTranslation().BULK_DOWNLOAD_CONTENT_MENU_ITEM_TITLE;
    let bIsDialogOpen = this.props.dataForDataIntegrationLogsView.isMessageDialogOpen;
    let fButtonHandler = this.handleMessageTypeDialogButtonClicked;

    return (<CustomDialogView modal={true}
                              open={bIsDialogOpen}
                              title={sTitle}
                              bodyStyle={oBodyStyle}
                              contentStyle={oBodyStyle}
                              bodyClassName=""
                              contentClassName=""
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler.bind()}
                              children={oMessageTypeViewDOM}>
    </CustomDialogView>);
  };

  getMessageTypeView = () => {
    let oData = this.props.dataForDataIntegrationLogsView;
    let sEntityType = "messageType";
    let aEntityTypesToShow = mockDataForDataIntegrationLogViewMessageType();
    return (
        <div className="messageTypeDialogBox">
          {this.getMSSView(oData.selectedMessageTypes, aEntityTypesToShow, sEntityType, getTranslation().MESSAGE_TYPE, false)}
        </div>
    )
  };

  getCalenderView = () => {
    let oTimeStampData = this.props.dataForDataIntegrationLogsView.timeStampData;
    let sTimeString = (oTimeStampData.userDateRangeStartDate) ? this.getDateInLabelFormat({
      startTime: oTimeStampData.userDateRangeStartDate,
      endTime: oTimeStampData.userDateRangeEndDate
    }) : "";

    let oInputView = (
        <input value={sTimeString} placeholder={getTranslation().PLEASE_SELECT} className="timestampValue"/>);
    let oDateTimeFormat = CommonUtils.getStandardDateTimeFormat();
    let oSelectionRange = {
      'Today': [Moment().subtract(0, 'days'), Moment()],
      'Yesterday': [Moment().subtract(1, 'days'), Moment()],
      'Past 7 Days': [Moment().subtract(6, 'days'), Moment()],
      'Past 30 Days': [Moment().subtract(29, 'days'), Moment()],
      'This Month': [Moment().startOf('month'), Moment().endOf('month')]
    };
    let oDateRangeLocale = {
      customRangeLabel: "Define Period",
      format: oDateTimeFormat.dateFormat + " " + oDateTimeFormat.timeFormat,
      cancelLabel: 'Clear'
    };

    let oDateRangePickerView = (
        <DateRangePickerView
            context={this.props.context}
            ranges={oSelectionRange}
            dateRangeLocale={oDateRangeLocale}
            calendarOpenDirection={'left'}
            alwaysShowCalendars={true}
            showDropdowns={true}
            timePicker24Hour={true}
            timePicker={false}
            timePickerSeconds={true}
        >
          {oInputView}
        </DateRangePickerView>);

    return oDateRangePickerView;

  };


  render () {
    return this.getView();
  }

}

DataIntegrationLogsView.propTypes = oPropTypes;

export const view = DataIntegrationLogsView;
export const events = oEvents;
