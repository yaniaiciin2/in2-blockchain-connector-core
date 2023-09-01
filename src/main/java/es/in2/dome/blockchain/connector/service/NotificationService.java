package es.in2.dome.blockchain.connector.service;


import es.in2.dome.blockchain.connector.exception.InvalidHashlinkComparisonException;

public interface NotificationService {
     void processNotification(String data);


     void processEvent(String data) throws InvalidHashlinkComparisonException;
}
