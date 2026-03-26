package kr.co.teamo.post.service;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.teamo.common.code.FileErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.common.file.dto.FileDto;
import kr.co.teamo.common.file.service.FileService;
import kr.co.teamo.post.dto.PostFileDto;
import kr.co.teamo.post.dto.PostRequestDto;
import kr.co.teamo.post.dto.PostResponseDto;
import kr.co.teamo.post.mapper.PostMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostMapper postMapper;

	private final FileService fileService;

	/**
	 * 게시물 목록 조회
	 * @param PostRequestDto
	 * @return List<PostResponseDto>
	 */
	public List<PostResponseDto> selectAllPosts(PostRequestDto req){

		return postMapper.selectAllPosts(req);
	}

	/**
	 * 게시물 등록
	 * @param PostRequestDto
	 */
	@Transactional
	public void createPost(PostRequestDto req) {

		// 1. POST 테이블 INSERT
		postMapper.createPost(req);

		// 2. POST_TECH 테이블 INSERT
		postMapper.insertPostTechStack(req);

		// 3. TEMP_YN = N 업데이트
		List<FileDto> tempFiles = fileService.selectTempFiles(req.getTempKey());
		fileService.confirmTempFiles(req.getTempKey());
		// 4. 게시판 <-> 파일 연결

		if(tempFiles.isEmpty()) {
			throw new CustomException(FileErrorCode.FILE_EMPTY);
		}


		List<PostFileDto> postFiles = tempFiles.stream()
				.map(f -> PostFileDto.builder()
						.postId(req.getPostId())
						.fileId(f.getFileId())
						.build())
				.toList();

		postMapper.insertPostFiles(postFiles);



	}

}
