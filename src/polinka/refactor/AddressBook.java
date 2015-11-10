package polinka.refactor;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AddressBook {

    public boolean hasMobile(String name) {
        return new AddressDb().findPerson(name).getPhoneNumber().getNumber().startsWith("070");
    }

    static {
        new Checker().start();
    }

    public int getSize() {
        return new AddressDb().getAll().size();
    }

    /**
     * Gets the given user's mobile phone number,
     * or null if he doesn't have one.
     */
    public String getMobile(String name) {
        return new AddressDb().findPerson(name).getPhoneNumber().getNumber();
    }

    /**
     * Returns all names in the book truncated to the given length.
     */
    public List<String> getAllNames(int maxLength) {
        List<String> names = new LinkedList<>();
        new AddressDb().getAll().stream().map(Person::getName).forEach(name -> {
            if (name.length() > maxLength) names.add(name.substring(0, maxLength));
            else names.add(name);
        });
        return names;
    }

    /**
     * Returns all people who have mobile phone numbers.
     */
    public List getMobilePhonesList() {
        List<Person> peopleWithMobilePhones = new LinkedList<>();
        return new AddressDb().getAll().stream()
                .filter(person -> person.getPhoneNumber().getNumber().startsWith("070"))
                .collect(Collectors.toList());

    }

    static class Checker extends Thread {
        public void run() {
            new AddressBook().getMobilePhonesList();
        }
    }

}
