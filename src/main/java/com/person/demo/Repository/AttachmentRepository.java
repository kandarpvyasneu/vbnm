package com.person.demo.Repository;

import com.person.demo.pojo.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByTransaction_id(Long transactionId);
}
