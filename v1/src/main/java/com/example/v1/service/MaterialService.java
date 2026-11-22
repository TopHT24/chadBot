package com.example.v1.service;

import com.example.v1.dao.SourceMaterialDao;
import com.example.v1.dto.request.CreateSourceMaterialRequest;
import com.example.v1.dto.response.SourceMaterialResponse;
import com.example.v1.model.SourceMaterial;
import com.example.v1.model.enums.SourceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {

	private final SourceMaterialDao sourceMaterialDao;
	private final TextExtractor textExtractor;

	/** Stores the file's text (not the file itself, for now). */
	public SourceMaterialResponse upload(MultipartFile file, Long userId, String title) {
		if (file == null || file.isEmpty()) {
			throw new BadRequestException("No file was uploaded");
		}
		String content = textExtractor.extract(file);
		String filename = file.getOriginalFilename();
		SourceMaterial saved = sourceMaterialDao.save(SourceMaterial.builder()
				.userId(userId)
				.title(title == null || title.isBlank() ? withoutExtension(filename) : title)
				.type(SourceType.TEXT)
				.content(content)
				.fileRef(filename)
				.build());
		return SourceMaterialResponse.from(saved);
	}

	public SourceMaterialResponse addText(CreateSourceMaterialRequest request) {
		SourceMaterial saved = sourceMaterialDao.save(SourceMaterial.builder()
				.userId(request.userId())
				.title(request.title())
				.type(SourceType.TEXT)
				.content(request.content().strip())
				.build());
		return SourceMaterialResponse.from(saved);
	}

	public List<SourceMaterialResponse> findByUser(Long userId) {
		return sourceMaterialDao.findByUserId(userId).stream().map(SourceMaterialResponse::from).toList();
	}

	public SourceMaterialResponse get(Long id) {
		return SourceMaterialResponse.from(getEntity(id));
	}

	public void delete(Long id) {
		getEntity(id);
		sourceMaterialDao.deleteById(id);
	}

	SourceMaterial getEntity(Long id) {
		return sourceMaterialDao.findById(id).orElseThrow(() -> new NotFoundException("Material", id));
	}

	private static String withoutExtension(String filename) {
		if (filename == null || filename.isBlank()) {
			return "Untitled";
		}
		int dot = filename.lastIndexOf('.');
		return dot > 0 ? filename.substring(0, dot) : filename;
	}
}
