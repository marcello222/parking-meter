package com.fiap.parking.meter.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSnsConfig {

    @Value("${aws.region}")
    private String region;

    @Value("${aws.secret.access.key}")
    private String accessKey;

    @Value("${aws.access.key.id}")
    private String secretKey;

    @Value("${aws.sns.topic.arn}")
    private String parkingMeterTopicArn;



    @Bean
    public AmazonSNS amazonSNSBuilder() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonSNSClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    @Bean
    @Qualifier("parkingMeterEventsTopic")
    public Topic snsCatalogTopicBuilder() {
        return new Topic().withTopicArn(parkingMeterTopicArn);
    }

}
