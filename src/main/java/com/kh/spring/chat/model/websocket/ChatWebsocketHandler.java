package com.kh.spring.chat.model.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kh.spring.chat.model.service.ChatService;

public class ChatWebsocketHandler extends TextWebSocketHandler{
	
	// 채팅서비스 주입
	@Autowired
	private ChatService chatService;
	
	/*
	 * WebSocketHandler 인터페이스 : 웹소켓을 위한 메소드를 지원하는 인터페이스
	 * -> WebSocketHandler 인터페이스를 구현하는 클래스(TextWebSocketHandler)을 이용해서 웹소켓 기능을 구현 할 예정
	 * 
	 * * 웹소켓 핸들러 주요 메소드 *
	 * 
	 * void handlerMessage(WebSocketSession session , WebSocketMessage message)
	 * - 클라이언트로부터 메세지가 도착했을 시 실행.
	 * 
	 * void afterConnectionEstablished(WebSocketSession session)
	 * - 클라이언트와 연결이 완료되고, 통신 할 준비가 완료되면 실행
	 * 
	 * void afterConnectionClosed(WebSocketSession session , CloseStatus closeStatus)
	 * - 클라이언트와 연결이 종료되면 실행.
	 * 
	 * void handlerTransportError(WebSocketSession session , Throwable exception)
	 * - 메세지 전송 중 에러발생하면 실행.
	 */
	private Set<WebSocketSession> sessions = Collections.synchronizedSet(new HashSet<WebSocketSession>()) ;
	
	// synchronizedSet : 동기화 된 Set 반환.
	// -> 멀티 스레드 환경에서 하나의 컬렉션 요소에 여러스레드가 동시에 접근하면 충돌이 발생할 수 있으므로 동기화를 진행.
	
	// 클라이언트와 연결이 수립되고, 통신 준비가 완료되면 수행되는 메소드
	@Override
	public void afterConnectionEstablished(WebSocketSession session) {
		
		// WebSocketSession : 웹소켓에 접속/요청한 클라이언트의 세션정보
		System.out.println(session.getId()+"가 연결함");
		
		sessions.add(session); // 전달 받은 session을 set에 추가
		
	}
	
	// 클라이언트와 연결이 종료되면 수행
	@Override
	public void afterConnectionClosed(WebSocketSession session , CloseStatus status) throws Exception {
		sessions.remove(session);
		// 웹소켓 연결이 종료되는 경우 , sessions 안에 저장되어 있던 클라이언트의 session 정보를 삭제
	}
	
	@Override // 클라이언트로 부터 텍스트 메세지를 전달 받았을 때 수행
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		// TextMessage : 웹소켓을 이용해 전달된 텍스트가 담겨있는 객체
		
		// Payload : 전송되는 데이터(json 객체)
		System.out.println("전달 된 메세지 : "+message.getPayload());
		
		
	}
}
