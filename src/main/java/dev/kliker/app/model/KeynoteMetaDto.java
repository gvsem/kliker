package dev.kliker.app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class KeynoteMetaDto {

    @JsonProperty("currentSlideIndex")
    Integer currentSlideId;

    public static KeynoteMetaDto fromKeynote(Keynote k) {
        var m = new KeynoteMetaDto();
        m.currentSlideId = k.getCurrentSlide();
        return m;
    }

}
