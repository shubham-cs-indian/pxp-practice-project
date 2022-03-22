import React from 'react';
import ReactPropTypes from 'prop-types';
import DashboardChartTypeDictionary from './../tack/dashboard-chart-type-dictionary';
import {Doughnut, Pie, Line, Bar} from 'react-chartjs-2';

let oEvents = {};

const oPropTypes = {
  chartType: ReactPropTypes.string,
  chartData: ReactPropTypes.object,
  chartOptions: ReactPropTypes.object,
};

// @CS.SafeComponent
class ChartJsChartView extends React.Component {
  static propTypes = oPropTypes;

  getDoughnutChart = () => {
    let data = {
      labels: [
        'Updated',
        'Pending'
      ],
      datasets: [{
        data: [20, 80],
        backgroundColor: [
          '#FF6384',
          '#36A2EB',
        ],
        hoverBackgroundColor: [
          '#FF6384',
          '#36A2EB',
        ]
      }]
    };
    let oOptions = {
      maintainAspectRatio: false,
    };

    return (<Doughnut data={data} options={oOptions}/>);
  };

  getPieChart = () => {
    let data = {
      labels: [
        'Supplier Staging',
        'Central Staging',
        'MDM'
      ],
      datasets: [{
        data: [40, 12, 48],
        backgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56'
        ],
        hoverBackgroundColor: [
          '#FF6384',
          '#36A2EB',
          '#FFCE56'
        ]
      }]
    };
    let oOptions = {
      maintainAspectRatio: false,
    };

    return (<Pie data={data} options={oOptions}/>);

  };

  getLineChart = () => {
    let data = {
      labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
      datasets: [
        {
          label: 'Monthly File Upload Trend',
          fill: false,
          lineTension: 0.1,
          backgroundColor: 'rgba(75,192,192,0.4)',
          borderColor: 'rgba(75,192,192,1)',
          borderCapStyle: 'butt',
          borderDash: [],
          borderDashOffset: 0.0,
          borderJoinStyle: 'miter',
          pointBorderColor: 'rgba(75,192,192,1)',
          pointBackgroundColor: '#fff',
          pointBorderWidth: 1,
          pointHoverRadius: 5,
          pointHoverBackgroundColor: 'rgba(75,192,192,1)',
          pointHoverBorderColor: 'rgba(220,220,220,1)',
          pointHoverBorderWidth: 2,
          pointRadius: 1,
          pointHitRadius: 10,
          data: [0, 152, 243, 164, 236, 178, 382, 257, 283, 183, 147, 249, 193]
        }
      ]
    };
    let oOptions = {
      maintainAspectRatio: false,
      scales: {
        yAxes: [{
          scaleLabel: {
            display: true,
            labelString: 'Number of Files Uploaded'
          }
        }],
        xAxes: [{
          scaleLabel: {
            display: true,
            labelString: 'Months'
          }
        }]
      }
    };

    return (<Line data={data} options={oOptions}/>);
  };

  getGroupedBarChart = () => {
    let data = this.props.chartData;
    let oOptions = this.props.chartOptions;

    return (<Bar data={data} options={oOptions}/>);
  };

  getStackedBarChart = () => {
    let data = {
      labels: ["January", "February", "March", "April", "May"],
      datasets: [{
        label: 'Food',
        backgroundColor: '#FF6384',
        data: [100, 125, 245, 147, 67]
      }, {
        label: 'Furniture',
        backgroundColor: '#36A2EB',
        data: [85, 156, 179, 211, 123]
      }, {
        label: 'Electronics',
        backgroundColor: '#FFCE56',
        data: [97, 87, 56, 267, 157]
      }]
    };
    let oOptions = {
      maintainAspectRatio: false,
      title: {
        display: true,
        text: "Category wise product split in stages"
      },
      tooltips: {
        mode: 'index',
        intersect: false
      },
      responsive: true,
      scales: {
        xAxes: [{
          stacked: true,
          label: 'Categories'
        }],
        yAxes: [{
          stacked: true,
          label: 'Number of products'
        }]
      }
    };

    return (<Bar data={data}
                 options={oOptions}/>);
  };

  getBarChart = () => {
    let data = {
      labels:  ['Inbox', 'No-Interest', 'Preparation', 'Active', 'Active-Blocked', 'Active-Banned', 'Retired', 'Archived', 'Deleted'],
      datasets: [{
        label: 'Overall Lifecycle Status',
        backgroundColor: '#FE5383',
        borderColor: '#FF6384',
        borderWidth: 1,
        data: [320, 200, 420, 240, 360, 100, 300, 650, 200]
      }]
    };
    let oOptions = {
      maintainAspectRatio: false,
      title: {
        display: true,
        text: "Overall Lifecycle Status"
      },
      tooltips: {
        mode: 'index',
        intersect: false
      },
      responsive: true,
      scales: {
        xAxes: [{
          label: 'Lifecycle'
        }],
        yAxes: [{
          label: 'Number of products'
        }]
      }
    };

    return (<Bar data={data} options={oOptions}/>);
  };

  render() {
    let oChart = null;

    //todo: remove default data

    switch (this.props.chartType) {
      case DashboardChartTypeDictionary.BAR:
        oChart = this.getBarChart();
        break;
      case DashboardChartTypeDictionary.DOUGHNUT:
        oChart = this.getDoughnutChart();
        break;
      case DashboardChartTypeDictionary.PIE:
        oChart = this.getPieChart();
        break;
      case DashboardChartTypeDictionary.STACKED_BAR:
        oChart = this.getStackedBarChart();
        break;
      case DashboardChartTypeDictionary.GROUPED_BAR:
        oChart = this.getGroupedBarChart();
        break;
      case DashboardChartTypeDictionary.LINE:
        oChart = this.getLineChart();
        break;
    }

    return (oChart);
  }
}

export const view = ChartJsChartView;
export const events = oEvents;
