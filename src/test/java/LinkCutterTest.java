import org.junit.Assert;
import org.junit.Test;
import utils.LinkCutter;

public class LinkCutterTest {
    private final String name = "ar_223";

    @Test
    public void cutShortLinkTest() {
        String input = "vk.com/" + name;

        String result = LinkCutter.cutLinks(input);

        Assert.assertEquals(result, name);
    }

    @Test
    public void cutFullLinkTest() {
        String input = "https://vk.com/" + name;

        String result = LinkCutter.cutLinks(input);

        Assert.assertEquals(result, name);
    }

    @Test
    public void cutFullMobileLinkTest() {
        String input = "https://m.vk.com/" + name;

        String result = LinkCutter.cutLinks(input);

        Assert.assertEquals(result, name);
    }

    @Test
    public void cutShortMobileLinkTest() {
        String input = "m.vk.com/" + name;

        String result = LinkCutter.cutLinks(input);

        Assert.assertEquals(result, name);
    }

    @Test
    public void clearSpacesTest() {
        String input = "  " + name + "  ";

        String result = LinkCutter.cutLinks(input);

        Assert.assertEquals(result, name);
    }
}
