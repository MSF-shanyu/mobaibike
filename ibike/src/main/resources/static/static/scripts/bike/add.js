$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
	$("#form-bike-add").validate({
		rules : {
			type : {
				required:true,
				minlength:0,
				maxlength:16
			},
			latitude : {
				required:true,
				range:[-90,90]
			},
			longitude:{
				required:true,
				range:[-180,180]
			}
		},
		onkeyup:false,
		focusCleanup:true,
		success:"valid",
		submitHandler:function(form){
			$(form).ajaxSubmit({
				type: 'post',
				url: "/bike",
				success: function(data){
					var index = parent.layer.getFrameIndex(window.name);
					parent.location.reload();
					parent.layer.close(index);
				},
                error: function(XmlHttpRequest, textStatus, errorThrown){
					layer.msg('ERROR!',{icon:1,time:1000});
				}
			});
		}
	});
});