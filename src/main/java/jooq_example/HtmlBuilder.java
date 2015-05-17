package jooq_example;

/**
 * Builder tworzący strone internetową o podanej treści
 * @author Adam Mościcki
 */
public class HtmlBuilder {

    public static String getHtmlPageWithTitleAndContent(String title, String content) {
        StringBuilder page = new StringBuilder();
        page.append("<html>");
        page.append("<head>");
        page.append("<title>");
        page.append(title);
        page.append("</title>");
        page.append("</head>");
        page.append("<body>");
        page.append(content);
        page.append("</body>");
        page.append("</html>");
        return page.toString();
    }
}
