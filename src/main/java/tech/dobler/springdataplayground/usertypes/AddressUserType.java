package tech.dobler.springdataplayground.usertypes;

import tech.dobler.springdataplayground.domainvalues.Address;

public class AddressUserType extends AbstractConversionUserType<Address, String>{
    public AddressUserType() {
        super(Address.class, String.class, Address::toDb, Address::fromDb);
    }
}
