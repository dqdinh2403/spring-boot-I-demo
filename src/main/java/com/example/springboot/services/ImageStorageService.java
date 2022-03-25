package com.example.springboot.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageStorageService implements IStorageService {
	
	private final Path storageFolder = Paths.get("uploads");
	
	public ImageStorageService() {
		try {
			Files.createDirectories(storageFolder);
		}
		catch(IOException exception) {
			throw new RuntimeException("Cannot initialize storage", exception);
		}
	}
	
	private boolean isImageFile(MultipartFile file){
		String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
		List<String> imageExtensions = Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"});
		return imageExtensions.contains(fileExtension.trim().toLowerCase());
	}

	@Override
	public String storeFile(MultipartFile file) {
		try {
			if(file.isEmpty()) {
				throw new RuntimeException("Failed to store empty file");
			}
			
			if(!isImageFile(file)) {
				throw new RuntimeException("You can only upload image file");
			}
			
			float fileSizeInMegabytes = file.getSize() / 1_000_000.0f;
			if(fileSizeInMegabytes > 5.0f) {
				throw new RuntimeException("File must be <= 5MB");
			}
			
			String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
			String generatedFileName = UUID.randomUUID().toString().replace("-", "");
			generatedFileName = generatedFileName + "." + fileExtension;
			Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName))
											.normalize().toAbsolutePath();
			if(!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())) {
				throw new RuntimeException("Cannot store file outside current directory");
			}
			
			try(InputStream inputStream = file.getInputStream()){
				Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
			}
			
			return generatedFileName;
		}
		catch(IOException exception) {
			throw new RuntimeException("Failed to store file", exception);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.storageFolder, 1)
					.filter(path -> {
						return !path.equals(this.storageFolder);
					})
					.map(this.storageFolder::relativize);
		}
		catch(IOException exception) {
			throw new RuntimeException("Failed to load stored files", exception);
		}
	}

	@Override
	public byte[] readFileContent(String fileName) {
		try {
			Path file = this.storageFolder.resolve(fileName);
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {
				byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
				return bytes;
			}
			else {
				throw new RuntimeException("Could not read file: " + fileName);
			}
		}
		catch(IOException exception) {
			throw new RuntimeException("Could not read file: " + fileName, exception);
		}
	}

	@Override
	public void deleteAllFiles() {
		// TODO Auto-generated method stub
	}

}
