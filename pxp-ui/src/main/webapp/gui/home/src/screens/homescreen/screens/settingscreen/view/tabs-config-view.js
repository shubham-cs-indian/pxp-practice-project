import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import {view as DragDropContextView} from "../../../../../viewlibraries/draggableDroppableView/drag-drop-context-view";

const oEvents = {
  HANDLE_DIALOG_BUTTON_CLICKED: "handle_dialog_button_clicked",
  TABS_CONFIG_VIEW_LIST_ITEM_SHUFFLED:"tabs_config_view_list_item_shuffled"
};

const oPropTypes = {
  gridViewProps: ReactPropTypes.object.isRequired,
  activeTabDetails: ReactPropTypes.object,
  referencedProperties: ReactPropTypes.object,
  ManageEntityDialog: ReactPropTypes.object,
  selectIconDialog: ReactPropTypes.object,
};

// @CS.SafeComponent
class TabsConfigView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleDialogButtonClicked = (sContext) => {
    EventBus.dispatch(oEvents.HANDLE_DIALOG_BUTTON_CLICKED, sContext);
  };

  onDragEnd = (oSource, oDestination) => {
    let iOldIndex = oSource.index;
    let iNewIndex = oDestination.index;
    if (iOldIndex != iNewIndex) {
      let oActiveTabDetails = this.props.activeTabDetails;
      let aPropertyCollectionList = oActiveTabDetails.propertySequenceList;
      let oChangedPC = aPropertyCollectionList[iOldIndex];
      EventBus.dispatch(oEvents.TABS_CONFIG_VIEW_LIST_ITEM_SHUFFLED, oChangedPC.id, iNewIndex);
    }
  };


  getTabDetailsView = ()=> {
    let __props = this.props;
    let oActiveTabDetails = __props.activeTabDetails;
    let fButtonHandler = this.handleDialogButtonClicked;
    if (CS.isEmpty(oActiveTabDetails) || oActiveTabDetails.isCreated) {
      return null;
    }

    let sortListItems = oActiveTabDetails.propertySequenceList;
    let aMovableList = [];
    CS.forEach(sortListItems, function (listItem) {
      let oListItemData = (
          <div className="tabSortViewWrapper">
            <div className={"itemTypeIcon " + listItem.type}></div>
            <div className="tabSortLabel">
              {listItem.label}
            </div>
          </div>
      );
      aMovableList.push({
        id: listItem.id,
        label: oListItemData,
        className: "tabSortViewContainer"
      });
    });

    let oListData = {
      droppableId: "tabSortableList",
      items: aMovableList
    };

    let oPropertySequenceList;
    if (CS.isEmpty(oActiveTabDetails.propertySequenceList)) {
      oPropertySequenceList = (<div className="nothingFoundMessage">{getTranslation().NOTHING_FOUND}</div>);
    } else {
      oPropertySequenceList = (
          <DragDropContextView
              listData={[oListData]}
              context={"tabSortableList"}
              onDragEnd={this.onDragEnd}
              showDraggableIcon={true}/>
      );
    }
    var aButtonData = [
      {
        id: "ok",
        label: getTranslation().OK,
        isDisabled: false,
        isFlat: false,
      }
    ];
    return (<CustomDialogView modal={true} open={true}
                              title={oActiveTabDetails.label}
                              bodyClassName=""
                              contentClassName="tabDetailsDialog"
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler}>
      <div className="tabPropertySequenceListContainer">
        {oPropertySequenceList}
      </div>

    </CustomDialogView>);
  };

  render () {
    let __props = this.props;
    let oTabDetailsView = this.getTabDetailsView();
    let oManageEntityDialog = this.props.ManageEntityDialog;
    let oColumnOrganizerData = this.props.columnOrganizerData;

    return (
        <div className="configGridViewContainer" key="tabsConfigGridViewContainer">
          <ContextualGridView {...__props.gridViewProps}
                    selectedColumns={oColumnOrganizerData.selectedColumns}
                    isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}/>
          {__props.children}
          {oTabDetailsView}
          {oManageEntityDialog}
        </div>
    )
  }

}

TabsConfigView.propTypes = oPropTypes;


export const view = TabsConfigView;
export const events = oEvents;
export const propTypes = TabsConfigView.propTypes;
