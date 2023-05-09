package com.kh.spring.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.kh.spring.member.model.vo.Member;

@Component
@Aspect
@Order(5)
public class AfterReturninTest {
	
	Logger logger = LoggerFactory.getLogger(AfterReturninTest.class);
	
	
	// @AfterReturning : 메소드 실행 이후에 반환값을 얻어오는 기능의 어노테이션.
	// returning : 반환값을 저장 할 매개변수명을 지정하는 속성.
	@AfterReturning(pointcut = "CommonPointcut.implPointcut()" , returning = "returnObj")
	public void returnValue(JoinPoint jp , Object returnObj) { // jp는 항상 첫번째 매개변수로 지정해 줘야함.
		
		if(returnObj instanceof Member) {
			Member loginMember = (Member)returnObj; // impl 에서 실행되는 메소드에 따라 바뀌는데 우리가 로그인 했을 때 실행되는 메소드는 반환값이 Member 이기 때문에 실행이 되는거다. 
			loginMember.setNickName("홍성홍성빈");
		}
		logger.info("return value : {} ", returnObj);
		
	}
	
}
