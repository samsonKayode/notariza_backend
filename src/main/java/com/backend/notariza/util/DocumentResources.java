package com.backend.notariza.util;

import com.backend.notariza.service.custom.AWSS3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
@Scope("singleton")
public class DocumentResources {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    public DocumentResources() {

    }

    @Autowired
    private AWSS3Service awsService;

    public DocumentResourcesListPojo getData(){

        List<DocumentResourcesListPojo> list = new ArrayList<>();
        DocumentResourcesListPojo documentResourcesListPojo = new DocumentResourcesListPojo();
        try{
            byte[] signature = awsService.downloadFile("resources/notariza_signature.png");
            byte[] stamp1 = awsService.downloadFile("resources/notariza_stamp1.png");
            byte[] redSeal = awsService.downloadFile("resources/notariza_seal_red.png");
            byte[] plainSeal = awsService.downloadFile("resources/notariza_seal.png");
            byte[] ctc = awsService.downloadFile("resources/notariza_stamp_ctc.png");

            documentResourcesListPojo.setCtc(ctc);
            documentResourcesListPojo.setPlainSeal(plainSeal);
            documentResourcesListPojo.setRedSeal(redSeal);
            documentResourcesListPojo.setSignature(signature);
            documentResourcesListPojo.setStamp1(stamp1);

            return documentResourcesListPojo;

        }catch (Exception exception){
            throw new RuntimeException("error downloading files");
        }

    }

    public byte[] getResource(String filename){

        try{
            byte[] result = awsService.downloadFile(filename);

            return result;
        }catch (Exception exception){
            throw new RuntimeException("Error downloading file using document Resource..");
        }
    }
}
