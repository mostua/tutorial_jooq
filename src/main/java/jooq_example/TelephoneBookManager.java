package jooq_example;

import jooq_sources.tables.pojos.Friend;
import jooq_sources.tables.pojos.PhoneNumber;

import java.util.List;

/**
 * Realizuje logike biznesową związana z dodawniem telefonów
 *
 * @author Adam Mościcki
 */


public interface TelephoneBookManager {
    /**
     * Pobiera listę znajomych
     * @return
     */
    List<Friend> getFriends();

    /**
     * Dodanie znajmego
     * @return
     */
    void addFriend(Friend friend);

    /**
     * Dodanie znajmego w raz z numerem telefonu
     * @return
     */
    void addFriendWithPhoneNumber(Friend friend, String phoneNumber);

    /**
     * Pobiera listę znajomych
     * @return
     */
    void removeFriend(Integer friendId);

    /**
     * Zwraca listę telefonów danego użytkownika
     * @param friendId
     * @return lista telefonów
     */
    List<PhoneNumber> getFriendPhones(Integer friendId);

    /**
     * Dodanie numeru telefonu dla danego użytkownika
     */
    void addPhoneToFriend(Integer friendId, String phoneNumber);

    /**
     * Usuniecie numeru telefonu
     */
    void removeNumber(Integer phoneId);
}
