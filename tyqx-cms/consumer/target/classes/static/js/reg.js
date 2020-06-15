layui.config({
    base : "/static/js/modules/"
}).extend({
    "common" : "common"
})
layui.use(['form','layer','jquery','common'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        common = layui.common;

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    });

    $("#imgCode img").click(function() {
        this.src = "/kaptcha/getKaptchaImage?rnd="+Math.random();
    });
    
    form.verify({
    	  username: function(value, item){ //value：表单的值、item：表单的DOM对象
    	    if(!new RegExp("^[a-zA-Z0-9_\u4e00-\u9fa5\\s·]+$").test(value)){
    	      return '用户名不能有特殊字符';
    	    }
    	    if(/(^\_)|(\__)|(\_+$)/.test(value)){
    	      return '用户名首尾不能出现下划线\'_\'';
    	    }
    	    if(/^\d+\d+\d$/.test(value)){
    	      return '用户名不能全为数字';
    	    }
    	  }
    	  
    	  //我们既支持上述函数式的方式，也支持下述数组的形式
    	  //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
    	  ,pass: [
    	    /^[\S]{6,12}$/
    	    ,'密码必须6到12位，且不能出现空格'
    	  ] 
    	});      

    //登录按钮
    form.on("submit(reg)",function(data){
        
        $.ajax({
            url: "/user/registerUser",
            type: "post",
            data: data.field,
            success: function(res){
            	if(res.data==false){
            		alert("用户已经存在")
            	}
            	if(res.lomg!=null){alert(res.lomg)}
            	if(res.and!=null){alert(res.and)}
            	if(res.msg!=null){
            		alert(res.msg)
            	  	location.href = "http://localhost:5555/";
            	}
          
            },
	        error: function (xmlHttpRequest) {
	            $("#reg").text("注册").removeAttr("disabled").removeClass("layui-disabled");
	            common.outErrorMsg(xmlHttpRequest);
	        }
        });
        return false;
    });

})