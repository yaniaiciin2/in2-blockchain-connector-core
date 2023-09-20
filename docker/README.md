# Build the ecosystem application

1. docker-compose build 
    ```
    cd docker
    docker-compose up -d
    ```
2. Execute the request to Post your ProductOffering to Provider's Orion-LD
    ```
    curl --location 'http://localhost:1027/ngsi-ld/v1/entities' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "@type": "ProductOffering",
        "id": "urn:ngsi-ld:product-offering:1234",
        "category": "B2C product orders",
        "channel": [
            {
                "id": "1",
                "name": "Online chanel",
                "role": "Used channel for order captures",
                "value": "Used channel for order capture"
            }
        ],
        "description": "Product Order illustration sample",
        "externalId": "PO-456",
        "note": [
            {
                "author": "Jean Pontus",
                "date": "2019-04-30T08:13:59.509Z",
                "id": "1",
                "text": "This is a TMF product order illustration",
                "value": "This is a TMF product order illustration"
            }
        ],
        "priority": "1",
        "productOrderItem": [
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
                "billingAccount": {
                    "@type": "BillingAccount",
                    "href": "https://host:port/billingAccountManagement/v4/billingAccount/1513",
                    "id": "1513"
                },
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
        ],
        "relatedParty": [
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
        ],
        "requestedCompletionDate": "2019-05-02T08:13:59.506Z",
        "requestedStartDate": "2019-05-03T08:13:59.506Z"
    }'
    ```
3. Check that your entity was stored in the Provider's Orion-LD
    ```
    curl --location --request GET 'http://localhost:1027/ngsi-ld/v1/entities/urn:ngsi-ld:product-offering:1234'
    ```
   
    Between these two steps, the entity was convert into a DOME Event and it is published to a Blockchain Node. 
    Marketplace's is subscribed to the same Blockchain Node, and it receives the Blockchain Event.  
    Then, Marketplace retrieve the entity from Provider's Orion-LD and stores it into its own Orion-LD.

4. Then, check that your entity was stores in the Marketplace's Orion-LD
    ```
    curl --location --request GET 'http://localhost:1037/ngsi-ld/v1/entities/urn:ngsi-ld:product-offering:1234'
    ```

