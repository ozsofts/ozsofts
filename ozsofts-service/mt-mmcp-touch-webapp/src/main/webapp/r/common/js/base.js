$(document).ready(function(){

	var getWidth = $(window).width();	

	var navSwitch = $('#sidebar-toggler');

	navSwitch.prop('_switch',true);

	$('body').prop({'_headFix':false,'_footerFix':false});

	if (getWidth<980) {
		$('.sidebar').hide();	
	}
	
	$('#moblieNavBtn').click(function(){
		$('#moblieMask').addClass('pop-cover');
		$('body').css('overflow','hidden');
		$('.sidebar').show();
		$('.sidebar-menu').animate({right:'0px'},700);
	});

	navSwitch.click(function(){
		var getWidth = $(window).width();
		var checkSidebarStatus = navSwitch.prop('_switch');
		
		if(getWidth>980){			
			if(checkSidebarStatus){
				navSwitch.prop('_switch',false);
				$('#navSwitch').addClass('sidebar-closed');
				var oNav = $('.sidebar-closed .sidebar-menu ul li ul.sub-menu');
			}
			else{
				navSwitch.prop('_switch',true);
				$('#navSwitch').removeClass('sidebar-closed');
			};
		}else{
			if(checkSidebarStatus){
				$('#moblieMask').removeClass('pop-cover');
				$('body').css('overflow','auto');
				$('.sidebar-menu').animate({right:'-300px'},500,function(){
						$('.sidebar').hide();
					});
				};
		};
	
	});

	//导航动态

	var leftNavLi = $('#siderbar-menu > li > a'); 

	leftNavLi.prop('_switch',false)
		.click(function(){
		
		var checkOpen = $(this).prop('_switch');

		var checkSidebarStatus = navSwitch.prop('_switch');
		
		if (!checkOpen && checkSidebarStatus){
			leftNavLi.prop('_switch',false).parents('li')
				.removeClass('active');				
			$(this).parent().addClass('active')
				.find('span').removeClass('icon-arrow-down').addClass('icon-arrow-top');
			$(this).next('ul.sub-menu').slideDown(300);
			$(this).parent().siblings().find('ul.sub-menu').slideUp(300);		
			$(this).prop('_switch',true);
		}
		else{
			$(this).prop('_switch',false)
				.next('ul.sub-menu').slideUp(300);
			$(this).children('span').removeClass('icon-arrow-top').addClass('icon-arrow-down');
		};
	
	});

	//页面头尾固定

	var fix_btn = $('.fix-btn');
	fix_btn.prop('_state',false);
	
	fix_btn.click(function(){
		var getId = $(this).attr('id');
		var getState  = $(this).prop('_state');
		if (getId == "fix-head"){
			if(getState){
				$('body').removeClass('header-fixed').prop('_headFix',false);
				$(this).text("头部固定").prop('_state',false);
			}
			else{
				$('body').addClass('header-fixed').prop('_headFix',true);
				$(this).text("取消固定").prop('_state',true);
			}			
		}
		else if (getId == "fix-buttom"){
			if(getState){
				$('body').removeClass('footer-fixed').prop('_footerFix',false);
				$(this).text("脚步固定").prop('_state',false);
			}
			else{
				$('body').addClass('footer-fixed').prop('_footerFix',true);
				$(this).text("取消固定").prop('_state',true);
			}
					
		};
		
		$(judgeFixed());

	});

	//头部导航下拉菜单

	var userDropdown = $('#userDropdown');
	
	userDropdown.prop('_switch',false)
		.click(function(){
			
			var checkOpen = $(this).prop('_switch');
			
			if (checkOpen) {
				$('#userDropdownMenu').slideUp(100);
			}
			else{			
				$('#userDropdownMenu').slideDown(100,function(){
					$(document).one('click',function(){					
						$('#userDropdownMenu').slideUp(100);
					});
				});
			};
			
			var judge = $('#userDropdownMenu').css('display');
			
			if (judege == 'block') {
				$(this).prop('_switch',true);
			}
			else{
				$(this).prop('_switch',false);
			};
 		
 		});

	//表格下拉动态

	$('.text-con-table').hide();

	$('.table-con-title').prop('_switch',false)
		.click(function(){
			var getState = $(this).prop('_switch');
			// $(this).addClass('active');
			// var getHeight = $(this).next().find('table').actual('height');
			// alert(getHeight);
			// $(this).next().animate({height:'-=300px'},400);
			$(this).next().find('.text-con-table').toggle();
			if (getState) {
				$(this).removeClass('active').prop('_switch',false);
			}
			else{
				$(this).addClass('active').prop('_switch',true);
			};
		});

	//统一容器高度
	
	$(setContainerHeigth());

});

$(window).resize(function(){
	
	$(setContainerHeigth());

});

//设定高度

function setContainerHeigth(){
	var getWidth = $(window).width();
	var getHeight = $(window).height();	
	if (getWidth > 980) {
		var getSiderbarHeight = $('.sidebar-menu').innerHeight();
		var getContentHeight = $('.content-bg').innerHeight();
		if(getContentHeight<getHeight&&getSiderbarHeight<getHeight){
			var getHead = $('.header').innerHeight();
			var getFoot = $('.footer').innerHeight();
			$('.content-bg').innerHeight(getHeight-getFoot-getHead);
			$('.sidebar-menu').innerHeight(getHeight-getFoot-getHead);
			$('body').css('overflow','hidden');
		}
		else{
			
			if (getSiderbarHeight > getContentHeight) {
				$('.content-bg').innerHeight(getSiderbarHeight);
			}
			else{
				$('.sidebar-menu').innerHeight(getContentHeight);	
			};
			$('body').css('overflow','auto');
		}		
	}
	else{
		$('.sidebar-menu').innerHeight(getHeight);
		$('body').css('overflow','auto');
	}
}

//判断页脚是否固定设置边距

function judgeFixed(){
	var oBody = $('body');
	var oContainer = $('.container');
	var getHead = oBody.prop('_headFix');
	var getFoot = oBody.prop('_footerFix');
	if (getHead) {
		getHeight = $('.header').innerHeight();
		oBody.css('padding-top',getHeight+'px');
	}
	else{
		oBody.css('padding-top',0);
	};

	if(getFoot){
		getHeight = $('.footer').innerHeight();
		oContainer.css('padding-bottom',getHeight+'px');
	}
	else{	
		oContainer.css('padding-bottom',0);
	};
}

