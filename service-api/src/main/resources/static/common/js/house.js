/**
 
 @Name: 家居商城模板
 @Author: star1029
 @Copyright: layui.com
 */


layui.define(['element', 'carousel', 'table', 'util','cookie'], function(exports){
  var $ = layui.$
  ,element = layui.element
  ,form = layui.form
  ,carousel = layui.carousel
  ,laypage = layui.laypage
  ,util = layui.util
  ,table = layui.table
  ,cookie=layui.cookie;



  //设置全局变量
  var myconfig = {
    admin_url:"http://mall.lqcnb.cn/api/",
    admin_name:"lqc520"
  };


  //初始化
  var houseNav = $(".house-header").find(".layui-nav");


  
  $(function(){
    //详情页——选中
    var ddDetail = $(".house-detail").find(".shopChoose").find("dl").children("dd");
    ddDetail.each(function(){
      if($(this).hasClass("active")){
        $(this).append('<i class="layui-icon layui-icon-ok active"></i>');
      };
    });
    //详情页——数量
    $(".house-detail").find(".shopChoose").find(".btn-input").children("input").val("1");
  });
  
  //轮播
  var elemBanner = $('#house-carousel'), ins1 = carousel.render({
    elem: elemBanner
    ,width: '100%'
    ,height: elemBanner.height() + 'px'
    ,arrow: 'none' 
    ,interval: 5000
  });
  $(window).on('resize', function(){
    var width = $(this).prop('innerWidth');
    ins1.reload({
      height: (width > 768 ? 500 : 150) + 'px'
    });
  });
  
  //首页——搜索
  $(".house-header").find("#search").on('click', function(){
    layer.open({
      type: 1
      ,title: false
      ,shadeClose: true
      ,area: '300px'
      ,content: '<div id="house-search" class="layui-form"><input type="text" placeholder="搜索好物" class="layui-input"></div>'
      ,success: function(layero, index){
        $("#house-search").find("input").on('keydown', function(e){
          if(e.keyCode === 13){
            e.preventDefault();       
            layer.close(index);
          };
        });
      }
    });
  });
  
  //首页——点击切换
  $(".house-header").find("#switch").on('click', function(){
    if(houseNav.hasClass("close")){
      $(".house-header").children(".layui-container")[0].style.height = 60 + houseNav[0].offsetHeight + 'px';
      houseNav.removeClass("close");
    }else{
      $(".house-header").children(".layui-container")[0].style.height = 50 + 'px';
      houseNav.addClass("close");
    }
  });
  
  // 登入页——弹框
  // $("#house-login").find(".getCode").children("button").on('click', function(){
  //   layer.msg('验证码已发送');
  // });
  // form.on('submit(user-login)', function(data){
  //   window.location.href = "index.html";
  // });
  
  //列表页——点击切换
  $(".house-list").children(".filter").find("ul").each(function(){
    $(this).children("li").on('click', function(){
      $(this).addClass("active").siblings().removeClass("active");
    });
  });
  
  //列表页——分页
  // laypage.render({
  //   elem: 'houseList'
  //   ,count: 60
  //   ,theme: '#daba91'
  //   ,layout: ['page', 'next']
  // });
  
  //详情页——图片选择
  var imgDetail = $(".house-detail").find(".intro-img").children("img")[0]
  ,srcDetail = $(imgDetail).attr("src")
  ,ulDetail = $(".house-detail").find(".thumb");
  ulDetail.children("li").each(function(){
    $(this).on('mouseenter', function(){
      imgDetail.src = $(this).children("img")[0].src;
    }).on("mouseleave", function(){
      imgDetail.src = srcDetail;
    });
  });
  
  //详情页——点击切换
  $(".house-detail").find(".shopChoose").find("dl").each(function(){
    $(this).children("dd").on('click', function(){
      $(this).addClass("active").siblings().removeClass("active");
      $(this).append('<i class="layui-icon layui-icon-ok active"></i>');
      $(this).siblings().children("i").replaceWith("");
    });
  });
  
  //详情页——数量选择
  // var btnDetail = $(".house-detail").find(".shopChoose").find(".btn-input").children("button")
  // ,inpDetail = $(".house-detail").find(".shopChoose").find(".btn-input").children("input")
  // ,tipDetail = $(".house-detail").find(".shopChoose").find(".number").children(".inputTips")
  // ,change = function(){
  //   if(inpDetail[0].value > 100){
  //     tipDetail.css("display", "inline-block");
  //   }else{
  //     tipDetail.css("display", "none");
  //   };
  // };
  // btnDetail.each(function(index){
  //   $(this).on('click', function(){
  //     if(index == "1"){
  //       inpDetail.val(Number(inpDetail.val()) + 1);
  //     }else{
  //       inpDetail[0].value = inpDetail[0].value > 1 ? inpDetail[0].value - 1 : 1;
  //     };
  //     change();
  //   })
  // });
  // inpDetail.on('keydown', function(e){
  //   if(e.keyCode === 13){
  //     e.preventDefault();
  //     this.value = isNaN(this.value) ? 1 : (this.value > 1 ? this.value : 1);
  //     change();
  //   };
  // });
  
  //详情页——分页
  laypage.render({
    elem: 'detailList'
    ,count: 50
    ,theme: '#daba91'
    ,layout: ['page', 'next']
  });
  
  //详情页——收藏
  $(".house-detail").find(".shopChoose").find(".collect").on('click', function(){
    $(this).find("#collect").addClass("layui-icon-rate-solid").removeClass("layui-icon-rate");
    $(this).find("#collect")[0].style.color = '#dbbb92';
    layer.msg('已收藏');
  });
  
  //我的收藏——点击切换
  $(".house-usercol").find(".user-list").children("li").each(function(){
    $(this).on('click', function(){
      $(this).addClass("active").siblings().removeClass("active");
    });
  });
  
  //我的收藏——分页
  laypage.render({
    elem: 'userList'
    ,count: 50
    ,theme: '#daba91'
    ,layout: ['page', 'next']
  });
  
  //我的收藏——删除
  // $(".house-usercol").find(".layui-tab-content").find(".goods").each(function(){
  //   $(this).children(".del").on('click', function(){
  //     $(this).parent("div").parent("div").remove();
  //   });
  // });
  //
  // //地址管理——表格
  // //  http://mall.lqcnb.cn/api/address/
  // table.render({
  //   elem: '#user-address'
  //   ,url:  ''
  //   ,skin: 'line'
  //   ,cols: [[
  //     {type:'space', width:100, align:'center', templet: '#spaceTpl', width:90}
  //     ,{field:'receiver', title:'收货人', align:'center', width:90}
  //     ,{field:'address', title:'地址', align:'center'}
  //     ,{field:'mobile', title:'联系方式', align:'center', width:140}
  //     ,{title:'操作', align:'center', templet: '#addressTpl', width:120}
  //   ]]
  // });
  //
  // //地址管理——监听工具条
  // table.on('tool(user-address)', function(obj){
  //   var data = obj.data;
  //   if(obj.event === 'del'){
  //     layer.confirm('真的删除行么', function(index){
  //       obj.del();
  //       layer.close(index);
  //     });
  //   }else if(obj.event === 'edit'){
  //     layer.open({
  //       type: 2
  //       ,title: '编辑地址'
  //       ,content: 'iframe.html'
  //       ,area: ['850px', '440px']
  //       ,shade: 0.8
  //       ,skin: 'address-class'
  //       ,btn: '确定'
  //       ,yes: function(index, layero){
  //         window['layui-layer-iframe'+ index].layui.form.on('submit(useradd-submit)', function(data){
  //           layer.close(index);
  //         });
  //         layero.find('iframe').contents().find("#useradd-submit").trigger('click');
  //       }
  //     });
  //   }
  // });
  //
  // $(".useradd").find(".address-add").on('click', function(){
  //   layer.open({
  //     type: 2
  //     ,title: '新建地址'
  //     ,content: 'iframe.html'
  //     ,area: ['850px', '440px']
  //     ,shade: 0.8
  //     ,skin: 'address-class'
  //     ,btn: '确定'
  //     ,yes: function(index, layero){
  //       window['layui-layer-iframe'+ index].layui.form.on('submit(useradd-submit)', function(data){
  //         layer.close(index);
  //       });
  //       layero.find('iframe').contents().find("#useradd-submit").trigger('click');
  //     }
  //   });
  // });
  
  // //个人中心——订单
  // table.render({
  //   elem: '#house-user-order'
  //   ,url:  'http://mall.lqcnb.cn:9000/api/order/getOrder'
  //   ,skin: 'line'
  //   ,cols: [[
  //     {title:'订单信息', align:'center', templet: '#orderTpl'}
  //     ,{field:'avatar', title:'订购商品', templet: '#imgTpl', align:'center'}
  //     ,{field:'number', title:'件数', align:'center', width:80}
  //     ,{title:'价格', align:'center', templet: '#priceTpl', width:100}
  //     ,{title:'订单状态', align:'center', templet: '#stateTpl', width:100}
  //     ,{title:'订单操作', align:'center', templet: '#handleTpl', width:120}
  //   ]]
  // });
  table.on('tool(house-user-order)', function(obj){
    var data = obj.data;
    console.log(data);
    /*==============评价====================*/
    var rate = layui.rate;
    //渲染
    var stat=4;
    var ins1 = rate.render({
      elem: '#rate',  //绑定元素
      choose: function(value){
        stat=value;
        if(value > 4) layer.msg("谢谢你的认可，欢迎再次光临");
        if(value < 4) layer.msg("我们会持续改善 请继续关注我们")
      }
    });
    /*==============评价====================*/
    if(obj.event === 'check'){
      layer.open({
        type: 2
        ,content: 'https://www.kuaidi100.com/?from=openv'
        ,area: ['500px', '300px']
      });
    }else if(obj.event === 'evaluate'){
      layer.open({
        title:'评价商品'
        ,type: 1
        ,content: $('#take')
        ,area: ['500px', '300px']
        ,btn:['确定','取消']
        ,yes: (index,layero) =>{
          $.ajax({
            url:base_url+'comment/add',
            data:{
              star:stat,
              content:$('#remarks').val(),
              goodsId:data.goods_id,
              memberId:data.member_id,
              oid:data.order_number
            },success:function (data) {
              layer.msg("评价成功");
            }
          });


          table.reload('house-user-order');
          layer.close(index);

        }
      });
    }else{
      $.ajax({
        url:base_url+'order/updateOrderStates',
        data:{
          orderNumber:data.order_number
        },success:function (data) {
          layer.msg(data.msg)
        }
      });
    }
  });

 // 购物车——表格http://mall.lqcnb.cn/api/card/getCards
  table.render({
    elem: '#house-usershop-table'
    ,url: base_url+'card/getCards'
    ,skin: 'line'
    ,cols: [[
      {type:'checkbox', width:50}
      ,{title:'商品', align:'center', minWidth:260, templet: '#goodsTpl'}
      ,{title:'单价', align:'center', minWidth:160, templet: '#priceTpl'}
      ,{title:'数量', align:'center', width:150, templet: '#numTpl'}
      ,{title:'小计', align:'center', width:120, templet: '#totalTpl'}
      ,{title:'操作', align:'center', width:100, templet: '#shopTpl'}
    ]]
    ,done: function(res, curr, count){
      //数字框
      $(".numVal").each(function(){
        //获得小计 单价
        var totalTd = $(this).parents("td").siblings().find(".total")[0]
        ,totalPrice = $(this).parents("td").siblings().find("span").filter(".price")[0].innerHTML;
        $(this).children("button").each(function(index){
          //获得数量
          var numVal = $(this).parent("div").children("input");
          $(this).on('click', function(){
            if(index == "1"){
              numVal.val(Number(numVal.val()) + 1);
            }else{
              numVal[0].value = numVal[0].value > 1 ? numVal[0].value - 1 : 1;
            };
            totalTd.innerHTML = '￥' + (numVal.val() * totalPrice.slice(0)).toFixed(2)
          });
        });
        $(this).children("input").on('keydown', function(e){
          if(e.keyCode === 13){
            e.preventDefault();
            this.value = isNaN(this.value) ? 1 : (this.value > 1 ? this.value : 1);
            totalTd.innerHTML = '￥' + (this.value * totalPrice.slice(0)).toFixed(2)
          };
        });
      });
      //合计
      // totalVal();
      if($("#house-usershop-table").next("div").find(".layui-none").length != 0){
        $(".house-usershop-table-num").css("display", "none");
      };
    }
    ,text: {
      none: '<div class="house-usershop-table-none"><div><img src="/common/img/shopnone.png"></div><p>购物车空空如也</p><a class="layui-btn layui-btn-primary" href="list.html">去逛逛</a></div>'
    }
    ,id: 'house-usershop-table'
  });

  // 合计
  var goodsVal = $(".house-usershop").find("#total").children("span")
  ,copyWith = $(".house-usershop").find("#toCope").children("p").children("big")
  ,copyTips = $(".house-usershop").find("#toCope").children("span");
  //监听复选框选择 获得总数
  table.on('checkbox(house-usershop-table)', function(obj){
    var checkStatus = table.checkStatus('house-usershop-table');
    goodsVal[0].innerHTML = 0;
    $(checkStatus.data).each(function(){
      goodsVal[0].innerHTML = parseFloat(this.number * this.price.slice(0)) + Number(goodsVal[0].innerHTML);
    });
    //满减
    if(goodsVal[0].innerHTML > 200){
      copyWith[0].innerHTML = '￥' + (goodsVal[0].innerHTML - 20).toFixed(2)
      copyTips.css("display", "inline-block");
    }else{
      copyWith[0].innerHTML =  '￥' + parseFloat(goodsVal[0].innerHTML).toFixed(2);
      copyTips.css("display", "none");
    };
    //转换格式
    goodsVal[0].innerHTML = parseFloat(goodsVal[0].innerHTML).toFixed(2);
    if(checkStatus.data.length != 0){
      $(".house-usershop-table-num").children("input")[0].checked = true;
      form.render('checkbox');
    }else{
      $(".house-usershop-table-num").children("input")[0].checked = false;
      form.render('checkbox');
    };
    $(".house-usershop-table-num").children(".numal").html('已选 ' + checkStatus.data.length + ' 件');
  });
  // table.on('tool(house-usershop-table)', function(obj){
  //   var data = obj.data;
  //   console.log(data.id );
    // if(obj.event === 'del'){
    //   layer.confirm('确定删除此物品？', function(index){
    //     let jsonCards=JSON.parse($.cookie("cards"));
    //     for(let i=0;i<jsonCards.length ;i++){
    //       if(jsonCards[i].id === data.id){
    //         jsonCards.pop();
    //         break;
    //       }
    //     }
    //     $.cookie("cards",JSON.stringify(jsonCards));
    //     obj.del();
    //     layer.close(index);
    //     //向服务端发送删除指令
    //   });
    // }
  // });
  // $(".house-usershop").find("#batchDel").on('click', function(){
  //   var checkStatus = table.checkStatus('house-usershop-table')
  //   ,checkData = checkStatus.data;
  //   if(checkData.length === 0){
  //     layer.msg('请选择数据');
  //   }else{
  //     //执行 Ajax 操作之后再重载
  //     table.reload('house-usershop-table');
  //     $(".house-usershop-table-num").children("input")[0].checked = false;
  //     form.render('checkbox');
  //     $(".house-usershop-table-num").children(".numal").html('已选 0 件')
  //     copyWith[0].innerHTML = goodsVal[0].innerHTML = '￥0.00';
  //     copyTips.css("display", "none");
  //     layer.msg('已删除');
  //   }
  // });
  
  
  //固定 bar
  util.fixbar({
    click: function(type){
      if(type === 'bar1'){
        //
      }
    }
  });
  
  exports('house',myconfig);
});