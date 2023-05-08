package com.kh.spring.common.scheduling;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.BoardImg;

@Component
public class FileDeleteScheduler {

	private Logger logger = LoggerFactory.getLogger(FileDeleteScheduler.class);
	
	@Autowired
	private ServletContext application;
	
	@Autowired
	private BoardService service;
	
	// 1. BOARD_IMG 테이블 안에 있는 이미지 목록들 모두 조회하여
	// 2. images/boardT 디렉토리 안에 있는 이미지들과 대조하여
	// 3. 일치하지 않는 이미지 파일들을 삭제(db에는 없는 데이터인데 , boardT 안에는 존재하는 경우)
	// 4. 우선 5초간격으로 테스트 후 정상적으로 작동한다면 매달 1일 정싣에 실행되는 스케쥴러로 만들기.
	
	@Scheduled(cron = "0 0 0 1 * *") // 0초 0분 0시 1일 매요일 매월
	public void deleteFile() {
		logger.info("파일 삭제 시작");
		
		// 1) board_img 테이블 안에 있는 모든 파일 목록들 조회.
		ArrayList<BoardImg> list = service.selectAll();
		// 2) images/board 폴더 아래에 존재하는 모든 이미지 파일목록 조회(File 클래스 활용)
		File path = new File(application.getRealPath("/resources/images/boardT"));
		// path가 참조하고 있는 폴더에 들어가서 모든 파일을 File 배열로 얻어오기
		File[] files = path.listFiles();
		// List<File> fileList = Arrays.asList(files); 둘다 컬렉션으로 만들어줌.
		// 3) 두 목록을 비교해서 일치하지 않는 파일 삭제(삭제 시 File 클래스의 delete() 활용)
		for(File file : files) {
			boolean exist = false;
			for(BoardImg bi : list) {
				if(bi.getChangeName().equals(file.getName()) ) {
					exist = true;
				}
			}
			if(!exist) {
				file.delete();
				logger.info("xxx파일 삭제함.");
			}
		}
		
		logger.info("서버 파일 삭제작업 끝");
		
	}
	
}
