/**
 * 显示投资详情信息的项目进度
 */
$(function () {
    $("#itemScale").radialIndicator({
        barColor: "orange",
        barWidth: 5,
        roundCorner : true,
        radius:30,
        format: "#%"
    });
    var radialObj = $("#itemScale").data("radialIndicator");
    radialObj.value($("#itemScale").attr("data-val"));
});
