package com.person.demo.controller;

import com.person.demo.Exception.AppException;
import com.person.demo.Service.AttachmentService;
import com.person.demo.pojo.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class AttachmentController {
/*
    @Autowired
    private AttachmentService attachmentService;

    @GetMapping("/{transactionId}/attachment")
    public ResponseEntity<List<Attachment>> getAllByTransactionId(@PathVariable Long transactionId) throws AppException {
        List<Attachment> attachmentList = attachmentService.getAllByTransactionId(transactionId);
        if(attachmentList == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        return ResponseEntity.ok(attachmentList);
    }

    @PostMapping("/{transactionId}/attachment")
    public ResponseEntity createByTransactionId(@RequestBody MultipartFile receipt, @PathVariable Long transactionId) throws AppException {
        ResponseEntity<String> responseEntity = attachmentService.createByTransactionId(receipt, transactionId);
        return responseEntity;
    }

    @PutMapping("/{transactionId}/attachment/{attachmentId}")
    public ResponseEntity updateByTransactionId(@RequestBody MultipartFile receipt, @PathVariable Long transactionId, @PathVariable Long attachmentId) throws AppException {
        ResponseEntity responseEntity = attachmentService.updateByTransactionId(receipt, transactionId, attachmentId);
        return responseEntity;
    }

    @DeleteMapping("/{transactionId}/attachment/{attachmentId}")
    public ResponseEntity deleteByTransactionId(@PathVariable Long transactionId, @PathVariable Long attachmentId) throws AppException {
        ResponseEntity responseEntity = attachmentService.deleteByTransactionId(transactionId, attachmentId);
        return responseEntity;
    }*/
}
