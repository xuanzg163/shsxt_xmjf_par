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


    //tab切换
    $('#tabs div').click(function () {
        $(this).addClass('tab_active');
        var show=$('#contents .tab_content').eq($(this).index());
        show.show();
        $('#tabs div').not($(this)).removeClass('tab_active');
        $('#contents .tab_content').not(show).hide();


        if($(this).index()==2){
            // 根据当前项目id 查询投资用户记录
            var itemId=$("#itemId").val();
        }
    });


});

/**
 * 切换Tab
 * @param ele
 * @param allNum
 * @param currentNum
 */
function picTab(ele,allNum,currentNum) {
    var ele=$('#imgLarge');
    var allNum=$('#slider').find('li');
    var  currentNum=0;
    allNum.click(function () {
        currentNum = $(this).index();
        ele.show(300);
        var ImgSrc = $(this).attr('data-url');

        ele.css('background-image', 'url('+ImgSrc+')');
    });
    $('.close').click(function () {
        ele.hide(300);
    });
    $('.left').click(function () {
        currentNum--;
        if (currentNum < 0) {
            currentNum = allNum.length - 1;
        }
        var ImgSrc = allNum.eq(currentNum).attr('data-url');
        ele.css('background-image', 'url('+ImgSrc+')');
    });

    $('.right').click(function () {
        currentNum++;
        if (currentNum > allNum.length - 1) {
            currentNum = 0;
        }
        var ImgSrc = allNum.eq(currentNum).attr('data-url');
        ele.css('background-image', 'url('+ImgSrc+')');
    })
}

/**
 * 充值页面
 */
function toRecharge() {

    $.ajax({
        type:"get",
        url:ctx+"/user/checkRealNameStatus",
        dataType:"json",
        success:function (data) {
            if(data.code==200){
                window.location.href=ctx+"/account/recharge";
            }else{
                layer.confirm('当前用户未实名,执行实名认证?', {
                    btn: ['实名认证','稍后认证'] //按钮
                }, function(){
                    window.location.href=ctx+"/auth";
                });
            }
        }
    })
}