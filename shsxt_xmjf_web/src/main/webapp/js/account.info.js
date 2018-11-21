$(function () {
   loadAccountInfo();

   loadInvestIncomeInfo();


    loadInvestIncomeInfo02();
});


function loadAccountInfo() {

    $.ajax({
        type:"post",
        url:ctx+"/account/countBusAccountInfoByUserId",
        dataType:"json",
        success:function (data) {
            var total=data.data1;
            var data2=data.data2;

             Highcharts.chart('pie_chart', {
                chart: {
                    spacing : [40, 0 , 40, 0]
                },
                title: {
                    floating:true,
                    text: "总金额:"+total+"￥"
                },
                tooltip: {
                    pointFormat: '{series.name}'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.y}￥',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            }
                        },
                        point: {
                            events: {
                            }
                        },
                    }
                },
                series: [{
                    type: 'pie',
                    innerSize: '80%',
                    name: '市场份额',
                    // List<XXXDto>
                    // List<Map<String,Object>>   总金额数据 total 100000
                    /**
                     *
                     **/
                    data: data2
                }]
            }, function(c) { // 图表初始化完毕后的会掉函数
                // 环形图圆心
                var centerY = c.series[0].center[1],
                    titleHeight = parseInt(c.title.styles.fontSize);
                // 动态设置标题位置
                c.setTitle({
                    y:centerY + titleHeight/2
                });
            });


        }
    });










}




function loadInvestIncomeInfo() {
    $.ajax({
        type:"post",
        url:ctx+"/invest/countInvestIncomeInfoByUserId",
        dataType:"json",
        success:function (data) {
            var data1=data.data1;
            var data2=data.data2;

           Highcharts.chart('line_chart', {
                chart: {
                    type: 'line'
                },
                title: {
                    text: '投资收益曲线图'
                },
                subtitle: {
                    text: '数据来源:小马金服'
                },
                xAxis: {
                    // List<String>
                    categories: data1
                },
                yAxis: {
                    title: {
                        text: '金额(￥)'
                    }
                },
                plotOptions: {
                    line: {
                        dataLabels: {
                            // 开启数据标签
                            enabled: true
                        },
                        // 关闭鼠标跟踪，对应的提示框、点击事件会失效
                        enableMouseTracking: false
                    }
                },
                series:data2
            });


        }
    })
}



function loadInvestIncomeInfo02() {
    $.ajax({
        type:"post",
        url:ctx+"/invest/countInvestIncomeInfoByUserId",
        dataType:"json",
        success:function (data) {
            var data1=data.data1;
            var data2=data.data2;



             Highcharts.chart('line_chart02',{
                chart: {
                    type: 'column'
                },
                title: {
                    text: '投资收益曲线图'
                },
                subtitle: {
                    text: '数据来源:小马金服'
                },
                xAxis: {
                    categories:data1,
                    crosshair: true
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: '金额(￥)'
                    }
                },
                tooltip: {
                    // head + 每个 point + footer 拼接成完整的 table
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        borderWidth: 0
                    }
                },
                series:data2
            });


        }
    })
}
