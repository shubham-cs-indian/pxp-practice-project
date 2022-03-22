import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CustomPopoverView } from '../../viewlibraries/customPopoverView/custom-popover-view';
import { view as NumberLocaleView } from '../../viewlibraries/numberlocaleview/number-locale-view.js';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import Constants from '../../commonmodule/tack/constants';
const availablePageSizes = [20, 40, 60]; //todo: hardcode alert

const oEvents = {
};

const oPropTypes = {
  from: ReactPropTypes.number,
  pageSize: ReactPropTypes.number,
  totalItems: ReactPropTypes.number,
  currentPageItems: ReactPropTypes.number,
  onChange: ReactPropTypes.func, /** this will return 'from' and 'pageSize' on any change in the pagination view */
  isMiniPaginationView: ReactPropTypes.bool,
  filterContext: ReactPropTypes.object,
  displayTheme: ReactPropTypes.string,
  pageSizes: ReactPropTypes.array,
  isMiniPaginationWithNextPreviousButtons: ReactPropTypes.bool,
};
/**
 * @class PaginationView - use for Display Pagination for Application.
 * @memberOf Views
 * @property {number} [from] - this prop is pass number of from items.
 * @property {number} [pageSize] - this prop is pass number of pageSize.
 * @property {number} [totalItems] - this prop is pass number of totalItems.
 * @property {number} [currentPageItems] - this prop is pass number of currentPageItems.
 * @property {func} [onChange] -  function which is used for onChange Pagination data event, this will return 'from' and 'pageSize' on any change in the pagination view .
 * @property {bool} [isMiniPaginationView] -  boolean value for isMiniPaginationView or not.
 * @property {string} [displayTheme] -  string of displayTheme.
 */

var oDarkTheme = {
  palette: {
    canvasColor: '#4a4a4a'
  }
};

// @CS.SafeComponent
class PaginationView extends React.Component{

  constructor(props) {
    super(props);

    this.state = {
      from: 0,
      pageSize: 20,
      totalItems: 0,
      currentPageItems: 20,
      showPageSizeList: false,
      isGoToPageOpen: false
    }
  }

  /*componentWillMount =()=> {
    this.setState({
      showPageSizeList: false
    });
  }*/

/*
  handleOnChange = (iFrom, iSize, oEvent) => {
    this.props.onChange({
      from: iFrom,
      pageSize: iSize
    }, oEvent);
  };
*/

  handlePageSizeChanged =(iNewPageSize, oEvent)=> {
    var iFrom = this.props.from;
    iFrom = Math.floor(iFrom/iNewPageSize) * iNewPageSize;
    var bCurrentShowPageSizeList = this.state.showPageSizeList;
    this.setState({
      showPageSizeList: !bCurrentShowPageSizeList,
    });

    let oPaginationData = {
      from: iFrom,
      pageSize: iNewPageSize
    };
    this.props.onChange(oPaginationData, this.props.filterContext, oEvent);
  }

  handlePageChanged =(iNewPageNumber, oEvent)=> {
    if (iNewPageNumber > 0) {
      iNewPageNumber--;
    }
    var iNewFrom = iNewPageNumber * this.props.pageSize;

    let oPaginationData = {
      from: iNewFrom,
      pageSize: this.props.pageSize
    };
    this.props.onChange(oPaginationData, this.props.filterContext, oEvent);
  }

  handleTogglePageSizeList =(oEvent)=> {
    var bCurrentShowPageSizeList = this.state.showPageSizeList;
    this.setState({
      showPageSizeList: !bCurrentShowPageSizeList,
      moreView: oEvent.currentTarget
    });
  }

  handleGoToPagePopOverStart = (event) => {
    this.setState({
      isGoToPageOpen: true,
      anchorEl: event.currentTarget,
    });
  };

  handleCloseMorePopoverButtonClicked =()=> {
    var bCurrentShowPageSizeList = this.state.showPageSizeList;
    this.setState({
      showPageSizeList: !bCurrentShowPageSizeList,
    });
  }

  handleGoToPagePopOverClose = () => {
    this.setState({
      isGoToPageOpen: false
    });
  };

