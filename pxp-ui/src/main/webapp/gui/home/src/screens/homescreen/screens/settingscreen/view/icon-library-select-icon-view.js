import React from "react";
import ReactPropTypes from "prop-types";
import CS from "../../../../../libraries/cs";
import {view as CustomDialogView} from "../../../../../viewlibraries/customdialogview/custom-dialog-view";
import {getTranslations as getTranslation} from "../../../../../commonmodule/store/helper/translation-manager";
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import {view as IconLibraryView} from "./icon-library-view";

const oEvents = {
  SELECT_ICON_SELECT_BUTTON_CLICKED: "select_icon_select_button_clicked",
  SELECT_ICON_CANCEL_BUTTON_CLICKED: "select_icon_cancel_button_clicked",
  SELECT_ICON_ELEMENT_SELECTED: "select_icon_element_selected",
  GRID_SELECT_ICON_SELECT_BUTTON_CLICKED: "grid_select_icon_select_button_clicked"
};

const oPropTypes = {
  selectIconData: ReactPropTypes.object,
  context: ReactPropTypes.string,
  isGridView: ReactPropTypes.bool,
  gridViewData: ReactPropTypes.object,
};

class IconLibrarySelectIconView extends React.Component {

  handleButtonClicked = (sButtonId, sIconId) => {
    if (sButtonId == "cancel") {
      EventBus.dispatch(oEvents.SELECT_ICON_CANCEL_BUTTON_CLICKED, this.props.context, this.props.isGridView);
    }
    else if (sButtonId == "ok") {
      let oSelectIconData = this.props.selectIconData;
      let sIconId = oSelectIconData.selectedIconIds[0];
      let oSelectedIconDetails = CS.find(oSelectIconData.icons, {id: sIconId});
      if(this.props.isGridView){
        EventBus.dispatch(oEvents.GRID_SELECT_ICON_SELECT_BUTTON_CLICKED, oSelectedIconDetails, this.props.gridViewData);
      }
      else{
        EventBus.dispatch(oEvents.SELECT_ICON_SELECT_BUTTON_CLICKED, oSelectedIconDetails, this.props.context);
      }
    }
  };

  handleIconElementSelected = (oIconDetails, oEvent) => {
    oEvent.nativeEvent.dontRaise = true;
    EventBus.dispatch(oEvents.SELECT_ICON_ELEMENT_SELECTED, oIconDetails.id, this.props.context);
  };

  render () {
    let oSelectIconData = this.props.selectIconData;
    let sTitle = 'Select Icon';
    let oBodyStyle = {
      maxWidth: "680px",
      minWidth: "400px",
      width: "100%",
      height: "100%",
      maxHeight: "445px",
      overflow: "hidden",
      padding:0
    };
    let aButtonData = [];

    if (CS.isNotEmpty(oSelectIconData.selectedIconIds)) {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslation().CANCEL,
          isFlat: true
        },
        {
          id: "ok",
          label: getTranslation().SELECT,
          isFlat: false
        }];
    }
    else {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslation().OK,
          isFlat: false
        }];
    }


    let fButtonHandler = this.handleButtonClicked;
    return (
        <CustomDialogView modal={true} open={true}
                          title={sTitle}
                          bodyStyle={oBodyStyle}
                          contentStyle={oBodyStyle}
                          bodyClassName=""
                          contentClassName="selectIconViewWrapper"
                          buttonData={aButtonData}
                          onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                          buttonClickHandler={fButtonHandler}>
          <IconLibraryView
                iconLibraryData={oSelectIconData}
                hideActionsOnElements={true}
                onSelectHandler={this.handleIconElementSelected}
                dragAndDropViewRequired={false}
                isCopyCodeRequired={false}
                nothingToDisplayMessage={"No Icons Uploaded"}>
          </IconLibraryView>
        </CustomDialogView>
    );
  }
}

export const view = IconLibrarySelectIconView;
export const events = oEvents;
