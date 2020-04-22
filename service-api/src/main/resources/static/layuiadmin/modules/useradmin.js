/**
用户管理 管理员管理 角色管理
*/


layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,form = layui.form
  ,setter=layui.setter;

  //用户管理
  table.render({
    elem: '#LAY-user-manage'
    ,url: setter.adminUrl+'member/getPageList'
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 50, title: 'ID', sort: true}
      ,{field: 'mobile', title: '手机', minWidth: 80}
      ,{field: 'avatar', title: '头像', width: 100, templet: '#imgTpl'}
      ,{field: 'nickname', title: '昵称'}
      ,{field: 'email',width:150, title: '邮箱'}
      ,{field: 'isActivate', title: '是否激活',sort: true,templet:'#activeTpl'}
      ,{field: 'createTime', title: '加入时间', sort: true,templet:'<div>{{ Format(d.createTime,"yyyy-MM-dd h:m:s")}}</div>'}
      ,{title: '操作', width: 150, align:'center', fixed: 'right', toolbar: '#table-useradmin-webuser'}
    ]]
    ,page: true
    ,limit: 30
    ,height: 'full-220'
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-user-manage)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
      layer.prompt({
        formType: 1
        ,title: '敏感操作，请验证口令'
      }, function(value, index){
        layer.close(index);
        
        layer.confirm('真的删除行么', function(index){
          obj.del();
          layer.close(index);
        });
      });
    } else if(obj.event === 'edit'){
      var tr = $(obj.tr);
      layer.open({
        type: 2
        ,title: '编辑用户'
        ,content: '/admin/user/user/userform.html?id='+data.id+"&nickname="+data.nickname
            +"&email="+data.email+"&avatar="+data.avatar+"&mobile="+data.mobile
        ,maxmin: true
        ,area: ['500px', '450px']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submitID = 'LAY-user-front-submit'
          ,submit = layero.find('iframe').contents().find('#'+ submitID);

          //监听提交
          iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
            var field = data.field; //获取提交的字段
            //提交 Ajax 成功后，静态更新表格中的数据
            $.ajax({
              url: setter.adminUrl+'member/update',
              type: "patch",
              data:field,
              success:function (data) {
                layer.msg(data.msg);
              }
            }).done(function () {
              table.reload('LAY-user-manage'); //数据刷新
              layer.close(index); //关闭弹层
            });
            table.reload('LAY-user-front-submit'); //数据刷新
            layer.close(index); //关闭弹层
          });  
          
          submit.trigger('click');
        }
        ,success: function(layero, index){
          
        }
      });
    }
  });

  //管理员管理
  table.render({
    elem: '#LAY-user-back-manage'
    ,url: setter.adminGateWayUrl + 'profile/getMapList' //模拟接口
    ,headers: {"Authorization": 'Bearer'+layui.data(setter.tableName).access_token}
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 80, title: 'ID', sort: true}
      ,{field: 'roleId', title: '角色id',hide:true}
      ,{field: 'mobile', title: '手机号'}
      ,{field: 'nickname', title: '昵称'}
      ,{field: 'email', title: '邮箱'}
      ,{field: 'role', title: '角色'}
      ,{field: 'create_time', title: '加入时间', sort: true,templet:'<div>{{ Format(d.create_time,"yyyy-MM-dd h:m:s")}}</div>'}
      ,{field: 'state', title:'审核状态', templet: '#buttonTpl', minWidth: 80, align: 'center'}
      ,{title: '操作', width: 180, align: 'center', fixed: 'right', toolbar: '#table-useradmin-admin'}
    ]]
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-user-back-manage)', function(obj){
    var data = obj.data;
    console.log(data);
    if(obj.event === 'del'){
      layer.prompt({
        formType: 1
        ,title: '敏感操作，请验证口令'
      }, function(value, index){
        layer.close(index);
        layer.confirm('确定删除此管理员？', function(index){

          obj.del();
          layer.close(index);
        });
      });
    }else if(obj.event === 'edit'){
      var tr = $(obj.tr);
      layer.open({
        type: 2
        ,title: '编辑管理员'
        ,content: '../../../admin/user/administrators/adminform.html'
        ,area: ['420px', '420px']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submitID = 'LAY-user-back-submit'
          ,submit = layero.find('iframe').contents().find('#'+ submitID);
          //监听提交
          iframeWindow.layui.form.on('submit('+ submitID +')', function(data){
            var field = data.field; //获取提交的字段
            if(!field.state){
              field.state = 0;
            }
            //提交 Ajax 成功后，静态更新表格中的数据
            //$.ajax({});
            layui.admin.req({
              url: layui.setter.adminGateWayUrl + 'profile/update'
              ,type: 'patch'
              ,data: field
              ,success:function (data) {
                layer.msg(data.msg);
              },done:function () {
                table.reload('LAY-user-back-manage'); //数据刷新
                layer.close(index); //关闭弹层
              }
            });

          });  
          
          submit.trigger('click');
        }
        ,success: function(layero, index){  //预先加载，将值从父窗口传到 子窗口
          let body = layer.getChildFrame('body', index);
          body.find("#mobile").val(data.mobile);   //通过class名进行获取数据
          body.find("#email").val(data.email);
          body.find("#nickname").val(data.nickname);
          body.find("#uid").val(data.id);
          if(data.state==0){
            body.find("#state").attr("checked", true);
          }else{
            body.find("#state").attr("checked", false);
          }
          body.find('#roleId').val(data.roleId);
          layui.form.render(); //在子界面渲染
          var iframeWin = window[layero.find('iframe')[0]['name']]; //得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
        }
      })
    }
  });




  //角色管理
  table.render({
    elem: '#LAY-user-back-role'
    ,url: layui.setter.adminGateWayUrl + 'role/getRoleList' //模拟接口
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 80, title: 'ID', sort: true}
      ,{field: 'name', title: '角色名'}
      ,{field: 'enname', title: '拥有权限'}
      ,{field: 'description', title: '具体描述'}
      ,{title: '操作', width: 180, align: 'center', fixed: 'right', toolbar: '#table-useradmin-admin'}
    ]]
    ,text: '对不起，加载出现异常！'
  });
  
  //监听工具条
  table.on('tool(LAY-user-back-role)', function(obj){
    var data = obj.data;
    if(obj.event === 'del'){
      layer.confirm('确定删除此角色？', function(index){
        obj.del();
        layer.close(index);
      });
    }else if(obj.event === 'edit'){
      var tr = $(obj.tr);

      layer.open({
        type: 2
        ,title: '编辑角色'
        ,content: '../../../admin/user/administrators/roleform.html'
        ,area: ['500px', '480px']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submit = layero.find('iframe').contents().find("#LAY-user-role-submit");

          //监听提交
          iframeWindow.layui.form.on('submit(LAY-user-role-submit)', function(data){
            var field = data.field; //获取提交的字段
            
            //提交 Ajax 成功后，静态更新表格中的数据
            //$.ajax({});
            table.reload('LAY-user-back-role'); //数据刷新
            layer.close(index); //关闭弹层
          });  
          
          submit.trigger('click');
        }
        ,success: function(layero, index){
        
        }
      })
    }
  });

  exports('useradmin', {})
});