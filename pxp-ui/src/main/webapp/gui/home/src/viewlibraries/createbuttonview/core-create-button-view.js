import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import { view as ContextMenuView } from '../contextmenuwithsearchview/context-menu-view-new';

const oEvents = {
  CONTENT_CREATE_BUTTON_VIEW_ITEM_CLICKED: "content_create_button_view_item_clicked",
  CREATE_BUTTON_CLICKED: "create_button_clicked"
};

const oPropTypes = {
  context: ReactPropTypes.string,
  placeholder: ReactPropTypes.string,
  itemList: ReactPropTypes.array,
  onClickHandler: ReactPropTypes.func,
  buttonType: ReactPropTypes.string,
  dragDropContext: ReactPropTypes.object,
  filterContext: ReactPropTypes.object,
  searchText: ReactPropTypes.string
};

class CoreCreateButtonView extends React.Component {

  static propTypes = oPropTypes;

  constructor (props) {
    super(props);
    this.state = {
      searchText: ""
    }
  }

  handleOnItemClicked = (oItem) => {
    if (CS.isFunction(this.props.onClickHandler)) {
      this.props.onClickHandler(oItem);
    } else {
      EventBus.dispatch(oEvents.CONTENT_CREATE_BUTTON_VIEW_ITEM_CLICKED, this, this.props.context, oItem);
    }
  };

  // Commented the code to stop event to be dispatched after clicking on create button.
  handleCreateButtonClicked = () => {
    //EventBus.dispatch(oEvents.CREATE_BUTTON_CLICKED, "", this.props.dragDropContext, this.props.filterContext, false);
  };

  handleLoadMoreClicked = () => {
    EventBus.dispatch(oEvents.CREATE_BUTTON_CLICKED, this.state.searchText, this.props.dragDropContext, this.props.filterContext, true);
  };

  handleSearchTextChanged = (sSearchText) => {
    this.setState({searchText: sSearchText});
    EventBus.dispatch(oEvents.CREATE_BUTTON_CLICKED, sSearchText, this.props.dragDropContext, this.props.filterContext, false);
  };

  getFlatCreateButton = (bShowButtonImage, fClickHandler) => {
    let _props = this.props;
    let oButtonImageDom = bShowButtonImage ? (<div className="buttonImage"/>) : null;
    let fOnClickHandler = fClickHandler ? fClickHandler : this.handleCreateButtonClicked;
    let sPlaceHolder = _props.placeholder || getTranslation().CREATE;
    return (
        <div className="createButtonView" onClick={fOnClickHandler} title={sPlaceHolder}>
          <div className="buttonText">{sPlaceHolder}</div>
          {oButtonImageDom}
        </div>
    );
  };

  getCreateButtonMenuView = () => {
    let _props = this.props;
    let sContext = _props.context;
    return (
        <ContextMenuView
            anchorOrigin={{horizontal: 'left', vertical: 'bottom'}}
            targetOrigin={{horizontal: 'left', vertical: 'top'}}
            contextMenuViewModel={_props.itemList}
            onClickHandler={this.handleOnItemClicked}
            searchHandler={sContext == "contentTile" ? this.handleSearchTextChanged : null}
            loadMoreHandler={sContext == "contentTile" ? this.handleLoadMoreClicked : null}
            searchText={this.props.searchText}
            clearSearchOnPopoverClose={true}
        >
          {this.getFlatCreateButton(true, null)}
        </ContextMenuView>);
  };

  render () {
    let oCreateButtonDOM = this.getCreateButtonMenuView();
    return (
        <div className="createButtonViewWrapper">
          {oCreateButtonDOM}
        </div>
    );
  }
}

export default {
  view: CoreCreateButtonView,
  events: oEvents,
  propTypes: oPropTypes
}