package utils;

public class LinkCutter {
    private LinkCutter() {

    }

    public static String cutLinks(String link) {
        link = cutMobileFullLink(link);
        link = cutMobileShortLink(link);
        link = cutFullLink(link);
        link = cutShortLink(link);
        link = clearSpaces(link);
        return link.toLowerCase();
    }

    private static String clearSpaces(String link) {
        return link.replace(" ", "");
    }

    private static String cutMobileFullLink(String link) {
        return link.replace("https://m.vk.com/", "").replace("http://m.vk.com", "");
    }

    private static String cutMobileShortLink(String link) {
        return link.replace("m.vk.com/", "");
    }

    private static String cutFullLink(String link) {
        return link.replace("https://vk.com/", "").replace("http://vk.com/", "");
    }

    private static String cutShortLink(String link) {
        return link.replace("vk.com/", "");
    }

}
