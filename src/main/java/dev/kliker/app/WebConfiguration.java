package dev.kliker.app;

public class WebConfigutation {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("some/path/*.html")
                .addResourceLocations("/static/");
    }
}

