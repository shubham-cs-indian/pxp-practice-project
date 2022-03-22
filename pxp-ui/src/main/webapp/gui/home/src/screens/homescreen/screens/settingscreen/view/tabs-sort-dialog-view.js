import ReactPropTypes from "prop-types";
import CS from "../../../../../libraries/cs";
import React from "react";
import EventBus from "../../../../../libraries/eventdispatcher/EventDispatcher";
import {getTranslations as getTranslation} from "../../../../../commonmodule/store/helper/translation-manager";
import {view as CustomDialogView} from "../../../../../viewlibraries/customdialogview/custom-dialog-view";
import {view as DragDropContextView} from "../../../../../viewlibraries/draggableDroppableView/drag-drop-context-view";
import { view as ImageFitToContainerView } from '../../../../../viewlibraries/imagefittocontainerview/image-fit-to-container-view';
import ViewLibraryUtils from "../../../../../viewlibraries/utils/view-library-utils";

const oEvents = {
    HANDLE_SORT_DIALOG_BUTTON_CLICKED: "handle_sort_dialog_button_clicked",
    TABS_SORT_VIEW_LIST_ITEM_SHUFFLED:"tabs_sort_view_list_item_shuffled"
};

const oPropTypes = {
    sortListItems: ReactPropTypes.array.isRequired,
};

// @CS.SafeComponent
class TabsSortDialogView extends React.Component {
    static propTypes = oPropTypes;

    handleSortDialogButtonClicked = (sButtonId) => {
        EventBus.dispatch(oEvents.HANDLE_SORT_DIALOG_BUTTON_CLICKED, sButtonId);
    };

  onDragEnd = (oSource, oDestination) => {
    let iOldIndex = oSource.index;
    let iNewIndex = oDestination.index;
    if (iOldIndex != iNewIndex) {
      let sortListItems = this.props.sortListItems;
      let oChangedPC = sortListItems[iOldIndex];
      EventBus.dispatch(oEvents.TABS_SORT_VIEW_LIST_ITEM_SHUFFLED, oChangedPC.id, iNewIndex, oChangedPC);
    }
  };

  getDialogView = () => {

    let sortListItems = this.props.sortListItems;
    let oPropertySequenceList;
    let fButtonHandler = this.handleSortDialogButtonClicked;

    let aMovableList = [];
    CS.forEach(sortListItems, function (listItem) {
      let sImageURL = ViewLibraryUtils.getIconUrl(listItem.iconKey);
      let oListItemData = (
          <div className="tabSortViewWrapper">
            <div className="itemTypeIcon">
              <ImageFitToContainerView imageSrc={sImageURL}></ImageFitToContainerView>
            </div>
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

    if (CS.isEmpty(sortListItems)) {
      oPropertySequenceList = (<div className="nothingFoundMessage">{getTranslation().NOTHING_FOUND}</div>);
    } else {
      oPropertySequenceList = (
            <DragDropContextView
                listData={[oListData]}
                context={"tabSortableList"}
                onDragEnd={this.onDragEnd}
                showDraggableIcon={false}/>
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
                                  title={getTranslation().REARRANGE_SEQUENCE}
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

    render() {
        return (this.getDialogView());
    }
}

export const view = TabsSortDialogView;
export const events = oEvents;
