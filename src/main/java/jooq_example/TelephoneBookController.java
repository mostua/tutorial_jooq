package jooq_example;

import jooq_sources.tables.pojos.Friend;
import jooq_sources.tables.pojos.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TelephoneBookController {

    @Autowired
    private TelephoneBookManager telephoneBookManager;

    private Logger logger = LoggerFactory.getLogger(TelephoneBookController.class);


    String title = "Ksiazka kontaktowa";

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String myPhones() {
        StringBuilder content = new StringBuilder();
        List<Friend> friends = telephoneBookManager.getFriends();
        makeTableWithFriends(content, friends);
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }

    private void makeTableWithFriends(StringBuilder content, List<Friend> friends) {
        content.append("<h1>Twoja książka kontaktowa</h1>");
        content.append("<table>");
        content.append("<tr>");
        content.append("<th>");
        content.append("Lp");
        content.append("</th>");
        content.append("<th>");
        content.append("Imie");
        content.append("</th>");
        content.append("<th>");
        content.append("Nazwisko");
        content.append("</th>");
        content.append("<th>");
        content.append("</th>");
        content.append("</tr>");
        for (int i = 0; i < friends.size(); ++i) {
            Friend friend = friends.get(i);
            content.append("<tr>");
            content.append("<td>");
            content.append(i);
            content.append("</td>");
            content.append("<td>");
            content.append(String.format("<a href=\"/getPhones/%d\">", friend.getId()));
            content.append(friend.getName());
            content.append("</a>");
            content.append("</td>");
            content.append("<td>");
            content.append(friend.getSurname());
            content.append("</td>");
            content.append("<td>");
            content.append(String.format("<a href=\"/removeFriend/%d\">Usuń</a>", friend.getId()));
            content.append("</td>");
            content.append("</tr>");
        }
        content.append("</table>");
        content.append("Kliknij na imie aby wyświetlić numery");
        content.append("<br />");
        content.append("<a href=\"/addFriend\">Dodaj znajomego</a>");
    }

    @RequestMapping(value = "/addFriend", method = RequestMethod.GET)
    public String addFriend() {
        StringBuilder content = new StringBuilder();
        makeAddUserForm(content);
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }

    private void makeAddUserForm(StringBuilder content) {
        content.append("<form action=\"/addFriend\" method=\"POST\"");
        content.append("<label>Imie*</label>");
        content.append("<input type=\"text\" name=\"name\" placeholder=\"Imie\"></input>");
        content.append("<br />");
        content.append("<label>Nazwisko*</label>");
        content.append("<input type=\"text\" name=\"surname\" placeholder=\"Nazwisko\"></input>");
        content.append("<br />");
        content.append("<label>Numer tefonu **</label>");
        content.append("<input type=\"text\" name=\"phoneNumber\" placeholder=\"Numer telefonu\"></input>");
        content.append("<br />");
        content.append("<input type=\"submit\" value=\"Submit\"></input>");
        content.append("<br />");
        content.append("</form>");
        content.append("* pola są wymagane");
        content.append("<br />");
        content.append("** jeżeli podasz numer telefonu to zostanie on dodany");
        content.append("<br />");
    }

    @RequestMapping(value = "/addFriend", method = RequestMethod.POST)
    public String addFriend(@RequestParam String name, @RequestParam String surname, String phoneNumber) {
        Friend friend = new Friend(null, name, surname);
        if(phoneNumber == null) {
            telephoneBookManager.addFriend(friend);
        } else {
            telephoneBookManager.addFriendWithPhoneNumber(friend, phoneNumber);
        }
        StringBuilder content = new StringBuilder();
        content.append("<h2>Dodano</h2>");
        content.append("<a href=\"/\">Książka</a>");
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }

    @RequestMapping(value = "/getPhones/{friendId}", method = RequestMethod.GET)
    public String getPhonesFriend(@PathVariable("friendId")Integer friendId) {
        logger.debug("friendID {}", friendId);
        StringBuilder content = new StringBuilder();
        List<PhoneNumber> phones = telephoneBookManager.getFriendPhones(friendId);
        makePhonesTable(content, phones);
        content.append(String.format("<a href=\"/addPhone/%d\">Dodaj numer</a>", friendId));
        content.append("<br />");
        content.append("Kliknij na numer, aby usunąć");
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }

    private void makePhonesTable(StringBuilder content, List<PhoneNumber> phones) {
        content.append("<h1>Numery telefonów</h1>");
        content.append("<table>");
        content.append("<tr>");
        content.append("<th>");
        content.append("Lp");
        content.append("</th>");
        content.append("<th>");
        content.append("Numer");
        content.append("</th>");
        content.append("</tr>");
        for (int i = 0; i < phones.size(); ++i) {
            PhoneNumber phone = phones.get(i);
            content.append("<tr>");
            content.append("<td>");
            content.append(i);
            content.append("</td>");
            content.append("<td>");
            content.append(String.format("<a href=\"/removePhone/%d\">", phone.getId()));
            content.append(phone.getPhone());
            content.append("</a>");
            content.append("</td>");
            content.append("</tr>");
        }
        content.append("</table>");
    }

    @RequestMapping(value = "/removePhone/{phoneId}", method = RequestMethod.GET)
    public String removePhone(@PathVariable("phoneId") Integer phoneId) {
        telephoneBookManager.removeNumber(phoneId);
        StringBuilder content = new StringBuilder();
        content.append("<h2>Usunięto numer</h2>");
        content.append("<a href=\"/\">Książka</a>");
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }

    @RequestMapping(value = "/removeFriend/{friendId}", method = RequestMethod.GET)
    public String removeFriend(@PathVariable("friendId") Integer friendId) {
        telephoneBookManager.removeFriend(friendId);
        StringBuilder content = new StringBuilder();
        content.append("<h2>Usunięto znajomego</h2>");
        content.append("<a href=\"/\">Książka</a>");
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }

    @RequestMapping(value = "/addPhone/{friendId}", method = RequestMethod.GET)
    public String addPhone(@PathVariable("friendId") Integer friendId) {
        StringBuilder content = new StringBuilder();
        content.append(String.format("<form action=\"/addPhone/%d\" method=\"POST\"", friendId));
        content.append("<label>Numer</label>");
        content.append("<input type=\"text\" name=\"phone\" placeholder=\"Numer\"></input>");
        content.append("<br />");
        content.append("<input type=\"submit\" value=\"Submit\"></input>");
        content.append("<br />");
        content.append("</form>");
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }
    @RequestMapping(value = "/addPhone/{friendId}", method = RequestMethod.POST)
    public String addPhone(@PathVariable("friendId") Integer friendId, @RequestParam String phone) {
        telephoneBookManager.addPhoneToFriend(friendId, phone);
        StringBuilder content = new StringBuilder();
        content.append("<h2>Dodano numer</h2>");
        content.append("<a href=\"/\">Książka</a>");
        return HtmlBuilder.getHtmlPageWithTitleAndContent(title, content.toString());
    }
}
