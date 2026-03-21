package kr.co.teamo.common.file.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
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

	@Value("${file.upload.path}")
	private String uploadPath;

	@Value("${file.upload.server}")
	private String uploadServer;

	private final FileMapper fileMapper;

	public String upload(MultipartFile file, String tempKey) throws IOException {

		if(file.isEmpty()) {
			throw new CustomException(CommonErrorCode.FILE_EMPTY);
		}

		// 원본파일명
		String originalName = file.getOriginalFilename();

		// 확장자추출
		String ext = originalName.substring(originalName.lastIndexOf("."));

		// UUID 파일명 생성
		String saveName = UUID.randomUUID() + ext;

		LocalDate today = LocalDate.now();

		String year = String.valueOf(today.getYear());
		String month = String.format("%02d", today.getMonthValue());
		String day = String.format("%02d", today.getDayOfMonth());

		// D:upload/2026/03/21
		Path dirPath = Paths.get(uploadPath, year, month, day);

		// 폴더 생성 (없으면 자동 생성)
		Files.createDirectories(dirPath);

		// 파일 경로
		Path savePath = dirPath.resolve(saveName);

		try {
			file.transferTo(savePath.toFile());
		}catch(IOException e) {
			throw new RuntimeException("파일 저장 실패", e);
		}

		FileDto fileDto = FileDto.builder()
				.orgFileNm(originalName)
				.saveFileNm(saveName)
				.filePath(dirPath.toString())
				.fileSize(file.getSize())
				.fileExt(ext.substring(1))
				.tempYn("Y")
				.tempKey(tempKey)
				.build();
		fileMapper.insertFile(fileDto);

		Path url =  Paths.get(uploadServer, year, month, day);
		Path realUrl = url.resolve(saveName);


		return realUrl.toString();
	}

	/**
	 * 임시파일을 제거하는 서비스
	 * @param file
	 */
	public void confirmTempFiles(String tempKey) {

		if(tempKey == null) return;

		fileMapper.confirmTempFiles(tempKey);

	}

	/**
	 * 임시파일 리스트 조회
	 * @param tempKey
	 * @return
	 */
	public List<FileDto> selectTempFiles(String tempKey){

		return fileMapper.selectTempFiles(tempKey);
	}
}
