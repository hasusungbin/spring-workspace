package com.kh.spring.common.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component // 런타임 시 필요한 위치에 코드를 끼워넣을 수 있도록 bean으로 등록 시켜주기.
@Aspect // 공통관심사가 작성된 클래스임을 명시
		// * 공통관심사 란? 특정 흐름 사이에 끼여서 수행 할 코드.
		// Aspect 어노테이션이 붙은 클래스에는 실행 할 코드(advice)와 pointCut이 정의되어 있어야 한다.
		// advice(실제로 끼어들어서 수행 할 메소드 , 코드)
		// @PointCut(advice가 끼어들어서 수행 될 클래스, 메소드 위치등을 정의)이 작성되어 있어야 한다.
public class Test {
	
	private Logger logger = LoggerFactory.getLogger(Test.class);
	
	// joinPoint : 클라이언트가 호출하는 모~든 비즈니스 메소드. advice가 적용될 수 있는 예비 후보.
	//			ex) 클래스의 인스턴스 생성 시점. 메소드 호출시점 , 예외 발생등 전부
	
	// PointCut : joinpoint들 중에서 "실제로" advice가 끼어들어서 실행 될 지점을 선택.
	
	/*
	 * PointCut 작성 방법
	 * 
	 * @PointCut("execution([접근제한자] 반환형 패키지 + 클래스명.메소드명([매개변수]))")
	 * PointCut 표현식
	 * " * " : 모든 | 아무값
	 * " .. " : 하위 | 아래(하위패키지) | 0개 이상의 매개변수
	 * @Before : PointCut에 지정된 메소드가 수행되기 "전"에 advice 수행을 명시하는 어노테이션.
	 * 
	 * com.kh.spring.board패키지 아래에 있는 Impl로 끝나는 클래스의 모든 메소드에(매개변수와 상관없이) PointCut 지정.
	 */
	
	@Before("execution(* com.kh.spring.board..*Impl*.*(..))") // 흠;;;; 진짜 어렵네
	public void start() { // 서비스 수행전에 실행되는 메소드(advice)
		logger.info("========================== Service Start =====================================================================");
	}
	
	@After("execution(* com.kh.spring.board..*Impl*.*(..))")
	public void end() {
		logger.info("========================== Service End =====================================================================");
	}
}
