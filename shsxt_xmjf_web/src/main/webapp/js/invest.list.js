$(function () {
    // 加载投资列表数据
    loadInvestListData();


    $(".tab").click(function () {
        //$(".tab").removeClass("list_active");
        $(this).addClass("list_active");
        $(".tab").not(this).removeClass("list_active");
        var itemCycle=$(this).index();
        var isHistory=0;
        if(itemCycle==4){
            isHistory=1;
        }
        var itemType=$("#itemType").val();
        loadInvestListData(itemCycle,itemType,isHistory);
    });


    /*$(".order_item").click(function () {
        alert($(this).index());
    })*/
});


/**
 * 加载投资列表数据
 */
function  loadInvestListData(itemCycle,itemType,isHistory,pageNum,pageSize) {
    var params={
        isHistory:0,
        pageNum:1,
        pageSize:10
    };
    if(!isEmpty(itemCycle)){
        params.itemCycle=itemCycle;
    }
    if(!isEmpty(itemType)){
        params.itemType=itemType;
    }
    if(!isEmpty(isHistory)&& isHistory !=0){
        params.isHistory=1;
    }

    if(!isEmpty(pageNum) && pageNum>1){
        params.pageNum=pageNum;
    }
    if(!isEmpty(pageSize) && pageSize!=10){
        params.pageSize=pageSize;
    }


    $.ajax({
        type:"post",
        url:ctx+"/item/list",
        data:params,
        dataType:"json",
        success:function (data) {
            var list=data.list;
            var pages=data.navigatepageNums;
            if(data.total>0){
                /**
                 * 初始化list 列表数据
                 * 初始化页面数据
                 */
                initTrHtml(list);
                initPageHtml(pages,data.pageNum);
            }else{
                $("#pcItemList").html("");
                $("#pages").html("");
            }
        }
    })
}


function initTrHtml(list) {
    if(list.length>0){
        var trs="";
        for(var i=0;i<list.length;i++){
            var temp=list[i];
            trs=trs+"<tr>";
            /**
             * 拼接单元格
             * td
             */
            trs=trs+"<td>";
            trs=trs+"<strong>"+temp.item_rate+"</strong>%";
            if(temp.item_add_rate>0){
                trs=trs+"<span>+"+temp.item_add_rate+"%</span>";
            }
            trs=trs+"</td>";
            /**
             * 项目期限
             */
            trs=trs+"<td>";
            if(temp.item_cycle_unit==1){
                trs=trs+temp.item_cycle+"|天";
            }
            if(temp.item_cycle_unit==2){
                trs=trs+temp.item_cycle+"|月";
            }

            if(temp.item_cycle_unit==3){
                trs=trs+temp.item_cycle+"|季";
            }
            if(temp.item_cycle_unit==4){
                trs=trs+temp.item_cycle+"|年";
            }
            trs+"</td>";

            // 项目名称
            trs=trs+"<td>";
            trs=trs+temp.item_name;
            if(temp.item_isnew==1){
                trs=trs+"<strong class='colorful' new>NEW</strong>";
            }
            if(temp.item_isnew==0 && temp.move_vip==1){
                trs=trs+"<strong class='colorful' app>APP</strong>";
            }
            if(temp.item_isnew==0 && temp.move_vip==0 && temp.item_isrecommend ==1){
                trs=trs+"<strong class='colorful' hot>HOT</strong>";
            }
            if(temp.item_isnew==0 && temp.move_vip==0 && temp.item_isrecommend ==0 && !(isEmpty(temp.password))){
                trs=trs+"<strong class='colorful' psw>LOCK</strong>";
            }
            trs=trs+"</td>";


            // 信用等级
            trs=trs+"<td class='trust_range'>";
            if(temp.total>90){
                trs=trs+"A+";
            }
            if(temp.total>85 && temp.total<=90){
                trs=trs+"A";
            }
            if(temp.total>75 && temp.total<=85){
                trs=trs+"A-";
            }
            if(temp.total>65 && temp.total<=75){
                trs=trs+"B";
            }
            trs=trs+"</td>";

            // 担保机构
            trs=trs+"<td><image src='/img/logo.png'></image></td>";


            //  投资进度
            if(temp.item_status==1){
                trs=trs+"<td>即将开放</td>";
            }else{
                trs=trs+"<td>"+temp.item_scale+"%</td>";
            }

            // 操作项
            trs=trs+"<td>";
            if(temp.item_status==1){
                trs=trs+"<p><a><input class='countdownButton' valid type='button' value='即将开标'></a></p>"
            }
            if(temp.item_status==10){
                trs=trs+"<p class='left_money'>可投金额1000元</p><p><a ><input valid type='button' value='立即投资'></a></p>";
            }
            if(temp.item_status==1){
                trs=trs+"<p><a><input class='countdownButton' valid type='button' value='即将开标'></a></p>"
            }
            if(temp.item_status==20){
                trs=trs+"<p><a><input not_valid  type='button' value='已抢完'></a></p>"
            }
            if(temp.item_status==30 || temp.item_status==31){
                trs=trs+"<p><a ><input not_valid type='button' value='还款中'></a></p>";
            }
            if(temp.item_status==32){
                trs=trs+"<p><a  class='yihuankuan'><input not_valid type='button' value='已还款'></a></p>";
            }
            if(temp.item_status==23){
                trs=trs+"<p><a  ><input not_valid type='button' value='已满标'></a></p>";
            }
            trs=trs+"</td>";

            trs=trs+"</tr>";
        }
        console.log(trs);
        // 拼接节点trs 到指定节点下
        $("#pcItemList").html(trs);
    }
}



function initPageHtml(pages,currentPage) {
    /**
     *  <li class="active"><a title="第一页" >1</a></li>
     <li><a title="第二页">2</a></li>
     <li><a title="第三页">3</a></li>
     */
    var lis="";
    for(var i=0;i<pages.length;i++){
        var p=pages[i];
        var href="javascript:toPageData("+p+")";
        if(currentPage==p){
            lis=lis+"<li class='active'><a title='第"+p+"页' href='"+href+"'>"+p+"</a></li>";
        }else{
            lis=lis+"<li ><a href='"+href+"' title='第"+p+"页' >"+p+"</a></li>";
        }

    }
    $("#pages").html(lis);
}



function initItemData(itemType) {
    var itemCycle;
    $(".tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle=$(this).index();
        }
    });

    var isHistory=0;
    if(itemCycle==4){
        isHistory=1;
    }
    loadInvestListData(itemCycle,itemType,isHistory);

}


function toPageData(pageNum) {
    var itemCycle;
    $(".tab").each(function () {
        if($(this).hasClass("list_active")){
            itemCycle=$(this).index();
        }
    });

    var isHistory=0;
    if(itemCycle==4){
        isHistory=1;
    }
    var itemType=$("#itemType").val();
    loadInvestListData(itemCycle,itemType,isHistory,pageNum);
}
