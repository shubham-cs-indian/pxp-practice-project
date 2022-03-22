import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher.js';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import { view as ListView } from '../../../../../viewlibraries/listview/list-view.js';
import { view as RelationshipDetailsView } from './relationship-details-view';
import ConfigRelationshipViewModel from './model/config-relationship-view-model.js';
import { view as CustomDialogView } from '../../../../../viewlibraries/customdialogview/custom-dialog-view';
import { view as ContextualGridView } from '../../../../../viewlibraries/contextualgridview/contextual-grid-view';

const oEvents = {
  HANDLE_RELATIONSHIP_CONFIG_DIALOG_BUTTON_CLICKED: "handle relationship config dialog button clicked",
};

const oPropTypes = {
  gridViewProps: ReactPropTypes.object,
  activeRelationship: ReactPropTypes.object,
  actionItemModel: ReactPropTypes.object.isRequired,
  relationshipListModel: ReactPropTypes.array,
  selectedRelationshipDetailedModel: ReactPropTypes.instanceOf(ConfigRelationshipViewModel).isRequired,
  bRelationshipCreated: ReactPropTypes.bool,
  relationshipContextList: ReactPropTypes.object,
  isRelationshipDirty: ReactPropTypes.bool,
  isRelationshipDummy: ReactPropTypes.bool,
  disabledFields: ReactPropTypes.array,
  relationshipConfigDetails: ReactPropTypes.object,
  dialogLabel: ReactPropTypes.string,
  iconLibraryData: ReactPropTypes.object,
  selectIconDialog: ReactPropTypes.object,
};

// @CS.SafeComponent
class RelationshipConfigView extends React.Component {
  static propTypes = oPropTypes;

  handleDialogButtonClicked = (sButtonId) => {
    EventBus.dispatch(oEvents.HANDLE_RELATIONSHIP_CONFIG_DIALOG_BUTTON_CLICKED, sButtonId);
  };

  getDetailViewDialog = () => {
    let oProps = this.props;
    if (!oProps.isDialogOpen) {
      return null;
    }

    let oBodyStyle = {
      maxWidth: "1200px",
      minWidth: "800px",
      padding: 0,
      overflowY: "auto",
      height: '70%'
    };

    let aButtonData = [];

    if (this.props.isRelationshipDirty) {
      aButtonData = [
        {
          id: "cancel",
          label: getTranslation().DISCARD,
          isDisabled: false,
          isFlat: false,
        },
        {
          id: "save",
          label: getTranslation().SAVE,
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
    let sContext = "relationshipConfigDetail";
    let oRelationshipDetailedViewModel = this.props.selectedRelationshipDetailedModel;
    let fButtonHandler = this.handleDialogButtonClicked;

    let oRelationshipDetailView = (
        <div className="relationshipDetailedViewContainer">
          <div className="relDetailedView">
            <RelationshipDetailsView
                activeRelationship={oProps.activeRelationship}
                context={sContext}
                selectedRelationshipDetailedModel={oRelationshipDetailedViewModel}
                relationshipContextList={this.props.relationshipContextList}
                isRelationshipDirty={this.props.isRelationshipDirty}
                disabledFields={this.props.disabledFields}
                relationshipConfigDetails={this.props.relationshipConfigDetails}
                iconLibraryData={this.props.iconLibraryData}
            />
          </div>
        </div>
    )

    return (<CustomDialogView modal={true} open={true}
                              title={getTranslation()[oProps.dialogLabel]}
                              bodyStyle={oBodyStyle}
                              contentStyle={oBodyStyle}
                              bodyClassName="relationshipDialogBody"
                              contentClassName="relationshipConfigDetail"
                              buttonData={aButtonData}
                              onRequestClose={fButtonHandler.bind(this, aButtonData[0].id)}
                              buttonClickHandler={fButtonHandler}
                              children={oRelationshipDetailView}>
    </CustomDialogView>);
  };

  render() {
    let oDetailViewDialog = this.getDetailViewDialog();
    let oManageEntityDialog = this.props.ManageEntityDialog;
    let oColumnOrganizerData = this.props.columnOrganizerData;
    let oSelectIconDialog = this.props.selectIconDialog;

    return (
        <div className="relationshipConfigViewContainer">

          <div className="configGridViewContainer" key="relationshipConfigGridViewContainer">
            <ContextualGridView
                {...this.props.gridViewProps}
                selectedColumns={oColumnOrganizerData.selectedColumns}
                isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
            />
            {oDetailViewDialog}
            {oManageEntityDialog}
            {oSelectIconDialog}
          </div>
        </div>
    );
  }
}

export const view = RelationshipConfigView;
export const events = oEvents;
