$(function() {
	$('.table-sort').DataTable({
				paging:true,
				pageLength:10,
				aLengthMenu:[5,10,20],
				order : [ [ 1, 'asc' ] ],
				ajax : {
					url : "/ibike/bikeList",
					type : 'POST',
					dataSrc : "obj"
				},
				columns : [  {
                    data : "id"
                }, {
					data : "id"
				}, {
					data : "status",
					defaultContent : "status"
				}, {
					data : "latitude",
					defaultContent : "latitude"
				}, {
					data : "longitude",
					defaultContent : "longitude"
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
					targets : [ 2 ],
					orderable : false,
					render : function(id, type, row, meta) {
						//状态: 0 未启用  1 启用且未开锁  2. 开锁使用中  3. 报修中 
						switch(id) {
					     case 0:
					    	 return "未启用";
					     case 1:
					    	 return "锁定中";
					     case 2:
					    	 return "使用中";
					     case 3:
					    	 return "报修中";
						} 
					}
				}, {
                    targets: [5],
                    render: function(data, type, row, meta) {
                        return '<a title="编辑" href="javascript:;" onclick="bike_edit('+ data +')" style="text-decoration:none"><i class="Hui-iconfont">&#xe6df;</i></a><a title="删除" href="javascript:;" onclick="bike_del(' + data +')" class="ml-5" style="text-decoration:none"><i class="Hui-iconfont">&#xe6e2;</i></a>'
                    }
                }]
			});
});
