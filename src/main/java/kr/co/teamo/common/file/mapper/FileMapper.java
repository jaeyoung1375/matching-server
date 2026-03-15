package kr.co.teamo.common.file.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.co.teamo.common.file.dto.FileDto;

@Mapper
public interface FileMapper {

	void insertFile(FileDto file);
}
