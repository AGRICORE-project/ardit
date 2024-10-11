package eu.agricore.indexer.ldap.model;

import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class LdapGroupAttributes implements AttributesMapper<LdapGroupDetails> {


    @Override
    public LdapGroupDetails mapFromAttributes(Attributes attributes) throws NamingException {

        LdapGroupDetails groupDetails = new LdapGroupDetails();
        List<String> groupMembers = new ArrayList<>();

        Iterator<String> iterator = (Iterator<String>) attributes.get("memberUid").getAll().asIterator();
        while (iterator.hasNext()) {
            groupMembers.add(iterator.next());
        }

        attributes.get("memberUid").get();

        groupDetails.setGroupId(Long.valueOf(attributes.get("gidNumber").get().toString()));
        groupDetails.setGroupMembers(groupMembers);
        groupDetails.setGroupName(attributes.get("cn").get().toString());

        return groupDetails;
    }
}
