package hellojpa.Section9ValueType.ValueTypeImmutabilityObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;

//@Embeddable
public class ImmutaionAddress {
    @Column(name = "CITY_ZIPCODE")
    private String city;
    @Column(name = "STREET_ZIPCODE")
    private String street;
    @Column(name = "IMMUTAION_ZIPCODE")
    private String zipcode;

    public ImmutaionAddress() {
    }

    public ImmutaionAddress(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }

    //set을 다지우거나 private형태로 사용한다


    private void setCity(String city) {
        this.city = city;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    private void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
