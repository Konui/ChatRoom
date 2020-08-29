window.onload=function(){
	if($(".uname").text()==""){
		alert("请先登录");
		$(".loginwarrp").css("display","block");
		$(".container").css("display","none");
	}
	var ws;
	//var un=prompt("用户名:");
	$('#msg').bind('keydown', function (event) {
		var event = window.event || arguments.callee.caller.arguments[0];
		if (event.keyCode == 13){
			sendMsg();
		}
	});

}

function connect(un){
	var host="ws://localhost:1101/ws/webSocket/";
	ws=new WebSocket(host+un);
	console.log(host+un);
	ws.onopen=function(evt){
		console.log("连接成功");

	};
	ws.onclose=function(evt){
		console.log("连接被关闭，状态码："+evt.code,"关闭原因："+evt.reason+",wasClean："+evt.wasClean);
		let div=$('<div></div>');
		div.attr("class","bubble you");
		div.text(evt.code+" "+evt.reason+" "+evt.wasClean);
		$("div[data-chat='person1']").append(div);
	};
	ws.onmessage=function(evt){
		var res=eval('('+evt.data+')');
		console.log("websocket接受到消息："+res);
		console.log(res[0]);
		console.log(res["content"]);
		if("type" in res){
			res=JSON.parse(evt.data);
			var cont;
			if(res["type"]=="img"){
				cont="<a href='"+res["content"]+"' target=\"_blank\"><img src='"+res["content"]+"' width=\"150\" height=\"150\"/></a>";
			}else if(res["type"]=="file"){
				cont="<a href='"+ res["content"]+"' target=\"_blank\" download>文件下载</a>";
			}else if(res["type"]=="del") {
				$("li[data-id='u"+res["from_uid"]+"']").remove();
				$("div[data-chat='u"+res["from_uid"]+"']").remove();
				$(".person").eq(0).click();
			}else{
				cont=res["content"];
			}
				if(res["from_uid"]!=$(".uid").text()){
					let id=parseInt(res["room_id"])>0?("r"+res["room_id"]):("u"+res["from_uid"]);
					let me="<div class='lname'>"+getUserName(res["from_uid"])+"</div><div class='bubble you'>"+cont+"</div>";
					$("div[data-chat='"+id+"']").append(me);
					$("li[data-id='"+id+"']").children(".time").text(formatDate(res["time"]));
					$("li[data-id='"+id+"']").children(".preview").text(res["content"].substring(0,20));
					let i=$("li[data-id='"+id+"']").children(".redpoint").text();
					if($("li[data-id='"+id+"']").attr("class")=="person"){
						$("li[data-id='"+id+"']").children(".redpoint").text(parseInt(i)+1);
						$("li[data-id='"+id+"']").children(".redpoint").show();
					}else{
						zd();
					}
				}
				resort();
		}else{
			//初次加载
			var number = $(".people").children().length;

			for(var i=0;i<res.length;i++){
				let temp="<li class='person' data-id='"+(res[i]["room"]?'r':'u')+(res[i]["id"])+"'><img src='/c/"+res[i]["url"]+"' alt='' /><span class='name'>"+res[i]["name"]+"</span><span class='time'></span><span class='preview'></span><span class='redpoint'>0</span></li>";
				$(".people").append(temp);
				let chatbody="<div class='chat' data-chat='"+(res[i]["room"]?'r':'u')+(res[i]["id"])+"'></div>";
				//添加消息*******************************
				$(".right").children(".top").after(chatbody);
				$("li[data-id="+(res[i]["room"]?'r':'u')+(res[i]["id"])+"]").children(".redpoint").hide()

				if("msgList" in res[i]){
					let id=((res[i]["room"])?"r":"u")+res[i]["id"];
					for(var j=0;j<res[i]["msgList"].length;j++){
						var cont;

						if(res[i]["msgList"][j]["type"]=="file"){
							cont="<a href='"+ res[i]["msgList"][j]["content"]+"' target=\"_blank\" download>文件下载</a>";
						}else if(res[i]["msgList"][j]["type"]=="img"){
							cont="<a href='"+res[i]["msgList"][j]["content"]+"' target=\"_blank\"><img src='"+res[i]["msgList"][j]["content"]+"' width=\"150\" height=\"150\"/></a>";
						}else{
							cont=res[i]["msgList"][j]["content"];
						}

						let me="<div class='"+(res[i]["msgList"][j]["from_uid"]==$(".uid").text()?"r":"l")+"name'>"+(res[i]["msgList"][j]["from_uid"]==$(".uid").text()?$(".uname").text():getUserName(res[i]["msgList"][j]["from_uid"]))+"</div><div class='bubble "+(res[i]["msgList"][j]["from_uid"]==$(".uid").text()?"me":"you")+"'>"+cont+"</div>";
						$("div[data-chat='"+id+"']").append(me);
						$("li[data-id='"+id+"']").children(".time").text(formatDate(res[i]["msgList"][j]["time"]));
						$("li[data-id='"+id+"']").children(".preview").text(res[i]["msgList"][j]["content"].substring(0,20));
					}
				}
			}
			$(".person").click(function(){
				let v=$(this).attr("class");
				v=v+" active"
				$(this).attr("class",v).siblings().attr("class","person");
				//显示对应聊天框
				let id=$(this).attr("data-id");
				let idn=id.substr(1);
				$("div[data-chat='"+id+"']").attr("class","chat-active-chat").siblings(".chat-active-chat").attr("class","chat");
				//设置用户名
				let name =$(this).children(".name").text();
				$(".tname").text(name);
				var fn=id.substr(0,1)=="u"?"delFriend("+idn+")":"delRoomUser("+idn+")";
				$("#rm").attr("href","javascript:"+fn+";");
				$(this).children(".redpoint").text("0");
				$(this).children(".redpoint").hide();
				zd();
			});
			resort();
			//显示第一个聊天框
			if(number==0){$(".person").eq(0).click();}
		}

	};
}