  getPageSizeSelectorDOM =()=> {
    var _this = this;
    var oProps = this.props;
    var oState = this.state;
    var iCurrentPageSize = oProps.pageSize;
    var sSelectedPageSizeLabel = getTranslation().SHOW + " " + iCurrentPageSize + " " + getTranslation().ITEMS;
    var aPageSizeListItems = [];
    var sDisplayTheme = "";
    let aPageSizes = oProps.pageSizes || availablePageSizes;

    switch (oProps.displayTheme) {
      case Constants.DARK_THEME:
        sDisplayTheme = "darkTheme";
        break;
    }

    CS.forEach(aPageSizes, function (iPageSize) {
      var sLabel = getTranslation().SHOW + " " + iPageSize + " " + getTranslation().ITEMS;
      if(iPageSize !== iCurrentPageSize) {
        aPageSizeListItems.push(
            <div key={iPageSize} className={"pageSizeListItem " + sDisplayTheme}
                 onClick={_this.handlePageSizeChanged.bind(_this, iPageSize)}>{sLabel}</div>
        );
      }
    });

    return (
        <div className="pageSizeSelector">
          <div className="selectedPageSize" onClick={this.handleTogglePageSizeList}>{sSelectedPageSizeLabel}</div>
          <CustomPopoverView
              className="popover-root"
              open={oState.showPageSizeList}
              anchorEl={oState.moreView}
              anchorOrigin={{horizontal: 'left', vertical: 'top'}}
              transformOrigin={{horizontal: 'left', vertical: 'bottom'}}
              onClose={this.handleCloseMorePopoverButtonClicked}>
            <div className={"pageSizeList " + sDisplayTheme}>
              {aPageSizeListItems}
            </div>
          </CustomPopoverView>
        </div>
    );
  }

  getPageNavigationForMiniPagination =()=> {
    var iTotalItems = this.props.totalItems;
    var iPageSize = this.props.pageSize;
    var iFrom = this.props.from;
    let bIsMiniPaginationWithNextPreviousButtons = this.props.isMiniPaginationWithNextPreviousButtons;
    var iTotalPages = (iTotalItems % iPageSize) ? Math.floor(iTotalItems/iPageSize + 1) : Math.floor(iTotalItems/iPageSize);
    var iCurrentPage = Math.floor(iFrom/iPageSize + 1);
    var fPreviousButtonHandler = null;
    var fNextButtonHandler = null;
    var sPreviousButtonClass = "pageButton previous ";
    var sNextButtonClass = "pageButton next ";
    var oBackupDOM = null;
    let sNextButtonLabel = "";
    let sPreviousButtonLabel = "";

    if (bIsMiniPaginationWithNextPreviousButtons) {
      sNextButtonLabel = getTranslation().NEXT;
      sPreviousButtonLabel = getTranslation().PREVIOUS;
      sNextButtonClass = "pageButton withLabelNext ";
      sPreviousButtonClass = "pageButton withLabelPrevious ";
    }
    if (iTotalPages === 1) {
      return oBackupDOM;
    }
    if (iCurrentPage > 1) {
      fPreviousButtonHandler = this.handlePageChanged.bind(this, (iCurrentPage - 1));
    } else {
      sPreviousButtonClass += "disabled";
    }

    if (iCurrentPage < iTotalPages) {
      fNextButtonHandler = this.handlePageChanged.bind(this, (iCurrentPage + 1));
    } else {
      sNextButtonClass += "disabled";
    }

    return (
        <div className="pageNavigationSection miniPagination">
          <div className={sPreviousButtonClass} onClick={fPreviousButtonHandler}>{sPreviousButtonLabel}</div>
          <div className={sNextButtonClass} onClick={fNextButtonHandler}>{sNextButtonLabel}</div>
        </div>
    );
  }

  handleOnKeyPress = (sValue, oEvent) => {
    let iPageNumber = sValue;
    let iTotalItems = this.props.totalItems;
    let iPageSize = this.props.pageSize;
    let iTotalPages = (iTotalItems % iPageSize) ? Math.floor(iTotalItems / iPageSize + 1) : Math.floor(iTotalItems / iPageSize);
    if (iPageNumber > iTotalPages) {
      iPageNumber = iTotalPages;
    } else if (iPageNumber <= 0) {
      iPageNumber = 1;
    }
    this.handlePageChanged(iPageNumber, oEvent);
    this.handleGoToPagePopOverClose();
  };

  getGoToPopoverDOM = () => {
    let oProps = this.props;
    let sDisplayTheme = "";

    switch (oProps.displayTheme) {
      case Constants.DARK_THEME:
        sDisplayTheme = "darkTheme";
        break;
    }

    let sLabel = getTranslation().GO_TO + ":";

    return (
        <CustomPopoverView
            className="popover-root"
            open={this.state.isGoToPageOpen}
            anchorEl={this.state.anchorEl}
            anchorOrigin={{horizontal: 'left', vertical: 'top'}}
            transformOrigin={{horizontal: 'left', vertical: 'bottom'}}
            onClose={this.handleGoToPagePopOverClose}>
          <div className={"goToPageContainer " + sDisplayTheme}>
            <div className={"goToPage"}>{sLabel}</div>
            <NumberLocaleView onBlur={this.handleOnKeyPress} handleEnterKeyPress={true} isAutoFocus={true} isOnlyInteger={true}/>
          </div>
        </CustomPopoverView>
    );
  }

