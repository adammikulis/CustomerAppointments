package model;

import helper.ClientQuery;
import helper.ContactQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ContactList {

    private static ContactQuery contactQuery = new ContactQuery();
    private static List<Contact> contacts = contactQuery.getAllContacts();
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList(contacts);
    private static ObservableList<Contact> filteredContacts = FXCollections.observableArrayList();

    public static ObservableList<Contact> getAllContacts() {
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
