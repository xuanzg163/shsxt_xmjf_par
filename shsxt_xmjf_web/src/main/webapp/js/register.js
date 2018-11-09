
/*页面加载调用方法*/
$(function () {

    /**
     * 点击验证码生成新的验证码
     */
    $(".validImg").click(function () {
        $(this).attr("src",ctx+"/image");
    });

    /**
     * 获取验证码
     */
    $("#clickMes").click(function () {
        var phone = $("#phone").val();
        var code = $("#code").val();

        // console.log(phone+""+ code);//测试拿取前台值
        if (isEmpty(phone)){
            layer.tips("请输入手机号","#phone");
            return;
        }

        if (isEmpty(code)){
            layer.tips("请输入图片验证码","#code");
            return;
        }

        /**
         * Ajax
         * 发送注册信息到后台
         */
        $.ajax({
            type:"post",
            url:ctx+"/sms",
            data:{
                phone:phone,
                imageCode:code,
                type:2
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
     * 注册
     */
    $("#register").click(function () {
        var phone = $("#phone").val();
        var password=$("#password").val();
        var code=$("#verification").val();

        if (isEmpty(phone)){
            layer.tips("请输入手机号","#phone");
            return;
        }

        if (isEmpty(password)){
            layer.tips("请输入密码","#password");
            return;
        }

        if (isEmpty(code)){
            layer.tips("请输入手机短信验证码","#code");
            return;
        }

        /**
         * 注册ajax
         */
        $.ajax({
            type:"post",
            url:ctx +"/user/register",
            data:{
                phone:phone,
                password:password,
                code:code
            },
            dataType:"json",
            success:function (data) {
                if (data.code==200){
                    window.location.href=ctx+"/login";
                } else{
                    layer.tips(data.msg,"#register")
                }
            }
        });


    })

});

/**
 * 按钮倒计时禁用
 */
function djs() {
    //按钮禁用时间
    var time = 60;
    setInterval(function () {
        if (time > 1){
            $("#clickMes").attr("disabled",true);
            $("#clickMes").val("("+time+"秒)");
            $("#clickMes").css("background","#7c7c7c");
            time--;
        } else{
            $("#clickMes").attr("disabled",false);
            $("#clickMes").val("获取验证码");
            $("#clickMes").css("background","#fcb22f");
            clearInterval(intervalId);// 清除定时器
        }
    },1000)
}