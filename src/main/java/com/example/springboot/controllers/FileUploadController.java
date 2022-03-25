package com.example.springboot.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.example.springboot.models.ResponseObject;
import com.example.springboot.services.IStorageService;

@Controller
@RequestMapping(path = "/api/fileUpload")
public class FileUploadController {
	
	@Autowired
	private IStorageService storageService;
	
	@PostMapping("")
	public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file") MultipartFile file){
		try {
			String generatedFileName = storageService.storeFile(file);
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject(HttpStatus.OK.getReasonPhrase(), "Upload file successfully", generatedFileName)
					);
		}
		catch(Exception exception) {
			return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
					new ResponseObject(HttpStatus.NOT_IMPLEMENTED.getReasonPhrase(), exception.getMessage(), "")
					);
		}
	}
	
	@GetMapping("/files/{fileName:.+}")
	public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName){
		try {
			byte[] bytes = storageService.readFileContent(fileName);
			return ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.IMAGE_JPEG)
					.body(bytes);
		}
		catch(Exception exception) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
		
	}
	
	@GetMapping()
	public ResponseEntity<ResponseObject> getUploadedFiles(){
		try {
			List<String> urls = storageService.loadAll()
					.map(path -> {
						String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class, "readDetailFile", 
								path.getFileName().toString()).build().toUri().toString();
						return urlPath;
					})
					.collect(Collectors.toList());
			
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject(HttpStatus.OK.getReasonPhrase(), "List files successfully", urls)
					);
		}
		catch(Exception exception) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
					new ResponseObject(HttpStatus.NO_CONTENT.getReasonPhrase(), "List files failed", new String[] {})
					);
		}
	}
	
}
