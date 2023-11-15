//package es.in2.blockchainconnector.core.utils;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.MockitoAnnotations;
//
//import java.security.NoSuchAlgorithmException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//
//class ApplicationUtilsTest {
//
//    @InjectMocks
//    private ApplicationUtils applicationUtils;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testCalculateSHA256Hash() throws NoSuchAlgorithmException {
//        // Arrange
//        String data = "Sample data";
//        String expectedHash = "d04815cd381cbd2edff87af962484e398563c328d1786f86b78c86fdb98c2de6";
//
//        // Act
//        String result = applicationUtils.calculateSHA256Hash(data);
//
//        // Assert
//        assertEquals(expectedHash, result);
//    }
//}
//
