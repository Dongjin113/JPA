package hellojpa.Section10JPQLBasicGrammar.Select;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//@Embeddable
public class Address {

    private String city;
    private String street;
    @Column(name = "ZIPCODE")
    private String zipcode;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
