package tech.dobler.springdataplayground.usertypes;

import tech.dobler.springdataplayground.domainvalues.PersonId;

public class PersonIdUserType extends AbstractConversionUserType<PersonId, String>{
    public PersonIdUserType() {
        super(PersonId.class, String.class, PersonId::value, PersonId::of);
    }
}
