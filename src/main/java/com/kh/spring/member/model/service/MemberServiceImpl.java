package com.kh.spring.member.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.controller.MemberController;
import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

@Service // 현재 클래스가 Service임을 명시 + bean으로 등록
public class MemberServiceImpl implements MemberService{
	
//	@Autowired // 순환 의존성문제 발생 (서로가 서로를 주입받고 있음)
//	private MemberController memberController;
	
	@Autowired // bean으로 등록된 객체중 같은타입이 있을경우 의존성(객체)을 주입해줌(DI)
	private MemberDao memberDao;
	

	
	@Override
	public Member loginMember(Member inputMember) {
		
		Member loginUser = memberDao.loginMember(inputMember);
		/* 
		 * SqlSessionTemplate 객체를 bean으로등록한 후 부터는 스프링 컨테이너가 자원 사용후
		 * 자동으로 반납을 해주기 때문에 close()할 필요가 없다
		 */
		
		return loginUser;
	}
	
	@Override
	public int insertMember(Member inputMember) {
		return memberDao.insertMember(inputMember);
	}
	
	@Override	
	public ArrayList<Member> selectAll(){
		return memberDao.selectAll();
	}
	
	
	
	
	
	
	
	
}
