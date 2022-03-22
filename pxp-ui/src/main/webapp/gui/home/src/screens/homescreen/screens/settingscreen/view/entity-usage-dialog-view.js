
import CS from '../../../../../libraries/cs';

import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import {getTranslations} from '../../../../../commonmodule/store/helper/translation-manager.js';
import {view as CustomDialogView} from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import Icon from '@material-ui/core/Icon';

const oEvents = {
  HANDLE_MANAGE_ENTITY_DIALOG_BUTTON_CLICKED: "handle_manage_entity_dialog_button_clicked"
};

const oPropTypes = {
  activeEntity: ReactPropTypes.object,
  entityType: ReactPropTypes.string,
  model: ReactPropTypes.object,
  extraFieldsToShow: ReactPropTypes.object,
  oEntityDatList: ReactPropTypes.object,
  usageSummary: ReactPropTypes.object,
  referenceData: ReactPropTypes.object,
  linkedIds: ReactPropTypes.object,
  usedBy: ReactPropTypes.object,
  buttonData: ReactPropTypes.array,
  title: ReactPropTypes.string,
  message: ReactPropTypes.string,
};

// @CS.SafeComponent
class ManageEntityDialogView extends React.Component {
  static propTypes = oPropTypes;

  handleEntityDialogButtonClicked = (sButtonId) => {
    var sEntityType = this.props.entityType;
    EventBus.dispatch(oEvents.HANDLE_MANAGE_ENTITY_DIALOG_BUTTON_CLICKED, sButtonId, sEntityType);
  };

  getDialogHeaderData = (oEntityUsageSummary) => {
    return (
          <div className="entityTitleWrapper">
            <TooltipView placement="bottom" label={CS.getLabelOrCode(oEntityUsageSummary)}>
              <h5 className="entityType">
                {CS.getLabelOrCode(oEntityUsageSummary)}
              </h5>
            </TooltipView>
            <span className="usedCount">
                {oEntityUsageSummary.totalCount}
              </span>
          </div>
    )
  }

  getEntityDetail = (oEntityUsageSummary, oEntityListReferenceData) => {
    let aEntityUsageListView = [];
    if (oEntityUsageSummary.totalCount == 0) {
      aEntityUsageListView.push(this.getDialogHeaderData(oEntityUsageSummary));
      aEntityUsageListView.push(
        <div className="no_usage_found">
          No Usage Found
            </div>
      );
    }
    else {
      aEntityUsageListView.push(this.getDialogHeaderData(oEntityUsageSummary));
      let aUsedBy = oEntityUsageSummary.usedBy;
      CS.forEach(aUsedBy, oUsedBy => {
        let oEntityType = getTranslations()[oUsedBy.entityType];
        CS.forEach(oUsedBy.linkedIds, id => {
          let oUsage = CS.find(oEntityListReferenceData, {id: id})
          aEntityUsageListView.push(
              <div className="manageEntityWrapper">
                <div className="used_entity_by">
                  <TooltipView placement="bottom" label={CS.getLabelOrCode(oUsage)}>
                    <div>
                      {CS.getLabelOrCode(oUsage)}
                    </div>
                  </TooltipView>
                  <div className="user_tab">
                  {oEntityType}
                </div>
                </div>
              </div>
          )
        })
      });
    }
    return <div className="entityConfiguration">{aEntityUsageListView}</div>;
  }

  getDialogContentView = () => {
    let oEntityUsageList = this.props.oEntityDatList;
    let aEntityListUsageSummary = oEntityUsageList.usageSummary;
    let oEntityListReferenceData = oEntityUsageList.referenceData;
    // if (totalCount != 0) {

    return CS.map(aEntityListUsageSummary, oEntityUsageSummary => {
      return this.getEntityDetail(oEntityUsageSummary, oEntityListReferenceData);
    })
  };

  getChildDependencyMessage(parentLabel) {
    let {bIsDelete} = this.props;
    return <div className="alert alert-danger" role="alert">
      <div className="row" style={{
        display: "flex",
        padding: "0px 10px"
      }}>
        <div className="" style={{
          paddingTop: bIsDelete ? 4 : 0
        }}>
          <Icon style={{
            color: "#a94442"
          }} component="div">
            warning
          </Icon>
        </div>
        <div className="" style={{
          paddingLeft: 8,
          paddingTop: bIsDelete ? 0 : 5
        }}>
          {`Child node/s are present under `}<strong>{parentLabel}</strong>{` which are being used by other entities.`} {bIsDelete && `This node cannot be deleted until and unless all the usages of child nodes are removed.`}
        </div>
      </div>
    </div>
  }

  getDialogView = () => {
    var aDefaultButtonData = [
      {
        id: "close",
        label: getTranslations().CLOSE,
        isDisabled: false,
        isFlat: false,
      }
    ];
    let aButtonData = CS.isNotEmpty(this.props.buttonData) ? this.props.buttonData : aDefaultButtonData;
    let sTitle = CS.isNotEmpty(this.props.title) ? this.props.title : "Entity Usage";
    let sDefaultMessage = "The entity is being used in following configuration entity/(ies)";
    let sMessage = CS.isNotEmpty(this.props.message) ? this.props.message : sDefaultMessage;
    let {hasChildDependency, usageSummary} = this.props.oEntityDatList;
    let parentLabel = ""
    let fButtonHandler = this.handleEntityDialogButtonClicked;
    if(hasChildDependency) {
      parentLabel = CS.getLabelOrCode(usageSummary[0]);
    }
    return (
      <CustomDialogView modal={true}
        open={true}
        title={sTitle}
        className={'manageEntityDialog'}
        ContentClassName="ManageEntityModalDialog"
        buttonData={aButtonData}
        onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
        buttonClickHandler={fButtonHandler}>
        {hasChildDependency && this.getChildDependencyMessage(parentLabel)}
        <div className="message">
          {this.props.bIsDelete && <span className="alertMessageIcon"></span>}
          {sMessage}
        </div>
        <div className="entityWrapper">
          {this.getDialogContentView()}
        </div>
      </CustomDialogView>
    )
  };

  render() {
    return (this.getDialogView());
  }
}

export const view = ManageEntityDialogView;
export const events = oEvents;