function sendMsg(){
	let me="<div class='rname'>"+$(".uname").text()+"</div><div class='bubble me'>"+$("#msg").val()+"</div>";
	let id=$(".chat-active-chat").attr("data-chat");
	$(".chat-active-chat").append(me);
	let c=$("#msg").val();
	resort();
	ws.send(JSON.stringify({
		"room_id":id[0]=="r"?id.substr(1):0,
		"type":"msg",
		"from_uid":$(".uid").text(),
		"to_uid":id[0]=="r"?null:id.substr(1),
		"content":$("#msg").val(),
		"time":nowData()
	}));
	$("li[data-id='"+id+"']").children(".time").text(nowData());
	$("li[data-id='"+id+"']").children(".preview").text(c.substring(0,20));
	var u={
		"room_id":id[0]=="r"?id.substr(1):0,
		"type":"msg",
		"from_uid":$(".uid").text(),
		"to_uid":id[0]=="r"?null:id.substr(1),
		"content":$("#msg").val(),
		"time":nowData()
	}

	$.ajax({
		type:"post",
		dataType:"json",
		url:"http://localhost:1101/c/msg",
		data:u,
		success:function(){
		},
		error: function(){

		}
	});
	$("#msg").val("");
}
function formatDate(d) {
	var date = new Date(d);
	var YY = date.getFullYear() + '-';
	var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
	var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
	var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
	var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
	return YY + MM + DD +" "+hh + mm + ss;
}
function nowData() {
	var date = new Date();
	var YY = date.getFullYear() + '-';
	var MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
	var DD = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate());
	var hh = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
	var mm = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
	var ss = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
	return YY + MM + DD +" "+hh + mm + ss;
}
function login(){

	var err="";
	if($("#username").val().length>10||$("#username").val().length<3){
		err="用户名长度在3-10之间！";
	}
	if($("#password").val().length>16||$("#password").val().length<6){
		err=err+"密码长度在6-16之间！";
	}

	if(err.length!=0){
		$("#error").html(err);
		return;
	}else{
		var pwi=$("#password").val();
		var pw=$.md5(pwi);
		var u={
			"name":$("#username").val(),
			"password":pw
		}
		$.ajax({
			type:"get",
			dataType:"json",
			url:"http://localhost:1101/c/pw",
			data:u,
			success:function(result){
				console.log(result);

				if(result.code==200){
					$(".loginwarrp").css("display","none");
					$(".container").css("display","block");
					$(".uname").text(result.data.name);
					$(".uid").text(result.data.id);
					$("#headimg").attr("src","/c/"+result.data.headimg);
					//console(result);
					var dtd=$.Deferred();
					//connect(result.data.id,dtd);
					connect(result.data.id);
				}else{
					$("#error").text(result.data.msg);
				}
			},
			error: function(){
				alert("登录错误");
			}
		});
		
		
	}
}
//注册
function zhuce(){
	var err="";
	if($("#username").val().length>10||$("#username").val().length<3){
		err="用户名长度在3-10之间！";
	}
	if($("#password").val().length>16||$("#password").val().length<6){
		err=err+"密码长度在6-16之间！";
	}

	if(err.length!=0){
		$("#error").html(err);
		return;
	}else{
		var pwi=$("#password").val();
		var pw=$.md5(pwi);
		var u={
			"name":$("#username").val(),
			"password":pw
		}
		$.ajax({
			type:"post",
			dataType:"json",
			url:"http://localhost:1101/c/user",
			data:u,
			success:function(result){
				if(result.code==200){
					alert("注册成功");
				}else{
					$("#error").text(result.data.msg);
				}
			},
			error: function(){
				alert("注册出错");
			}
		});


	}
}
//排序
function resort(){
	var $t=$(".person");
	$t.sort(function(a,b){
		var va=$(a).find('span:eq(1)').text();
		if(va==""){
			va="2020-01-01 00:00";
		}
		var vb=$(b).find('span:eq(1)').text();
		if(vb==""){
			vb="2020-01-01 00:00";
		}
		if(new Date(va)>new Date(vb))
		{return -1;}
		else {
			return 1;
		}
	});
	$t.appendTo(".people");
}
function addFriend(){
	var fid = prompt("请输入要添加好友的名称");
	if(fid.length>0){
		var id=$(".uid").text();
		$.ajax({
			type:"get",
			dataType:"json",
			url:"http://localhost:1101/c/friend/"+id+"/"+fid,
			success:function(result){
				if(result.code==200){
					alert("添加成功");
				}else{
					$("#error").text(result.data.msg);
				}
			},
			error: function(){
				alert("添加出错");
			}
		});
	}else{
		alert("好友名不能为空");
	}
}

