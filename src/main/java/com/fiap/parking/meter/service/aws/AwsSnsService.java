package com.fiap.parking.meter.service.aws;


import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.Topic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AwsSnsService {


    private final AmazonSNS amazonSnsClient;
    private final Topic parkingMeterTopic;

    public AwsSnsService(AmazonSNS amazonSnsClient, @Qualifier("parkingMeterEventsTopic") Topic parkingMeterTopic) {
        this.amazonSnsClient = amazonSnsClient;
        this.parkingMeterTopic = parkingMeterTopic;
    }

    public void publish(MessageDto message) {
        this.amazonSnsClient.publish(parkingMeterTopic.getTopicArn(), message.message());
    }

}
