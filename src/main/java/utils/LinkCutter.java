package utils;

public class LinkCutter {

    public String cutLinks(String link) {
        link = cutMobileFullLink(link);
        link = cutMobileShortLink(link);
        link = cutFullLink(link);
        link = cutShortLink(link);
        link = clearSpaces(link);
        return link.toLowerCase();
    }

    private String clearSpaces(String link) {
        return link.replace(" ", "");
    }

    private String cutMobileFullLink(String link) {
        return link.replace("https://m.vk.com/", "").replace("http://m.vk.com", "");
    }

    private String cutMobileShortLink(String link) {
        return link.replace("m.vk.com/", "");
    }

    private String cutFullLink(String link) {
        return link.replace("https://vk.com/", "").replace("http://vk.com/", "");
    }

    private String cutShortLink(String link) {
        return link.replace("vk.com/", "");
    }

}