function addRoom(){
	var name = prompt("请输入要添加房间的名称");
	if(name.length>0){
		var id=$(".uid").text();
		$.ajax({
			type:"get",
			dataType:"json",
			url:"http://localhost:1101/c/addroom/"+id+"/"+name,
			success:function(result){
				if(result.code==200){
					alert("添加成功");
				}else{
					$("#error").text(result.data.msg);
				}
			},
			error: function(){
				alert("添加出错");
			}
		});
	}else{
		alert("房间名不能为空");
	}
}
function createRoom(){
	var name = prompt("请输入要创建房间的名称");
	if(name.length>0){
		var id=$(".uid").text();
		$.ajax({
			type:"get",
			dataType:"json",
			url:"http://localhost:1101/c/room/"+id+"/"+name,
			success:function(result){
				if(result.code==200){
					alert("创建成功");
				}else{
					$("#error").text(result.data.msg);
				}
			},
			error: function(){
				alert("创建出错");
			}
		});
	}else{
		alert("房间名不能为空");
	}
}
function rePw(){
	var pw = prompt("请输入新密码");
	if(pw.length>16||pw.length<6){
		alert("密码长度在6-16之间！");
	}else{
		var pw=$.md5(pw);
		var u={
			"name":$("#username").val(),
			"password":pw
		}
		$.ajax({
			type:"post",
			dataType:"json",
			url:"http://localhost:1101/c/pw",
			data:u,
			success:function(result){
				if(result.code==200){
					alert("重置成功");
				}else{
					alert("重置失败");
				}
			},
			error: function(){
				alert("重置出错");
			}
		});


	}
}
function reImg() {
	$("#file").click();
}
function cuploadImg(){
	$("#file1").click();
}
function cuploadFile(){
	$("#file2").click();
}

function uploadImg(){
	var formData = new FormData();
	var fileName = $("#file1")[0].files[0].name;
	var fileType = fileName.substr(fileName.lastIndexOf(".")).toUpperCase();
	if (fileType != ".BMP" && fileType != ".PNG" && fileType != ".GIF" && fileType != ".JPG" && fileType != ".JPEG") {
		alert("图片限于bmp,png,gif,jpeg,jpg格式");
		return false;
	}else{
		formData.append("file",$("#file1")[0].files[0]);
		formData.append("uid",$(".uid").text());
		$.ajax({
			url:'http://localhost:1101/c/uploadFile',
			type:'post',
			data: formData,
			contentType: false,
			processData: false,
			success:function(res){
				if(res.code==200){
					sendImg(res.data);
				}else{
					alert(res.data.msg);
				}
			}
		})
	}
}
function uploadFile(){
	var formData = new FormData();
	formData.append("file",$("#file2")[0].files[0]);
	formData.append("uid",$(".uid").text());
	$.ajax({
		url:'http://localhost:1101/c/uploadFile',
		type:'post',
		data: formData,
		contentType: false,
		processData: false,
		success:function(res){
			if(res.code==200){
				sendFile(res.data);
			}else{
				alert('上传失败');
			}
		}
	})
}

function reHImg(){
	var formData = new FormData();
	formData.append("file",$("#file")[0].files[0]);
	formData.append("uid",$(".uid").text());
	$.ajax({
		url:'http://localhost:1101/c/headImg',
		type:'post',
		data: formData,
		contentType: false,
		processData: false,
		success:function(res){
			if(res.code==200){
				$("#headimg").attr("src",res.data);
				alert('更改成功');
			}else{
				alert('更改失败');
			}
		}
	})
}



