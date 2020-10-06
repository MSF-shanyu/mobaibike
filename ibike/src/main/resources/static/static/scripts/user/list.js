$(function() {
	$('.table-sort').DataTable(
			{	
				paging:true,
				pageLength:10,
				aLengthMenu:[5,10,20],
				order : [ [ 1, 'desc' ] ],
				ajax : {
					url : "/ibike/userList",
					type : 'POST',
					dataSrc : "obj"
				},
				columns : [ {
					data : "id"
				}, {
					data : "id"
				}, {
					data : "openId"
				}, {
					data : "phoneNum"
				}, {
					data : "deposit"
				}, {
					data : "balance"
				}, {
					data : "name",
					defaultContent : ""
				}, {
					data : "idNum",
					defaultContent : ""
				}, {
					data : "status",
					defaultContent : ""
				}, {
					data : "id",
					defaultContent : ""
				} ],
				columnDefs : [{
					targets : [ 0 ],
					orderable : false,
					render : function(id, type, row, meta) {
						return '<input id="input-' + id
								+ '" type="checkbox" name="ids" value=' + id
								+ '><label for="input-' + id + '"></label>';
					}
				}, {
					targets: [8],
					render: function(id, type, row, meta) {
						//状态: 0 未启用  1 启用且未开锁  2. 开锁使用中  3. 报修中 
						switch(id) {
					     case 0:
					    	 return "未注册";
					     case 1:
					    	 return "未充值押金";
					     case 2:
					    	 return "未身份验证";
					     case 3:
					    	 return "可骑行";
					     case 4:
					    	 return "骑行中";
						} 
					}
				}, {
                    targets: [9],
                    render: function(data, type, row, meta) {
                        return '<a title="编辑" href="javascript:;" onclick="bike_edit('+ data +')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a><a title="删除" href="javascript:;" onclick="bike_del(' + data +')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>'
                    }
                }]
			});

});

function user_add(title, url, w, h) {
	layer_show(title, url, w, h);
}

function user_dels() {
	var cks = $(".table-sort input[name='ids']:checked");
	layer.confirm('确认要删除吗？', function(index) {
		var param = [];
		cks.each(function() {
			param.push($(this).val());
		});
		$.ajax({
			url : "/user/" + param,
			type : "delete",
			success: function(msg){
				location.reload();
				layer.close(index);
			}
		});
		
	});
}

function user_del(id) {
	layer.confirm('确认要删除吗？', function(index) {
		$.ajax({
			//   /user/10
			url : "/user/" + id,
			type : "delete",
			success: function(msg){
				location.reload();
				layer.close(index);
			}
		});
		
	});
}

function user_edit(id) {
	
	layer.open({
		type: 2,
		area: [800+'px', 510 +'px'],
		fix: false, //不固定
		maxmin: true,
		shade:0.4,
		title: "编辑教师",
		content: "/user_edit",
		success: function(layero, index){
			var body = layer.getChildFrame('body', index);
			$.ajax({
				url : "/user/" + id,
				type : "get",
				dataType : "json",
				success : function(data) {
					body.find('#uid').val(data.id);
					body.find('#username').val(data.name);
					body.find("input[type='radio'][value="+ data.gender +"]").attr("checked", "checked")
					body.find("#userType option[value="+ data.type +"]").attr('selected', 'selected');
					body.find('#desc').val(data.description);
				}
			});
		}
	});
	
}
