package aww.delp.models.groupon;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Merchant {
    @JsonProperty("uuid")
    String uuid;

    @JsonProperty("name")
    String name;

    @JsonProperty("websiteUrl")
    String websiteUrl;

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }
}
