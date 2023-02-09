package hellojpa.Section7LuxuriousMapping.InheritanceRelationMapping.InheritanceMapping.OneTable;

import javax.persistence.Entity;

//@Entity
public class Movie extends Item{

    private String director;
    private String actor;

}
