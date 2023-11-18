package model;

import helper.ClientQuery;
import helper.ContactQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/** Class for creating a list of contacts*/
public class ContactList {

    private static ContactQuery contactQuery = new ContactQuery();
    private static List<Contact> contacts = contactQuery.getAllContacts();
    private static ObservableList<Contact> allContacts = FXCollections.observableArrayList(contacts);
    private static ObservableList<Contact> filteredContacts = FXCollections.observableArrayList();

    /** Returns all contacts as an observable list
     *
     * @return allContacts
     */
    public static ObservableList<Contact> getAllContacts() {
        return allContacts;
    }

    /** Adds contact to list
     *
     * @param contact contact to add
     */
    public static void addContact(Contact contact) {
        allContacts.add(contact);
    }

    /** Deletes contact from list
     *
     * @param contact contact to delete
     */
    public static void deleteContact(Contact contact) {
        allContacts.remove(contact);
    }

    /** Returns list of contacts from list of strings
     *
     * @param contactNames string list of contact names to parse
     * @return contacts
     */
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