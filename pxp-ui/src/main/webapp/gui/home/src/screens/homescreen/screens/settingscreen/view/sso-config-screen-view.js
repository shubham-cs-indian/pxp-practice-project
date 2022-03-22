import React from 'react';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import {view as ContextualGridView} from "../../../../../viewlibraries/contextualgridview/contextual-grid-view";

const oEvents = {
    HANDLE_SSO_SETTING_REFERESH : "handle_sso_setting_referesh"
};

const oPropTypes = {

};

// @CS.SafeComponent
class SSOConfigScreenView extends React.Component {
    static propTypes = oPropTypes;

    refreshSSOConfigScreen = () => {
        EventBus.dispatch(oEvents.HANDLE_SSO_SETTING_REFERESH);
    };

    render() {
        let oProps = this.props;
        let oColumnOrganizerData = this.props.columnOrganizerData;

        return (
            <ContextualGridView {...oProps}
                      refreshButtonClickedHandler={this.refreshSSOConfigScreen}
                      selectedColumns={oColumnOrganizerData.selectedColumns}
                      isColumnOrganizerDialogOpen={oColumnOrganizerData.isColumnOrganizerDialogOpen}
            />
        );
    }
}

export const view = SSOConfigScreenView;
export const events = oEvents;
