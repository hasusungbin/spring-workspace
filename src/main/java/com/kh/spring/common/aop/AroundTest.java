package com.kh.spring.common.aop;

import org.apache.logging.log4j.core.config.Order;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(4)
public class AroundTest {

	private Logger logger = LoggerFactory.getLogger(AroundTest.class);
	
	// @Around : Before + After 가 합쳐진 어노테이션.
	@Around("CommonPointcut.implPointcut()")
	public Object checkRunningTime(ProceedingJoinPoint jp) throws Throwable {
		
		// ProceedingJoinPoint 인터페이스 : 전/후 처리 관련된 기능을 제공. TargetObject 의 값을 얻어올 수 있는 메소드도 함께 제공.
		
		// proceed() 메소드 호출 전 : @Before 용도로 사용 할 advice 작성
		// proceed() 메소드 호출 후 : @After 용도로 사용 할 advice 작성
		// 메소드의 마지막에 proceed() 의 반환값을 리턴 해 줘야함.
		
		// Before 시작
		long start = System.currentTimeMillis(); // 시스템 서버시간을 ms단위로 나타낸 값.
		
		
		// Before 끝
		
		Object obj = jp.proceed(); // 중간지점
		
		// After 시작
		long end = System.currentTimeMillis();
		
		logger.info("RunningTime : {} ms" , (end - start)); // {} system.print f문이랑 비슷함. 뒤에 밸류값이 {} 안으로 들어감.
		
		// After 끝
		return obj;
	}
}
