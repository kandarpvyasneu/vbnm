package com.person.demo.Service;

import com.person.demo.Exception.AppException;
import com.person.demo.Repository.AttachmentRepository;
import com.person.demo.Repository.PersonRepository;
import com.person.demo.Repository.TransactionRepository;
import com.person.demo.pojo.Attachment;
import com.person.demo.pojo.Person;
import com.person.demo.pojo.Transaction;
//import org.apache.log4j.Logger;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class AttachmentService {

//    private AmazonClient amazonClient;
//
//    @Autowired
//    public AttachmentService(AmazonClient amazonClient) {
//        this.amazonClient = amazonClient;
//    }
//
//    @Autowired
//    private Environment environment;
//
//    public boolean isBucketExists() {
//        return amazonClient.isBucketExists();
//    }
/*

    //private static final Logger LOG = Logger.getLogger(AttachmentService.class);
    private static String UPLOADED_FOLDER = "/Users/urvi/Fall_2018/CloudComputing/" +
            "dev/csye6225-fall2018/webapp/cloud_application/src/main/resources/Uploaded_Files";

    private AmazonClient amazonClient;

    @Autowired
    public AttachmentService(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MyUserDetailsService userService;

    @Autowired
    private Environment environment;

    public List<Attachment> getAllByTransactionId(Long transactionId) throws AppException {
        //LOG.info("Getting all attachments for transaction with id = "+transactionId);
        try {
            Person person = personRepository.findPersonByPersonEmail(userService.userName);
            Transaction transaction = transactionRepository.findOneById(transactionId);
            if(transaction.getId() != null) {
                if (person.equals(transaction.getPerson()))
                    return attachmentRepository.findAllByTransaction_id(transactionId);
                else
                    return null;
            }
            else
                return null;
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error getting attachment for transaction with id = "+transactionId, e);
            throw new AppException("Error getting attachment for transaction with id = "+transactionId);
        }
    }

    public ResponseEntity<String> createByTransactionId(MultipartFile reciept, Long transactionId) throws AppException {
        //LOG.info("Creating new attachment for transaction with id = "+transactionId);
        try {
                Person person = personRepository.findPersonByPersonEmail(userService.userName);
                Transaction transaction = transactionRepository.findOneById(transactionId);
                if(person.equals(transaction.getPerson())) {
                    Attachment attachment = new Attachment();
                    if(environment.getActiveProfiles().length!=0){
                        String url = amazonClient.uploadFile(reciept);
                        attachment.setUrl(url);
                        attachment.setTransaction(transaction);
                        Attachment attachment1 = attachmentRepository.save(attachment);
                        return ResponseEntity.ok("Receipt attached to transaction and stored in S3 Bucket on AWS");
                    }
                    else {
                        String mimeType = reciept.getContentType();
                        String type = mimeType.split("/")[1];
                        if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpeg")
                                || type.equalsIgnoreCase("jpg")) {
                            byte[] bytes = reciept.getBytes();
                            Path path = Paths.get(UPLOADED_FOLDER + "/" + reciept.getOriginalFilename());
                            Files.write(path, bytes);
                            attachment.setUrl(path.toString());
                            attachment.setTransaction(transaction);
                            Attachment attachment1 = attachmentRepository.save(attachment);
                        } else
                            return ResponseEntity.ok("File Type not supported. Supported file types are: '.png','.jpeg','.jpg'");
                    return ResponseEntity.ok("Receipt attached to transaction and stored in local database");
                    }
                }
                else
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Permission denied to add receipt to this transaction");
        } catch (DataException e){
           // LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error creating new attachment for transaction with id = "+transactionId, e);
            throw new AppException("Error creating new attachment for transaction with id = "+transactionId);
        }
    }

    public ResponseEntity<String> updateByTransactionId(MultipartFile receipt, Long transactionId, Long attachmentId) throws AppException {
       // LOG.info("Updating attachment with id = "+attachmentId+" for transaction with id = "+transactionId);
        try {
            Person person = personRepository.findPersonByPersonEmail(userService.userName);
            Transaction transaction = transactionRepository.findOneById(transactionId);
            if(person.equals(transaction.getPerson())) {
                List<Attachment> attachments = attachmentRepository.findAllByTransaction_id(transactionId);
                for (Attachment attachment : attachments) {
                    if(attachment.getId() == attachmentId){
                        if(environment.getActiveProfiles().length!=0){
                            String url = amazonClient.uploadFile(receipt);
                            attachment.setUrl(url);
                            amazonClient.deleteFileFromS3Bucket(attachmentRepository.getOne(attachmentId).getUrl());
                            Attachment attachment1 = attachmentRepository.save(attachment);
                            return ResponseEntity.ok("Receipt updated to transaction and stored updated one in S3 Bucket on AWS");
                        }
                        else {
                            String mimeType = receipt.getContentType();
                            String type = mimeType.split("/")[1];
                            if (type.equalsIgnoreCase("png") || type.equalsIgnoreCase("jpeg")
                                    || type.equalsIgnoreCase("jpg")) {
                                byte[] bytes = receipt.getBytes();
                                Path path = Paths.get(UPLOADED_FOLDER + "/" + receipt.getOriginalFilename());
                                Files.write(path, bytes);
                                File file = new File(attachment.getUrl());
                                file.delete();
                                attachment.setUrl(path.toString());
                                Attachment attachment1 = attachmentRepository.save(attachment);
                            } else
                                return ResponseEntity.ok("File Type not supported. Supported file types are: '.png','.jpeg','.jpg'");
                        }
                        break;
                    }
                    else {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Attachment for this transaction doesn't exist");
                    }
                }
                return ResponseEntity.ok("Receipt updated for your transaction");
            }
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Permission denied to update this transaction");
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error updating attachment with id = "+attachmentId+" for transaction with id = "+transactionId, e);
            throw new AppException("Error updating attachment with id = "+attachmentId+" for transaction with id = "+transactionId);
        }
    }

    public ResponseEntity<String> deleteByTransactionId(Long transactionId, Long attachmentId) throws AppException {
        //LOG.info("Deleting attachment with id = "+attachmentId+" for transaction with id = "+transactionId);
        try {
            Person person = personRepository.findPersonByPersonEmail(userService.userName);
            Transaction transaction = transactionRepository.findOneById(transactionId);
            if(person.equals(transaction.getPerson())) {
                if(environment.getActiveProfiles().length == 0) {
                    File file = new File(attachmentRepository.getOne(attachmentId).getUrl());
                    file.delete();
                    attachmentRepository.deleteById(attachmentId);
                    return ResponseEntity.ok("Receipt was deleted");
                }
                else {
                    amazonClient.deleteFileFromS3Bucket(attachmentRepository.getOne(attachmentId).getUrl());
                    return ResponseEntity.ok("Receipt was deleted from S3 bucket");
                }
            }
            else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Permission denied to delete this receipt");
        } catch (DataException e){
            //LOG.error(e.getMessage());
            throw new AppException(400, e.getMessage());
        } catch (Exception e) {
            //LOG.error("Error deleting attachment with id = "+attachmentId+" for transaction with id = "+transactionId, e);
            throw new AppException("Error deleting attachment with id = "+attachmentId+" for transaction with id = "+transactionId);
        }
    }
*/

}
