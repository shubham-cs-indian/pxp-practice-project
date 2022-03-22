import React from "react";
import { makeStyles } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';

// const useStyles = makeStyles({
//     root: {
//         width: '100%',
//     },
//     tableWrapper: {
//         maxHeight: "100%",
//         overflow: 'auto',
//     },
// });

const GenericTableView = (oProps) => {

    const getHeaderView = headerData => {
        return <TableRow className="tableRow">
            {headerData.map(cellData => {
                return <TableCell className="tableCell" key={cellData.id}>
                    <div className="headerCellContainer">
                        <div className="headerCellLabel">{cellData.label}</div>
                        {cellData.actionView && <div className="headerCellAction">{cellData.actionView}</div>}
                    </div>
                </TableCell>
            })}
        </TableRow>
    }

    const getBodyView = bodyData => {
        return bodyData.map(row => {
            return <TableRow className="tableRow" key={row.id}>
                {row.cells.map(cellData => {
                    return <TableCell className="tableCell" key={cellData.id}>
                        <div className="bodyCellContainer">
                            <div className="bodyCellLabel">{cellData.cellView}</div>
                            {cellData.actionView && <div className="bodyCellAction">{cellData.actionView}</div>}
                        </div>
                    </TableCell>
                })}
            </TableRow>
        })
    }

    const { headerData, bodyData } = oProps;
    //const classes = useStyles();

    return (<div className="genericTable">
        <Table aria-label="sticky table" className="tableContainer" style={{tableLayout: 'auto'}}>
            <TableHead className="tableHead">
                {getHeaderView(headerData)}
            </TableHead>
            <TableBody className="tableBody">
              {getBodyView(bodyData)}
            </TableBody>
        </Table>
    </div>)

}

export default GenericTableView;