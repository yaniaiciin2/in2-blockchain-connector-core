package es.in2.blockchainconnector.configuration.properties;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import static es.in2.blockchainconnector.utils.Utils.isNullOrBlank;

public record OpenApiInfoLicenseProperties(String name, String url) {

    @ConstructorBinding
    public OpenApiInfoLicenseProperties(String name, String url) {
        this.name = isNullOrBlank(name) ? "Apache 2.0" : name;
        this.url = isNullOrBlank(url) ? "https://www.apache.org/licenses/LICENSE-2.0.html" : url;
    }

}
