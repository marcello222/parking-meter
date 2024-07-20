package com.fiap.parking.meter.notification.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.fiap.parking.meter.entity.DriverEntity;
import com.fiap.parking.meter.service.ParkingPeriodStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationService {

    @Autowired
    private List<ParkingPeriodStrategy> strategies;

    private AmazonSimpleEmailService sesClient;

    @Value("${email.sender}")
    private String from;

    @Autowired
    public NotificationService(@Value("${aws.access.key.id}") String accessKeyId,
                               @Value("${aws.secret.access.key}") String secretAccessKey) {
        this.sesClient = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKeyId, secretAccessKey)))
                .build();
    }


    @Async
    public void sendToDrivers(List<DriverEntity> drivers, String message) {
        for (DriverEntity driver : drivers) {
            String FROM = from;
            String TO = driver.getEmail();
            String SUBJECT = "Parking Notification";
            String BODY = message;

            try {
                SendEmailRequest request = new SendEmailRequest()
                        .withDestination(new Destination().withToAddresses(TO))
                        .withMessage(new Message()
                                .withBody(new Body()
                                        .withText(new Content()
                                                .withCharset("UTF-8").withData(BODY)))
                                .withSubject(new Content()
                                        .withCharset("UTF-8").withData(SUBJECT)))
                        .withSource(FROM);
                sesClient.sendEmail(request);
                System.out.println("Email sent to: " + TO);
            } catch (Exception ex) {
                System.out.println("The email was not sent to " + TO + ". Error message: " + ex.getMessage());
            }
        }
    }

}