


layui.define('layer',function (exports) {
    var $ = layui.jquery
    ,setter = layui.setter;

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
});


