$(document).ready(function() {
  var series;

  Highcharts.setOptions({
    global: {
      useUTC: false,
      getTimezoneOffset: function(timestamp) {
        return (-1 * moment.tz(timestamp, 'Europe/Amsterdam').utcOffset());
      }
    }
  });

  $('#chart').highcharts({
    chart: {
      events: {
        load: function() {
          series = this.series[0];
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

  var eb = new vertx.EventBus('http://localhost:8080/eventbus');
  var i = 0;

  eb.onopen = function() {
    eb.registerHandler('random', function(msg) {
      series.addPoint([msg.date, msg.value], true, i >= 20);
      if (i < 20) i++;
    });
  };

});
