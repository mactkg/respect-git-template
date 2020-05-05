package net.makerbox.rgt;

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

public class TemplateFinderTest extends TestCase {
    TemplateFinder finder;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        finder = new TemplateFinder();
    }

    public void testGetTemplate() {
        assertThat(finder.getTemplate()).isNotEqualTo("");
    }

    public void testGetGlobalTemplate() {
        finder.setForceGlobal(true);
        System.out.println(finder.getTemplate());
    }

    public void testGetTemplateString() {
        assertThat(finder.getTemplatePathString()).isEqualTo(".gittemplate");
    }

    public void testGetGlobalTemplateString() {
        finder.setForceGlobal(true);
        System.out.println(finder.getTemplatePathString());
    }
}