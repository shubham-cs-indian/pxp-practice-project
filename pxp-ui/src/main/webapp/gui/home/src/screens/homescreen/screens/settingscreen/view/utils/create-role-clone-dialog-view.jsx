import ReactPropTypes from 'prop-types';
import React from "react";
import EventBus from "../../../../../../libraries/eventdispatcher/EventDispatcher";
import {getTranslations as getTranslation} from "../../../../../../commonmodule/store/helper/translation-manager";
import {view as CustomDialogView} from "../../../../../../viewlibraries/customdialogview/custom-dialog-view";
import {view as RoleDetailView} from "../role-detail-view";
import CS from '../../../../../../libraries/cs';
import TooltipView from '../../../../../../viewlibraries/tooltipview/tooltip-view';
import {view as CustomTextFieldView} from "../../../../../../viewlibraries/customtextfieldview/custom-text-field-view";

const oEvents = {
  CREATE_ROLE_CLONE_DIALOG_ENTITY_BUTTON_CLICKED: "create_role_clone_dialog_entity_button_clicked",
  CREATE_ROLE_CLONE_DIALOG_CHECKBOX_CLICKED: "create_role_clone_dialog_checkbox_clicked",
  CREATE_ROLE_CLONE_DIALOG_EXACT_CLONE_CHECKBOX_CLICKED: "create_role_clone_dialog_exact_clone_checkbox_clicked",
  CREATE_ROLE_CLONE_DIALOG_EDIT_VALUE_CHANGED: "create_role_clone_dialog_edit_value_changed"
};

const oPropTypes = {
  selectedRoleDetailedModel: ReactPropTypes.object.required,
  entitiesList: ReactPropTypes.array.required,
  isUserSystemAdmin: ReactPropTypes.bool,
  checkboxData: ReactPropTypes.object,
  createRoleCloneDialogData: ReactPropTypes.object.required
};

class CreateRoleCloneDialogView extends React.Component {

  static propTypes = oPropTypes;

  constructor (props) {
    super(props);

    this.state = CreateRoleCloneDialogView.getNewState();
  };

  static getNewState = () => {
    let oState = {
      showLabelInputBox: false,
      showCodeInputBox: false
    };
    return oState;
  };

  handleEntityDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.CREATE_ROLE_CLONE_DIALOG_ENTITY_BUTTON_CLICKED, sButtonId);
  };

  handleCheckBoxClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.CREATE_ROLE_CLONE_DIALOG_CHECKBOX_CLICKED, sButtonId);
  };

  handleEditButtonClicked = (sButtonId) => {
    if (sButtonId === "label") {
      this.setState({
        showLabelInputBox: !this.state.showLabelInputBox,
      });
    } else {
      this.setState({
        showCodeInputBox: !this.state.showCodeInputBox
      });
    }
  };

  handleExactCloneButtonClicked = () => {
    EventBus.dispatch(oEvents.CREATE_ROLE_CLONE_DIALOG_EXACT_CLONE_CHECKBOX_CLICKED);
  };

  handleValueChanged = (sContext, sValue) => {
    if (sContext === "label") {
      this.state.showLabelInputBox = false;
    } else {
      this.state.showCodeInputBox = false;
    }
    EventBus.dispatch(oEvents.CREATE_ROLE_CLONE_DIALOG_EDIT_VALUE_CHANGED, sContext, sValue);
  };

  getCustomTextFieldView = (sValue, sContext, sPlaceholder) => {
    return (
        <CustomTextFieldView
            value={sValue}
            onBlur={this.handleValueChanged.bind(this, sContext)}
            hintText={sPlaceholder}
            forceBlur={true}
            shouldFocus={true}
        />)
  };

  getLabelDOM = (sId, sValue) => {
    let oValueView = (sId === "code" && CS.isEmpty(sValue)) ? getTranslation().AUTO_GENERATED_CODE : sValue;
    return (<TooltipView placement={"bottom"} label={oValueView}>
      <div className="labelBox">
        {oValueView}
      </div>
    </TooltipView>);
  };

  getHeaderDom = (sId, bIsEditButtonClicked, sValue, sPlaceHolder, sLabel, sClassName) => {

    let oValueViewDOM = bIsEditButtonClicked ? this.getCustomTextFieldView(sValue, sId, sPlaceHolder) : this.getLabelDOM(sId, sValue);
    return(
        <div className={"cloneHeader" + sClassName}>
          <TooltipView placement={"bottom"} label={sLabel}>
            <div className="titleBox">
              {sLabel}
            </div>
          </TooltipView>
          {oValueViewDOM}
          <div className="editLabelValueIcon" onClick={this.handleEditButtonClicked.bind(this, sId)}/>
        </div>
    );
  };

  getCloneWizardHeaderView = () => {
    let oDialogData = this.props.createRoleCloneDialogData;
    let bIsExactCloneSelected = oDialogData.bIsExactClone;
    let sExactCloneClassName = bIsExactCloneSelected ? "cloneWizardCheckButton checkedItem" : "cloneWizardCheckButton";
    return (
        <div className="cloneHeaderView">
          {this.getHeaderDom("label", this.state.showLabelInputBox, oDialogData.sLabel, "", getTranslation().NAME, "")}
          {this.getHeaderDom("code", this.state.showCodeInputBox, oDialogData.sCode, getTranslation().AUTO_GENERATED_CODE, getTranslation().CODE, " code")}
          <div className="exactCloneView">
            <div className={sExactCloneClassName} onClick={this.handleExactCloneButtonClicked}></div>
            <TooltipView placement={"bottom"} label={getTranslation().EXACT_CLONE}>
              <div className="exactCloneLabel">
                {getTranslation().EXACT_CLONE}
              </div>
            </TooltipView>
          </div>
        </div>
    );
  };

  getDialogView = () => {
    var aButtonData = [
      {
        id: "cancel",
        label: getTranslation().CANCEL,
        isFlat: true
      },
      {
        id: "save",
        label: getTranslation().APPLY,
        isFlat: false
      }
    ];
    let sTitle = getTranslation().CREATE_CLONE;
    let fButtonHandler = this.handleEntityDialogButtonClicked;
    return (
        <CustomDialogView modal={true}
                          open={true}
                          title={sTitle}
                          className={'createRoleCloneDialog'}
                          ContentClassName="CreateRoleCloneModalDialog"
                          buttonData={aButtonData}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          <div className="roleCloneWizardView">
            {this.getCloneWizardHeaderView()}
              <RoleDetailView
                  selectedRoleDetailedModel={this.props.selectedRoleDetailedModel}
                  entitiesList={this.props.entitiesList}
                  hideSystemsSelectionView={true}
                  isUserSystemAdmin={this.props.isUserSystemAdmin}
                  handleCheckBoxClicked={this.handleCheckBoxClicked}
                  checkBoxData={this.props.checkboxData}
                  sContext={"roleCloneDialog"}/>
          </div>
        </CustomDialogView>
    )
  };

  render () {
    let oCloneDialogView = this.getDialogView();
    return oCloneDialogView;
  };
}

export const view = CreateRoleCloneDialogView;
export const events = oEvents;
