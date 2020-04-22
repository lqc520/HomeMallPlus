/**
 * 必要数据出口
 */


layui.define(['layer','table'],function (exports) {
    var $ = layui.jquery
    ,setter = layui.setter
    ,table = layui.table;
    //获取网站信息
    $.get(setter.adminUrl + "website/get",function (data) {
        exports("homemall",data);
    });

    //获取cpu内存使用信息
    $.ajax({
        url: layui.setter.adminUrl+'system/getCpuMemory',
        success:function (data) {
            cpu = data['cpu'];
            memory = data['memory'];
            exports('system',data)
        }
    });
    //设置全局 table 实例的 token（这样一来，所有 table 实例均会有效）
    table.set({
        headers: { //通过 request 头传递
            Authorization: 'Bearer'+layui.data(setter.tableName).access_token
        }
        ,where: { //通过参数传递
            access_token: layui.data(setter.tableName).access_token
        }
    });


});


