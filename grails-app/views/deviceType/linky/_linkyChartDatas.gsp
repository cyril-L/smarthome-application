<%@ page import="smarthome.automation.ChartViewEnum" %>

<div data-chart-datas="true" class="hidden">
	chartDatas = new google.visualization.DataTable(${ raw(chart.toJsonDataTable().toString(false)) })

	<g:if test="${ compareChart }">
		chartOldDatas = new google.visualization.DataTable(${ raw(compareChart.toJsonDataTable().toString(false)) })
	</g:if>
	
	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		chartOptions = {
			  title: '${label }',
			  pointSize: '2',
		      width: '${ params.chartWidth ?: '100%' }',
	          height: '${ params.chartHeight ?: '600' }',
	          legend: {position: 'top'},
	          selectionMode: 'multiple',
	          seriesType: 'steppedArea',
	          series: {
	          	${chart.colonnes.size()-2}: {targetAxisIndex: 1, type: 'line', pointsVisible: false},
	          },
	          vAxes: {
	          	0: {title: 'Consommation (Wh)'},
	          	1: {title: 'Puissance (W)'}
	          },
		      chartArea: {
		      	width: '90%'
		      },
		      explorer: {
				axis: 'horizontal',
				keepInBounds: true,
				actions: ['dragToZoom', 'rightClickToReset']
			  },
			  hAxis: {
			  	title: '${ chart.hAxisTitle(command) }',
			  	gridlines: { color: 'none' },
	          	slantedText: true,
	          	format: '${ chart.format(command) }',
	          	ticks: [${ chart.ticks(command) }]
		      },
	  	};
	  	
	  	chartType = 'ComboChart';
	</g:if>
	<g:else>
		chartDatas = new google.visualization.DataView(chartDatas)
      	
      	<g:if test="${ !compareChart }">
      	
      		<g:if test="${ command.viewMode == ChartViewEnum.year }">
		      	chartDatas.setColumns([0,
		      		<g:each var="col" in="${ (1..<chart.colonnes.size()) }">
					${col},{ calc: "stringify",
		                sourceColumn: ${col},
		                type: "string",
		                role: "annotation" },
		            </g:each>
		        ])
	        </g:if>
		    	
			chartOptions = {
				  'title': '${label }',
			      'width': '${ params.chartWidth ?: '100%' }',
		          'height': '${ params.chartHeight ?: '600' }',
		          legend: {position: 'top'},
		          isStacked: true,
		          'chartArea': {
			      	width: '90%'
			      },
			      selectionMode: 'multiple',
			      'series': {
		          	${chart.colonnes.size()-2}: {targetAxisIndex: 1, type: 'line'},
		          },
		          vAxes: {
		          	0: {title: 'Consommation (kWh)'},
		          	1: {title: 'Puissance (W)'}
		          },
		          'seriesType': 'bars',
		          hAxis: {
		          	title: '${ chart.hAxisTitle(command) }',
				  	gridlines: { color: 'none' },
		          	slantedText: true,
		          	format: '${ chart.format(command) }',
		          	ticks: [${ chart.ticks(command) }]
			      },
		  	};
	  	</g:if>
	  	<g:else>
			chartOptions = {
				  'title': '${label }',
			      'width': '${ params.chartWidth ?: '100%' }',
		          'height': '${ params.chartHeight ?: '600' }',
		          legend: {position: 'top'},
		          isStacked: true,
		          'chartArea': {
			      	width: '90%'
			      },
			      selectionMode: 'multiple',
		          'seriesType': 'bars',
		          diff: {
				  	oldData: {
				  		tooltip: {
				  			prefix: null
				  		}
				  	},
				  	newData: {
				  		tooltip: {
				  			prefix: null
				  		}
				  	},
				  },
				  hAxis: {
				  	title: '${ chart.hAxisTitle(command) }',
				  	gridlines: { color: 'none' },
		          	slantedText: true,
		          	format: '${ chart.format(command) }',
		          	ticks: [${ chart.ticks(command) }]
			      },
		  	};
	  	</g:else>
	  	
	  	chartType = 'ColumnChart';
	</g:else>
	
	chartSelectFunction = function(event) {
		$('#selectionConso').val('')
  		var interval = chartSelectionInterval(chart)
  		var sum = chartSelectionSumInterval(chartDatas, interval)
  		$('#selectionConso').val(sum ? sum : '')
  	}
</div>