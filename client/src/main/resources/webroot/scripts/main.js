$(document).ready(function() {
  var randomData;
  var eb = new vertx.EventBus('http://localhost:8080/eventbus');

  Highcharts.setOptions({
    global: {
      useUTC: false,
      getTimezoneOffset: function(timestamp) {
        return (-1 * moment.tz(timestamp, 'Europe/Amsterdam').utcOffset());
      }
    }
  });

  $('#random').highcharts({
    chart: {
      events: {
        load: function() {
          randomData = this.series[0];
        }
      }
    },
    series: [{
      name: 'Random data'
    }],
    title: {
      text: 'Random data'
    },
    xAxis: {
      type: 'datetime'
    },
    yAxis: {
      min: 0,
      max: 100
    }
  });

  var df = 'YYYY-MM-DD';
  var startDate = moment().subtract(6, 'months').format(df);

  var stockData = {
    PHG: [],
    AAPL: [],
    ASML: [],
    GOOGL: []
  };

  var stockOptions = {
    chart: {
      renderTo: 'stocks'
    },
    plotOptions: {
      series: {
        compare: 'percent',
        tooltip: {
          valuePrefix: '$',
          pointFormat: '<span style="color:{point.color}">\u25CF</span> {series.name}: <b>{point.y}</b> ({point.change}%)<br/>'
        }
      }
    },
    series: [{
      id: 'PHG',
      name: 'Koninklijke Philips N.V'
    }, {
      id: 'AAPL',
      name: 'Apple Inc.'
    }, {
      id: 'ASML',
      name: 'ASML Holding NV'
    }],
    title: {
      text: 'Stocks'
    },
    yAxis: {
      labels: {
        formatter: function() {
          return (this.value > 0 ? '+' : '') + this.value + '%';
        }
      }
    }
  };

  var stockChart;
  var stocksLoaded = false;

  $.ajax('http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.historicaldata%20where%20symbol%20in%20(%22PHG%22%2C%20%22AAPL%22%2C%20%22ASML%22)%20and%20startDate%20%3D%20%22' + startDate + '%22%20and%20endDate%20%3D%20%22' + moment().format(df) + '%22&format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys', {
    success: function(data) {
      _.forEach(data.query.results.quote, function(quote) {
        if (stockData[quote.Symbol]) {
          stockData[quote.Symbol].push([moment(quote.Date).valueOf(), parseFloat(/^(\d+\.\d{2})\d*$/.exec(quote.Close)[1])]);
        }
      });
      stockOptions.series[0].data = _.sortBy(stockData['PHG'], function(stock) {
        return moment(stock[0]).valueOf();
      });
      stockOptions.series[1].data = _.sortBy(stockData['AAPL'], function(stock) {
        return moment(stock[0]).valueOf();
      });
      stockOptions.series[2].data = _.sortBy(stockData['ASML'], function(stock) {
        return moment(stock[0]).valueOf();
      });

      stockChart = Highcharts.StockChart(stockOptions, function() {
        stocksLoaded = true;
      });
    }
  });

  var randomCounter = 0;

  eb.onopen = function() {
    eb.registerHandler('random', function(msg) {
      randomData.addPoint([msg.date, msg.value], true, randomCounter >= 20);
      if (randomCounter < 20) randomCounter++;
    });

    eb.registerHandler('stocks', function(msg) {
      var date = moment(msg.query.created).valueOf();
      _.forEach(msg.query.results.row, function(row) {
        if (stocksLoaded) {
          var stock = stockChart.get(row.symbol);
          if (stock) {
            stock.addPoint([date, parseFloat(/^(\d+\.\d{2})\d*$/.exec(row.price)[1])]);
          }
        }
      });
    });
  };

});
