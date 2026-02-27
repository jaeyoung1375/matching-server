package kr.co.matching.common.file.dto.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import kr.co.matching.common.file.dto.FileDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class FileUtil {

	@Value("${file.upload.path}")
	private String filePath;


	/**
	 * 파일 저장
	 */
	public FileDto saveFile(MultipartFile file, String subDir) {

		if(file == null || file.isEmpty()) {
			log.info("파일이 비어있습니다.");
			return null;
		}

		try {
			String orgFileNm = file.getOriginalFilename();
			String ext = getExtension(orgFileNm);
			String saveFileNm = UUID.randomUUID() + "." + ext;

			String saveDirPath = filePath + File.separator + subDir;
			File saveDir = new File(saveDirPath);

			// 디렉터리가 없으면 생성
			if(!saveDir.exists()) {
				saveDir.mkdir();
			}

			File saveFile = new File(saveDirPath,saveFileNm);
			file.transferTo(saveFile);

	        return FileDto.builder()
                    .orgFileNm(orgFileNm)
                    .saveFileNm(saveFileNm)
                    .filePath(saveDirPath)
                    .fileSize(file.getSize())
                    .fileExt(ext)
                    .build();
		}catch(IOException e) {
			  throw new RuntimeException("파일 저장 실패", e);
		}
	}

	 private String getExtension(String fileName) {
	        if (fileName == null || !fileName.contains(".")) {
	            return "";
	        }
	        return fileName.substring(fileName.lastIndexOf(".") + 1);
	    }

}
