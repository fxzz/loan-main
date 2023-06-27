package com.fastcampus.loan.controller;

import com.fastcampus.loan.dto.TermsDTO.Request;
import com.fastcampus.loan.dto.TermsDTO.Response;
import com.fastcampus.loan.dto.ResponseDTO;
import com.fastcampus.loan.service.TermsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/terms")
public class TermsController extends AbstractController {

  private final TermsService termsService;

  @PostMapping
  public ResponseDTO<Response> create(@RequestBody Request request) {
    return ok(termsService.create(request));
      /* 약관 등록
    request :

    {
    "name" : "신청 이용 약관",
    "termsDetailUrl" : "https://www.dddddd.com/ddddddddddddddd"

    }

    response :

      "result" : {
        "code" : "0000",
        "desc" : "success"
      },
      "data" : {
      "termsId" : 1,
      "name" : "신청 이용 약관",
      "termsDetailUrl" : "https://www.dddddd.com/ddddddddddddddd"
      "createdAt" : "2023-06-26T21:17:53.419123",
      "updatedAt" : "2023-06-26T21:17:53.419123"
      }

     */
  }

  // 전체 약관 조회
  @GetMapping()
  public ResponseDTO<List<Response>> getAll() {
    return ok(termsService.getAll());
  }
}
