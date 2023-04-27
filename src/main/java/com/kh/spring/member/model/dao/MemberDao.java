package com.kh.spring.member.model.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.kh.spring.member.model.vo.Member;

@Repository // bean으로 등록하는 어노테이션.
public class MemberDao {
	
	public Member loginMember(SqlSession sqlSession , Member inputMember) {
		
		return sqlSession.selectOne("memberMapper.loginMember" , inputMember);
		
	}
	
}
