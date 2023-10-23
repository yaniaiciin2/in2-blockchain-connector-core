package es.in2.blockchain.connector.integration.orionld.controller;


import es.in2.blockchain.connector.core.service.impl.OnChainServiceImpl;
import es.in2.blockchain.connector.integration.orionld.service.OrionLdNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;


@AutoConfigureMockMvc
class OrionLdNotificationControllerTest {
    @Mock
    private OnChainServiceImpl onChainEntityService;

    @Mock
    private OrionLdNotificationService orionLdNotificationService;

    @InjectMocks
    private OrionLdNotificationController orionLdNotificationController;
    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(orionLdNotificationController).build();
    }

    @Test
    void createAndPublishEntityWithValidDatathenReturn200() {

        String jsonRequest = """
                {
                  "id": "urn:ngsi-ld:Notification:b0f522da-489c-11ee-9f5d-0242ac1c0009",
                  "type": "Notification",
                  "subscriptionId": "urn:ngsi-ld:Subscription:default1693428833476",
                  "notifiedAt": "2023-09-01T07:53:51.598Z",
                  "data": [
                    {
                      "id": "urn:ngsi-ld:product-offering:1234",
                      "type": "ProductOffering",
                      "category": {
                        "type": "Property",
                        "value": "B2C product orders"
                      },
                      "channel": {
                        "type": "Property",
                        "value": "Used channel for order capture",
                        "@id": {
                          "type": "Property",
                          "value": "1"
                        },
                        "name": {
                          "type": "Property",
                          "value": "Online chanel"
                        },
                        "role": {
                          "type": "Property",
                          "value": "Used channel for order captures"
                        }
                      },
                      "description": {
                        "type": "Property",
                        "value": "Product Order illustration sample"
                      },
                      "externalId": {
                        "type": "Property",
                        "value": "PO-456"
                      },
                      "note": {
                        "type": "Property",
                        "value": "This is a TMF product order illustration",
                        "author": {
                          "type": "Property",
                          "value": "Jean Pontus"
                        },
                        "date": {
                          "type": "Property",
                          "value": "2019-04-30T08:13:59.509Z"
                        },
                        "@id": {
                          "type": "Property",
                          "value": "1"
                        },
                        "text": {
                          "type": "Property",
                          "value": "This is a TMF product order illustration"
                        }
                      },
                      "priority": {
                        "type": "Property",
                        "value": "1"
                      },
                      "productOrderItem": {
                        "type": "Property",
                        "value": [
                          {
                            "@type": "ProductOrderItem",
                            "action": "add",
                            "id": "100",
                            "productOffering": {
                              "href": "https://host:port/productCatalogManagement/v4/productOffering/14277",
                              "id": "14277",
                              "name": "TMF25"
                            },
                            "productOrderItemRelationship": [
                              {
                                "id": "110",
                                "relationshipType": "bundles"
                              },
                              {
                                "id": "120",
                                "relationshipType": "bundles"
                              },
                              {
                                "id": "130",
                                "relationshipType": "bundles"
                              }
                            ],
                            "quantity": 1
                          },
                          {
                            "@type": "ProductOrderItem",
                            "action": "add",
                            "id": "110",
                            "itemPrice": [
                              {
                                "description": "Access Fee",
                                "name": "Access Fee",
                                "price": {
                                  "dutyFreeAmount": {
                                    "unit": "EUR",
                                    "value": 0.99
                                  },
                                  "taxIncludedAmount": {
                                    "unit": "EUT",
                                    "value": 0.99
                                  },
                                  "taxRate": 0
                                },
                                "priceType": "nonRecurring"
                              }
                            ],
                            "payment": [
                              {
                                "@referredType": "Payment",
                                "@type": "CashPayment",
                                "href": "https://host:port/paymentManagement/v4/cashPayment/2365",
                                "id": "2365",
                                "name": "Cash payment for access fee"
                              }
                            ],
                            "product": {
                              "@type": "Product",
                              "isBundle": false,
                              "productCharacteristic": [
                                {
                                  "name": "TEL_MSISDN",
                                  "value": "415 279 7439",
                                  "valueType": "string"
                                }
                              ],
                              "productSpecification": {
                                "@type": "ProductSpecificationRef",
                                "href": "https://host:port/productCatalogManagement/v4/productSpecification/14307",
                                "id": "14307",
                                "name": "Mobile Telephony",
                                "version": "1"
                              }
                            },
                            "productOffering": {
                              "href": "https://host:port/productCatalogManagement/v4/productOffering/14305",
                              "id": "14305",
                              "name": "TMF Mobile Telephony"
                            },
                            "quantity": 1
                          },
                          {
                            "@type": "ProductOrderItem",
                            "action": "add",
                            "id": "120",
                            "itemPrice": [
                              {
                                "description": "Tariff plan monthly fee",
                                "name": "MonthlyFee",
                                "price": {
                                  "dutyFreeAmount": {
                                    "unit": "EUR",
                                    "value": 20
                                  },
                                  "taxIncludedAmount": {
                                    "unit": "EUR",
                                    "value": 20
                                  },
                                  "taxRate": 0
                                },
                                "priceAlteration": [
                                  {
                                    "applicationDuration": 3,
                                    "description": "20% for first 3 months",
                                    "name": "WelcomeDiscount",
                                    "price": {
                                      "@type": "price",
                                      "percentage": 20,
                                      "taxRate": 0
                                    },
                                    "priceType": "recurring",
                                    "priority": 1,
                                    "recurringChargePeriod": "month"
                                  }
                                ],
                                "priceType": "recurring",
                                "recurringChargePeriod": "month"
                              }
                            ],
                            "itemTerm": [
                              {
                                "description": "Tariff plan 12 Months commitment",
                                "duration": {
                                  "amount": 12,
                                  "units": "month"
                                },
                                "name": "12Months"
                              }
                            ],
                            "product": {
                              "@type": "Product",
                              "isBundle": false,
                              "productSpecification": {
                                "@type": "ProductSpecificationRef",
                                "href": "https://host:port/productCatalogManagement/v4/productSpecification/14395",
                                "id": "14395",
                                "name": "TMF Tariff plan",
                                "version": "1"
                              }
                            },
                            "productOffering": {
                              "href": "https://host:port/productCatalogManagement/v4/productOffering/14344",
                              "id": "14344",
                              "name": "TMF Tariff Plan"
                            },
                            "productOrderItemRelationship": [
                              {
                                "id": "110",
                                "relationshipType": "reliesOn"
                              }
                            ],
                            "quantity": 1
                          },
                          {
                            "@type": "ProductOrderItem",
                            "action": "add",
                            "id": "130",
                            "product": {
                              "@type": "Product",
                              "isBundle": false,
                              "productCharacteristic": [
                                {
                                  "name": "CoverageOptions",
                                  "value": "National",
                                  "valueType": "string"
                                }
                              ],
                              "productSpecification": {
                                "@type": "ProductSpecificationRef",
                                "href": "https://host:port/productCatalogManagement/v4/productSpecification/14353",
                                "id": "14353",
                                "name": "Coverage",
                                "version": "1"
                              }
                            },
                            "productOffering": {
                              "href": "https://host:port/productCatalogManagement/v4/productOffering/14354",
                              "id": "14354",
                              "name": "Coverage Options"
                            },
                            "productOrderItemRelationship": [
                              {
                                "id": "110",
                                "relationshipType": "reliesOn"
                              }
                            ],
                            "quantity": 1
                          }
                        ]
                      },
                      "relatedParty": {
                        "type": "Property",
                        "value": [
                          {
                            "@referredType": "Individual",
                            "@type": "RelatedParty",
                            "href": "https://host:port/partyManagement/v4/individual/456-dd-df45",
                            "id": "456-dd-df45",
                            "name": "Joe Doe",
                            "role": "Seller"
                          },
                          {
                            "@referredType": "Customer",
                            "@type": "RelatedParty",
                            "href": "https://host:port/partyRoleManagement/v4/customer/ff55-hjy4",
                            "id": "ff55-hjy4",
                            "name": "Jean Pontus"
                          }
                        ]
                      },
                      "requestedCompletionDate": {
                        "type": "Property",
                        "value": "2019-05-02T08:13:59.506Z"
                      },
                      "requestedStartDate": {
                        "type": "Property",
                        "value": "2019-05-03T08:13:59.506Z"
                      }
                    }
                  ]
                }""";


        webTestClient.post()
                .uri("/notifications/orion-ld")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);

    }


    @Test
    void createAndPublishEntityWithInvalidDataThenReturn400() {
        String jsonRequest = "{" +
                "\"id\":\"urn:ngsi-ld:Notification:b0f522da-489c-11ee-9f5d-0242ac1c0009\"," +
                "\"type\":\"Notification\"," +
                "\"subscriptionId\":\"urn:ngsi-ld:Subscription:default1693428833476\"," +
                "\"notifiedAt\":\"2023-09-01T07:53:51.598Z\"," +
                "\"data\":[{" +
                "\"id\":\"urn:ngsi-ld:product-offering:1234\"," +
                "\"type\":\"ProductOffering\"," +
                "\"category\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"B2C product orders\"" +
                "}," +
                "\"channel\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"Used channel for order capture\"," +
                "\"@id\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"1\"" +
                "}," +
                "\"name\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"Online chanel\"" +
                "}," +
                "\"role\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"Used channel for order captures\"" +
                "}" +
                "}," +
                "\"description\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"Product Order illustration sample\"" +
                "}," +
                "\"externalId\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"PO-456\"" +
                "}," +
                "\"note\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"This is a TMF product order illustration\"," +
                "\"author\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"Jean Pontus\"" +
                "}," +
                "\"date\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"2019-04-30T08:13:59.509Z\"" +
                "}," +
                "\"@id\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"1\"" +
                "}," +
                "\"text\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"This is a TMF product order illustration\"" +
                "}" +
                "}," +
                "\"priority\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"1\"" +
                "}," +
                "\"productOrderItem\":{" +
                "\"type\":\"Property\"," +
                "\"value\":[" +
                "{" +
                "\"@type\":\"ProductOrderItem\"," +
                "\"action\":\"add\"," +
                "\"id\":\"100\"," +
                "\"productOffering\":{" +
                "\"href\":\"https://host:port/productCatalogManagement/v4/productOffering/14277\"," +
                "\"id\":\"14277\"," +
                "\"name\":\"TMF25\"" +
                "}," +
                "\"productOrderItemRelationship\":[" +
                "{" +
                "\"id\":\"110\"," +
                "\"relationshipType\":\"bundles\"" +
                "}," +
                "{" +
                "\"id\":\"120\"," +
                "\"relationshipType\":\"bundles\"" +
                "}," +
                "{" +
                "\"id\":\"130\"," +
                "\"relationshipType\":\"bundles\"" +
                "}" +
                "]," +
                "\"quantity\":1" +
                "}," +
                "{" +
                "\"@type\":\"ProductOrderItem\"," +
                "\"action\":\"add\"," +
                "\"id\":\"110\"," +
                "\"itemPrice\":[{" +
                "\"description\":\"Access Fee\"," +
                "\"name\":\"Access Fee\"," +
                "\"price\":{" +
                "\"dutyFreeAmount\":{" +
                "\"unit\":\"EUR\"," +
                "\"value\":0.99" +
                "}," +
                "\"taxIncludedAmount\":{" +
                "\"unit\":\"EUT\"," +
                "\"value\":0.99" +
                "}," +
                "\"taxRate\":0" +
                "}," +
                "\"priceType\":\"nonRecurring\"" +
                "}]," +
                "\"payment\":[{" +
                "\"@referredType\":\"Payment\"," +
                "\"@type\":\"CashPayment\"," +
                "\"href\":\"https://host:port/paymentManagement/v4/cashPayment/2365\"," +
                "\"id\":\"2365\"," +
                "\"name\":\"Cash payment for access fee\"" +
                "}]," +
                "\"product\":{" +
                "\"@type\":\"Product\"," +
                "\"isBundle\":false," +
                "\"productCharacteristic\":[{" +
                "\"name\":\"TEL_MSISDN\"," +
                "\"value\":\"415 279 7439\"," +
                "\"valueType\":\"string\"" +
                "}]," +
                "\"productSpecification\":{" +
                "\"@type\":\"ProductSpecificationRef\"," +
                "\"href\":\"https://host:port/productCatalogManagement/v4/productSpecification/14307\"," +
                "\"id\":\"14307\"," +
                "\"name\":\"Mobile Telephony\"," +
                "\"version\":\"1\"" +
                "}" +
                "}," +
                "\"productOffering\":{" +
                "\"href\":\"https://host:port/productCatalogManagement/v4/productOffering/14305\"," +
                "\"id\":\"14305\"," +
                "\"name\":\"TMF Mobile Telephony\"" +
                "}," +
                "\"quantity\":1" +
                "}," +
                "{" +
                "\"@type\":\"ProductOrderItem\"," +
                "\"action\":\"add\"," +
                "\"id\":\"120\"," +
                "\"itemPrice\":[{" +
                "\"description\":\"Tariff plan monthly fee\"," +
                "\"name\":\"MonthlyFee\"," +
                "\"price\":{" +
                "\"dutyFreeAmount\":{" +
                "\"unit\":\"EUR\"," +
                "\"value\":20" +
                "}," +
                "\"taxIncludedAmount\":{" +
                "\"unit\":\"EUR\"," +
                "\"value\":20" +
                "}," +
                "\"taxRate\":0" +
                "}," +
                "\"priceAlteration\":[{" +
                "\"applicationDuration\":3," +
                "\"description\":\"20% for first 3 months\"," +
                "\"name\":\"WelcomeDiscount\"," +
                "\"price\":{" +
                "\"@type\":\"price\"," +
                "\"percentage\":20," +
                "\"taxRate\":0" +
                "}," +
                "\"priceType\":\"recurring\"," +
                "\"priority\":1," +
                "\"recurringChargePeriod\":\"month\"" +
                "}]," +
                "\"priceType\":\"recurring\"," +
                "\"recurringChargePeriod\":\"month\"" +
                "}]," +
                "\"itemTerm\":[{" +
                "\"description\":\"Tariff plan 12 Months commitment\"," +
                "\"duration\":{" +
                "\"amount\":12," +
                "\"units\":\"month\"" +
                "}," +
                "\"name\":\"12Months\"" +
                "}]," +
                "\"product\":{" +
                "\"@type\":\"Product\"," +
                "\"isBundle\":false," +
                "\"productSpecification\":{" +
                "\"@type\":\"ProductSpecificationRef\"," +
                "\"href\":\"https://host:port/productCatalogManagement/v4/productSpecification/14395\"," +
                "\"id\":\"14395\"," +
                "\"name\":\"TMF Tariff plan\"," +
                "\"version\":\"1\"" +
                "}" +
                "}," +
                "\"productOffering\":{" +
                "\"href\":\"https://host:port/productCatalogManagement/v4/productOffering/14344\"," +
                "\"id\":\"14344\"," +
                "\"name\":\"TMF Tariff Plan\"" +
                "}," +
                "\"productOrderItemRelationship\":[{" +
                "\"id\":\"110\"," +
                "\"relationshipType\":\"reliesOn\"" +
                "}]," +
                "\"quantity\":1" +
                "}," +
                "{" +
                "\"@type\":\"ProductOrderItem\"," +
                "\"action\":\"add\"," +
                "\"id\":\"130\"," +
                "\"product\":{" +
                "\"@type\":\"Product\"," +
                "\"isBundle\":false," +
                "\"productCharacteristic\":[{" +
                "\"name\":\"CoverageOptions\"," +
                "\"value\":\"National\"," +
                "\"valueType\":\"string\"" +
                "}]," +
                "\"productSpecification\":{" +
                "\"@type\":\"ProductSpecificationRef\"," +
                "\"href\":\"https://host:port/productCatalogManagement/v4/productSpecification/14353\"," +
                "\"id\":\"14353\"," +
                "\"name\":\"Coverage\"," +
                "\"version\":\"1\"" +
                "}" +
                "}," +
                "\"productOffering\":{" +
                "\"href\":\"https://host:port/productCatalogManagement/v4/productOffering/14354\"," +
                "\"id\":\"14354\"," +
                "\"name\":\"Coverage Options\"" +
                "}," +
                "\"productOrderItemRelationship\":[{" +
                "\"id\":\"110\"," +
                "\"relationshipType\":\"reliesOn\"" +
                "}]," +
                "\"quantity\":1" +
                "}]" +
                "}]," +
                "\"relatedParty\":{" +
                "\"type\":\"Property\"," +
                "\"value\":[" +
                "{" +
                "\"@referredType\":\"Individual\"," +
                "\"@type\":\"RelatedParty\"," +
                "\"href\":\"https://host:port/partyManagement/v4/individual/456-dd-df45\"," +
                "\"id\":\"456-dd-df45\"," +
                "\"name\":\"Joe Doe\"," +
                "\"role\":\"Seller\"" +
                "}," +
                "{" +
                "\"@referredType\":\"Customer\"," +
                "\"@type\":\"RelatedParty\"," +
                "\"href\":\"https://host:port/partyRoleManagement/v4/customer/ff55-hjy4\"," +
                "\"id\":\"ff55-hjy4\"," +
                "\"name\":\"Jean Pontus\"" +
                "}" +
                "]}" +
                "}," +
                "\"requestedCompletionDate\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"2019-05-02T08:13:59.506Z\"" +
                "}," +
                "\"requestedStartDate\":{" +
                "\"type\":\"Property\"," +
                "\"value\":\"2019-05-03T08:13:59.506Z\"" +
                "}" +
                "}";

        webTestClient.post()
                .uri("/notifications/orion-ld")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonRequest)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);


    }

















}

