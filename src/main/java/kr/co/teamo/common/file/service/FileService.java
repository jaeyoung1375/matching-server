package kr.co.teamo.common.file.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.teamo.common.code.CommonErrorCode;
import kr.co.teamo.common.exception.CustomException;
import kr.co.teamo.common.file.dto.FileDto;
import kr.co.teamo.common.file.mapper.FileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

	@Value("${file.upload.editor-path}")
	private String uploadPath;

	@Value("${file.base-url}")
	private String baseUrl;

	private final FileMapper fileMapper;

	public String upload(MultipartFile file) {

		if(file.isEmpty()) {
			throw new CustomException(CommonErrorCode.FILE_EMPTY);
		}

		// 원본파일명
		String originalName = file.getOriginalFilename();

		// 확장자추출
		String ext = originalName.substring(originalName.lastIndexOf("."));

		// UUID 파일명 생성
		String saveName = UUID.randomUUID() + ext;

		File dir = new File(uploadPath + File.separator);

		if (!dir.exists()) {
		    dir.mkdirs();
		}

		// 저장경로
		File saveFile = new File(dir, saveName);

		try {
			file.transferTo(saveFile);
		}catch(IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}

		FileDto fileDto = FileDto.builder()
				.orgFileNm(originalName)
				.saveFileNm(saveName)
				.filePath(uploadPath)
				.fileSize(file.getSize())
				.fileExt(ext.substring(1))
				.tempYn("Y")
				.build();
		fileMapper.insertFile(fileDto);

		return baseUrl+ "/upload/editor/"+ saveName;
	}
}
