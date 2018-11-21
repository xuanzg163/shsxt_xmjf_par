$(function () {
   loadAccountInfo();
});

/**
 * 载入用户资产信息
 */
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
