package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping.OneTable;

import hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping.OneTable.Item;

import javax.persistence.Entity;

//@Entity
public class Album extends Item {

    private String artist;

}
