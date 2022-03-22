import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as CustomMaterialButtonView } from '../../../../../viewlibraries/custommaterialbuttonview/custom-material-button-view';
import { view as ContextMenuView } from '../../../../../viewlibraries/contextmenuwithsearchview/context-menu-view-new';
import { view as ButtonView } from '../../../../../viewlibraries/buttonview/button-view';


const oEvents = {
  CONTENT_EDIT_TOOLBAR_HANDLE_BUTTON_CLICKED: 'content_edit_toolbar_handle_button_clicked',
  HANDLE_COMMENT_CHANGE: "handle_comment_change"
};

const oPropTypes = {
  toolbarItems: ReactPropTypes.object,
  onClickHandler: ReactPropTypes.func,
  activeTask:ReactPropTypes.object
};

// @CS.SafeComponent
class ContentEditToolbarView extends React.Component {

  constructor (props) {
    super(props);
    this.state = {isMoreOptionsEnabled: false};
  };

  handleItemClicked (sButtonId) {
    if (CS.isFunction(this.props.onClickHandler)) {
      this.props.onClickHandler(sButtonId);
    } else {
      EventBus.dispatch(oEvents.CONTENT_EDIT_TOOLBAR_HANDLE_BUTTON_CLICKED, sButtonId);
    }
  };

  handleContextMenuItemClicked = (oContextMenuModel) => {
    this.handleItemClicked(oContextMenuModel.id);
  };

  handleCommentChange(oEvent){
    var oDom = oEvent.target;
    var sNewValue = !CS.isEmpty(oDom) ? oDom.value : "";
    EventBus.dispatch(oEvents.HANDLE_COMMENT_CHANGE, this, sNewValue);
  };

  getToolbarView () {
    var oButtonLabelStyles = {
      fontSize: 11,
      lineHeight: "26px",
      textTransform: "uppercase",
      padding: "0 10px"
    };
    let _this = this;
    let aButtonsView = [];
    let oButtonModels = this.props.toolbarItems;

    if(!CS.isEmpty(oButtonModels.moreButtons)) {
      aButtonsView.push(
              <div className="toolbarButton rightMargin" key="moreOptions">
                  <ContextMenuView showSearch={false}
                                   contextMenuViewModel={oButtonModels.moreButtons}
                                   anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
                                   targetOrigin={{horizontal: 'left', vertical: 'bottom'}}
                                   onClickHandler={_this.handleContextMenuItemClicked}
                                   showCustomIcon={true}>

                      <ButtonView id={'moreOptions'}
                                  showLabel={false}
                                  isDisabled={false}
                                  tooltip={getTranslation().MORE_OPTIONS}
                                  placement={"bottom"}
                                  className={"button moreOptions"}
                                  type={"simple"}
                                  theme={"light"}
                      />
                  </ContextMenuView>
              </div>
          );
    }

    let bIfSaveButtonExists = CS.find(oButtonModels.textButtons, {id: "save"}) ? true : false;
    CS.forEach(oButtonModels.textButtons, function (oMaterialButton) {
      let bIsButtonVisible = true;
      if(oMaterialButton.id == "complete" &&  bIfSaveButtonExists) {
        bIsButtonVisible = false;
      }
      if (CS.isEmpty(oMaterialButton.iconClassName) && bIsButtonVisible) {
        aButtonsView.push(
            <div className="toolbarButton smallRightMargin noBorder" key={oMaterialButton.id}>
              <CustomMaterialButtonView
                  label={CS.getLabelOrCode(oMaterialButton)}
                  isRaisedButton={true}
                  isDisabled={false}
                  labelStyle={oButtonLabelStyles}
                  onButtonClick={_this.handleItemClicked.bind(_this, oMaterialButton.id)}/>
            </div>
        );
      }
    });

    let bIsSaveCommentDisabled = this.props.isSaveCommentDisabled;
    let bShowSaveCommentButton = false;

    CS.forEach(oButtonModels.textButtons, function (oMaterialButton) {
      let aAllowedButtonId = ["saveAndPublish", "saveWithWarning", "saveCollection"];
      if (CS.includes(aAllowedButtonId, oMaterialButton.id) && !bIsSaveCommentDisabled){
        bShowSaveCommentButton = true;
      } else if (!CS.isEmpty(oMaterialButton.iconClassName)) {
        aButtonsView.push(<ButtonView id={oMaterialButton.id}
                                      showLabel={false}
                                      isDisabled={false}
                                      tooltip={oMaterialButton.label}
                                      className={"button " + oMaterialButton.iconClassName}
                                      type={"simple"}
                                      theme={"light"}
                                      clickHandler={_this.handleItemClicked}/>);

      }
    });

    if (bShowSaveCommentButton) {
      aButtonsView.push(
          <div className="toolbarButton smallRightMargin" key="saveComment">
            <input type="text"
                   className="toolbarCommentInputBox"
                   onBlur={_this.handleCommentChange}
                   placeholder={getTranslation().PLEASE_ENTER_SAVE_COMMENT + "..."}/>
          </div>
      );
    }

    return aButtonsView;
  }


  render () {
    let oToolbarView = this.getToolbarView();
    return (
        <div className="contentEditToolbarViewContainer">
          {oToolbarView}
        </div>
    );
  };
}

ContentEditToolbarView.propTypes = oPropTypes;

export const view = ContentEditToolbarView;
export const events = oEvents;
