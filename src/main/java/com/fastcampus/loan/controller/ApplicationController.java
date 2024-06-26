package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.ApplicationDTO.AcceptTerms;
import com.fastcampus.loan.dto.ApplicationDTO.Request;
import com.fastcampus.loan.dto.ApplicationDTO.Response;
import com.fastcampus.loan.dto.FileDTO;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.ApplicationService;
import com.fastcampus.loan.service.FileStorageService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/applications")
public class ApplicationController extends AbstractController {

  private final ApplicationService applicationService;

  private final FileStorageService fileStorageService;

  @PostMapping
  public ResponseDTO<Response> create(@RequestBody Request request) {
    return ok(applicationService.create(request));

    /*
    request :
    신청 등록
    {
    "name" : "김아무",
    "cellPhone" : "010-1111-2222",
    "email" : "ssss@ffff.ffff.com",
    "hopeAmount" : "5000000"
    }

    response :

      "result" : {
        "code" : "0000",
        "desc" : "success"
      },
      "data" : {
      "applicationId" : 1,
      "name" : "김아무",
      "cellPhone" : "010-1111-2222",
      "email" : "ssss@ffff.ffff.com",
      "hopeAmount" : "5000000",
      "appliedAt" : "2023-06-26T21:17:53.419123",
      "createdAt" : "2023-06-26T21:17:53.419123",
      "updatedAt" : "2023-06-26T21:17:53.419123"
      }

     */
  }

  @GetMapping("/{applicationId}")
  public ResponseDTO<Response> get(@PathVariable Long applicationId) {
    return ok(applicationService.get(applicationId));


     /*
    request :

   localhost:8080/applications/1


    response :

      "result" : {
        "code" : "0000",
        "desc" : "success"
      },
      "data" : {
      "applicationId" : 1,
      "name" : "김아무",
      "cellPhone" : "010-1111-2222",
      "email" : "ssss@ffff.ffff.com",
      "hopeAmount" : "5000000",
      "appliedAt" : "2023-06-26T21:17:53.419123",
      "createdAt" : "2023-06-26T21:17:53.419123",
      "updatedAt" : "2023-06-26T21:17:53.419123"
      }

     */
  }

  @PutMapping("/{applicationId}")
  public ResponseDTO<Response> update(@PathVariable Long applicationId, @RequestBody Request request) {
    return ok(applicationService.update(applicationId, request));
  }

  @DeleteMapping("/{applicationId}")
  public ResponseDTO<Void> delete(@PathVariable Long applicationId) {
    applicationService.delete(applicationId);
    return ok();
  }

  @PostMapping("/{applicationId}/terms")
  public ResponseDTO<Boolean> acceptTerms(@PathVariable Long applicationId, @RequestBody AcceptTerms request) {
    return ok(applicationService.acceptTerms(applicationId, request));

         /*
    request :
   신청 약관 동의
   localhost:8080/applications/1/terms
   {
    "acceptTermsIds" : [1, 2]
   }


    response :
      {
      "result" : {
        "code" : "0000",
        "desc" : "success"
      },
      "data" : true

      }

     */
  }

  @PostMapping(value = "/{applicationId}/files")
  public ResponseDTO<Void> upload(@PathVariable Long applicationId, MultipartFile file) throws IllegalStateException {
    fileStorageService.save(applicationId, file);
    return ok();
  }

  @GetMapping("/{applicationId}/files")
  public ResponseEntity<Resource> download(@PathVariable Long applicationId, @RequestParam(value="filename") String filename) throws IllegalStateException, IOException {
    Resource file = fileStorageService.load(applicationId, filename);
    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }

  @DeleteMapping("/{applicationId}/files")
  public ResponseDTO<Void> deleteAll(@PathVariable Long applicationId) {
    fileStorageService.deleteAll(applicationId);
    return ok();
  }

  @GetMapping("/{applicationId}/files/info")
  public ResponseDTO<List<FileDTO>> getFileInfos(@PathVariable Long applicationId) {
    List<FileDTO> fileInfos = fileStorageService.loadAll(applicationId).map(path -> {
      String fileName= path.getFileName().toString();
      return FileDTO.builder()
          .name(fileName)
          .url(MvcUriComponentsBuilder.fromMethodName(ApplicationController.class, "download", applicationId, fileName).build().toString()).build();
    }).collect(Collectors.toList());

    return ok(fileInfos);
  }

  @PutMapping("/{applicationId}/contract")
  public ResponseDTO<Response> contract(@PathVariable Long applicationId) {
    return ok(applicationService.contract(applicationId));

             /*
    request :
    대출 계약
   localhost:8080/applications/1/contract


    response :
      {
      "result" : {
        "code" : "0000",
        "desc" : "success"
      },
      "data" : null
      }

     */
  }
}
