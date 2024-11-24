package nh.springgraphql.remoteservice.publisher;


import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Nils Hartmann (nils@nilshartmann.net)
 */
record Publisher(
    String id,
    String name,
    @JsonProperty("contact_type") String contactType,
    @JsonProperty("contact_value") String contactValue) {
}
