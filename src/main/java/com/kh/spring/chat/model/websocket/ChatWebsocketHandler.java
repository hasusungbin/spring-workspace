package com.kh.spring.chat.model.websocket;

import java.sql.Date;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.kh.spring.chat.model.service.ChatService;
import com.kh.spring.chat.model.vo.ChatMessage;

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
		
		// JackSon 라이브러리 : java에서 json을 다루기 위한 라이브러리.
		
		// JackSon-databind -> ObjectMapper를 이용해서 JSON형태로 넘어온 데이터를 특정 VO필드에 맞게 자동 매핑
		ObjectMapper objectMapper = new ObjectMapper();
		
		ChatMessage chatMessage = objectMapper.readValue( message.getPayload() , ChatMessage.class );
		
		chatMessage.setCreateDate( new Date(System.currentTimeMillis()));
		
		// 전달받은 채팅 메세지를 db에 삽입
		System.out.println(chatMessage);
		
		int result = chatService.insertMessage(chatMessage);
		
		if(result > 0) {
			// 같은방에 접속중인 클라이언트에게 전달받은 메세지 뿌리기.
			for( WebSocketSession s  : sessions ) {
				
				// 반복을 진행중인 WebSocketSession 안에 담겨있는 방번호 == 메세지 안에 담겨있는 방번호가 일치하는 경우 메세지 뿌리기
				int chatRoomNo = (int) s.getAttributes().get("chatRoomNo");
				
				// 메세지에 담겨있는 채팅방 번호와 chatRoomNo 와 일치하는지 비교
				if(chatMessage.getChatRoomNo() == chatRoomNo) {
					// 같은방 클라이언트에게 JSON형태로 메세지를 보냄.
					// s.sendMessage( new TextMessage( JSON 객체 ) )
					s.sendMessage( new TextMessage( new Gson().toJson(chatMessage) )); // 문자열을 객체로 매핑 시키는게 jackson 이고 객체를 문자열(json)로 바꾸는게 gson
				}
			}
		}
	}
	
	
	
	
	
	
}
