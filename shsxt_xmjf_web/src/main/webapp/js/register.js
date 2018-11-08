
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
    })

})