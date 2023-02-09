package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping.OneParentThreeChild;

import hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping.Item;

import javax.persistence.Entity;

//@Entity
public class Album extends Item {

    private String artist;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
