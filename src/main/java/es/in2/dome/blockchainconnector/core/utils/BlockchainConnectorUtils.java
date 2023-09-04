package es.in2.dome.blockchainconnector.core.utils;


import org.springframework.stereotype.Component;

@Component
public class BlockchainConnectorUtils {

    private BlockchainConnectorUtils() {

    }

    public static final String HASHLINK_PARAMETER = "?hl=";
    public static final String DATA_FIELD = "data";
    public static final String ID_FIELD = "id";
    public static final String CONTENT_HEADER = "Content-Type";
    public static final String APPJSON_HEADER = "application/json";
    public static final String EVENT_PREFIX = "Event";

}
