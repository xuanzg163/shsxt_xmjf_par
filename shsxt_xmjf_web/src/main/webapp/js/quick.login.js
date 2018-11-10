
/**
 * 页面加载完毕生效
 * 用户快速登陆
 *
 */
$(function () {

    /**
     * 点击更换图形验证码
     */
    $(".validImg").click(function () {
        $(this).attr("src", ctx + "/image");
    });


    $("#clickMes").click(function () {

        var phone=$("#phone").val();
        var code=$("#code").val();

        if(isEmpty(phone)){
            layer.tips("请输入手机号!","#phone");
            return;
        }

        if(isEmpty(code)){
            layer.tips("请输入图片验证码!","#code");
            return;
        }

        /**
         * 发送短信ajax
         */
        $.ajax({
            type:"post",
            url:ctx+"/sms",
            data:{
                phone:phone,
                imageCode:code,
                type:1
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    /**
                     * 倒计时 60秒不能重复点击
                     */
                    djs();
                }else{
                    layer.tips(data.msg,"#clickMes");
                }
            }
        });

    });


    /**
     * 快速登陆
     */
    $("#login").click(function () {
        var phone=$("#phone").val();
        var code=$("#verification").val();

        console.log(code);

        if(isEmpty(phone)){
            layer.tips("请输入手机号!","#phone");
            return;
        }

        if(isEmpty(code)){
            layer.tips("请输入手机验证码!","#code");
            return;
        }

        /**
         * 快速登陆ajax
         */
        $.ajax({
            type:"post",
            url:ctx+"/user/quickLogin",
            data:{
                phone:phone,
                code:code
            },
            dataType:"json",
            success:function (data) {
                if(data.code==200){
                    window.location.href=ctx+"/index";
                }else{
                    layer.tips(data.msg,"#login");
                }
            }

        });

    });


});


/**
 * 定时器
 */
function  djs() {
    var time=60;
    var intervalId= setInterval(function () {
        if(time>=1){
            $("#clickMes").attr("disabled",true);
            $("#clickMes").val("("+time+"秒)");
            $("#clickMes").css("background","#7c7c7c");
            time--;
        }else{
            $("#clickMes").attr("disabled",false);
            $("#clickMes").val("获取验证码");
            $("#clickMes").css("background","#fcb22f");
            clearInterval(intervalId);// 清除定时器
        }
    },1000)
}