function sendImg(url){
	//图片大小
	let id=$(".chat-active-chat").attr("data-chat");

	let me="<div class='rname'>"+$(".uname").text()+"</div><div class='bubble me'>"+"<a href='"+url+"' target=\"_blank\"><img src='"+url+"' width=\"100\" height=\"100\"/></a>"+"</div>";
	$(".chat-active-chat").append(me);
	let c=url;
	resort();
	ws.send(JSON.stringify({
		"room_id":id[0]=="r"?id.substr(1):0,
		"type":"img",
		"from_uid":$(".uid").text(),
		"to_uid":id[0]=="r"?null:id.substr(1),
		"content":url,
		"time":nowData()
	}));
	$("li[data-id='"+id+"']").children(".time").text(nowData());
	$("li[data-id='"+id+"']").children(".preview").text("[图片]");
	var u={
		"room_id":id[0]=="r"?id.substr(1):0,
		"type":"img",
		"from_uid":$(".uid").text(),
		"to_uid":id[0]=="r"?null:id.substr(1),
		"content":url,
		"time":nowData()
	}

	$.ajax({
		type:"post",
		dataType:"json",
		url:"http://localhost:1101/c/msg",
		data:u,
		success:function(){
		},
		error: function(){

		}
	});
}
function sendFile(url){
	//图片大小
	let id=$(".chat-active-chat").attr("data-chat");

	let me="<div class='rname'>"+$(".uname").text()+"</div><div class='bubble me'>"+"<a href='"+url+"' target=\"_blank\" download>文件下载</a>"+"</div>";
	$(".chat-active-chat").append(me);
	let c=url;
	resort();
	ws.send(JSON.stringify({
		"room_id":id[0]=="r"?id.substr(1):0,
		"type":"file",
		"from_uid":$(".uid").text(),
		"to_uid":id[0]=="r"?null:id.substr(1),
		"content":url,
		"time":nowData()
	}));
	$("li[data-id='"+id+"']").children(".time").text(nowData());
	$("li[data-id='"+id+"']").children(".preview").text("[文件]");
	var u={
		"room_id":id[0]=="r"?id.substr(1):0,
		"type":"file",
		"from_uid":$(".uid").text(),
		"to_uid":id[0]=="r"?null:id.substr(1),
		"content":url,
		"time":nowData()
	}

	$.ajax({
		type:"post",
		dataType:"json",
		url:"http://localhost:1101/c/msg",
		data:u,
		success:function(){
		},
		error: function(){

		}
	});
}
function getUserName(id){
	var name;
	$.ajax({
		type:"get",
		dataType:"json",
		url:"http://localhost:1101/c/name/"+id,
		async: false,
		success:function(res){
			console.log(res.data);
			name=res.data;
		},
		error: function(){
			name="unknow";
		}
	});
	return name;
}
function delFriend(fid){
	$("li[data-id='u"+fid+"']").remove();
	$("div[data-chat='u"+fid+"']").remove();
	$(".person").eq(0).click();
	var id=$(".uid").text();
	$.ajax({
		type:"get",
		dataType:"json",
		url:"http://localhost:1101/c/rmFriend/"+id+"/"+fid,
		async: false,
		success:function(){
		},
	});
}
function delRoomUser(rid){
	$("li[data-id='r"+rid+"']").remove();
	$("div[data-chat='r"+rid+"']").remove();
	$(".person").eq(0).click();
	var id=$(".uid").text();
	$.ajax({
		type:"get",
		dataType:"json",
		url:"http://localhost:1101/c/rmRoom/"+id+"/"+rid,
		async: false,
		success:function(){
		},
	});
}

function onlineon(){
	$('#onlinemeb').css('visibility','visible');

	$.ajax({
		type:"get",
		dataType:"json",
		url:"http://localhost:1101/c/onlinemeb",
		success:function(res){
			for(key in res.data){
				let z=res.data[key].split("-");
				let li="<li><a href=\"javascript:addRoombyOnline("+z[1]+",'"+z[0]+"')\">"+z[0]+"("+z[2]+")</a></li>";
				$("#roomonlinelist").append(li);
			}
		},
	});

}
function onlineoff(){
	$("#roomonlinelist").empty();
	$('#onlinemeb').css('visibility','hidden');
}
function addRoombyOnline(id,name){
		var id=$(".uid").text();
		$.ajax({
			type:"get",
			dataType:"json",
			url:"http://localhost:1101/c/addroom/"+id+"/"+name,
			success:function(result){
				if(result.code==200){
					alert("添加成功");
					$('#onlinemeb').css('visibility','hidden');
				}else{
					$("#error").text(result.data.msg);
				}
			},
			error: function(){
				alert("添加出错");
			}
		});
}

function exit(){
    ws.close();
    location.reload();
}
function zd(){
	$('.chat-active-chat').scrollTop( $('.chat-active-chat')[0].scrollHeight);
}