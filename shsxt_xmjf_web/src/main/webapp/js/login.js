
/**
 * 用户登陆
 */
$(function () {

    /**
     * 用户登陆
     */
    $("#login").click(function () {
        var phone = $("#phone").val();
        var password = $("#password").val();

        // console.log(phone+"----"+password);

       if (isEmpty(phone)){
           layer.tips("请输入手机号!","#phone")
           return;
       }

        if (isEmpty(password)){
            layer.tips("请输入密码!","#password")
            return;
        }

        console.log(phone+"----"+password);
        /**
         * 用户登陆Ajax
         */
        $.ajax({
            type:"post",
            url:ctx +"/user/login",
            data:{
                phone:phone,
                password:password,
            },
            dataType:"json",
            success:function (data) {
                if (data.code == 200){
                    window.location.href=ctx+"/index";
                }else{
                    layer.tips(data.msg,"#login");
                }
            }
        });
    });

})