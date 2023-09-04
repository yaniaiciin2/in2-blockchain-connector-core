package es.in2.dome.blockchainconnector.core.service;


import es.in2.dome.blockchainconnector.core.exception.InvalidHashlinkComparisonException;

public interface NotificationService {
     void processOrionLdNotification(String data);
     void processBlockchainNodeNotification(String data) throws InvalidHashlinkComparisonException;
}
