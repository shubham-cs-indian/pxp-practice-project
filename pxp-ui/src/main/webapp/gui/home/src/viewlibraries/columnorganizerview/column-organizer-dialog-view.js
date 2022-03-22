import {getTranslations as getTranslation} from "../../commonmodule/store/helper/translation-manager";
import CS from "../../libraries/cs";
import React from "react";
import ReactPropTypes from "prop-types";
import {view as ColumnOrganizerView} from "./column-organizer-view";
import {view as CustomDialogView} from "../customdialogview/custom-dialog-view";
import EventBus from "../../libraries/eventdispatcher/EventDispatcher";
import ColumnOrganizerStore from "./column-organizer-store";


const oEvents = {
  COLUMN_ORGANIZER_DIALOG_BUTTON_CLICKED : "column_organizer_dialog_button_clicked"
};

const oPropTypes = {
  columns: ReactPropTypes.array,
  searchText: ReactPropTypes.string,
  searchBarPlaceHolder: ReactPropTypes.string,
  disableSearch: ReactPropTypes.bool,
  selectedColumns: ReactPropTypes.array,
  emptyMessage: ReactPropTypes.object,
  sequenceListLimit: ReactPropTypes.string,
  dialogButtonClickHandler: ReactPropTypes.func,
};

// @CS.SafeComponent
class ColumnOrganizerDialogView extends React.Component {
  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
    this.state = {
      store: {}
    };

    this.initializeStore();
  }

  initializeStore = () => {
    this.state.store = new ColumnOrganizerStore(this.props, this.stateChanged);
  };

  stateChanged = () => {
    this.setState({});
  };

  handleDialogButtonClicked = (sButtonId) => {
    if(CS.isFunction(this.props.dialogButtonClickHandler)){
      this.props.dialogButtonClickHandler(sButtonId);
    }
    else {
      this.state.store.handleColumnOrganizerDialogButtonClicked(sButtonId, this.props.tableContextId);
    }
    /** Get Contextual props or normal props **/
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);

    let aSelectedOrganizerColumns = oColumnOrganizerProps.getSelectedOrganizedColumns();
    EventBus.dispatch(oEvents.COLUMN_ORGANIZER_DIALOG_BUTTON_CLICKED, sButtonId, this.props.context, aSelectedOrganizerColumns);
  };

  getView = () => {
    let oProps = this.props;
    return <ColumnOrganizerView {...oProps} onShuffleCallback={this.stateChanged}
                                hideHeader={true}
                                store={this.state.store}/>
  };

  render () {
    let oColumnOrganizerProps = this.state.store.getColumnOrganizerProps(this.props.tableContextId);
    let oContentStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
      height: "80%",
      width: "75%",
      padding:0,
      overflow:"hidden"
    };
    let oBodyStyle = {
      padding:0,
      overflow:"hidden"
    };

    let fButtonHandler = this.handleDialogButtonClicked;
    let aButtonData = [];
    if (CS.isNotEmpty(oColumnOrganizerProps.getSelectedOrganizedColumns().clonedObject)) {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "save",
          label: getTranslation().APPLY,
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
    return(
      <CustomDialogView modal={true} open={true}
                        title={getTranslation().REARRANGE_COLUMN_SEQUENCE}
                        contentStyle={oContentStyle}
                        bodyStyle={oBodyStyle}
                        bodyClassName=""
                        contentClassName="organizeColumnDialog"
                        buttonData={aButtonData}
                        onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                        buttonClickHandler={fButtonHandler}>
        {this.getView()}
      </CustomDialogView>
    )
  }
}

export const view = ColumnOrganizerDialogView;
export const events = oEvents;
