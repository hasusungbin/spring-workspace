package com.kh.spring.common.interceptor;

import java.util.ArrayList;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.BoardType;

public class BoardTypeInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	private BoardService boardService;
	
	@Override // 전처리 할 메소드 작성
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		
		//application scope에 boardTypeList 가 있는지 체크, 없을 경우 이를 조회하는 boardService메소드 호출 후 결과를 셋팅
		
		// application scope에 객체 얻어오기
		ServletContext application = request.getServletContext();
		
		if(application.getAttribute("boardTypeList") == null ) {
			ArrayList<BoardType> list = boardService.selectBoardTypeList();
			
			application.setAttribute("boardTypeList", list);
		}
		return true;
	}
	
	@Override // 후처리 할 메소드 작성
	public void postHandle(HttpServletRequest reqeust, HttpServletResponse response, Object handler , ModelAndView modelAndView) {
			System.out.println("후처리 실행");
	}
	
	@Override
	public void afterCompletion(HttpServletRequest reqeust, HttpServletResponse response, Object handler, Exception ex) {
		System.out.println("view 처리 완료 후 실행");
	}
	
}
