package model;

import java.util.ArrayList;
import java.util.List;

public class ContactList {

    private static List<Contact> allContacts = new ArrayList<>();

    public static List<Contact> getAllContacts() {
        return allContacts;
    }

    public static void addContact(Contact contact) {
        allContacts.add(contact);
    }

    public static void deleteContact(Contact contact) {
        allContacts.remove(contact);
    }

    public static List<Contact> getContactsByNames(List<String> contactNames) {
        List<Contact> contacts = new ArrayList<>();
        for (String contactName : contactNames) {
            for (Contact contact : allContacts) {
                if (contact.getContactName().equals(contactName)) {
                    contacts.add(contact);
                    break;
                }
            }
        }
        return contacts;
    }


}