  getPageNavigationSectionDOM =()=> {
    var _this = this;
    var iTotalItems = this.props.totalItems;
    var iPageSize = this.props.pageSize;
    var iFrom = this.props.from;
    var iTotalPages = (iTotalItems % iPageSize) ? Math.floor(iTotalItems/iPageSize + 1) : Math.floor(iTotalItems/iPageSize);
    var iCurrentPage = Math.floor(iFrom/iPageSize + 1);

    var aPageButtons = [];
    var iHiddenPageCount = 0;
    var oBackupDOM = null;

    if (iTotalPages === 1) {
      return oBackupDOM;
    }
    CS.times(iTotalPages, function (iIndex) {
      var iPageNumber = iIndex + 1;
      var sPageButtonClass = "pageButton ";
      if (
          (iPageNumber === 1) || //first page
          (iPageNumber === iTotalPages) || //last page
          (iPageNumber === iCurrentPage) || //current page
          (iPageNumber === (iCurrentPage - 1)) || //previous page
          (iPageNumber === (iCurrentPage + 1)) //next page
      ) {
        if (iHiddenPageCount > 1) { //if more than 1 hidden pages, show ...
          aPageButtons.push(
                <div key={"dotsSection" + iPageNumber} className={sPageButtonClass + "dots "} onClick={_this.handleGoToPagePopOverStart}>···</div>
          );
        } else if (iHiddenPageCount === 1) { //no need to show ... if only a single page is being hidden
          aPageButtons.push(oBackupDOM);
        }
        iHiddenPageCount = 0;
        if (iCurrentPage === iPageNumber) {
          sPageButtonClass += "selected ";
        }
        aPageButtons.push(
            <div key={iPageNumber} className={sPageButtonClass}
                 onClick={_this.handlePageChanged.bind(_this, iPageNumber)}>{iPageNumber}</div>
        );
      }
      else {
        iHiddenPageCount++;
        oBackupDOM = (<div key={iPageNumber} className={sPageButtonClass}
                           onClick={_this.handlePageChanged.bind(_this, iPageNumber)}>{iPageNumber}</div>);
      }
    });

    var fPreviousButtonHandler = null;
    var fNextButtonHandler = null;
    var sPreviousButtonClass = "pageButton previous ";
    var sNextButtonClass = "pageButton next ";

    if (iCurrentPage > 1) {
      fPreviousButtonHandler = this.handlePageChanged.bind(this, (iCurrentPage - 1));
    } else {
      sPreviousButtonClass += "disabled";
    }

    if (iCurrentPage < iTotalPages) {
      fNextButtonHandler = this.handlePageChanged.bind(this, (iCurrentPage + 1));
    } else {
      sNextButtonClass += "disabled";
    }

    return (
        <div className="pageNavigationSection">
          <div className={sPreviousButtonClass} onClick={fPreviousButtonHandler}>{getTranslation().PREVIOUS}</div>
          <div className="pageListContainer">
            {aPageButtons}
          </div>
          <div className={sNextButtonClass} onClick={fNextButtonHandler}>{getTranslation().NEXT}</div>
        </div>
    );
  }

  render() {

    if(!this.props.totalItems){
      return null;
    }

    let oPageSizeSelectorDOM = (this.props.isMiniPaginationView) ? null : this.getPageSizeSelectorDOM();
    let oPageNavigationSectionDOM = (this.props.isMiniPaginationView) ? this.getPageNavigationForMiniPagination() : this.getPageNavigationSectionDOM();
    let oGoToPopoverDOM = (this.props.isMiniPaginationView) ? null : this.getGoToPopoverDOM();
    let sFromToInfo = "";
    if (this.props.currentPageItems) {
      sFromToInfo = (this.props.from + 1) + " - " + (this.props.from + this.props.currentPageItems);
    } else {
      sFromToInfo = "0"; //show 0, if there are no items being shown on the current page
    }
    let sCurrentPageInformation = sFromToInfo + " " + getTranslation().OF + " " + (this.props.totalItems) + " " + getTranslation().ITEMS;


    return (
        <div className="paginationView">

          {/*current page information*/}
          <div className="currentPageInformation">{sCurrentPageInformation}</div>

          {/*page size selector*/}
          {oPageSizeSelectorDOM}

          {/*GOTO Page POPOVER*/}
          {oGoToPopoverDOM}

          {/*page navigation section*/}
          {oPageNavigationSectionDOM}

        </div>
    );
  }

}

PaginationView.propTypes = oPropTypes;

PaginationView.defaultProps = {
  from: 0,
  pageSize: 20,
  totalItems: 0,
  currentPageItems: 20
}

export const view = PaginationView;
export const events = oEvents;
